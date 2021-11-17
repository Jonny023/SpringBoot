package com.example.springbootdynamicdatasourceck.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springbootdynamicdatasourceck.domain.entity.Event2;

import java.util.List;

public interface Event2Service extends IService<Event2> {

    List<Event2> queryAll();
}
