package cc.kevinlu.snow.client.instance.pojo;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

/**
 * the params for query
 * 
 * @author chuan
 */
@Data
@ToString
public class ServiceQuery implements Serializable {

    /**
     * service's name
     */
    private String name;

    /**
     * group's code
     */
    private String groupCode;

}
