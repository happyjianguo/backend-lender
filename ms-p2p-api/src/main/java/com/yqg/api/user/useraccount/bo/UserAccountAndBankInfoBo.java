package com.yqg.api.user.useraccount.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: hyy
 * @Date: 2019/5/25 10:16
 * @Version 1.0
 * @EMAIL: hanyangyang@yishufu.com
 */
@Data
public class UserAccountAndBankInfoBo {
    private String bankCarkNo;
    private BigDecimal currentBalance;
    private String companyBankCarkNo;
}