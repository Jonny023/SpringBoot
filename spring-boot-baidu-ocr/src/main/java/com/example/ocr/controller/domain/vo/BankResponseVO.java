package com.example.ocr.controller.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankResponseVO {

    /**
     *  银行卡号
     */
    private String bank_card_number;
    /**
     *  有效期
     */
    private String valid_date;
    /**
     *  银行卡类型
     */
    private int bank_card_type;
    /**
     *  银行名称
     */
    private String bank_name;
    /**
     *  持卡人姓名
     */
    private String holder_name;

    public void setBank_card_number(String bank_card_number) {
        if (bank_card_number == null) {
            bank_card_number = "";
        }
        this.bank_card_number = bank_card_number.replaceAll(" ", "");
    }
}
