package com.example.springbootshardingsphere.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springbootshardingsphere.domain.entity.Order;
import com.example.springbootshardingsphere.domain.vo.common.PageVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    /**
     * 分页查询
     *
     * @param param 参数
     * @return 分页数据
     */
    List<Order> listPage(PageVO param);
}