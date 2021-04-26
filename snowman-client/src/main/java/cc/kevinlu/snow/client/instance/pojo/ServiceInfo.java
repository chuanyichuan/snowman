package cc.kevinlu.snow.client.instance.pojo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.ToString;

/**
 * service base info
 *
 * @author chuan
 */
@Data
@ToString
public class ServiceInfo implements Serializable {

    /**
     * service name
     */
    private String             name;

    /**
     * group's code
     */
    private String             groupCode;

    /**
     * the list of instance
     */
    private List<InstanceInfo> instances;

    /**
     * instance info
     * 
     * @author chuan 
     */
    protected static class InstanceInfo implements Serializable {

        /**
         * instance's code
         */
        private String  serverCode;

        /**
         * number of snowman calls 
         */
        private Integer snowTimes;

        /**
         * the min value of the last call
         */
        private Long    lastFromValue;

        /**
         * the max value of the last call
         */
        private Long    lastToValue;
    }

}
