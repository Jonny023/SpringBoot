package com.example.domain.dto;

import com.example.pay.enums.BooleanEnum;
import com.example.pay.enums.PayType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("支付订单vo")
public class OrderPayVO {

    @ApiModelProperty(value = "订单id", required = true)
    @NotNull
    private Long orderId;

    @NotNull
    @ApiModelProperty(value = "三方支付类型", required = true)
    private PayType payType;

    @NotNull
    @ApiModelProperty(value = "1.h5 0.native", required = true)
    private BooleanEnum h5;

}
