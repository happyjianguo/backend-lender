package com.yqg.api.pay.loan.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by zhaoruifeng on 2018/9/5.
 */
@Data
public class PaymentThirdVo {
    private String externalId;//交易流水号
    private String bankCode;//银行卡编码
    private String accountNumber;//银行卡号
    private String accountHolderName;//持卡人
    private BigDecimal amount;//金额
    private String description;//描述
    private String disburseType;//交易类型
    private String disburseChannel;//交易渠道
}
