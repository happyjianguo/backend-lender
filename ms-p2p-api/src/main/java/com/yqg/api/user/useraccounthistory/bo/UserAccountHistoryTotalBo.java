package com.yqg.api.user.useraccounthistory.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Remark:
 * Created by huwei on 19.5.24.
 */
@Data
public class UserAccountHistoryTotalBo {
    private String userUuid;
    private BigDecimal amount;
}
