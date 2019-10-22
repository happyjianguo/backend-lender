package com.yqg.api.user.useraccount.ro;

import com.yqg.common.core.request.BaseSessionIdRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: hyy
 * @Date: 2019/6/3 16:06
 * @Version 1.0
 * @EMAIL: hanyangyang@yishufu.com
 */
@Data
public class UserAccountSessionRo extends BaseSessionIdRo {
    //用户id
    @ApiModelProperty(value = "用户id", required = true)
    private String userId;
    //金额
    @ApiModelProperty(value = "金额", required = true)
    private BigDecimal amount;
}