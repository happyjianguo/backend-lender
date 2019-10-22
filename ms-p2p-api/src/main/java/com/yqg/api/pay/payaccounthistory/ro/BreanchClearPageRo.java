package com.yqg.api.pay.payaccounthistory.ro;

import com.yqg.common.core.request.BasePageRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Remark:
 * Created by huwei on 19.6.27.
 */
@Data
public class BreanchClearPageRo extends BasePageRo {
    @ApiModelProperty(value = "状态", required = true)
    private Integer status;

    @ApiModelProperty(value = "时间极大值", required = true)
    private String timeMax;

    @ApiModelProperty(value = "时间极小值", required = true)
    private String timeMin;

}
