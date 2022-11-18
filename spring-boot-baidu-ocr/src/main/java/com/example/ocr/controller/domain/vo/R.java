package com.example.ocr.controller.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class R<T> {

    private Integer code;
    private String message;
    private T data;

    public static <T> R<T> ok(T data) {
        return (R<T>) R.builder().code(200).message("success").data(data).build();
    }

    public static R error() {
        return R.builder().code(500).message("failed").build();
    }

    public static R error(String message) {
        return R.builder().code(500).message(message).build();
    }
}
