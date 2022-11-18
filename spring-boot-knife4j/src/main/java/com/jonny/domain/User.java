package com.jonny.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @description:
 * @author: e-lijing6
 * @date: 2021-04-02
 */
@Data
@ApiModel("用户")
@AllArgsConstructor
public class User {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;
}
