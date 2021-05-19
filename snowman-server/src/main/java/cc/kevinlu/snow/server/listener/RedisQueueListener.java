package cc.kevinlu.snow.server.listener;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import cc.kevinlu.snow.server.config.Constants;
import cc.kevinlu.snow.server.data.mapper.GroupMapper;
import cc.kevinlu.snow.server.data.mapper.ServiceInstanceMapper;
import cc.kevinlu.snow.server.data.model.GroupDO;
import cc.kevinlu.snow.server.data.model.GroupDOExample;
import cc.kevinlu.snow.server.data.model.ServiceInstanceDOExample;
import cc.kevinlu.snow.server.listener.pojo.PreGenerateBO;
import cc.kevinlu.snow.server.processor.redis.RedisProcessor;
import cc.kevinlu.snow.server.processor.task.CheckChunkProcessor;
import cc.kevinlu.snow.server.processor.task.pojo.RegenerateBO;
import cc.kevinlu.snow.server.utils.CollectionUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * Redis消息接收
 * 
 * @author chuan
 */
@Slf4j
@Component
public class RedisQueueListener implements MessageListener {

    @Autowired
    private RedisProcessor        redisProcessor;
    @Autowired
    private GroupMapper           groupMapper;
    @Autowired
    private ServiceInstanceMapper serviceInstanceMapper;
    @Autowired
    private CheckChunkProcessor   checkChunkProcessor;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String topic = new String(message.getChannel());
        String content = new String(message.getBody());
        if (Constants.CHECK_CHUNK_TOPIC.equalsIgnoreCase(topic)) {
            // check chunk size
            PreGenerateBO preGenerateBO = JSONObject.parseObject(content, PreGenerateBO.class);
            checkChunkSize(preGenerateBO);
        } else {
            log.warn("receive other message!");
        }
    }

    /**
     * 检查缓存中数据是否达标
     * 
     * @param preGenerateBO
     */
    private void checkChunkSize(PreGenerateBO preGenerateBO) {
        String groupCode = preGenerateBO.getGroup();
        String instanceCode = preGenerateBO.getInstance();
        String lock = String.format(Constants.CHECK_CHUNK_LOCK_PATTERN, groupCode);
        int time = 0;
        while (!redisProcessor.tryLockWithLua(lock, instanceCode, 1000)) {
            log.warn("[{}] - [{}] 第[{}]次尝试加锁失败", groupCode, instanceCode, ++time);
            if (time >= 3) {
                return;
            }
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
            }
        }

        try {
            // 校验数据
            log.warn("[{}] - [{}] 第[{}]次尝试加锁成功", groupCode, instanceCode, ++time);
            GroupDOExample groupExample = new GroupDOExample();
            groupExample.createCriteria().andGroupCodeEqualTo(groupCode);
            groupExample.setOrderByClause("id asc limit 1");
            List<GroupDO> groupList = groupMapper.selectByExample(groupExample);
            if (CollectionUtils.isEmpty(groupList)) {
                log.warn("group [{}] 不存在", groupCode);
                return;
            }
            GroupDO group = groupList.get(0);
            ServiceInstanceDOExample instanceExample = new ServiceInstanceDOExample();
            instanceExample.createCriteria().andGroupIdEqualTo(group.getId()).andServerCodeEqualTo(instanceCode);
            instanceExample.setOrderByClause("id asc limit 1");
            long instanceCount = serviceInstanceMapper.countByExample(instanceExample);
            if (instanceCount == 0L) {
                log.warn("instance [{}] 不存在", instanceCode);
                return;
            }
            // 检查当前是否需要扩容
            RegenerateBO regenerate = RegenerateBO.builder().group(groupCode).mode(group.getMode())
                    .instance(instanceCode).chunk(group.getChunk()).lastValue(group.getLastValue())
                    .times(preGenerateBO.getTimes()).build();
            boolean redo = checkChunkProcessor.preRegenerate(regenerate);
            if (redo) {
                // 生成ID
                checkChunkProcessor.startRegenerate(regenerate);
            }

        } catch (Exception e) {
        } finally {
            redisProcessor.releaseLock(lock, instanceCode);
        }
    }
}
