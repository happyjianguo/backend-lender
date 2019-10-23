package com.yqg.common.core.request;

import com.yqg.common.core.annocation.ReqIntGreaterThan0;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

/**
 * 基础分页请求
 * Created by gao on 2017/12/20.
 */
@Data
public class BasePageRo extends BaseSessionIdRo {

    @ReqIntGreaterThan0
    @ApiModelProperty(value = "页数,不能为0或负数,默认1")
    private Integer pageNo = 1;

    @ReqIntGreaterThan0
    @ApiModelProperty(value = "单页数据量,不能为0或负数,默认10")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "排序字段")
    private String sortProperty;

    @ApiModelProperty(value = "排序方向 ASC DESC")
    private Sort.Direction sortDirection;

    public PageRequest convertPageRequest() {
        PageRequest pageRequest = null;
        if (null == sortDirection || StringUtils.isEmpty(sortProperty)) {
            pageRequest = new PageRequest(pageNo - 1, pageSize);
        } else {
            pageRequest = new PageRequest(pageNo - 1, pageSize, sortDirection, sortProperty);
        }
        return pageRequest;
    }
}
