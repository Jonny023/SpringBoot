package com.example.springbootshardingsphere.domain.vo.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author admin
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageVO {

    /**
     * 当前页
     */
    @Builder.Default
    private Integer pageNo = 1;

    /**
     * 每页条数
     */
    @Builder.Default
    private Integer pageSize = 10;

}