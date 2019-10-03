package com.yqg.api.user.student.studentloanstepone.bo;

import com.yqg.api.user.student.studentaddressdetail.ro.StudentAddressBasicRo;
import lombok.Data;
import java.util.List;
/**
 * 学生借款申请步骤1 业务对象
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-10-08 10:31:24
 */
@Data
public class StudentLoanStepOneBo {
    private String userUuid;
    private String userName;
    private String idCard;
    private Integer sex;
    private Integer age;
    private String religion;
    private StudentAddressBasicRo idAddressRo;
    private StudentAddressBasicRo liveAddressRo;
    private String email;
    private String mobileNumber;
    private String familyYearSalary;
    private String familyMember;
    private String bankName;
    private String bankNo;
    private Integer loanAble;
    private String loanAmount;
    private String idCardUrl;
    private String familyCardUrl;
    private String studentCardUrl;
    private String idCardInHandUrl;
    private String collageStudentUrl;
}

