package cc.kevinlu.snow.server.processor.pojo;

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
public class RecordAcquireBO implements Serializable {

    private Long    groupId;

    private Long    instanceId;

    private String  instanceCode;

    private Integer mode;

    private Integer chunk;

    @Tolerate
    public RecordAcquireBO() {
    }
}
