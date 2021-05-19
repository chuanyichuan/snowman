package cc.kevinlu.snow.server.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cc.kevinlu.snow.client.enums.IdAlgorithmEnums;
import cc.kevinlu.snow.server.data.mapper.BatchMapper;
import cc.kevinlu.snow.server.data.mapper.GroupMapper;
import cc.kevinlu.snow.server.data.model.GroupDO;
import cc.kevinlu.snow.server.pojo.PersistentBO;
import cc.kevinlu.snow.server.processor.algorithm.DigitPersistentProcessor;
import cc.kevinlu.snow.server.processor.algorithm.PersistentProcessor;
import cc.kevinlu.snow.server.processor.algorithm.SnowflakePersistentProcessor;
import cc.kevinlu.snow.server.processor.algorithm.UuidPersistentProcessor;
import cc.kevinlu.snow.server.processor.pojo.AsyncCacheBO;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chuan
 */
@Slf4j
@Component
public class AlgorithmProcessor {

    @Autowired
    private InstanceCacheProcessor       instanceCacheProcessor;
    @Autowired
    private UuidPersistentProcessor      uuidPersistentProcessor;
    @Autowired
    private SnowflakePersistentProcessor snowflakePersistentProcessor;
    @Autowired
    private DigitPersistentProcessor     digitPersistentProcessor;

    @Autowired
    private GroupMapper                  groupMapper;
    @Autowired
    private BatchMapper                  batchMapper;

    /**
     * get instance id
     *
     * @param groupId
     * @param instanceCode
     * @return
     */
    public Long instanceId(Long groupId, String instanceCode) {
        return instanceCacheProcessor.getInstanceId(groupId, instanceCode);
    }

    public void persistentToDb(PersistentBO persistent) {
        PersistentProcessor persistentProcessor = getProcessor(persistent.getMode());
        persistentProcessor.syncToDb(persistent);
    }

    /**
     * record snow times
     *
     * @param instanceId
     */
    public void recordSnowTimes(long instanceId) {
        batchMapper.updateSnowTimes(instanceId);
    }

    /**
     * record the last value
     *
     * @param groupId
     * @param value
     */
    public void recordGroupLastValue(Long groupId, String value) {
        GroupDO group = new GroupDO();
        group.setId(groupId);
        group.setLastValue(value);
        groupMapper.updateByPrimaryKeySelective(group);
    }

    public void asyncDataToCache(AsyncCacheBO asyncCacheBO) {
        PersistentProcessor persistentProcessor = getProcessor(asyncCacheBO.getMode());
        persistentProcessor.asyncToCache(asyncCacheBO);
    }

    private PersistentProcessor getProcessor(int mode) {
        IdAlgorithmEnums algorithm = IdAlgorithmEnums.getEnumByAlgorithm(mode);
        switch (algorithm) {
            case UUID:
                return uuidPersistentProcessor;
            case SNOWFLAKE:
                return snowflakePersistentProcessor;
            default:
                return digitPersistentProcessor;
        }
    }

}
