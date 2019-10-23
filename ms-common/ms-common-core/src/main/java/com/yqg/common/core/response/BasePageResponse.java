package com.yqg.common.core.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 基础分页返回值
 * Created by gao on 2017/12/19.
 */
@Data
public class BasePageResponse<T> {

    private int totalPages;//总页数
    private long totalElements;//数据总条数
    private int  numberOfElements;//当前页数据条数
    private List<T> content;//数据结构列表

    public BasePageResponse(Page page) {
        this.totalPages = page.getTotalPages();
        this.totalElements=page.getTotalElements();
        this.numberOfElements=page.getNumberOfElements();
    }
}
