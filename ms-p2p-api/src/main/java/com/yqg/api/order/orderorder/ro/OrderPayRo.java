package com.yqg.api.order.orderorder.ro;

import com.yqg.common.core.request.BaseSessionIdRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderPayRo extends BaseSessionIdRo {
    @ApiModelProperty(value = "订单编号", required = true)
    private String orderNo;
    @ApiModelProperty(value = "userUuid", required = true)
    private String userUuid;
    @ApiModelProperty(value = "payPWD", required = true)
    private String payPWD;
}

