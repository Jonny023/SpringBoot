package com.example.springbootshiro.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String email;

    private String username;

    private String password;

    private String realName;

    private String phone;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
