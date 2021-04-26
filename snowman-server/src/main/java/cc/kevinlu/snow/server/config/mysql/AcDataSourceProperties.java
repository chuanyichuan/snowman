package cc.kevinlu.snow.server.config.mysql;

import org.springframework.context.annotation.Configuration;

import com.alibaba.nacos.api.config.annotation.NacosValue;

@Configuration
//@ConfigurationProperties(prefix = "spring.datasource")
public class AcDataSourceProperties {

    @NacosValue("${spring.datasource.url:}")
    private String url;

    @NacosValue("${spring.datasource.username:}")
    private String username;

    @NacosValue("${spring.datasource.password:}")
    private String password;

    @NacosValue("${spring.datasource.driver-class-name:}")
    private String driverClassName;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }
}
