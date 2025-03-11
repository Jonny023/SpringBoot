package com.example.springbootshardingsphere.domain.request;

import com.example.springbootshardingsphere.domain.vo.common.PageVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author admin
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderRequest extends PageVO {

    /**
     * 订单编号
     */
    private String orderNo;
}