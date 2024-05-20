package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfig {

    // @Bean
    // public UserDetailsService userDetailsService() {
    //     User.UserBuilder users = User.withDefaultPasswordEncoder();
    //     UserDetails user = users.username("user")
    //             .password("{noop}pass")
    //             .authorities("ROLE_ACTIVITI_USER")
    //             .build();
    //     return new InMemoryUserDetailsManager(user);
    // }

    // @Bean
    // public PasswordEncoder passwordEncoder() {
    //     return new BCryptPasswordEncoder();
    // }
}