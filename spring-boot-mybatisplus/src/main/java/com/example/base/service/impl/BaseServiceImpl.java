package com.example.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.base.BasePageRequestVO;
import com.example.base.service.BaseService;

public class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements BaseService<T> {

    @Override
    public IPage<T> listByPage(BasePageRequestVO param, QueryWrapper<T> wrapper) {
        IPage<T> page = new Page<>();
        if (param.isEnablePage()) {
            page = new Page<>(param.getPageNo(), param.getPageSize());
            return page(page, wrapper);
        } else {
            return page.setRecords(list(wrapper));
        }
    }

    @Override
    public IPage<T> listByPage(BasePageRequestVO param, LambdaQueryWrapper<T> wrapper) {
        IPage<T> page = new Page<>();
        if (param.isEnablePage()) {
            page = new Page<>(param.getPageNo(), param.getPageSize());
            return page(page, wrapper);
        } else {
            return page.setRecords(list(wrapper));
        }
    }
}