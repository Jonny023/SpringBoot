package com.example.springbootdynamicdatasourceck.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springbootdynamicdatasourceck.domain.entity.Event;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Event2Mapper extends BaseMapper<Event> {
    List<Event> list();
}
