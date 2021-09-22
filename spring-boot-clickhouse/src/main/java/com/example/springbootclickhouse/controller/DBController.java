package com.example.springbootclickhouse.controller;

import com.example.springbootclickhouse.service.IDbService;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: e-lijing6
 * @description:
 * @date:created in 2021/4/27 14:19
 * @modificed by:
 */
@RequestMapping("/db")
@RestController
public class DBController {

  @Resource
  private IDbService dbService;

  @GetMapping("/query")
  public List<Map<String, Object>> query(String sql) {
    return dbService.query(sql);
  }
}
