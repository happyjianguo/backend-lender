package com.yqg.user.controllor.student;

import com.yqg.api.user.student.studentloanstepinfo.ro.StudentLoanStepSearchRo;
import com.yqg.api.user.student.studentloansteptwo.StudentLoanStepTwoServiceApi;
import com.yqg.api.user.student.studentloansteptwo.bo.StudentLoanStepTwoBo;
import com.yqg.api.user.student.studentloansteptwo.ro.StudentLoanStepTwoRo;
import com.yqg.common.core.BaseControllor;
import com.yqg.common.core.request.BaseSessionIdRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.user.service.student.studentloansteptwo.StudentLoanStepTwoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

/**
 * 学生借款申请步骤2
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-10-08 10:31:24
 */
@RestController
public class StudentLoanStepTwoController extends BaseControllor {
    @Autowired
    StudentLoanStepTwoService studentloansteptwoService;

    @ApiOperation(value = "添加学生借款申请第二步数据", notes = "添加学生借款申请第二步数据")
    @PostMapping(value = StudentLoanStepTwoServiceApi.path_studentStepTwoAdd)
    public BaseResponse stepOneAdd(@RequestBody StudentLoanStepTwoRo ro) throws BusinessException {
        this.studentloansteptwoService.stepTwoInfoAdd(ro);
        return new BaseResponse().successResponse();
    }

    @ApiOperation(value = "查询反显学生第二步数据", notes = "查询反显学生第二步数据")
    @PostMapping(value = StudentLoanStepTwoServiceApi.path_studentStepTwoInfo)
    public BaseResponse<StudentLoanStepTwoBo> getInfo(@RequestBody BaseSessionIdRo ro) throws BusinessException{
        StudentLoanStepTwoBo response = this.studentloansteptwoService.getInfo(ro,0,"");
        return new BaseResponse<StudentLoanStepTwoBo>().successResponse(response);
    }


    @ApiOperation(value = "管理后台反显学生借款第二部数据", notes = "管理后台反显学生借款第二部数据")
    @PostMapping(value = StudentLoanStepTwoServiceApi.path_studentStepTwoInfoSys)
    public BaseResponse<StudentLoanStepTwoBo> getInfoSys(@RequestBody StudentLoanStepSearchRo ro) throws BusinessException{
        StudentLoanStepTwoBo response = this.studentloansteptwoService.getInfoSys(ro);
        return new BaseResponse<StudentLoanStepTwoBo>().successResponse(response);
    }
}