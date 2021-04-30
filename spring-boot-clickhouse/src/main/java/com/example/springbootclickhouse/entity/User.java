package com.example.springbootclickhouse.entity;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
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
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "addr")
    private String addr;

    @Column(name = "create_time")
    private Timestamp createTime;
}
