package org.jonny.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jonny.entity.User;
import org.jonny.vo.UserVO;

public interface UserMapper extends BaseMapper<User> {

    @Select("select * from sys_user where id = #{id}")
    UserVO getById(@Param("id")Long id);
}