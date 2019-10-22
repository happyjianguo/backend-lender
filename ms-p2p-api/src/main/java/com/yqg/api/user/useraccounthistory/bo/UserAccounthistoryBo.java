package com.yqg.api.user.useraccounthistory.bo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
/**
 * 用户账户明细表 业务对象
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-11 10:43:36
 */
@Data
public class UserAccounthistoryBo {
    private String userUuid;
    private Integer type;
    private String dealTime;
    private BigDecimal lastBanlance;
    private BigDecimal amount;
    private BigDecimal currentBanlance;
    private BigDecimal lockedBanlance;
    private BigDecimal investBanlance;
    private String tradeNo;
    private String tradeInfo;
    private Integer payType;
}

