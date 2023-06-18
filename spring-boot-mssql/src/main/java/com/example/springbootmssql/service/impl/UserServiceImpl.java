package com.example.springbootmssql.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springbootmssql.entity.User;
import com.example.springbootmssql.mapper.UserMapper;
import com.example.springbootmssql.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public PageInfo<User> listPage(PageDTO pageDTO) {
        List<Long> ids = Stream.iterate(1L, i -> i + 1).limit(2200).map(id -> Long.valueOf(String.valueOf(id))).collect(Collectors.toList());
        List<List<Long>> result = Lists.partition(ids, 500);
        return PageHelper.startPage((int) pageDTO.getCurrent(), (int) pageDTO.getSize()).doSelectPageInfo(() -> baseMapper.listPage(result));
    }
}