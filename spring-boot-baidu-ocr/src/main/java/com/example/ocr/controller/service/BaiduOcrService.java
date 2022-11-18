package com.example.ocr.controller.service;

import com.alibaba.fastjson.JSON;
import com.baidu.aip.ocr.AipOcr;
import com.example.ocr.controller.domain.vo.BankResponseVO;
import com.example.ocr.controller.domain.vo.IdCardResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

@Slf4j
@Component
public class BaiduOcrService {

    @Resource
    private AipOcr apiOcr;


    /**
     * 身份证识别
     * url: https://cloud.baidu.com/doc/OCR/s/rk3h7xzck
     *
     * @param file
     */
    public IdCardResponseVO idCard(MultipartFile file) {
        try {
            byte[] bytes = getBytes(file);
            HashMap<String, String> options = new HashMap<>();
            //front图像方向：正向
            JSONObject jsonObject = apiOcr.idcard(bytes, "front", options);
            log.info("百度身份证识别响应数据：{}", jsonObject.toString());
            //image_status normal-识别正常
            if (Objects.equals(jsonObject.getString("image_status"), "normal")) {
                JSONObject wordsResult = jsonObject.getJSONObject("words_result");
                return IdCardResponseVO.builder()
                        .name(wordsResult.getJSONObject("姓名").getString("words"))
                        .sex(wordsResult.getJSONObject("性别").getString("words"))
                        .nation(wordsResult.getJSONObject("民族").getString("words"))
                        .idNum(wordsResult.getJSONObject("公民身份号码").getString("words"))
                        .birthday(wordsResult.getJSONObject("出生").getString("words"))
                        .address(wordsResult.getJSONObject("住址").getString("words"))
                        .build();
            } else {
                throw new RuntimeException("身份证识图片无法识别");
            }
        } catch (Exception e) {
            log.error("身份证识别失败", e);
            throw new RuntimeException("身份证图片识别失败");
        }
    }

    /**
     * 银行卡识别
     * url:https://cloud.baidu.com/doc/OCR/s/ak3h7xxg3
     *
     * @param file
     */
    public BankResponseVO bankCard(MultipartFile file) {
        try {
            byte[] bytes = getBytes(file);
            JSONObject jsonObject = apiOcr.bankcard(bytes, new HashMap<String, String>());
            log.info("百度ocr银行卡识别响应数据：{}", jsonObject.toString());
            JSONObject result = jsonObject.getJSONObject("result");
            //bank_card_type银行卡类型，0：不能识别; 1：借记卡; 2：贷记卡（原信用卡大部分为贷记卡）; 3：准贷记卡; 4：预付费卡
            if (result != null && result.getInt("bank_card_type") != 0) {
                return JSON.parseObject(result.toString(), BankResponseVO.class);
            } else {
                throw new RuntimeException("银行卡图片识别失败");
            }
        } catch (RuntimeException e) {
            log.error("银行卡识别失败", e);
            throw new RuntimeException("银行卡图片识别失败");
        }
    }

    private byte[] getBytes(MultipartFile file) {
        byte[] bite = new byte[0];
        try {
            bite = file.getBytes();
        } catch (IOException e) {
            log.error("图片解析失败");
        }
        return bite;
    }
}
