package com.yqg.api.pay.payaccounthistory.ro;

import com.yqg.common.core.annocation.ReqStringNotEmpty;
import com.yqg.common.core.request.BasePageRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: hyy
 * @Date: 2019/5/22 14:24
 * @Version 1.0
 * @EMAIL: hanyangyang@yishufu.com
 */
@Data
public class PayAccountListPageRo extends BasePageRo {

    @ApiModelProperty(value = "投资人姓名", required = true)
    private String name;

    @ApiModelProperty(value = "投资人手机号", required = true)
    private String mobile;

    @ApiModelProperty(value = "状态", required = true)
    private Integer status;

    @ApiModelProperty(value = "时间极大值", required = true)
    private String timeMax;

    @ApiModelProperty(value = "时间极小值", required = true)
    private String timeMin;

    @ApiModelProperty(value = "支付渠道", required = true)
    private String channel;

    @ReqStringNotEmpty
    @ApiModelProperty(value = "交易类型", required = true)
    private String type;
}