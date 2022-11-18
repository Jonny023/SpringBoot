package com.example.springbootshiro.configuration.shiro.config;

import com.example.springbootshiro.configuration.shiro.filter.JwtTokenFilter;
import com.example.springbootshiro.configuration.shiro.realm.UserRealm;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShiroConfiguration {

    @Bean
    public UserRealm userRealm() {
        UserRealm userRealm = new UserRealm();
        return userRealm;
    }

    @Bean("securityManager")
    public DefaultWebSecurityManager getManager(UserRealm realm) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();

        //使用自己的realm
        manager.setRealm(realm);

        //关闭shiro自带的session，详情见文档
        //http://shiro.apache.org/session-management.html#SessionManagement-StatelessApplications%28Sessionless%29
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        manager.setSubjectDAO(subjectDAO);

        return manager;
    }

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean factory(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();

        //添加自己的过滤器并且取名为token
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("jwt", new JwtTokenFilter());
        factoryBean.setFilters(filterMap);
        factoryBean.setSecurityManager(securityManager);

        //自定义url规则: http://shiro.apache.org/web.html#urls-
        Map<String, String> filterRuleMap = new HashMap<>();

        //swagger
        filterRuleMap.put("/doc.html", "anon");
        filterRuleMap.put("/**/*.js", "anon");
        filterRuleMap.put("/**/*.png", "anon");
        filterRuleMap.put("/**/*.ico", "anon");
        filterRuleMap.put("/**/*.css", "anon");
        filterRuleMap.put("/**/ui/**", "anon");
        filterRuleMap.put("/**/swagger-resources/**", "anon");
        filterRuleMap.put("/**/api-docs/**", "anon");

        //登录
        filterRuleMap.put("/api/user/login", "anon");
        filterRuleMap.put("/api/user/verifyCode", "anon");

        // 所有请求通过我们自己的JWT token Filter
        filterRuleMap.put("/**", "jwt");
        factoryBean.setFilterChainDefinitionMap(filterRuleMap);
        return factoryBean;
    }

    /**
     * setUsePrefix(false)用于解决一个奇怪的bug。在引入spring aop的情况下。
     * 在@Controller注解的类的方法中加入@RequiresRole等shiro注解，会导致该方法无法映射请求，导致返回404。
     * 加入这项配置能解决这个bug
     */
    @Bean
    public static DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator=new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setUsePrefix(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

}
