package cc.kevinlu.snow.autoconfigure;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cc.kevinlu.snow.autoconfigure.constants.UrlConstants;
import cc.kevinlu.snow.autoconfigure.utils.HttpClientUtils;
import cc.kevinlu.snow.client.enums.IdAlgorithmEnums;
import cc.kevinlu.snow.client.instance.pojo.ServiceInfo;
import lombok.extern.slf4j.Slf4j;

/**
 * kit for snowman-client
 *
 * @author chuan
 */
@Slf4j
public class SnowmanClient {

    private SnowmanProperties properties;

    private String            urlInstances;
    private String            urlRegister;
    private String            urlGenerate;

    public SnowmanClient(SnowmanProperties properties) {
        this.properties = properties;

        // set url for standby
        initRequestUrl();

        // register the client to snowman server
        registerToServer();
    }

    private void initRequestUrl() {
        String baseUrl = properties.getProtocol() + "://" + properties.getHost() + ":" + properties.getPort()
                + properties.getPath();

        urlInstances = baseUrl + UrlConstants.INSTANCE_API;
        urlRegister = baseUrl + UrlConstants.REGISTER_API;
        urlGenerate = baseUrl
                + String.format(UrlConstants.SNOWFLAKE_API, properties.getGroupId(), properties.getServerId());
    }

    /**
     * generate snowflake ID
     *
     * @return
     */
    public List<Object> generateSnowId() {
        HttpClientUtils client = HttpClientUtils.getInstance(Charset.defaultCharset());

        try {
            String result = client.doGet(urlGenerate);
            log.info("snowman client get id records success ");
            return JSONArray.parseArray(result, Object.class);
        } catch (Exception e) {
            throw new RuntimeException("snowman client register error!");
        }
    }

    /**
     * show all instances in the group
     *
     * @return
     */
    public ServiceInfo showGroupInstances() {
        HttpClientUtils client = HttpClientUtils.getInstance(Charset.defaultCharset());

        try {
            String result = client.doGet(urlInstances);
            log.info("snowman client get all instances success ");
            return JSONObject.parseObject(result, ServiceInfo.class);
        } catch (Exception e) {
            throw new RuntimeException("snowman client register error!");
        }
    }

    /**
     * register client to server
     */
    public void registerToServer() {
        HttpClientUtils client = HttpClientUtils.getInstance(Charset.defaultCharset());

        Map<String, Object> params = new HashMap<>(8);
        params.put("name", this.properties.getName());
        params.put("groupCode", this.properties.getGroupId());
        params.put("instanceCode", this.properties.getServerId());
        params.put("chunk", this.properties.getChunk());

        IdAlgorithmEnums algorithm = algorithmEnums(this.properties.getMode());
        params.put("mode", algorithm);

        try {
            String result = client.doPostJson(urlRegister, params);
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
