package cc.kevinlu.snow.server.web;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import cc.kevinlu.snow.client.exceptions.ParamIllegalException;
import cc.kevinlu.snow.client.instance.ServiceInstanceClient;
import cc.kevinlu.snow.client.instance.pojo.ServiceInfo;
import cc.kevinlu.snow.client.instance.pojo.ServiceInstance;
import cc.kevinlu.snow.client.instance.pojo.ServiceQuery;
import cc.kevinlu.snow.server.service.ServiceInstanceService;

/**
 * @author chuan
 */
@RestController
public class ServiceInstanceController implements ServiceInstanceClient {

    @Autowired
    private ServiceInstanceService serviceInstanceService;

    @Override
    public boolean registerService(ServiceInstance instance) {
        // 校验参数
        if (StringUtils.isAnyBlank(instance.getName(), instance.getGroupCode(), instance.getInstanceCode())) {
            throw new ParamIllegalException("name,groupCode,instanceCode all need!");
        }
        return serviceInstanceService.registerService(instance);
    }

    @Override
    public ServiceInfo services(ServiceQuery params) {
        return serviceInstanceService.services(params);
    }
}
