package com.example.service.impl;

import com.example.domain.entity.User;
import com.example.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Jonny
 */
@Service
public class UserServiceImpl implements UserDetailsService, UserService {


    @Override
    public User findByEmail(String email) {
        return findUser();
    }

    @Override
    public User findByPhone(String phone) {
        return findUser();
    }

    @Override
    public User findByUsername(String username) {
        return findUser();
    }

    private User findUser() {
        // 密码调用passwordEncoder.encode("123456")生成
        return User.builder().id(1L).username("jonny").email("123@qq.com").phone("13000000000").password("$2a$10$nGdV.wluA4/L0IBY3fDKNeuY3XPkQy5BKWuPwVDg9HvV8Wawq2qaW").build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return (UserDetails) findByUsername(username);
    }
}
