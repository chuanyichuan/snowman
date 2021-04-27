package cc.kevinlu.snow.server.processor;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cc.kevinlu.snow.server.data.mapper.DigitMapper;
import cc.kevinlu.snow.server.data.mapper.ServiceInstanceMapper;
import cc.kevinlu.snow.server.data.mapper.SnowflakeMapper;
import cc.kevinlu.snow.server.data.mapper.UuidMapper;
import cc.kevinlu.snow.server.data.model.DigitDO;
import cc.kevinlu.snow.server.data.model.ServiceInstanceDO;
import cc.kevinlu.snow.server.data.model.SnowflakeDO;
import cc.kevinlu.snow.server.data.model.UuidDO;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chuan
 */
@Slf4j
@Component
public class AlgorithmProcessor {

    @Autowired
    private InstanceCacheProcessor instanceCacheProcessor;
    @Autowired
    private ServiceInstanceMapper  serviceInstanceMapper;
    @Autowired
    private SnowflakeMapper        snowflakeMapper;
    @Autowired
    private DigitMapper            digitMapper;
    @Autowired
    private UuidMapper             uuidMapper;

    /**
     * get instance id
     * @param groupId
     * @param instanceCode
     * @return
     */
    public Long instanceId(Long groupId, String instanceCode) {
        return instanceCacheProcessor.getInstanceId(groupId, instanceCode);
    }

    /**
     * persistence digit records
     * 
     * @param instanceId
     * @param idList
     */
    public void persistentDigit(long instanceId, List<Long> idList) {
        long from = idList.get(0);
        long to = idList.get(idList.size() - 1);
        DigitDO digit = new DigitDO();
        digit.setChunk(idList.size());
        digit.setFromValue(from);
        digit.setToValue(to);
        digit.setServiceInstanceId(instanceId);
        digit.setGmtCreated(new Date());
        digitMapper.insertSelective(digit);
    }

    /**
     * persistence snowflake records
     * 
     * @param instanceId
     * @param idList
     */
    public void persistentSnowflake(long instanceId, List<Long> idList) {
        int chunk = idList.size();
        Date date = new Date();
        SnowflakeDO snowflake;
        for (Long id : idList) {
            snowflake = new SnowflakeDO();
            snowflake.setChunk(chunk);
            snowflake.setServiceInstanceId(instanceId);
            snowflake.setGValue(id);
            snowflake.setGmtCreated(date);
            snowflakeMapper.insertSelective(snowflake);
        }
    }

    /**
     * persistence uuid records
     * 
     * @param instanceId
     * @param idList
     */
    public void persistentUuid(long instanceId, List<String> idList) {
        int chunk = idList.size();
        Date date = new Date();
        for (String id : idList) {
            UuidDO uuid = new UuidDO();
            uuid.setChunk(chunk);
            uuid.setServiceInstanceId(instanceId);
            uuid.setGValue(id);
            uuid.setGmtCreated(date);
            uuidMapper.insertSelective(uuid);
        }
    }

    /**
     * record snow times
     * 
     * @param instanceId
     */
    public void recordSnowTimes(long instanceId) {
        ServiceInstanceDO instance = serviceInstanceMapper.selectByPrimaryKey(instanceId);
        instance.setSnowTimes(instance.getSnowTimes() + 1);
        instance.setGmtUpdated(new Date());
        serviceInstanceMapper.updateByPrimaryKeySelective(instance);
    }
}
