package com.yqg.api.order.creditorinfo.bo;

import lombok.Data;

/**
 * Remark:
 * Created by huwei on 19.5.23.
 */
@Data
public class LoanHistiryBo {
    private String borrowingPurpose;
    private String borrowingTerm;
    private Double amount;
    private Integer status;
}
