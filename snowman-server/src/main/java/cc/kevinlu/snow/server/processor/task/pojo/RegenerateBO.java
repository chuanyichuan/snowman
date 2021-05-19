package cc.kevinlu.snow.server.processor.task.pojo;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Tolerate;

/**
 * @author chuan
 */
@Data
@Builder
@ToString
public class RegenerateBO implements Serializable {

    /**
     * id of group in db
     */
    private Long    groupId;

    /**
     * groupId of service
     */
    private String  group;

    /**
     * instanceID of service
     */
    private String  instance;

    /**
     * 服务组每次获取ID数量
     */
    private Integer chunk;

    /**
     * 1:数字;2:雪花算法;3:UUID
     */
    private Integer mode;

    /**
     * 服务组最近一次获取的ID最大值
     */
    private String  lastValue;

    /**
     * message times
     */
    private Integer times;

    @Tolerate
    public RegenerateBO() {
    }

}
