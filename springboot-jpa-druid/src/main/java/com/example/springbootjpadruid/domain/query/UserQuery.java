package com.example.springbootjpadruid.domain.query;

import com.example.springbootjpadruid.domain.common.BasePage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserQuery extends BasePage {

    private String username;

}
