package com.yqg.user.controllor.student;

import com.yqg.api.user.student.studentloanstepinfo.StudentLoanStepInfoServiceApi;
import com.yqg.api.user.student.studentloanstepinfo.bo.StudentLoanRepayBo;
import com.yqg.api.user.student.studentloanstepinfo.bo.StudentLoanStepInfoBo;
import com.yqg.api.user.student.studentloanstepinfo.ro.StudentLoanCheckRo;
import com.yqg.api.user.student.studentloanstepinfo.ro.StudentLoanInitRo;
import com.yqg.api.user.student.studentloanstepinfo.ro.StudentLoanStepInfoRo;
import com.yqg.common.core.BaseControllor;
import com.yqg.common.core.request.BaseSessionIdRo;
import com.yqg.common.core.response.BasePageResponse;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.user.entity.student.StudentLoanStepInfo;
import com.yqg.user.service.student.studentloanstepinfo.StudentLoanStepInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 学生借款信息步骤表
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-10-08 10:31:24
 */
@RestController
public class StudentLoanStepInfoController extends BaseControllor {
    @Autowired
    StudentLoanStepInfoService studentloanstepinfoService;

    @ApiOperation(value = "查询学生认证步骤", notes = "查询学生认证步骤")
    @PostMapping(value = StudentLoanStepInfoServiceApi.path_studentStepInfo)
    public BaseResponse<StudentLoanStepInfoBo> addSysUserLoginLog(@RequestBody BaseSessionIdRo ro) throws BusinessException {
        StudentLoanStepInfoBo response = this.studentloanstepinfoService.getInfo(ro);
        return new BaseResponse<StudentLoanStepInfoBo>().successResponse(response);
    }

    @ApiOperation(value = "学生认证订单初始化", notes = "学生认证订单初始化")
    @PostMapping(value = StudentLoanStepInfoServiceApi.path_studentLoanInit)
    public BaseResponse<StudentLoanStepInfoBo> StudentLoanInit(@RequestBody StudentLoanInitRo ro) throws BusinessException {
        StudentLoanStepInfoBo response = this.studentloanstepinfoService.StudentLoanInit(ro);
        return new BaseResponse<StudentLoanStepInfoBo>().successResponse(response);
    }

    @ApiOperation(value = "计算学生还款计划", notes = "计算学生还款计划")
    @PostMapping(value = StudentLoanStepInfoServiceApi.path_studentLoanRepayCount)
    public BaseResponse<List<StudentLoanRepayBo>> StudentLoanRepayCount(@RequestBody StudentLoanInitRo ro) throws BusinessException {
        List<StudentLoanRepayBo> response = this.studentloanstepinfoService.StudentLoanRepayCount(ro);
        return new BaseResponse<List<StudentLoanRepayBo>>().successResponse(response);
    }

    @ApiOperation(value = "分页按条件查询学生借款订单信息", notes = "分页按条件查询学生借款订单信息")
    @PostMapping(value = StudentLoanStepInfoServiceApi.path_studentLoanQueryForPage)
    public BaseResponse<BasePageResponse<StudentLoanStepInfo>> StudentLoanRepayQueryForPage(@RequestBody StudentLoanStepInfoRo ro) throws BusinessException,ParseException {
        BasePageResponse response = this.studentloanstepinfoService.queryForPage(ro);
        return new BaseResponse<BasePageResponse<StudentLoanStepInfo>>().successResponse(response);
    }

    //
    @ApiOperation(value = "分页按条件查询学生借款订单信息", notes = "分页按条件查询学生借款订单信息")
    @PostMapping(value = StudentLoanStepInfoServiceApi.path_checkStudentLoan)
    public BaseResponse<BasePageResponse<StudentLoanStepInfo>> StudentLoanRepayQueryForPage(@RequestBody StudentLoanCheckRo ro) throws BusinessException,ParseException {
        this.studentloanstepinfoService.checkStudentLoan(ro);
        return new BaseResponse().successResponse();
    }
}