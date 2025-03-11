package com.example.springbootshardingsphere.service;

import com.example.springbootshardingsphere.domain.entity.Order;
import com.example.springbootshardingsphere.domain.request.OrderRequest;
import com.example.springbootshardingsphere.mapper.OrderMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    public void createOrder(Order order) {
        orderMapper.insert(order);
    }

    public List<Order> orderList() {
        return orderMapper.selectList(null);
    }

    public Order findById(Long id) {
        return orderMapper.selectById(id);
    }

    /**
     * 分页查询
     *
     * @param param 查询参数
     * @return 分页数据
     */
    public PageInfo<Order> listPage(OrderRequest param) {
        PageHelper.startPage(param.getPageNo(), param.getPageSize());
        List<Order> list = orderMapper.listPage(param);
        return new PageInfo<>(list);
    }
}