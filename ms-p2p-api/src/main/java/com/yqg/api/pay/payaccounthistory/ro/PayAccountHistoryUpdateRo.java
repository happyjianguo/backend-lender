package com.yqg.api.pay.payaccounthistory.ro;

import com.yqg.common.core.annocation.ReqStringNotEmpty;
import com.yqg.common.core.request.BaseSessionIdRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: hyy
 * @Date: 2019/5/23 10:40
 * @Version 1.0
 * @EMAIL: hanyangyang@yishufu.com
 */
@Data
public class PayAccountHistoryUpdateRo extends BaseSessionIdRo{

    @ReqStringNotEmpty
    @ApiModelProperty(value = "id", required = true)
    private String id;

    @ReqStringNotEmpty
    @ApiModelProperty(value = "处理情况", required = true)
    private String dealStatus;

    @ReqStringNotEmpty
    @ApiModelProperty(value = "备注信息", required = true)
    private String remark;

    @ReqStringNotEmpty
    @ApiModelProperty(value = "支付时间", required = true)
    private String payTime;
}