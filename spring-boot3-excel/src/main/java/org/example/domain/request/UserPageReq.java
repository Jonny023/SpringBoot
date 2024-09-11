package org.example.domain.request;

import lombok.Data;

import java.util.Objects;

@Data
public class UserPageReq extends BasePageReq {

    private Long id;

    public void validate() {
        if (Boolean.TRUE.equals(getIsExport())) {
            if (Objects.isNull(id)) {
                throw new IllegalArgumentException("id不能为空");
            }
        }
    }

}