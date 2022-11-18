package com.example.base;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class ResultVO<T> implements Serializable {

    @ApiModelProperty("信息码 00000-成功")
    private String code;

    @ApiModelProperty("信息描述")
    private String msg;

    @ApiModelProperty("返回数据")
    private T data;

    public static <T> ResultVO<T> create() {
        return new ResultVO<>();
    }

    public ResultVO<T> success() {
        this.setCode(ResultEnum.SUCCESS.getCode());
        this.setMsg(ResultEnum.SUCCESS.getMsg());
        return this;
    }

    public ResultVO<T> success(T data) {
        this.setCode(ResultEnum.SUCCESS.getCode());
        this.setMsg(ResultEnum.SUCCESS.getMsg());
        this.data = data;
        return this;
    }

    public static <T> ResultVO<T> ok(T data) {
        return (ResultVO<T>) create().success().data(data);
    }

    public static <T> ResultVO<T> ok() {
        return (ResultVO<T>) create().success().data(null);
    }

    public ResultVO<T> fail(String code, String description) {
        this.setCode(code);
        this.setMsg(description);
        return this;
    }

    public ResultVO<T> fail(String code) {
        fail(code, null);
        return this;
    }

    public static <T> ResultVO<T> error(String code, String msg) {
        ResultVO dto = new ResultVO();
        dto.setCode(code);
        dto.setMsg(msg);
        dto.setData(null);
        return dto;
    }

    public ResultVO<T> fail(ResultEnum resultEnum) {
        this.setCode(resultEnum.getCode());
        this.setMsg(resultEnum.getMsg());
        return this;
    }

    public ResultVO<T> code(String code) {
        this.setCode(code);
        return this;
    }

    public ResultVO<T> data(T data) {
        this.data = data;
        return this;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}