package com.example.service.impl;

import com.example.base.service.impl.BaseServiceImpl;
import com.example.domain.entity.User;
import com.example.domain.mapper.UserMapper;
import com.example.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户信息 服务实现类
 * </p>
 *
 * @author Jonny
 * @since 2021-09-18
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User> implements IUserService {

}