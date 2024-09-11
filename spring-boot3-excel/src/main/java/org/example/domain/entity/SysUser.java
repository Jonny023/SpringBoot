package org.example.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import static com.baomidou.mybatisplus.annotation.IdType.AUTO;

@Data
@TableName("user")
public class SysUser {

    @TableId(value = "id", type = AUTO)
    private Long id;

    private String name;

}