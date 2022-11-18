package com.example.ocr.controller.domain.vo;

import lombok.Data;

@Data
public class BaiduIdCardResponse {

    private WordsResult words_result;

    @Data
    public static class WordsResult {


    }
}
