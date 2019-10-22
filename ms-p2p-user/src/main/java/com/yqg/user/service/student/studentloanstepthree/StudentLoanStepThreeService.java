package com.yqg.user.service.student.studentloanstepthree;

import com.yqg.api.user.student.studentloanstepinfo.ro.StudentLoanStepSearchRo;
import com.yqg.api.user.student.studentloanstepthree.bo.StudentLoanStepThreeBo;
import com.yqg.api.user.student.studentloanstepthree.ro.StudentLoanStepThreeRo;
import com.yqg.common.core.BaseService;
import com.yqg.common.core.request.BaseSessionIdRo;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.user.entity.student.StudentLoanStepThree;

/**
 * 学生借款申请步骤3(担保人信息)
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-10-08 10:31:24
 */
public interface StudentLoanStepThreeService extends BaseService<StudentLoanStepThree> {

    /**
     * 添加学生借款申请第三步数据
     * */
    void stepThreeInfoAdd(StudentLoanStepThreeRo ro) throws BusinessException;

    /**
     * 查询返现学生第三部数据
     * */
    StudentLoanStepThreeBo getInfo(BaseSessionIdRo ro,Integer flag,String id) throws BusinessException;

    /**
     * 管理后台查询学生借款第三部数据*/
    StudentLoanStepThreeBo getInfoSys(StudentLoanStepSearchRo ro) throws BusinessException;
}