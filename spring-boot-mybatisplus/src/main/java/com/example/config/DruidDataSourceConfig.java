package com.example.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DruidDataSourceConfig {

//    private Logger LOG = LoggerFactory.getLogger(DruidDataSourceConfig.class);

//    @Value("${mybatis.mapper-locations}")
//    private String location;

//    @Resource
//    private PaginationInterceptor paginationInterceptor;

    @Primary
    @Bean(value = "dataSource")
    @ConfigurationProperties("spring.datasource.master")
    public DataSource dataSource() {
        return new DruidDataSource();
    }

//    @Bean
//    @ConfigurationProperties(prefix = "mybatis-plus.configuration")
//    public org.apache.ibatis.session.Configuration configuration() {
//        return new org.apache.ibatis.session.Configuration();
//    }
//
//    @Bean(name = "transactionManager")
//    public DataSourceTransactionManager transactionManager(){
//        return new DataSourceTransactionManager(dataSource());
//    }

//    public PageInterceptor pageInterceptor(){
//        LOG.info("register PageHelper plugins finish.");
//        PageInterceptor pageInterceptor = new PageInterceptor();
//        Properties properties = new Properties();
//        properties.setProperty("helperDialect", "mysql");
//        properties.setProperty("offsetAsPageNum", "true");
//        properties.setProperty("rowBoundsWithCount", "true");
//        pageInterceptor.setProperties(properties);
//        return pageInterceptor;
//    }

//    @Bean(name = "sqlSessionFactory")
//    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource, org.apache.ibatis.session.Configuration configuration) throws Exception {
//        final SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
//        sessionFactoryBean.setDataSource(dataSource);
////        sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(location));
//        sessionFactoryBean.setConfiguration(configuration);
//
//        //设置MyBatis分页插件
////        PageInterceptor pageInterceptor = this.pageInterceptor();
//        sessionFactoryBean.setPlugins(new Interceptor[]{paginationInterceptor});
//        return sessionFactoryBean.getObject();
//    }

}