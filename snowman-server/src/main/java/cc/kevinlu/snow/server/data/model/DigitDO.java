package cc.kevinlu.snow.server.data.model;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
* @author chuan
* @time 2021-04-27
*/
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DigitDO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 服务实例ID
     */
    private Long serviceInstanceId;

    /**
     * 服务实例本次获取ID的数量
     */
    private Integer chunk;

    /**
     * ID起始值(含括)
     */
    private Long fromValue;

    /**
     * ID结束值(含括)
     */
    private Long toValue;

    /**
     * gmt_created
     */
    private Date gmtCreated;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", serviceInstanceId=").append(serviceInstanceId);
        sb.append(", chunk=").append(chunk);
        sb.append(", fromValue=").append(fromValue);
        sb.append(", toValue=").append(toValue);
        sb.append(", gmtCreated=").append(gmtCreated);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}