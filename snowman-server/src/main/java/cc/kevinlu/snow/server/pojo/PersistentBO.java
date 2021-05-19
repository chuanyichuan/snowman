package cc.kevinlu.snow.server.pojo;

import java.io.Serializable;
import java.util.List;

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
public class PersistentBO<T> implements Serializable {

    private Long    instanceId;

    private List<T> idList;

    /**
     * 0: no, 1: yes
     */
    private Boolean used;

    @Tolerate
    public PersistentBO() {
    }
}
