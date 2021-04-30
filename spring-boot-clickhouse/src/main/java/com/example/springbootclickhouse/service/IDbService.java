package com.example.springbootclickhouse.service;

import java.util.List;
import java.util.Map;

/**
 * @author: e-lijing6
 * @description: 数据源连接
 * @date:created in 2021/4/27 13:59
 * @modificed by:
 */
public interface IDbService {

  List<Map<String, Object>> query(String sql);
}
