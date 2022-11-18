package com.example.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("支付订单DTO")
public class PcPayDTO {

    @JsonIgnore
    @ApiModelProperty(value = "预支付id，不需要使用")
    private String prepayid;

    private String payCode;

}
