package com.yqg.api.user.student.studentloanstepthree.bo;

import com.yqg.api.user.student.studentaddressdetail.ro.StudentAddressBasicRo;
import lombok.Data;
import java.util.List;
/**
 * 学生借款申请步骤3(担保人信息) 业务对象
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-10-08 10:31:24
 */
@Data
public class StudentLoanStepThreeBo {
    private String userUuid;
    private String userName;
    private String idCardNo;
    private String relationship;
    private StudentAddressBasicRo idAddressRo;
    private StudentAddressBasicRo liveAddressRo;
    private String taxCardNo;
    private String email;
    private String mobileNumber;
    private String companyName;
    private String companyMobile;
    private StudentAddressBasicRo liveCompanyAddressRo;
    private String position;
    private String income;
    private Integer doitLoan;
    private String idCardUrl;
    private String payDetailUrl;
    private String aggrementUrl;
    private String aggrementInHandUrl;
}

