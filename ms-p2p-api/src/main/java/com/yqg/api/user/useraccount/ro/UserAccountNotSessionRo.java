package com.yqg.api.user.useraccount.ro;

import com.yqg.common.core.request.BaseRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by liyixing on 2018/12/18.
 */
@Data
public class UserAccountNotSessionRo extends BaseRo {
    //用户id
    @ApiModelProperty(value = "用户id", required = true)
    private String userId;
    //金额
    @ApiModelProperty(value = "金额", required = true)
    private BigDecimal amount;
}
