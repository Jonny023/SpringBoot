package com.example.app.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class SysDb {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String url;
    private String username;
    private String password;
    private String type;
}
