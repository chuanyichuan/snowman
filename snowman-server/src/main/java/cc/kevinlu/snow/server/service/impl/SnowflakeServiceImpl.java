package cc.kevinlu.snow.server.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cc.kevinlu.snow.server.data.mapper.GroupMapper;
import cc.kevinlu.snow.server.data.mapper.ServiceInstanceMapper;
import cc.kevinlu.snow.server.data.mapper.SnowflakeMapper;
import cc.kevinlu.snow.server.data.model.GroupDO;
import cc.kevinlu.snow.server.data.model.GroupDOExample;
import cc.kevinlu.snow.server.data.model.ServiceInstanceDO;
import cc.kevinlu.snow.server.data.model.SnowflakeDO;
import cc.kevinlu.snow.server.generate.GenerateAlgorithmFactory;
import cc.kevinlu.snow.server.processor.InstanceCacheProcessor;
import cc.kevinlu.snow.server.processor.SnowflakeLockProcessor;
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
    private SnowflakeMapper          snowflakeMapper;
    @Autowired
    private ServiceInstanceMapper    serviceInstanceMapper;
    @Autowired
    private SnowflakeLockProcessor   snowflakeLockProcessor;
    @Autowired
    private InstanceCacheProcessor   instanceCacheProcessor;
    @Autowired
    private GenerateAlgorithmFactory generateAlgorithmFactory;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Object> generate(String groupCode, String instanceCode) {
        List<Object> result = new ArrayList<>();

        // add lock
        while (!snowflakeLockProcessor.tryLock(groupCode, 3000L)) {
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
            }
        }
        try {
            GroupDOExample groupExample = new GroupDOExample();
            groupExample.createCriteria().andGroupCodeEqualTo(groupCode);
            groupExample.setOrderByClause("id desc limit 1");
            List<GroupDO> groupList = groupMapper.selectByExample(groupExample);
            if (CollectionUtils.isEmpty(groupList)) {
                return result;
            }
            GroupDO group = groupList.get(0);

            // generate
            result = generateAlgorithmFactory.factory(group.getMode()).generate(group, instanceCode);

            int chunk = group.getChunk();
            long lastValue = group.getLastValue();

            long fromValue = lastValue + 1;
            long toValue = lastValue + chunk;
            // update database
            group.setLastValue(toValue);
            group.setGmtUpdated(new Date());
            groupMapper.updateByPrimaryKeySelective(group);

            long instanceId = instanceCacheProcessor.getInstanceId(group.getId(), instanceCode);

            SnowflakeDO snowflake = new SnowflakeDO();
            snowflake.setChunk(chunk);
            snowflake.setServiceInstanceId(instanceId);
            snowflake.setFromValue(fromValue);
            snowflake.setToValue(toValue);
            snowflakeMapper.insertSelective(snowflake);

            ServiceInstanceDO instance = serviceInstanceMapper.selectByPrimaryKey(instanceId);
            instance.setSnowTimes(instance.getSnowTimes() + 1);
            instance.setGmtUpdated(new Date());
            serviceInstanceMapper.updateByPrimaryKeySelective(instance);

            return result;
        } catch (Exception e) {
            result.clear();
            log.error("generate error!", e);
        } finally {
            snowflakeLockProcessor.releaseLock(groupCode);
        }
        return result;
    }
}
