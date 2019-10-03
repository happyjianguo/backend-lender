package com.yqg.api.user.student.studentloanstepfour.bo;

import lombok.Data;
import java.util.List;
/**
 * 学生借款申请步骤4(联系人信息) 业务对象
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-10-08 10:31:24
 */
@Data
public class StudentLoanStepFourBo {
    private String userUuid;
    private String username;
    private String relationship;
    private String mobileNumber;
}

