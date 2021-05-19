package cc.kevinlu.snow.server.processor.pojo;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

/**
 * @author chuan
 */
@Data
@ToString
public class AsyncCacheBO implements Serializable {

    /**
     * id of group in db
     */
    private Long    groupId;

    /**
     * instanceID of service
     */
    private Long    instanceId;

    /**
     * 1:数字;2:雪花算法;3:UUID
     */
    private Integer mode;

}
