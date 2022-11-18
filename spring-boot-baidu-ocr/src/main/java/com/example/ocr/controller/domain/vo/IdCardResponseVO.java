package com.example.ocr.controller.domain.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IdCardResponseVO {

    /**
     *  姓名
     */
    private String name;

    /**
     *  性别
     */
    private String sex;

    /**
     *  出生日期
     */
    private String birthday;

    /**
     *  身份证号
     */
    private String idNum;

    /**
     * 民族
     */
    private String nation;


    /**
     *  住址
     */
    private String address;

}
