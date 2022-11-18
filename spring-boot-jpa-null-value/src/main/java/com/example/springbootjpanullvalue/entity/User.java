package com.example.springbootjpanullvalue.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 *  用户
 */
@Data
@Entity
@Accessors(chain = true)
@Table(name = "sys_user")
public class User {

    @Id
    @Column(name = "id")
    // 不能设置主键生成策略
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "addr")
    private String addr;
}
