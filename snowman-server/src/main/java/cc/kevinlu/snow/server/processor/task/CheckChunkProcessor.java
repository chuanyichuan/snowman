package cc.kevinlu.snow.server.processor.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import cc.kevinlu.snow.client.enums.IdAlgorithmEnums;
import cc.kevinlu.snow.server.config.Constants;
import cc.kevinlu.snow.server.generate.GenerateAlgorithmFactory;
import cc.kevinlu.snow.server.listener.RedisQueueListener;
import cc.kevinlu.snow.server.listener.pojo.PreGenerateBO;
import cc.kevinlu.snow.server.processor.InstanceCacheProcessor;
import cc.kevinlu.snow.server.processor.redis.RedisProcessor;
import cc.kevinlu.snow.server.processor.task.pojo.RegenerateBO;
import cc.kevinlu.snow.server.utils.CollectionUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 检查剩余预生成数据是否充裕
 * 
 * @author chuan
 */
@Slf4j
@Component
public class CheckChunkProcessor {

    @Autowired
    private RedisProcessor           redisProcessor;
    @Autowired
    private GenerateAlgorithmFactory generateAlgorithmFactory;
    @Autowired
    private InstanceCacheProcessor   instanceCacheProcessor;

    /**
     * the max times for regenerate message
     */
    private static final int         REGENERATE_TIMES_LIMIT  = 3;
    /**
     * The default pre-generate threshold
     */
    private static final int         DEFAULT_MULTIPLE_FACTOR = 7;
    /**
     * The default scaling threshold, which can also be configured by the client
     */
    private static final float       DEFAULT_LOAD_FACTOR     = 0.3f;

    /**
     * Send message to redis
     * 
     * @param preGenerateBO
     * @see RedisQueueListener
     */
    @Async
    public void sendChunkMessage(PreGenerateBO preGenerateBO) {
        if (StringUtils.isAnyBlank(preGenerateBO.getGroup(), preGenerateBO.getInstance())) {
            log.warn("Reject Illegal Call!");
            return;
        }
        if (preGenerateBO.getTimes() > REGENERATE_TIMES_LIMIT) {
            log.warn("Times overflow!");
            return;
        }
        redisProcessor.sendMessage(Constants.CHECK_CHUNK_TOPIC, preGenerateBO);
    }

    /**
     * Check
     * 
     * @param regenerate
     * @return
     */
    public boolean preRegenerate(RegenerateBO regenerate) {
        // redis_key
        Long count = redisCount(regenerate);
        if (IdAlgorithmEnums.DIGIT.getAlgorithm() == regenerate.getMode()) {
            return count < DEFAULT_MULTIPLE_FACTOR * DEFAULT_LOAD_FACTOR;
        }
        return count <= regenerate.getChunk() * DEFAULT_MULTIPLE_FACTOR * DEFAULT_LOAD_FACTOR;
    }

    /**
     * query count from redis
     * 
     * @param regenerate
     * @return
     */
    private Long redisCount(RegenerateBO regenerate) {
        long instanceId = instanceCacheProcessor.getInstanceId(regenerate.getGroupId(), regenerate.getInstance());
        String key = String.format(Constants.CACHE_ID_LOCK_PATTERN, regenerate.getGroupId(), instanceId,
                regenerate.getMode());
        return redisProcessor.lGetListSize(key);
    }

    /**
     * regenerate
     * 
     * @param regenerate
     */
    public void startRegenerate(RegenerateBO regenerate) {
        long total = regenerate.getChunk() * DEFAULT_MULTIPLE_FACTOR;
        long survivor = redisCount(regenerate);
        int size = (int) (total - survivor) / regenerate.getChunk();
        List idList = new ArrayList();
        regenerate.setChunk(regenerate.getChunk());
        idList.addAll(generateAlgorithmFactory.factory(regenerate.getMode()).regenerate(regenerate));
        for (int i = 1; i < size; i++) {
            regenerate.setLastValue(String.valueOf(idList.get(idList.size() - 1)));
            idList.addAll(generateAlgorithmFactory.factory(regenerate.getMode()).regenerate(regenerate));
        }
        if (CollectionUtils.isEmpty(idList)) {
            log.info("regenerate error!");
            PreGenerateBO preGenerateBO = new PreGenerateBO();
            preGenerateBO.setGroup(regenerate.getGroup());
            preGenerateBO.setInstance(regenerate.getInstance());
            preGenerateBO.setTimes(regenerate.getTimes() + 1);
            sendChunkMessage(preGenerateBO);
            return;
        }
        // regenerate success!
    }
}
