package cc.kevinlu.snow.client.instance;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import cc.kevinlu.snow.client.instance.pojo.ServiceInfo;
import cc.kevinlu.snow.client.instance.pojo.ServiceInstance;
import cc.kevinlu.snow.client.instance.pojo.ServiceQuery;

/**
 * service instance client<br/>
 * <p>It is a feign client, other service can visit Snowman by reference it.</p>
 * <p>we can call the method of it to register the instance of service, and also
 * can get all instance's info with it.</p>
 * 
 * @author chuan
 */
@FeignClient(name = "snowman", contextId = "serviceInstance")
public interface ServiceInstanceClient {

    /**
     * register service
     * 
     * @return
     */
    @PostMapping(value = "/register_service_instance", produces = MediaType.APPLICATION_JSON_VALUE)
    default boolean registerService(@RequestBody ServiceInstance instance) {
        return false;
    }

    /**
     * query all instance of service by name or group
     * 
     * @param params
     * @return
     */
    @PostMapping(value = "/query_service_instances", consumes = MediaType.APPLICATION_JSON_VALUE)
    default List<ServiceInfo> services(@RequestBody ServiceQuery params) {
        return new ArrayList<>();
    }

}
