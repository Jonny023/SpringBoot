package com.jonny.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: e-lijing6
 * @date: 2021-04-02
 */
@Data
@ApiModel("userForm")
public class UserForm {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("密码1")
    private String password1;

}
