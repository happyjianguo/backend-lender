package com.yqg.api.order.orderorder.ro;

import com.yqg.common.core.request.BasePageRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by liyixing on 2018/9/6.
 */
@Data
public class OrderOrderPageRo extends BasePageRo {

    @ApiModelProperty(value = "1.投资中 2.一结束", required = true)
    private Integer status;

    @ApiModelProperty(value = "产品类型--1.散标 2活期账户 3.理财账户", required = true)
    private Integer productType;

    private String startTime;
    private String endTime;

    @ApiModelProperty(value = "0.自选时间 1.全部 2今天 3.近一个月 4.近三个月", required = true)
    private Integer flag;

}
