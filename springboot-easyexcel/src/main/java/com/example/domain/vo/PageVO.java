package com.example.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageVO<T> {

    /**
     * 当前页的数据
     */
    private List<T> data;

    /**
     * 总记录数
     */
    private int total;

    /**
     * 当前页码
     */
    private int page;

    /**
     * 每页大小
     */
    private int pageSize;

}
