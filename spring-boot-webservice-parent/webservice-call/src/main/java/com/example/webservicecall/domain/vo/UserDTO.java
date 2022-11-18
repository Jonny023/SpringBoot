package com.example.webservicecall.domain.vo;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO {

    private Integer id;
    private String username;
    private Integer age;
    private String sex;
}
