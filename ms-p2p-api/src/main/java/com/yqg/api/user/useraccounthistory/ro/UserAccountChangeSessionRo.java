package com.yqg.api.user.useraccounthistory.ro;

import com.yqg.common.core.request.BaseSessionIdRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: hyy
 * @Date: 2019/6/19 10:26
 * @Version 1.0
 * @EMAIL: hanyangyang@yishufu.com
 */
@Data
public class UserAccountChangeSessionRo extends BaseSessionIdRo{
    //用户id
    @ApiModelProperty(value = "用户id", required = true)
    private String userUuid;
    //交易金额
    @ApiModelProperty(value = "交易金额", required = true)
    private BigDecimal amount;
    //可用余额
    @ApiModelProperty(value = "动账前金额", required = true)
    private BigDecimal lastBanlance;
    //可用余额
    @ApiModelProperty(value = "可用余额", required = true)
    private BigDecimal currentBanlance;
    //冻结余额
    @ApiModelProperty(value = "冻结余额", required = true)
    private BigDecimal lockedBanlance;
    //在投余额
    @ApiModelProperty(value = "在投余额", required = true)
    private BigDecimal investBanlance;
    //交易信息
    @ApiModelProperty(value = "交易信息 ", required = true)
    private String tradeInfo;
    //交易信息
    @ApiModelProperty(value = "交易号 ", required = true)
    private String tradeNo;
    //账户变动类型
    @ApiModelProperty(value = "账户变动类型", required = true)
    private String type;
    //业务场景类型
    @ApiModelProperty(value = "业务场景类型", required = true)
    private String businessType;
    //产品类型 1.散标 2活期账户 3.定期账户
    @ApiModelProperty(value = "支付渠道 1. 2 3.", required = true)
    private String payType;

    @ApiModelProperty(value = "动账时间", required = true)
    private Date dealTime;

    //操作人
    @ApiModelProperty(value = "操作人", required = true)
    private String createUser;
}