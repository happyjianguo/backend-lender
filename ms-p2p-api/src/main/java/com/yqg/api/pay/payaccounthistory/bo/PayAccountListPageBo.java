package com.yqg.api.pay.payaccounthistory.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: hyy
 * @Date: 2019/5/22 14:29
 * @Version 1.0
 * @EMAIL: hanyangyang@yishufu.com
 */
@Data
public class PayAccountListPageBo {
    private String id;
    private String tradeNo;
    private String buildTime;
    private String createTime;
    private BigDecimal amount;
    private String paychannel;
    private Integer status;
    private String fromUserId;
    private String toUserId;
    private String payTime;
    private String dealStatus;
    private String remark;

}