package cc.kevinlu.snow.server.service;

import cc.kevinlu.snow.client.instance.pojo.ServiceInfo;
import cc.kevinlu.snow.client.instance.pojo.ServiceInstance;
import cc.kevinlu.snow.client.instance.pojo.ServiceQuery;

/**
 * 
 * @author chuan
 */
public interface ServiceInstanceService {

    /**
     * logic: <br />
     * <p>1. get the field of groupCode for lock</p>
     * <p>2. verify the groupCode, if it not in ConcurrentHashMapA, to 3, else to 4</p>
     * <p>3. insert the groupCode into database, and put it into ConcurrentHashMapA for cache</p>
     * <p>4. verify the instanceCode, if it not in ConcurrentHashMapB, to 5, else to 6</p>
     * <p>5. insert the instanceCode into database, and put it into ConcurrentHashMapB for cache</p>
     * 
     * @param instance
     * @return
     */
    boolean registerService(ServiceInstance instance);

    /**
     * get all instance info from database
     * 
     * @param params
     * @return
     */
    ServiceInfo services(ServiceQuery params);
}
