package cc.kevinlu.snow.server.config.mysql;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 *  多数据库DataSource 配置文件信息
 *  
 *  @author
 *  @date 2019/03/21
 */
@Configuration
@MapperScan(basePackages = { "cc.kevinlu.snow.server.data.mapper" }, sqlSessionFactoryRef = "sqlSessionFactory")
public class DataSourceConfig {

    @Resource
    private AcDataSourceProperties acDataSourceProperties;

    @Bean(name = "dataSourceCode")
    @Primary
    public DataSource dataSourceCode() {
        DataSourceBuilder builder = DataSourceBuilder.create();
        builder.url(acDataSourceProperties.getUrl());
        builder.username(acDataSourceProperties.getUsername());
        builder.password(acDataSourceProperties.getPassword());
        builder.driverClassName(acDataSourceProperties.getDriverClassName());
        return builder.build();
    }

    @Bean(name = "sqlSessionFactory")
    @Autowired
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSourceCode") DataSource dataSourceTest)
            throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSourceTest);
        factoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/*.xml"));
        return factoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("dataSourceCode") DataSource dataSourceHatsUser)
            throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory(dataSourceHatsUser));
    }

    @Bean(name = "code_transaction")
    public PlatformTransactionManager prodTransactionManager(@Qualifier("dataSourceCode") DataSource dataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource);
        dataSourceTransactionManager.setNestedTransactionAllowed(true);
        return dataSourceTransactionManager;
    }

}
