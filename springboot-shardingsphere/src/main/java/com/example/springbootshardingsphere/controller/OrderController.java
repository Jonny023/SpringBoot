package com.example.springbootshardingsphere.controller;

import com.example.springbootshardingsphere.domain.entity.Order;
import com.example.springbootshardingsphere.domain.request.OrderRequest;
import com.example.springbootshardingsphere.service.OrderService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody Order order) {
        orderService.createOrder(order);
        return ResponseEntity.ok("success");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(orderService.findById(id));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Order> orderList() {
        return orderService.orderList();
    }

    @PostMapping("listPage")
    public ResponseEntity<PageInfo<Order>> createOrder(@RequestBody OrderRequest param) {
        return ResponseEntity.ok(orderService.listPage(param));
    }
}