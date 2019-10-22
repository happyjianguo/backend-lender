package com.yqg.api.user.useraccount.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: hyy
 * @Date: 2019/5/17 10:03
 * @Version 1.0
 * @EMAIL: hanyangyang@yishufu.com
 */
@Data
public class OrganOrSuperUserBo {
    private String id;
    private String realName;
    private String mobileNumber;
    private String bankCarkNo;
    private String bankCode;
    private BigDecimal currentBalance;
    private BigDecimal lockedBalance;
}