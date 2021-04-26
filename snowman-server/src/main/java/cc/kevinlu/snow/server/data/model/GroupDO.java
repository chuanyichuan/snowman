package cc.kevinlu.snow.server.data.model;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
* @author chuan
* @time 2021-04-26
*/
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GroupDO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 服务组编号
     */
    private String groupCode;

    /**
     * 服务组每次获取ID数量
     */
    private Integer chunk;

    /**
     * 服务组最近一次获取的ID最大值
     */
    private Long lastValue;

    /**
     * gmt_created
     */
    private Date gmtCreated;

    /**
     * gmt_updated
     */
    private Date gmtUpdated;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", groupCode=").append(groupCode);
        sb.append(", chunk=").append(chunk);
        sb.append(", lastValue=").append(lastValue);
        sb.append(", gmtCreated=").append(gmtCreated);
        sb.append(", gmtUpdated=").append(gmtUpdated);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}