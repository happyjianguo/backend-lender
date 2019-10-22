package com.yqg.api.user.useraccounthistory.ro;

import com.yqg.common.core.request.BasePageRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户账户明细表 请求对象
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-11 10:43:36
 */
@Data
public class UserAccounthistoryRo extends BasePageRo{
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
    //账户变动类型
    @ApiModelProperty(value = "账户变动类型", required = true)
    private String type;
    //业务场景类型
    @ApiModelProperty(value = "业务场景类型", required = true)
    private String businessType;
    //产品类型 1.散标 2活期账户 3.定期账户
    @ApiModelProperty(value = "支付渠道 1. 2 3.", required = true)
    private String payType;
}

