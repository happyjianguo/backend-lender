package com.yqg.api.pay.payaccounthistory.bo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
/**
 * 资金流水表 业务对象
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 17:28:20
 */
@Data
public class PayAccountHistoryBo {
    private String orderNo;
    private String tradeNo;
    private BigDecimal amount;
    private BigDecimal fee;
    private Integer status;
    private Integer tradeType;
    private String fromUserId;
    private String toUserId;
}

