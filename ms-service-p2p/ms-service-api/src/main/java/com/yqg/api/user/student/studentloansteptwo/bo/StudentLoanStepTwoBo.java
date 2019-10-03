package com.yqg.api.user.student.studentloansteptwo.bo;

import com.yqg.api.user.student.studentaddressdetail.ro.StudentAddressBasicRo;
import lombok.Data;
import java.util.List;
/**
 * 学生借款申请步骤2 业务对象
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-10-08 10:31:24
 */
@Data
public class StudentLoanStepTwoBo {
    private String userUuid;
    private String schoolName;
    private StudentAddressBasicRo schoolAddressRo;
    private String schoolMobile;
    private String level;
    private String enterYear;
    private String subject;
    private String studentId;
    private String averageBasePoint;
    private String schoolFee;
    private String schoolFeeTerm;
    private String schoolFeePayWay;
    private String schoolFeePayBank;
    private String schoolFeePayBankNo;
    private String schoolFeePayDetail;
}

