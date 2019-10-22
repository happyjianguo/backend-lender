package com.yqg.api.order.scatterstandard.ro;

import com.yqg.common.core.request.BaseSessionIdRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Remark:
 * Created by huwei on 19.5.13.
 */
@Data
public class LoanHistoryRo extends BaseSessionIdRo {
    @ApiModelProperty(value = "借款人手机号", required = true)
    private String mobileNumber;
}
