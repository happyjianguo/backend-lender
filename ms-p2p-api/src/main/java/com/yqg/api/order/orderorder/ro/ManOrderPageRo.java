package com.yqg.api.order.orderorder.ro;

import com.yqg.common.core.request.BasePageRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ManOrderPageRo extends BasePageRo {

    @ApiModelProperty(value = "理财订单号", required = true)
    private String id;

    @ApiModelProperty(value = "理财人手机号", required = true)
    private String mobile;

    @ApiModelProperty(value = "购买时间极小值", required = true)
    private String buyTimeMin;

    @ApiModelProperty(value = "购买时间极大值", required = true)
    private String buyTimeMax;

    @ApiModelProperty(value = "产品类型", required = true)
    private Integer productType;

    @ApiModelProperty(value = "年化收益率", required = true)
    private String yearRateFin;

    @ApiModelProperty(value = "到期日极小值", required = true)
    private String dueTimeMin;

    @ApiModelProperty(value = "到期日极大值", required = true)
    private String dueTimeMax;

}
