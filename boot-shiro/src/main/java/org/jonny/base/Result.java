package org.jonny.base;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result implements Serializable {

    private int code;
    private String msg;
    private Object data;

    public static Result ok(Object data) {
        Result result = new Result();
        result.setCode(0);
        result.setData(data);
        result.setMsg("操作成功");
        return result;
    }

    public static Result ok(String msg, Object data) {
        Result result = new Result();
        result.setCode(0);
        result.setData(data);
        result.setMsg(msg);
        return result;
    }

    public static Result fail(String msg) {
        Result result = new Result();
        result.setCode(-1);
        result.setData(null);
        result.setMsg(msg);
        return result;
    }

    public static Result fail(String msg, Object data) {
        Result result = new Result();
        result.setCode(-1);
        result.setData(data);
        result.setMsg(msg);
        return result;
    }

    public static Result fail(int code, String msg, Object data) {
        Result result = new Result();
        result.setCode(code);
        result.setData(data);
        result.setMsg(msg);
        return result;
    }
}
