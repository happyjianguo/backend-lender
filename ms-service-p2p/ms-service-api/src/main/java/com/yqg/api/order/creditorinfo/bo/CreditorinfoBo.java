package com.yqg.api.order.creditorinfo.bo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 债权表 业务对象
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 11:02:36
 */
@Data
public class CreditorinfoBo {
    private String borrowingPurposes;
    private Integer riskLevel;
    private String creditorNo;
    private String lenderId;
    private BigDecimal amountApply;
    private Integer term;
    private BigDecimal borrowerYearRate;
    private BigDecimal serviceFee;
    private Date biddingTime;
    private Integer channel;
    private String bankCode;
    private String bankName;
    private String bankNumber;
    private String bankCardholder;
}

