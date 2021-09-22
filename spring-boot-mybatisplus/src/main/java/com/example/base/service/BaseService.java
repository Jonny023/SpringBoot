package com.example.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.base.BasePageRequestVO;

public interface BaseService<T> extends IService<T> {

    IPage<T> listByPage(BasePageRequestVO param);
}