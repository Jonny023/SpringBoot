package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.domain.entity.SysUser;
import org.example.domain.request.UserPageReq;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<SysUser> {

    List<SysUser> userList(@Param("req") UserPageReq req);
}