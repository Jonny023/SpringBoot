package com.jonny.springbootkafkastream.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: Jonny
 * @description:
 * @date:created in 2021/4/23 9:34
 * @modificed by:
 */
@Data
@Accessors(chain = true)
public class User implements Serializable {

  private static final long serialVersionUID = 3050285841838315910L;

  private String username;
  private String area;
  private Integer age;
}
