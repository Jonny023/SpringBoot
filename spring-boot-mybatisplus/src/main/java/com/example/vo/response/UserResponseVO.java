package com.example.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

@ApiModel("用户VO")
public class UserResponseVO {

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "用户登录标识")
    private String uuid = UUID.randomUUID().toString();

    public String getUsername() {
        return username;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
