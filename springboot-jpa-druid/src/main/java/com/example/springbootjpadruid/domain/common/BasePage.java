package com.example.springbootjpadruid.domain.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BasePage {

    private int page = 1;

    private int size = 10;

    /**
     * 是否需要分页
     */
    private boolean needPage = false;

    public int getPage() {
        if (page < 1) {
            return 0;
        } else {
            return page - 1;
        }
    }

    public int getOffset() {
        return getPage() * size;
    }
}
