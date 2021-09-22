package com.example.springbootclickhouse.service.impl;

import com.example.springbootclickhouse.service.IDbService;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: e-lijing6
 * @description:
 * @date:created in 2021/4/27 14:00
 * @modificed by:
 */
@Service
public class DbServiceImpl implements IDbService {

  @Resource
  private JdbcTemplate jdbcTemplate;

  @Override
  @Transactional(readOnly = true)
  public List<Map<String, Object>> query(String sql) {
    List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
    return maps;
  }
}
