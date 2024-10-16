package com.example.springbootlogin.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class R {

    private static final int OK = 200;
    private static final String OK_MSG = "success";
    private static final int ERR = 500;
    private static final String ERR_MSG = "fail";

    private int code;
    private String msg;
    private Object data;

    public static R ok() {
        return R.builder().code(OK).msg(OK_MSG).build();
    }

    public static R ok(Object data) {
        return R.builder().code(OK).msg(OK_MSG).data(data).build();
    }

    public static R error() {
        return R.builder().code(ERR).msg(ERR_MSG).build();
    }

    public static R errorMsg(String msg) {
        return R.builder().code(ERR).msg(msg).build();
    }

    public static R errorMsg(int code, String msg) {
        return R.builder().code(code).msg(msg).build();
    }
}
