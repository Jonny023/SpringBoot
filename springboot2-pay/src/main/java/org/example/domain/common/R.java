package org.example.domain.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author admin
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class R<T> {

    private static final int OK_CODE = 200;
    private static final String OK_MSG = "success";

    private static final int ERR_CODE = 500;
    private static final String ERROR_MSG = "failed";

    @ApiModelProperty("状态码 200成功")
    private int code;

    @ApiModelProperty("消息")
    private String msg;

    @ApiModelProperty("响应消息")
    private T data;

    public static <T> R.RBuilder<T> instance() {
        return R.builder();
    }

    private static <T> R.RBuilder<T> okBuilder() {
        return (RBuilder<T>) instance().code(OK_CODE).msg(OK_MSG);
    }

    private static <T> R.RBuilder<T> errBuilder() {
        return (RBuilder<T>) instance().code(ERR_CODE).msg(ERROR_MSG);
    }

    public static <T> R<T> ok(T data) {
        return (R<T>) okBuilder().data(data).build();
    }

    public static <T> R<T> ok() {
        return (R<T>) okBuilder().build();
    }

    public static <T> R<T> error() {
        return (R<T>) errBuilder().code(OK_CODE).msg(ERROR_MSG).build();
    }

    public static <T> R<T> error(int code, String msg) {
        return (R<T>) instance().code(code).msg(msg).build();
    }

    public static <T> R<T> error(String msg) {
        return (R<T>) instance().msg(msg).build();
    }

}