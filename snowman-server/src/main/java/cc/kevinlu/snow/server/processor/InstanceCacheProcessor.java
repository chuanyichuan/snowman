package cc.kevinlu.snow.server.processor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.nacos.common.utils.CollectionUtils;

import cc.kevinlu.snow.server.data.mapper.GroupMapper;
import cc.kevinlu.snow.server.data.mapper.ServiceInstanceMapper;
import cc.kevinlu.snow.server.data.model.GroupDO;
import cc.kevinlu.snow.server.data.model.GroupDOExample;
import cc.kevinlu.snow.server.data.model.ServiceInstanceDO;
import cc.kevinlu.snow.server.data.model.ServiceInstanceDOExample;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chuan
 */
@Slf4j
@Component
public class InstanceCacheProcessor implements InitializingBean {

    /**
     * groupCode cache set
     */
    private volatile Map<String, Long> groupCodeMap     = new ConcurrentHashMap<>();

    /**
     * instanceCode cache set
     */
    private volatile Map<String, Long> instanceCodeMap  = new ConcurrentHashMap<>();

    private static final String        INSTANCE_PATTERN = "%s_%s";

    @Autowired
    private GroupMapper                groupMapper;

    @Autowired
    private ServiceInstanceMapper      serviceInstanceMapper;

    /**
     * Let groupCode be added to the cache
     * 
     * @param groupCode
     */
    public void putGroupCode(String groupCode, Long id) {
        groupCodeMap.put(groupCode, id);
    }

    /**
     * Let instanceCode be added to the cache
     * 
     * @param instanceCode
     */
    public void putInstanceCode(Long groupId, String instanceCode, Long id) {
        instanceCodeMap.put(String.format(INSTANCE_PATTERN, groupId, instanceCode), id);
    }

    /**
     * Verify that the group exists
     * 
     * @param groupCode
     * @return true: exists, false: not exists
     */
    public boolean hasGroup(String groupCode) {
        return groupCodeMap.containsKey(groupCode);
    }

    /**
     * Verify that the instance exists
     * 
     * @param groupId
     * @param instanceCode
     * @return true: exists, false: not exists
     */
    public boolean hasInstance(Long groupId, String instanceCode) {
        return instanceCodeMap.containsKey(String.format(INSTANCE_PATTERN, groupId, instanceCode));
    }

    /**
     * remove key from group cache
     * 
     * @param groupCode
     */
    public void removeGroup(String groupCode) {
        groupCodeMap.remove(groupCode);
    }

    /**
     * remove key from instance cache
     * 
     * @param groupId
     * @param instanceCode
     */
    public void removeInstance(Long groupId, String instanceCode) {
        instanceCodeMap.remove(String.format(INSTANCE_PATTERN, groupId, instanceCode));
    }

    /**
     * get id of group
     * 
     * @param groupCode
     * @return
     */
    public Long getGroupId(String groupCode) {
        return groupCodeMap.get(groupCode);
    }

    /**
     * get id of service instance
     * 
     * @param groupId
     * @param instanceCode
     * @return
     */
    public Long getInstanceId(Long groupId, String instanceCode) {
        return instanceCodeMap.get(String.format(INSTANCE_PATTERN, groupId, instanceCode));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<GroupDO> groupList = groupMapper.selectByExample(new GroupDOExample());
        if (CollectionUtils.isNotEmpty(groupList)) {
            groupCodeMap.putAll(groupList.stream().collect(Collectors.toMap(GroupDO::getGroupCode, GroupDO::getId)));
        }
        List<ServiceInstanceDO> instanceList = serviceInstanceMapper.selectByExample(new ServiceInstanceDOExample());
        if (CollectionUtils.isNotEmpty(instanceList)) {
            instanceCodeMap.putAll(instanceList.stream().collect(Collectors
                    .toMap(v -> String.format(INSTANCE_PATTERN, v.getGroupId(), v.getServerCode()), v -> v.getId())));
        }
    }
}
