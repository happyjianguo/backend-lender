package com.yqg.api.pay.income.bo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class IncomeBo {

    String paymentCode;

    String depositChannel;

    String externalId;

    String depositStatus;

    String bankCode;

    String timestamp;

    boolean needPwd;

    BigDecimal needPay;

    String orderNo;

    BigDecimal amountPay;

    BigDecimal orderAmount;

    boolean tip;

    String transactionId;
}