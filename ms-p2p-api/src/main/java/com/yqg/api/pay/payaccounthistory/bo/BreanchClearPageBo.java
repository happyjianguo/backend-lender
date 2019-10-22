package com.yqg.api.pay.payaccounthistory.bo;

import lombok.Data;

import java.util.Date;

/**
 * Remark:
 * Created by huwei on 19.6.27.
 */
@Data
public class BreanchClearPageBo {
    private String id;
    private Date createTime;
    private String fromUser;
    private String toUser;
    private String toAccount;
//    private String toAccountNo;
    private String amount;
    private Integer status;
    private String remark;
}
