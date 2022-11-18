package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.base.BasePageRequestVO;
import com.example.base.ResultPageVO;
import com.example.base.ResultVO;
import com.example.base.dto.UserDTO;
import com.example.domain.entity.User;
import com.example.domain.mapper.UserMapper;
import com.example.service.IUserService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 用户信息 前端控制器
 * </p>
 *
 * @author Jonny
 * @since 2021-09-18
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;
    @Resource
    private UserMapper userMapper;

    @PostMapping("/getUser")
    public User getUser() {
        return userService.getById(1);
    }

    @PostMapping("/findUsername")
    public ResultVO<UserDTO> findUsername(String username) {
        return ResultVO.ok(userMapper.queryUserByUsername(username));
    }

    @PostMapping("/pageList")
    public ResultPageVO<User> list(BasePageRequestVO param) {
        return ResultPageVO.ok(userService.listByPage(param, new QueryWrapper<>()));
    }

}
