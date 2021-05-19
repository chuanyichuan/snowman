package cc.kevinlu.snow.server.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import cc.kevinlu.snow.server.config.Constants;
import cc.kevinlu.snow.server.data.mapper.GroupMapper;
import cc.kevinlu.snow.server.data.model.GroupDO;
import cc.kevinlu.snow.server.data.model.GroupDOExample;
import cc.kevinlu.snow.server.generate.GenerateAlgorithmFactory;
import cc.kevinlu.snow.server.listener.pojo.PreGenerateBO;
import cc.kevinlu.snow.server.processor.SnowflakeLockProcessor;
import cc.kevinlu.snow.server.processor.task.CheckChunkProcessor;
import cc.kevinlu.snow.server.processor.task.pojo.RegenerateBO;
import cc.kevinlu.snow.server.service.SnowflakeService;
import cc.kevinlu.snow.server.utils.CollectionUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chuan
 */
@Slf4j
@Service
public class SnowflakeServiceImpl implements SnowflakeService {

    @Autowired
    private GroupMapper              groupMapper;
    @Autowired
    private ThreadPoolTaskExecutor   taskExecutor;
    @Autowired
    private CheckChunkProcessor      checkChunkProcessor;
    @Autowired
    private SnowflakeLockProcessor   snowflakeLockProcessor;
    @Autowired
    private GenerateAlgorithmFactory generateAlgorithmFactory;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Object> generate(String groupCode, String instanceCode) {

        // acquire lock
        int lockTimes = 0;
        while (!snowflakeLockProcessor.tryLock(groupCode, Constants.DEFAULT_TIMEOUT)) {
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
            }
            if (lockTimes++ > 3) {
                throw new CannotAcquireLockException("acquire lock timeout!");
            }
        }
        try {
            GroupDOExample groupExample = new GroupDOExample();
            groupExample.createCriteria().andGroupCodeEqualTo(groupCode);
            groupExample.setOrderByClause("id desc limit 1");
            List<GroupDO> groupList = groupMapper.selectByExample(groupExample);
            if (CollectionUtils.isEmpty(groupList)) {
                return null;
            }
            GroupDO group = groupList.get(0);

            // generate
            RegenerateBO regenerate = RegenerateBO.builder().group(groupCode).mode(group.getMode())
                    .instance(instanceCode).chunk(group.getChunk()).lastValue(group.getLastValue()).build();
            return generateAlgorithmFactory.factory(group.getMode()).generate(regenerate);
        } catch (Exception e) {
            log.error("generate error!", e);
        } finally {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                    snowflakeLockProcessor.releaseLock(groupCode);
                    // check next chunk
                    taskExecutor.execute(() -> {
                        PreGenerateBO preGenerateBO = PreGenerateBO.builder().group(groupCode).instance(instanceCode)
                                .times(0).build();
                        checkChunkProcessor.sendChunkMessage(preGenerateBO);
                    });
                }
            });
        }
        return null;
    }
}
