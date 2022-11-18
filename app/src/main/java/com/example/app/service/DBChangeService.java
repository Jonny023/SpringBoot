package com.example.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.app.domain.entity.SysDb;

import java.util.List;

public interface DBChangeService extends IService<SysDb> {

    List<SysDb> list();

    boolean changeDb(Long datasourceId) throws Exception;
}
