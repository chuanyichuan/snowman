package cc.kevinlu.snow.server.web;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import cc.kevinlu.snow.client.exceptions.ParamIllegalException;
import cc.kevinlu.snow.client.snowflake.SnowflakeClient;
import cc.kevinlu.snow.server.service.SnowflakeService;

@RestController
public class SnowflakeController implements SnowflakeClient {

    @Autowired
    private SnowflakeService snowflakeService;

    @Override
    public List<Object> generate(String groupCode, String instanceCode) {
        if (StringUtils.isAnyBlank(groupCode, instanceCode)) {
            throw new ParamIllegalException("groupCode or instanceCode is null!");
        }
        return snowflakeService.generate(groupCode, instanceCode);
    }
}
