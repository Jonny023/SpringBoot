package com.example.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jonny
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BasePage {

    /**
     * 当前页
     */
    @Builder.Default
    private Integer page = 1;

    /**
     * 每页大小
     */
    @Builder.Default
    private Integer pageSize = 10;
}