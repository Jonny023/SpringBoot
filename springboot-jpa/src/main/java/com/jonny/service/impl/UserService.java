package com.jonny.service.impl;

import com.jonny.entity.User;
import com.jonny.repository.UserRepository;
import com.jonny.response.ResponseApi;
import com.jonny.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseApi list() {
        List<User> users = userRepository.findAll();
        return ResponseApi.success(users);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseApi save(User user) {
        System.out.println(user);
        userRepository.save(user);
        return ResponseApi.success(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseApi update(User user) {
        userRepository.save(user);
        return ResponseApi.success(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseApi delete(Long id) {
        userRepository.deleteById(id);
        return ResponseApi.success("删除成功");
    }

    @Override
    public ResponseApi findByUsername(String username) {
        return ResponseApi.success(userRepository.findByUsername(username));
    }

    @Override
    public ResponseApi findByUsernameAndName(String username, String name) {
        return ResponseApi.success(userRepository.findByUsernameAndName(username, name));
    }

    @Override
    public ResponseApi queryUsernames() {
        return ResponseApi.success(userRepository.queryUsernames());
    }

    @Override
    public ResponseApi queryColByUsernameAndName(String username, String name) {
        return ResponseApi.success(userRepository.queryColByUsernameAndName(username, name));
    }

    @Override
    public ResponseApi userList(String username, String name) {
        return ResponseApi.success(userRepository.userList(username, name));
    }

    @Override
    public ResponseApi sqlQuery(String username, String name) {
        return ResponseApi.success(userRepository.sqlQuery(username, name));
    }

}
