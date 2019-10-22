package com.yqg.api.user.student.studentloanstepinfo.bo;

import lombok.Data;

import java.math.BigDecimal;


/**
 * 计算学生借款每期应还金额*/
@Data
public class StudentLoanCountBo {
    private BigDecimal amountApply;     //借款金额

    private Integer term;               //借款期限

    private BigDecimal interest;        //利息

    private BigDecimal firstPayment;    //首付

    //private BigDecimal ;
}
