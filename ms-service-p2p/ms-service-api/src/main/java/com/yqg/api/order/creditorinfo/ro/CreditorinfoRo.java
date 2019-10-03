package com.yqg.api.order.creditorinfo.ro;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 债权表 请求对象
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 11:02:36
 */
@Data
public class CreditorinfoRo{
//借款用途
@ApiModelProperty(value = "借款用途", required = true)
private String borrowingPurposes;
//风险等级
@ApiModelProperty(value = "风险等级", required = true)
private Integer riskLevel;
//债权编号
@ApiModelProperty(value = "债权编号", required = true)
private String creditorNo;
//借款人用户uuid
@ApiModelProperty(value = "借款人用户uuid", required = true)
private String lenderId;
//申请金额
@ApiModelProperty(value = "申请金额", required = true)
private BigDecimal amountApply;
//期限
@ApiModelProperty(value = "期限", required = true)
private Integer term;
//借款年化利率
@ApiModelProperty(value = "借款年化利率", required = true)
private BigDecimal borrowerYearRate;
//前置服务费
@ApiModelProperty(value = "前置服务费", required = true)
private BigDecimal serviceFee;
//发标时间
@ApiModelProperty(value = "发标时间", required = true)
private String biddingTime;
//来源
@ApiModelProperty(value = "来源", required = true)
private Integer channel;
//银行code
@ApiModelProperty(value = "银行code", required = true)
private String bankCode;
//银行名称
@ApiModelProperty(value = "银行名称", required = true)
private String bankName;
//银行卡号
@ApiModelProperty(value = "银行卡号", required = true)
private String bankNumber;
//银行卡持卡人
@ApiModelProperty(value = "银行卡持卡人", required = true)
private String bankCardholder;
//签名
private String sign;
}

