package cc.kevinlu.snow.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * snowman配置项
 * 
 * @author chuan
 */
@ConfigurationProperties(prefix = SnowmanConstants.CONFIG_PREFIX)
public class SnowmanProperties {

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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getChunk() {
        return chunk;
    }

    public void setChunk(int chunk) {
        this.chunk = chunk;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    @Override
    public String toString() {
        return "SnowmanProperties{" + "host='" + host + '\'' + ", port=" + port + ", path='" + path + '\'' + ", chunk="
                + chunk + ", timeout=" + timeout + ", groupId='" + groupId + '\'' + ", serverId='" + serverId + '\''
                + '}';
    }
}
