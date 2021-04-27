package cc.kevinlu.snow.server.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.nacos.common.utils.CollectionUtils;

import cc.kevinlu.snow.client.enums.ServiceStatusEnums;
import cc.kevinlu.snow.client.instance.pojo.ServiceInfo;
import cc.kevinlu.snow.client.instance.pojo.ServiceInstance;
import cc.kevinlu.snow.client.instance.pojo.ServiceQuery;
import cc.kevinlu.snow.server.data.mapper.GroupMapper;
import cc.kevinlu.snow.server.data.mapper.ServiceInstanceMapper;
import cc.kevinlu.snow.server.data.mapper.SnowflakeMapper;
import cc.kevinlu.snow.server.data.model.*;
import cc.kevinlu.snow.server.processor.InstanceCacheProcessor;
import cc.kevinlu.snow.server.service.ServiceInstanceService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chuan 
 */
@Slf4j
@Service
public class ServiceInstanceServiceImpl implements ServiceInstanceService {

    @Autowired
    private GroupMapper            groupMapper;
    @Autowired
    private SnowflakeMapper        snowflakeMapper;
    @Autowired
    private ServiceInstanceMapper  serviceInstanceMapper;
    @Autowired
    private InstanceCacheProcessor instanceCacheProcessor;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean registerService(ServiceInstance instance) {
        String groupCode = instance.getGroupCode();
        String instanceCode = instance.getInstanceCode();
        Long groupId = null, serviceInstanceId = null;
        boolean gflag = false, iflag = false;
        Date date = new Date();
        try {
            if ((groupId = instanceCacheProcessor.getGroupId(groupCode)) == null) {
                // insert group into database
                GroupDO groupDO = new GroupDO();
                groupDO.setName(instance.getName());
                groupDO.setGroupCode(groupCode);
                groupDO.setChunk(instance.getChunk());
                groupDO.setMode(instance.getMode().getAlgorithm());
                groupDO.setLastValue(0L);
                groupDO.setGmtCreated(date);
                groupDO.setGmtUpdated(date);
                groupMapper.insertSelective(groupDO);
                // set cache
                groupId = groupDO.getId();
                instanceCacheProcessor.putGroupCode(groupCode, groupId);
                gflag = true;
            }
            if ((serviceInstanceId = instanceCacheProcessor.getInstanceId(groupId, instanceCode)) == null) {
                // insert instance into database
                ServiceInstanceDO instanceDO = new ServiceInstanceDO();
                instanceDO.setGroupId(groupId);
                instanceDO.setServerCode(instance.getInstanceCode());
                instanceDO.setSnowTimes(0);
                instanceDO.setStatus(ServiceStatusEnums.ONLINE.getStatus());
                instanceDO.setGmtCreated(date);
                instanceDO.setGmtUpdated(date);
                serviceInstanceMapper.insertSelective(instanceDO);
                // set cache
                serviceInstanceId = instanceDO.getId();
                instanceCacheProcessor.putInstanceCode(groupId, instanceCode, serviceInstanceId);
                iflag = true;
            }
        } catch (Exception e) {
            if (gflag) {
                instanceCacheProcessor.removeGroup(groupCode);
            }
            if (iflag) {
                instanceCacheProcessor.removeInstance(groupId, instanceCode);
            }
            return false;
        }
        return true;
    }

    @Override
    public ServiceInfo services(ServiceQuery params) {
        GroupDOExample groupExample = new GroupDOExample();
        GroupDOExample.Criteria criteria = groupExample.createCriteria();
        if (StringUtils.isNotBlank(params.getName())) {
            criteria.andNameEqualTo(params.getName());
        }
        if (StringUtils.isNotBlank(params.getGroupCode())) {
            criteria.andGroupCodeEqualTo(params.getGroupCode());
        }
        List<GroupDO> groupList = groupMapper.selectByExample(groupExample);
        if (CollectionUtils.isEmpty(groupList)) {
            return null;
        }
        GroupDO group = groupList.get(0);
        ServiceInstanceDOExample instanceExample = new ServiceInstanceDOExample();
        instanceExample.createCriteria().andGroupIdEqualTo(group.getId());
        List<ServiceInstanceDO> instanceList = serviceInstanceMapper.selectByExample(instanceExample);
        // result
        List<ServiceInfo.InstanceInfo> instanceInfoList = new ArrayList<>(instanceList.size());
        ServiceInfo result = new ServiceInfo();
        result.setName(group.getName());
        result.setGroupCode(group.getGroupCode());
        result.setInstances(instanceInfoList);

        if (CollectionUtils.isNotEmpty(instanceList)) {
            SnowflakeDOExample snowflakeExample = new SnowflakeDOExample();
            instanceList.stream().forEach(item -> {
                // query last snowflake
                snowflakeExample.clear();
                snowflakeExample.createCriteria().andServiceInstanceIdEqualTo(item.getId());
                snowflakeExample.setOrderByClause("id desc limit 1");
                List<SnowflakeDO> snowflakeList = snowflakeMapper.selectByExample(snowflakeExample);
                ServiceInfo.InstanceInfo instanceInfo = ServiceInfo.InstanceInfo.builder()
                        .serverCode(item.getServerCode()).snowTimes(item.getSnowTimes()).build();
                if (CollectionUtils.isNotEmpty(snowflakeList)) {
                    SnowflakeDO snowflake = snowflakeList.get(0);
                    instanceInfo.setLastFromValue(snowflake.getFromValue());
                    instanceInfo.setLastToValue(snowflake.getToValue());
                }
                instanceInfoList.add(instanceInfo);
            });
        }
        return result;
    }
}
