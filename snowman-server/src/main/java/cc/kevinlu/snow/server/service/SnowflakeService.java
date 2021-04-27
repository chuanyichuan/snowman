package cc.kevinlu.snow.server.service;

import java.util.List;

/**
 * @author chuan
 */
public interface SnowflakeService {

    /**
     * 
     *
     * @param groupCode
     * @param instanceCode
     * @return
     */
    List<Object> generate(String groupCode, String instanceCode);
}
