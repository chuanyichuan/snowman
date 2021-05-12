package cc.kevinlu.snow.client.instance;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import cc.kevinlu.snow.client.instance.pojo.ServiceInfo;
import cc.kevinlu.snow.client.instance.pojo.ServiceInstance;
import cc.kevinlu.snow.client.instance.pojo.ServiceQuery;

/**
 * <p>service instance client</p>
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
     * @param instance Instance information
     * @return true or false
     */
    @PostMapping(value = "/register_service_instance", produces = MediaType.APPLICATION_JSON_VALUE)
    boolean registerService(@RequestBody ServiceInstance instance);

    /**
     * query all instance of service by name or group
     * 
     * @param params parameter for query
     * @return Service Information
     */
    @PostMapping(value = "/query_service_instances", consumes = MediaType.APPLICATION_JSON_VALUE)
    ServiceInfo services(@RequestBody ServiceQuery params);

}
