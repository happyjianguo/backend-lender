package com.yqg.user.service.student.studentloanstepfour;

import com.yqg.api.user.student.studentloanstepfour.bo.StudentLoanStepFourBo;
import com.yqg.api.user.student.studentloanstepfour.ro.StudentLoanStepFourRo;
import com.yqg.api.user.student.studentloanstepinfo.ro.StudentLoanStepSearchRo;
import com.yqg.common.core.BaseService;
import com.yqg.common.core.request.BaseSessionIdRo;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.user.entity.student.StudentLoanStepFour;

/**
 * 学生借款申请步骤4(联系人信息)
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-10-08 10:31:24
 */
public interface StudentLoanStepFourService extends BaseService<StudentLoanStepFour> {

    /**
     * 添加学生借款申请第四步数据
     * */
    void stepFourInfoAdd(StudentLoanStepFourRo ro) throws BusinessException;

    /**
     * 查询返现学生第四部数据
     * */
    StudentLoanStepFourBo getInfo(BaseSessionIdRo ro,Integer flag,String id) throws BusinessException;

    /**
     * 管理后台反显学生借款第四步*/
    StudentLoanStepFourBo getInfoSys(StudentLoanStepSearchRo ro) throws BusinessException;
}