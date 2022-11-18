package com.example.springbootdynamicdatasourceck.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springbootdynamicdatasourceck.domain.entity.Event2;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Event1Mapper extends BaseMapper<Event2> {
    List<Event2> list();
}
