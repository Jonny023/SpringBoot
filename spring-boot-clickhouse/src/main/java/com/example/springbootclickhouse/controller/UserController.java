package com.example.springbootclickhouse.controller;

import com.example.springbootclickhouse.base.BaseRepository;
import com.example.springbootclickhouse.entity.User;
import com.example.springbootclickhouse.repository.UserRepository;
import com.example.springbootclickhouse.utils.SqlWrapperUtil;
import com.google.common.collect.Lists;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import javax.annotation.Resource;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

  @Resource
  private JdbcTemplate jdbcTemplate;
  @Resource
  private UserRepository userRepository;
  @Resource
  private BaseRepository<User, Long> baseRepository;

  @GetMapping("/save")
  public String save() {
    User user = new User().setId(1L).setUsername("admin").setAddr("China1");
    userRepository.save(user);
    return "save success";
  }

  @GetMapping("/save1")
  public String save1() {
    User user = new User().setId(1L).setAddr("China1");
    userRepository.save(user);
    return "save success";
  }

  @GetMapping("/batch")
  public String batch() {
    long start = System.currentTimeMillis();
    List<User> users = new ArrayList<>();
    User user = null;
    Timestamp now = new Timestamp(System.currentTimeMillis());
    for (long i = 1; i <= 100000; i++) {
      user = new User().setId(i).setUsername("admin_" + i).setAddr("China_" + i).setCreateTime(now);
      users.add(user);
    }
    //按每500一组分割
    List<List<User>> parts = Lists.partition(users, 500);
    parts.stream().forEach(list -> {
      userRepository.batchInsert(list);
    });
    double total = (System.currentTimeMillis() - start) / 1000;
    return "batch save success, time: " + total + "s";
  }

  @GetMapping("/list")
  public List<User> list() {
    return userRepository.findAll();
  }

  @GetMapping("/delete/{id}")
  public String delete(@PathVariable("id") Long id) {
    int i = baseRepository.delById("sys_user", id);
    System.out.printf("影响行数：%d\n", i);
    return "delete success";
  }

  @GetMapping("/saveAll")
  public String saveAll() throws InstantiationException, IllegalAccessException {
    long start = System.currentTimeMillis();
    List<User> users = new ArrayList<>();
    User user = null;
    Timestamp now = new Timestamp(System.currentTimeMillis());
    for (long i = 1; i <= 100000; i++) {
      user = new User().setId(i).setUsername("admin_" + i).setAddr("China_" + i).setCreateTime(now);
      users.add(user);
    }
    int fieldSize = SqlWrapperUtil.fieldSize(User.class);
    List<List<Map<String, Object>>> dataList = SqlWrapperUtil.collect(users);
    jdbcTemplate.batchUpdate(SqlWrapperUtil.buildInsert(User.class),
        new BatchPreparedStatementSetter() {
          @Override
          public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
            Stream.iterate(0, n -> n + 1).limit(fieldSize).forEach(n -> {
              try {
                preparedStatement.setObject(n + 1, dataList.get(i).get(n).get("value"));
              } catch (SQLException sqlException) {
                sqlException.printStackTrace();
              }
            });
          }

          @Override
          public int getBatchSize() {
            return users.size();
          }
        });

    System.out.println("=====完成=====");
    double total = (System.currentTimeMillis() - start) / 1000;
    return "batch save success, time: " + total + "s";
  }

}
