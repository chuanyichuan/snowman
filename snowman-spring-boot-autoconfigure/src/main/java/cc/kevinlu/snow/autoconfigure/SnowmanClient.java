package cc.kevinlu.snow.autoconfigure;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;

import cc.kevinlu.snow.autoconfigure.constants.UrlConstants;
import cc.kevinlu.snow.autoconfigure.utils.HttpClientUtils;
import cc.kevinlu.snow.client.enums.IdAlgorithmEnums;
import lombok.extern.slf4j.Slf4j;

/**
 * kit for snowman-client
 *
 * @author chuan
 */
@Slf4j
public class SnowmanClient {

    private SnowmanProperties properties;

    private String            base_url;

    public SnowmanClient(SnowmanProperties properties) {
        this.properties = properties;
        base_url = properties.getProtocol() + "://" + properties.getHost() + ":" + properties.getPort()
                + properties.getPath();

        registerToServer();
    }

    /**
     * generate snowflake ID
     *
     * @return
     */
    public List<Object> generateSnowId() {
        HttpClientUtils client = HttpClientUtils.getInstance(Charset.defaultCharset());
        String url = String.format(UrlConstants.SNOWFLAKE_API, this.properties.getGroupId(),
                this.properties.getServerId());

        try {
            String result = client.doGet(base_url + url);
            log.info("snowman client get id records success ");
            return JSONArray.parseArray(result, Object.class);
        } catch (Exception e) {
            throw new RuntimeException("snowman client register error!");
        }
    }

    /**
     * register client to server
     */
    public void registerToServer() {
        HttpClientUtils client = HttpClientUtils.getInstance(Charset.defaultCharset());

        Map<String, Object> params = new HashMap<>();
        params.put("name", this.properties.getName());
        params.put("groupCode", this.properties.getGroupId());
        params.put("instanceCode", this.properties.getServerId());
        params.put("chunk", this.properties.getChunk());

        IdAlgorithmEnums algorithm = algorithmEnums(this.properties.getMode());
        params.put("mode", algorithm);

        try {
            String result = client.doPostJson(base_url + UrlConstants.REGISTER_API, params);
            log.info("snowman client register: " + result);
        } catch (Exception e) {
            throw new RuntimeException("snowman client register error!");
        }
    }

    private IdAlgorithmEnums algorithmEnums(String mode) {
        if ("snowflake".equalsIgnoreCase(mode)) {
            return IdAlgorithmEnums.SNOWFLAKE;
        } else if ("uuid".equalsIgnoreCase(mode)) {
            return IdAlgorithmEnums.UUID;
        } else {
            return IdAlgorithmEnums.DIGIT;
        }
    }
}
