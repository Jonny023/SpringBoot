package com.jonny.springbootjsr303.form;

import com.jonny.springbootjsr303.validation.Insert;
import com.jonny.springbootjsr303.validation.Update;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class UserAddOrUpdateForm {

    @Min(value = 1, message = "主键格式不正确", groups = Update.class)
    @NotNull(message = "主键不能为空", groups = Update.class)
    private Long id;

    @NotEmpty(message = "用户名", groups = Insert.class)
    private String username;

    @NotEmpty(message = "昵称不能为空", groups = Insert.class)
    private String nickname;

    private String email;

}