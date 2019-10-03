package com.yqg.user.service.student.studentloanstepinfo;

import com.yqg.api.user.student.studentloanstepinfo.bo.StudentLoanAddInfBo;
import com.yqg.api.user.student.studentloanstepinfo.bo.StudentLoanRepayBo;
import com.yqg.api.user.student.studentloanstepinfo.bo.StudentLoanStepInfoBo;
import com.yqg.api.user.student.studentloanstepinfo.ro.StudentLoanCheckRo;
import com.yqg.api.user.student.studentloanstepinfo.ro.StudentLoanInitRo;
import com.yqg.api.user.student.studentloanstepinfo.ro.StudentLoanStepInfoRo;
import com.yqg.common.core.BaseService;
import com.yqg.common.core.request.BaseSessionIdRo;
import com.yqg.common.core.response.BasePageResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.user.entity.student.StudentLoanStepInfo;

import java.text.ParseException;
import java.util.List;

/**
 * 学生借款信息步骤表
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-10-08 10:31:24
 */
public interface StudentLoanStepInfoService extends BaseService<StudentLoanStepInfo> {

    /**
     * 添加修改学生认证步骤
     *
     *
     * */
    String addInfo(StudentLoanAddInfBo bo) throws BusinessException;


    /**
     * 查询学生认证步骤
     * */
    StudentLoanStepInfoBo getInfo(BaseSessionIdRo ro) throws BusinessException;

    /**
     * 学生认证订单初始化
     * */
    StudentLoanStepInfoBo StudentLoanInit(StudentLoanInitRo ro) throws BusinessException;

    /**
     * 计算学生还款计划
     * */
    List<StudentLoanRepayBo> StudentLoanRepayCount(StudentLoanInitRo ro) throws BusinessException;

    /**
     * 通过用户id查询订单信息
     * */
    StudentLoanStepInfo loanInfoByUserUuid(String userUuid) throws BusinessException;

    /**
     * 分页按条件查询学生借款订单信息*/
    BasePageResponse<StudentLoanStepInfo> queryForPage(StudentLoanStepInfoRo ro) throws BusinessException,ParseException;

    /**
     * 审核学生用户订单*/
    void checkStudentLoan(StudentLoanCheckRo ro) throws BusinessException;

}