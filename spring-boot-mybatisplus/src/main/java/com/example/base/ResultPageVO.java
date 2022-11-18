package com.example.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Optional;

public class ResultPageVO<T> extends ResultVO<List<T>> {

    @ApiModelProperty("当前页码")
    private Integer pageNo;

    @ApiModelProperty("每页显示条数")
    private Integer pageSize;

    @ApiModelProperty("每页显示条数")
    private Long total;

    /**
     * 分页结果
     *
     * @param page
     * @param <T>
     * @return
     */
    public static <T> ResultPageVO<T> ok(IPage<T> page) {
        page = Optional.ofNullable(page).orElse(new Page<>());
        ResultPageVO resultPageVO = new ResultPageVO();
        resultPageVO.setCode(ResultEnum.SUCCESS.getCode());
        resultPageVO.setMsg(ResultEnum.SUCCESS.getMsg());
        resultPageVO.setPageNo((int) page.getCurrent());
        resultPageVO.setPageSize((int) page.getSize());
        resultPageVO.setTotal(page.getTotal());
        resultPageVO.setData(page.getRecords());
        return resultPageVO;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
