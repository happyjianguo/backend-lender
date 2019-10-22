package com.yqg.api.order.orderorder.ro;

import com.yqg.common.core.request.BasePageRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by liyixing on 2018/9/6.
 */
@Data
public class OrderOrderPageRo extends BasePageRo {

    @ApiModelProperty(value = "orderNo", required = true)
    private String orderNo;

    @ApiModelProperty(value = "1.投资中 2.一结束", required = true)
    private Integer status;

    private Integer payStatus;

    @ApiModelProperty(value = "产品类型--1.散标 2活期账户 3.理财账户", required = true)
    private Integer productType=1;

    private String startTime;
    private String endTime;


    private String userName;
    private String mobile;


    private Boolean isAdmin=false;

    @ApiModelProperty(value = "0.自选时间 1.全部 2今天 3.近一个月 4.近三个月", required = true)
    private Integer flag;

}
