package com.example.webservicecore.config;

import com.example.webservicecore.service.UserService;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.xml.ws.Endpoint;

/**
 * https://www.cnblogs.com/fishpro/p/spring-boot-study-webservice.html
 * https://www.jianshu.com/p/31ccff0e1cab
 */
@Configuration
public class CxfWebServiceConfiguration {

    @Resource
    private Bus bus;
    @Resource
    private UserService userService;


    /**
     * JAX-WS
     * 站点服务
     */
    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, userService);
        endpoint.publish("/user");//发布地址
        return endpoint;
    }

}
