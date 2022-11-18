package com.example.springbootdynamicdatasourceck.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springbootdynamicdatasourceck.constant.Const;
import com.example.springbootdynamicdatasourceck.domain.entity.Event2;
import com.example.springbootdynamicdatasourceck.domain.mapper.Event1Mapper;
import com.example.springbootdynamicdatasourceck.service.Event2Service;
import org.springframework.stereotype.Service;

import java.util.List;

@DS(Const.CK2)
@Service
public class Event2ServiceImpl extends ServiceImpl<Event1Mapper, Event2> implements Event2Service {

    @Override
    public List<Event2> queryAll() {
        return baseMapper.list();
    }
}