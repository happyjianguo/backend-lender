package com.yqg.api.order.scatterstandard.ro;

import com.yqg.common.core.request.BasePageRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by liyixing on 2018/9/5.
 */
@Data
public class ScatterstandardPageRo extends BasePageRo {

    @ApiModelProperty(value = "理财年化利率", required = true)
    private BigDecimal yearRateFin;
    //债权总金额
    @ApiModelProperty(value = "债权总金额", required = true)
    private BigDecimal amountApply;
    //期限
    @ApiModelProperty(value = "期限", required = true)
    private Integer term;
    @ApiModelProperty(value = "状态", required = true)
    private Integer status;

}
