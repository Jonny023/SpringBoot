package org.jonny.vo;

import lombok.Data;

import java.util.Date;

@Data
public class UserVO {

    private Long id;
    private String username;
    private String avatar;
    private String email;
    private Integer status;
    private Date created;
    private Date lastLogin;

}
