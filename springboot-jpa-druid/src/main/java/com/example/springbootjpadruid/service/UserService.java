package com.example.springbootjpadruid.service;

import com.example.springbootjpadruid.domain.common.PageVO;
import com.example.springbootjpadruid.domain.entity.primary.User;
import com.example.springbootjpadruid.domain.query.UserQuery;
import com.example.springbootjpadruid.domain.vo.UserVO;

import java.util.List;

public interface UserService {

    String init(List<User> users);

    User getUser();

    PageVO<UserVO> listPage(UserQuery param);

    PageVO<UserVO> nativeListPage(UserQuery param);

    PageVO<UserVO> listPageByJpaUtil(UserQuery param);
}