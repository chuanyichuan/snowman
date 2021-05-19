package cc.kevinlu.snow.server.processor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cc.kevinlu.snow.server.config.Constants;
import cc.kevinlu.snow.server.data.mapper.*;
import cc.kevinlu.snow.server.data.model.DigitDO;
import cc.kevinlu.snow.server.data.model.GroupDO;
import cc.kevinlu.snow.server.data.model.SnowflakeDO;
import cc.kevinlu.snow.server.data.model.UuidDO;
import cc.kevinlu.snow.server.pojo.PersistentBO;
import cc.kevinlu.snow.server.utils.CollectionUtils;
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

    public SnowflakeMapper getSnowflakeMapper() {
        return snowflakeMapper;
    }

    public DigitMapper getDigitMapper() {
        return digitMapper;
    }

    public UuidMapper getUuidMapper() {
        return uuidMapper;
    }

    @Autowired
    private DigitMapper digitMapper;
    @Autowired
    private UuidMapper  uuidMapper;
    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private BatchMapper batchMapper;

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
     * @param persistent
     */
    public void persistentDigit(PersistentBO<Long> persistent) {
        long instanceId = persistent.getInstanceId();
        List<Long> idList = persistent.getIdList();
        int status = persistent.getUsed() ? 1 : 0;
        long from = idList.get(0);
        long to = idList.get(idList.size() - 1);
        DigitDO digit = new DigitDO();
        digit.setChunk(idList.size());
        digit.setFromValue(from);
        digit.setToValue(to);
        digit.setServiceInstanceId(instanceId);
        digit.setStatus(status);
        digit.setGmtCreated(new Date());
        digitMapper.insertSelective(digit);
    }

    /**
     * persistence snowflake records
     * 
     * @param persistent
     */
    public void persistentSnowflake(PersistentBO<Long> persistent) {
        long instanceId = persistent.getInstanceId();
        List<Long> idList = persistent.getIdList();
        int status = persistent.getUsed() ? 1 : 0;
        int chunk = idList.size();
        Date date = new Date();
        SnowflakeDO snowflake;
        List<SnowflakeDO> records = new ArrayList<>();
        int index = 1;
        for (Long id : idList) {
            snowflake = new SnowflakeDO();
            snowflake.setChunk(chunk);
            snowflake.setServiceInstanceId(instanceId);
            snowflake.setGValue(id);
            snowflake.setStatus(status);
            snowflake.setGmtCreated(date);
            records.add(snowflake);
            if (index++ % Constants.BATCH_INSERT_SIZE == 0) {
                batchMapper.insertSnowflake(records);
                records.clear();
            }
        }
        if (!CollectionUtils.isEmpty(records)) {
            batchMapper.insertSnowflake(records);
        }
    }

    /**
     * persistence uuid records
     * 
     * @param persistent
     */
    public void persistentUuid(PersistentBO<String> persistent) {
        long instanceId = persistent.getInstanceId();
        List<String> idList = persistent.getIdList();
        int status = persistent.getUsed() ? 1 : 0;
        int chunk = idList.size();
        Date date = new Date();
        List<UuidDO> records = new ArrayList<>();
        int index = 1;
        for (String id : idList) {
            UuidDO uuid = new UuidDO();
            uuid.setChunk(chunk);
            uuid.setServiceInstanceId(instanceId);
            uuid.setGValue(id);
            uuid.setStatus(status);
            uuid.setGmtCreated(date);
            records.add(uuid);
            if (index++ % Constants.BATCH_INSERT_SIZE == 0) {
                batchMapper.insertUuid(records);
                records.clear();
            }
        }
        if (!CollectionUtils.isEmpty(records)) {
            batchMapper.insertUuid(records);
        }
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
}
