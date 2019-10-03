package com.yqg.api.pay.payaccounthistory.ro;

import com.yqg.common.core.request.BasePageRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PayAccountPageRo extends BasePageRo {
    @ApiModelProperty(value = "理财订单号", required = true)
    private String orderNo;

    @ApiModelProperty(value = "债权编号", required = true)
    private String tradeNo;

    @ApiModelProperty(value = "时间极大值", required = true)
    private String timeMax;

    @ApiModelProperty(value = "时间极小值", required = true)
    private String timeMin;

    @ApiModelProperty(value = "交易类型", required = true)
    private Integer tradeType;
}
