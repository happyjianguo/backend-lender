package com.yqg.user.controllor.student;

import com.yqg.api.user.student.studentloanstepinfo.ro.StudentLoanStepSearchRo;
import com.yqg.api.user.student.studentloanstepone.StudentLoanStepOneServiceApi;
import com.yqg.api.user.student.studentloanstepone.bo.StudentLoanStepOneBo;
import com.yqg.api.user.student.studentloanstepone.ro.StudentLoanStepOneRo;
import com.yqg.common.core.BaseControllor;
import com.yqg.common.core.request.BaseSessionIdRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.user.service.student.studentloanstepone.StudentLoanStepOneService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

/**
 * 学生借款申请步骤1
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-10-08 10:31:24
 */
@RestController
public class StudentLoanStepOneController extends BaseControllor {
    @Autowired
    StudentLoanStepOneService studentloansteponeService;

    @ApiOperation(value = "添加学生借款申请第一步数据", notes = "添加学生借款申请第一步数据")
    @PostMapping(value = StudentLoanStepOneServiceApi.path_studentStepOneAdd)
    public BaseResponse stepOneAdd(@RequestBody StudentLoanStepOneRo ro) throws BusinessException{
        this.studentloansteponeService.stepOneInfoAdd(ro);
        return new BaseResponse().successResponse();
    }

    @ApiOperation(value = "查询反显学生第一步数据", notes = "查询反显学生第一步数据")
    @PostMapping(value = StudentLoanStepOneServiceApi.path_studentStepOneInfo)
    public BaseResponse<StudentLoanStepOneBo> getInfo(@RequestBody BaseSessionIdRo ro) throws BusinessException{
        StudentLoanStepOneBo response = this.studentloansteponeService.getInfo(ro, 0, "");
        return new BaseResponse<StudentLoanStepOneBo>().successResponse(response);
    }

    @ApiOperation(value = "管理后台查询反显学生第一步数据", notes = "管理后台查询反显学生第一步数据")
    @PostMapping(value = StudentLoanStepOneServiceApi.path_studentStepOneInfoSys)
    public BaseResponse<StudentLoanStepOneBo> getInfoSys(@RequestBody StudentLoanStepSearchRo ro) throws BusinessException{
        StudentLoanStepOneBo response = this.studentloansteponeService.getInfoSys(ro);
        return new BaseResponse<StudentLoanStepOneBo>().successResponse(response);
    }


}