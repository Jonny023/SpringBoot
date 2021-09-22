package com.example.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

@ApiModel
public class BasePageRequestVO {

    @ApiModelProperty(value = "是否开启分页", required = true, example = "true")
    private boolean enablePage = true;

    @ApiModelProperty(value = "页码", required = true, example = "1")
    @NotNull(message = "页码不能为空")
    private Integer pageNo = 1;

    @ApiModelProperty(value = "每页条数", required = true, example = "10")
    @NotNull(message = "每页条数不能为空")
    private Integer pageSize = 10;

    public boolean isEnablePage() {
        return enablePage;
    }

    public void setEnablePage(boolean enablePage) {
        this.enablePage = enablePage;
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
}
