package com.example.webservicecore.domain.vo;

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
