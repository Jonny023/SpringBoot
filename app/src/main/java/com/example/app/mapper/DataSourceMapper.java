package com.example.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.app.domain.entity.SysDb;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DataSourceMapper extends BaseMapper<SysDb> {

    @Select("SELECT * FROM sys_db")
    List<SysDb> list();
}