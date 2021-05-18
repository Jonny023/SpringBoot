package com.example.springbootclickhousenoneid.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "dws_web_summary_counts")
@IdClass(DwsWebSummaryCountsKey.class)
public class DwsWebSummaryCounts implements Serializable {

  private static final long serialVersionUID = 5478600893211632322L;

  @Id
  @Column(name = "si")
  private String si;

  @Column(name = "event_time")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date eventTime;

  @Column(name = "pv")
  private Integer pv;

  @Column(name = "uv")
  private Integer uv;

  @Column(name = "ip_counts")
  private Integer ipCounts;

}
