package org.example.mapper;

import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    void update(@Param("tasks") List<TaskEntity> tasks);
}