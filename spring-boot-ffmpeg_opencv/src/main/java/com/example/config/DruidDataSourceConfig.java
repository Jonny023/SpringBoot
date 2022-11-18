package com.example.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DruidDataSourceConfig {

    @Primary
    @Bean(value = "dataSource")
    @ConfigurationProperties("spring.datasource.master")
    public DataSource dataSource(){
        return new DruidDataSource();
    }

}