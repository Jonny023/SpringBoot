package com.example.springbootclickhousenoneid.base;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultDTO<T> implements Serializable {

    private String code;

    private String msg;

    private T data;

    public static <T> ResultDTO<T> create() {
        ResultDTO<T> result = new ResultDTO<T>();
        return result;
    }

    public ResultDTO<T> success(){
        this.setCode(ResultEnum.SUCCESS.getCode());
        this.setMsg(ResultEnum.SUCCESS.getMsg());
        return this;
    }

    public ResultDTO<T> success(T data){
        this.setCode(ResultEnum.SUCCESS.getCode());
        this.setMsg(ResultEnum.SUCCESS.getMsg());
        this.data = data;
        return this;
    }

    public static <T> ResultDTO<T> ok(T data){
        return (ResultDTO<T>) create().success().data(data);
    }

    public static <T> ResultDTO<T> ok(){
        return (ResultDTO<T>) create().success().data(null);
    }

    public ResultDTO<T> fail(String code, String description){
        this.setCode(code);
        this.setMsg(description);
        return this;
    }

    public ResultDTO<T> fail(String code){
        fail(code, null);
        return this;
    }

    public static <T> ResultDTO<T> error(String code, String msg){
        return (ResultDTO<T>) ResultDTO.builder().code(code).msg(msg).data(null).build();
    }

    public ResultDTO<T> fail(ResultEnum resultEnum){
        this.setCode(resultEnum.getCode());
        this.setMsg(resultEnum.getMsg());
        return this;
    }

    public ResultDTO<T> code(String code){
        this.setCode(code);
        return this;
    }

    public ResultDTO<T> data(T data){
        this.data = data;
        return this;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}