package com.example.springbootdynamicdatasourceck.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springbootdynamicdatasourceck.domain.entity.Event;

import java.util.List;

public interface EventService extends IService<Event> {

    List<Event> queryAll();
}
