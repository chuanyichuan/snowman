package cc.kevinlu.snow.client.instance.pojo;

import java.io.Serializable;

import cc.kevinlu.snow.client.enums.IdAlgorithmEnums;
import lombok.Data;
import lombok.ToString;

/**
 * service instance pojo
 * 
 * @author chuan
 */
@Data
@ToString
public class ServiceInstance implements Serializable {

    /**
     * name of service
     */
    private String           name;

    /**
     * group of service 
     */
    private String           groupCode;

    /**
     * code of instance depends on the group
     */
    private String           instanceCode;

    /**
     * the count for obtain in one time
     */
    private Integer          chunk;

    /**
     * the algorithm of generate
     */
    private IdAlgorithmEnums mode;
}
