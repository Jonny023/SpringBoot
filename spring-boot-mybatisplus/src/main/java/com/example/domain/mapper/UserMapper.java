package com.example.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.base.dto.UserDTO;
import com.example.domain.entity.User;

/**
 * <p>
 * 用户信息 Mapper 接口
 * </p>
 *
 * @author Jonny
 * @since 2021-09-18
 */
public interface UserMapper extends BaseMapper<User> {
    UserDTO queryUserByUsername(String username);
}