package com.example.springbootjpadruid.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.slf4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {


    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DataSourceConfig.class);

    @Primary
    @Bean(value = "primaryDataSource",initMethod = "init")
    @ConfigurationProperties("spring.datasource.druid.primary") //标红为yml文件中数据源路径:primary
    public DataSource dataSourceOne(){
        log.info("Init DataSourceOne");
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(value = "secondDataSource",initMethod = "init")
    @ConfigurationProperties("spring.datasource.druid.secondary")//标红为yml文件中数据源路径：secondary
    public DataSource dataSourceTwo(){
        log.info("Init DataSourceTwo");
        return DruidDataSourceBuilder.create().build();
    }

}
