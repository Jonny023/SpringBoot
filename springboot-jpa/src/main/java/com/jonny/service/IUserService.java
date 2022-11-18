package com.jonny.service;

import com.jonny.entity.User;
import com.jonny.base.ResponseApi;

public interface IUserService {

    ResponseApi list();

    ResponseApi save(User user);

    ResponseApi update(User user);

    ResponseApi delete(Long id);

    ResponseApi findByUsername(String username);

    ResponseApi findByUsernameAndName(String username, String name);

    ResponseApi queryUsernames();

    ResponseApi queryColByUsernameAndName(String username, String name);

    ResponseApi userList(String username, String name);

    ResponseApi sqlQuery(String username, String name);
}
