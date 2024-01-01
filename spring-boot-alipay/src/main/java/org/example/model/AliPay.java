package org.example.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AliPay {

    private String traceNo;
    private BigDecimal totalAmount;
    private String subject;
    private String alipayTraceNo;
}