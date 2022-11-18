package com.example.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.app.config.DBContextHolder;
import com.example.app.config.DynamicDataSource;
import com.example.app.domain.entity.SysDb;
import com.example.app.mapper.DataSourceMapper;
import com.example.app.service.DBChangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DBChangeServiceImpl extends ServiceImpl<DataSourceMapper, SysDb> implements DBChangeService {

    @Autowired
    DataSourceMapper dataSourceMapper;
    @Autowired
    private DynamicDataSource dynamicDataSource;

    @Override
    public List<SysDb> list() {
        return dataSourceMapper.list();
    }

    @Override
    public boolean changeDb(Long datasourceId) throws Exception {

        //默认切换到主数据源,进行整体资源的查找
        DBContextHolder.clearDataSource();

        List<SysDb> dataSourcesList = dataSourceMapper.list();

        for (SysDb dataSource : dataSourcesList) {
            if (dataSource.getId().equals(datasourceId)) {
                System.out.println("需要使用的的数据源已经找到,datasourceId是：" + dataSource.getId());
                //创建数据源连接&检查 若存在则不需重新创建
                dynamicDataSource.createDataSourceWithCheck(dataSource);
                //切换到该数据源
                DBContextHolder.setDataSource(dataSource.getId());
                return true;
            }
        }
        return false;

    }

}
