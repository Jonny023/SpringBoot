package org.example.domain.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BasePageReq {

    /**
     * 当前页码
     */
    @Builder.Default
    private Integer current = 1;

    /**
     * 每页条数
     */
    @Builder.Default
    private Integer size = 10;

    /**
     * 是否为导出
     */
    @Builder.Default
    private Boolean isExport = Boolean.FALSE;

    /**
     * 仅count同级
     */
    @Builder.Default
    private Boolean isCount = Boolean.FALSE;

}
