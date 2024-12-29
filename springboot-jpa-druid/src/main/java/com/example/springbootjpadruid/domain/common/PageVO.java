package com.example.springbootjpadruid.domain.common;

import cn.hutool.core.bean.BeanUtil;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PageVO<T> {

    private int page = 1;

    private int size = 10;

    private long total;

    private int totalPage;

    private List<T> list;


    public static <S, T> PageVO<T> of(Page<S> page, Class<T> clazz) {
        PageVO<T> pageVO = new PageVO<>();
        pageVO.setPage(page.getNumber() + 1);
        pageVO.setSize(page.getSize());
        pageVO.setTotal(page.getTotalElements());
        List<S> content = page.getContent();
        pageVO.setList(BeanUtil.copyToList(content, clazz));
        pageVO.setTotalPage(page.getTotalPages());
        return pageVO;
    }

    public static <T> PageVO<T> of(Page<T> page) {
        PageVO<T> pageVO = new PageVO<>();
        pageVO.setPage(page.getNumber() + 1);
        pageVO.setSize(page.getSize());
        pageVO.setTotal(page.getTotalElements());
        pageVO.setList(page.getContent());
        pageVO.setTotalPage(page.getTotalPages());
        return pageVO;
    }
}
