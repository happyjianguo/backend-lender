package com.yqg.api.user.student.studentloanstepinfo.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 学生借款信息步骤表 业务对象
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-10-08 10:31:24
 */
@Data
public class StudentLoanRepayBo {
    private String time;
    private Integer amount;
}
