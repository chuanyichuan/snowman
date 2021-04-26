package cc.kevinlu.snow.autoconfigure;

import java.util.List;

/**
 * kit for snowman-client
 * 
 * @author chuan
 */
public class SnowmanClient {

    private SnowmanProperties properties;

    public SnowmanClient(SnowmanProperties properties) {
        this.properties = properties;
    }

    /**
     * generate snowflake ID
     * 
     * @return
     */
    public List<Long> generateSnowId() {
        return null;
    }

}
