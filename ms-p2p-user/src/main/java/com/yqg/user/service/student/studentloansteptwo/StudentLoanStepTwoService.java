package com.yqg.user.service.student.studentloansteptwo;

import com.yqg.api.user.student.studentloanstepinfo.ro.StudentLoanStepSearchRo;
import com.yqg.api.user.student.studentloansteptwo.bo.StudentLoanStepTwoBo;
import com.yqg.api.user.student.studentloansteptwo.ro.StudentLoanStepTwoRo;
import com.yqg.common.core.BaseService;
import com.yqg.common.core.request.BaseSessionIdRo;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.user.entity.student.StudentLoanStepTwo;

/**
 * 学生借款申请步骤2
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-10-08 10:31:24
 */
public interface StudentLoanStepTwoService extends BaseService<StudentLoanStepTwo> {

    /**
     * 添加学生借款申请第二步数据
     * */
    void stepTwoInfoAdd(StudentLoanStepTwoRo ro) throws BusinessException;

    /**
     * 查询返现学生第二部数据*/
    StudentLoanStepTwoBo getInfo(BaseSessionIdRo ro,Integer flag,String id) throws BusinessException;

    /**
     * 管理后台反显学生借款第二部数据*/
    StudentLoanStepTwoBo getInfoSys(StudentLoanStepSearchRo ro) throws BusinessException;
}