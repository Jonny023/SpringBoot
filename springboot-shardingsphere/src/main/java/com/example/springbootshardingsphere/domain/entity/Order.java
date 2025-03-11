package com.example.springbootshardingsphere.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("orders")
public class Order {

    private Long id;
    private Long userId;
    private String orderNo;
    private BigDecimal totalAmount;
    private LocalDateTime createTime;
}