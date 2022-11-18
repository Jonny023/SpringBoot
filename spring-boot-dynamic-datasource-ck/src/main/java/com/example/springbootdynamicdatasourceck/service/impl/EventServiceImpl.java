package com.example.springbootdynamicdatasourceck.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springbootdynamicdatasourceck.constant.Const;
import com.example.springbootdynamicdatasourceck.domain.entity.Event;
import com.example.springbootdynamicdatasourceck.domain.mapper.Event2Mapper;
import com.example.springbootdynamicdatasourceck.service.EventService;
import org.springframework.stereotype.Service;

import java.util.List;

@DS(Const.CK1)
@Service
public class EventServiceImpl extends ServiceImpl<Event2Mapper, Event> implements EventService {

    @Override
    public List<Event> queryAll() {
        return baseMapper.list();
    }
}