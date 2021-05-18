package com.example.springbootclickhousenoneid.controller;

import com.example.springbootclickhousenoneid.base.ResultDTO;
import com.example.springbootclickhousenoneid.entity.DwsWebSummaryCounts;
import com.example.springbootclickhousenoneid.entity.QDwsWebSummaryCounts;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

  @Resource
  private JPAQueryFactory jpaQueryFactory;

  @GetMapping("/")
  public ResultDTO index() {
    QDwsWebSummaryCounts entity = QDwsWebSummaryCounts.dwsWebSummaryCounts;
    JPAQuery<DwsWebSummaryCounts> query = jpaQueryFactory.selectFrom(entity);
    List<DwsWebSummaryCounts> list = query.fetch();
    return ResultDTO.ok(list);
  }
}
