package cc.kevinlu.snow.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import lombok.ToString;

/**
 * snowman配置项
 * 
 * @author chuan
 */
@Data
@ToString
@ConfigurationProperties(prefix = SnowmanConstants.CONFIG_PREFIX)
public class SnowmanProperties {

    /**
     * 项目名称
     */
    private String name;

    /**
     * snowman服务地址,默认localhost
     */
    private String host    = "localhost";

    /**
     * snowman服务端口,默认8080
     */
    private int    port    = 8080;

    /**
     * snowman请求路由
     */
    private String path    = "/";

    /**
     * 每次获取ID数量,默认10
     */
    private int    chunk   = 10;

    /**
     * ID生成算法,包括数字、雪花算法、UUID
     */
    private String mode    = "normal";

    /**
     * 请求超时时间,单位毫秒,默认30秒
     */
    private long   timeout = 30000;

    /**
     * 客户端组ID,必须项
     */
    private String groupId;

    /**
     * 客户端ID,必须项
     */
    private String serverId;

}
