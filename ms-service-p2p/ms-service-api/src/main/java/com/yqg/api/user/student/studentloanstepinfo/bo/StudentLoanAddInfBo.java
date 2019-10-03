package com.yqg.api.user.student.studentloanstepinfo.bo;

import lombok.Data;


/**
 * 添加学生借款信息
 */
@Data
public class StudentLoanAddInfBo {

    private Integer step;

    private String userUuid;

    private String realname;

    private String inviteCode;

    public StudentLoanAddInfBo(Integer step, String userUuid, String realname ){
        this.step = step;
        this.userUuid = userUuid;
        this.realname = realname;
    }
}
