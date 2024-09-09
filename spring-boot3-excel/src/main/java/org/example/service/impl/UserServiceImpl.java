package org.example.service.impl;

import org.example.domain.vo.UserVO;
import org.example.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Jonny
 */
@Service
public class UserServiceImpl implements UserService {

    private static final String[] NAMES = {"刘德华", "陈龙", "吴彦祖", "甄子丹", "李连杰", "周星驰", "周润发", "梁朝伟", "张学友", "黎明"};
    private static final String[] ADDRESSES = {"北京", "上海", "广州", "深圳", "杭州", "南京", "武汉", "成都", "重庆", "西安"};

    @Override
    public List<UserVO> list() {
        return Stream.iterate(1, i -> i + 1).limit(200).map(i -> UserVO.builder()
                .id(Long.valueOf(i))
                .name(NAMES[new Random().nextInt(10)])
                .address(ADDRESSES[new Random().nextInt(10)])
                .build()).collect(Collectors.toList());
    }
}
