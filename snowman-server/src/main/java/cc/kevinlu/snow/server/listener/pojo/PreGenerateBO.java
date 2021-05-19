package cc.kevinlu.snow.server.listener.pojo;

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
public class PreGenerateBO implements Serializable {

    /**
     * groupId of service
     */
    private String  group;

    /**
     * instanceID of service
     */
    private String  instance;

    /**
     * message times
     */
    private Integer times;

    @Tolerate
    public PreGenerateBO() {
    }
}
