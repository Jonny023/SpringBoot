package com.example.springbootclickhouse.entity;

import com.example.springbootclickhouse.utils.SqlWrapperUtil;
import java.util.Arrays;
import java.util.List;

/**
 * @author: e-lijing6
 * @description:q
 * @date:created in 2021/4/29 9:57
 * @modificed by:
 */
public class Te {

  public static void main(String[] args) throws InstantiationException, IllegalAccessException {
    User u1 = new User().setId(1L).setUsername("zhangsan1").setAddr("HongKong1");
    User u2 = new User().setId(2L).setUsername("zhangsan2").setAddr("HongKong2");
    User u3 = new User().setId(3L).setUsername("zhangsan3").setAddr("HongKong3");
    List<User> users = Arrays.asList(u1, u2, u3);
    System.out.println(SqlWrapperUtil.buildInsert(User.class));
  }
}