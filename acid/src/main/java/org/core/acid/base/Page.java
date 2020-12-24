package org.core.acid.base;

import com.querydsl.core.QueryResults;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class Page<T> {

    private Long offset;
    private Long pageSize;
    private long total;
    private long totalPage;
    private T datas;

    public Page() {
        init(this);
    }

    /**
     *  防止报错及大量访问数据最大限制20
     * @param page
     * @return
     */
    public Page<T> init(Page<T> page) {
        if (page.getOffset() == null || page.getOffset() < 0) {
            this.offset = 1L;
            page.setOffset(1L);
        }
        if (page.getPageSize() == null || page.getPageSize() > 20) {
            this.pageSize = 10L;
            page.setPageSize(10L);
        }
        return this;
    }


    /**
     *  数据起始游标
     * @param page
     * @return
     */
    public Long getIndex(Page page) {
        init(page);
        return (page.getOffset() - 1) * page.getPageSize();
    }

    /**
     *  封装Page
     * @param results
     * @return
     */
    public static Page calc(QueryResults results) {
        return Page.builder()
                .offset((results.getOffset() / results.getLimit()) + 1)
                .pageSize(results.getLimit())
                .total(results.getTotal())
                .datas(results.getResults())
                .totalPage((results.getTotal() + results.getLimit() - 1) / results.getLimit())
                .build();
    }

    public Long getOffset() {
        if (this.offset == null || this.offset < 0) {
            this.offset = 10L;
        }
        return offset;
    }

    public Long getPageSize() {
        if (this.pageSize == null || this.pageSize > 20) {
            this.pageSize = 10L;
        }
        return pageSize;
    }


}
