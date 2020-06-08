package com.yqg.api.order.orderorder.ro;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderSuccessRo {

    @ApiModelProperty(value = "interest", required = true)
    private BigDecimal serviceFee;
    @ApiModelProperty(value = "insurance", required = true)
    private BigDecimal insurance;
}
