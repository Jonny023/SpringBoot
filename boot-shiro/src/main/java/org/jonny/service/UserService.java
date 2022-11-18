package org.jonny.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jonny.entity.User;
import org.jonny.mapper.UserMapper;
import org.jonny.vo.UserVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public UserVO getById(Long id) {
//        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
//        wrapper.select(User::getId, User::getUsername, User::getEmail, User::getAvatar, User::getCreated, User::getLastLogin, User::getStatus)
//        wrapper.select(User.class, u -> !"password".equals(u.getColumn()))
//                .eq(User::getId, id);
//        User user = userMapper.selectOne(wrapper);
//        System.out.println(user);
        return userMapper.getById(id);
    }

    @Override
    public List<User> list() {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.select(User::getId, User::getUsername, User::getEmail, User::getAvatar, User::getCreated, User::getLastLogin, User::getStatus);
        wrapper.select(User.class, u -> !"password".equals(u.getColumn()));
        return userMapper.selectList(wrapper);
    }
}
