package org.example.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class R<T> {

    private int code;

    private String msg;

    private T data;

    public static <T> R<T> ok() {
        return (R<T>) R.builder().code(200).msg("成功").build();
    }

    public static <T> R<T> ok(T data) {
        R<T> ok = ok();
        ok.setData(data);
        return ok;
    }

    public static <T> R<T> error(int code, String msg) {
        return (R<T>) R.builder().code(code).msg(msg).build();
    }
}