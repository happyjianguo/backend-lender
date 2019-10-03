package com.yqg.user.service.student.studentloanstepone;

import com.yqg.api.user.student.studentloanstepinfo.ro.StudentLoanStepSearchRo;
import com.yqg.api.user.student.studentloanstepone.bo.StudentLoanStepOneBo;
import com.yqg.api.user.student.studentloanstepone.ro.StudentLoanStepOneRo;
import com.yqg.common.core.BaseService;
import com.yqg.common.core.request.BaseSessionIdRo;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.user.entity.student.StudentLoanStepOne;

/**
 * 学生借款申请步骤1
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-10-08 10:31:24
 */
public interface StudentLoanStepOneService extends BaseService<StudentLoanStepOne> {


    /**
     * 添加学生借款申请第一步数据
     * */
    void stepOneInfoAdd(StudentLoanStepOneRo ro) throws BusinessException;

    /**
     * 查询反显学生第一步数据
     * */
    StudentLoanStepOneBo getInfo(BaseSessionIdRo ro,Integer flag,String id) throws BusinessException;

    /**
     * 管理后台查询学生第一步数据*/
    StudentLoanStepOneBo getInfoSys(StudentLoanStepSearchRo ro) throws BusinessException;
}