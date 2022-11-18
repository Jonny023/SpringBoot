package com.example.springbootclickhouse.repository;

import com.example.springbootclickhouse.base.BaseRepository;
import com.example.springbootclickhouse.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends BaseRepository<User, Long> {

    @Query(value = "alter table sys_user delete where id=:id", nativeQuery = true)
    int delUser(@Param("id") Long id);
}
