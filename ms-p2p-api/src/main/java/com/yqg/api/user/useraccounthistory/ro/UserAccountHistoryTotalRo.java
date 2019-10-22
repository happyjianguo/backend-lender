package com.yqg.api.user.useraccounthistory.ro;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Remark:
 * Created by huwei on 19.5.24.
 */
@Data
public class UserAccountHistoryTotalRo {
    //订单号
    @ApiModelProperty(value = "目标日期", required = true)
    private Date date;

    //用户id
    @ApiModelProperty(value = "用户id", required = true)
    private String userUuid;
}
