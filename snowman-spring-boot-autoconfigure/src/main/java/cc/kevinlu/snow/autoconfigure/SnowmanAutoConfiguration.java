package cc.kevinlu.snow.autoconfigure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ SnowmanClient.class })
@EnableConfigurationProperties({ SnowmanProperties.class })
public class SnowmanAutoConfiguration {

    @Autowired
    private SnowmanProperties properties;

    public SnowmanClient snowmanClient() {
        SnowmanClient client = new SnowmanClient(properties);
        return client;
    }

}
