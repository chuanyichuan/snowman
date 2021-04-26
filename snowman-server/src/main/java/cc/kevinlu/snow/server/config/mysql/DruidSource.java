package cc.kevinlu.snow.server.config.mysql;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;

import lombok.Data;

@Data
@NacosConfigurationProperties(prefix = "spring.datasource.druid", dataId = "open-api")
public class DruidSource {
    private String  dbUrl;

    private String  username;

    private String  password;

    private String  driverClassName;

    private int     initialSize;

    private int     minIdle;

    private int     maxActive;

    private int     maxWait;

    private int     timeBetweenEvictionRunsMillis;

    private int     minEvictableIdleTimeMillis;
    private String  validationQuery;

    private boolean testWhileIdle;
    private boolean testOnBorrow;

    private boolean testOnReturn;

    private boolean poolPreparedStatements;

    private int     maxPoolPreparedStatementPerConnectionSize;

    private String  filters;

    private String  connectionProperties;

    @Bean
    @Primary
    public DataSource dataSource() throws SQLException {
        DruidDataSource datasource = new DruidDataSource();

        datasource.setUrl(this.dbUrl);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName);

        //configuration
        datasource.setInitialSize(initialSize);
        datasource.setMinIdle(minIdle);
        datasource.setMaxActive(maxActive);
        datasource.setMaxWait(maxWait);
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setValidationQuery(validationQuery);
        datasource.setTestWhileIdle(testWhileIdle);
        datasource.setTestOnBorrow(testOnBorrow);
        datasource.setTestOnReturn(testOnReturn);
        datasource.setPoolPreparedStatements(poolPreparedStatements);
        datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);

        datasource.setFilters(filters);

        return datasource;
    }
}
