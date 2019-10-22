package com.yqg.user.controllor.student;

import com.yqg.api.user.student.studentloanstepfour.StudentLoanStepFourServiceApi;
import com.yqg.api.user.student.studentloanstepfour.bo.StudentLoanStepFourBo;
import com.yqg.api.user.student.studentloanstepfour.ro.StudentLoanStepFourRo;
import com.yqg.api.user.student.studentloanstepinfo.ro.StudentLoanStepSearchRo;
import com.yqg.common.core.BaseControllor;
import com.yqg.common.core.request.BaseSessionIdRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.user.service.student.studentloanstepfour.StudentLoanStepFourService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

/**
 * 学生借款申请步骤4(联系人信息)
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-10-08 10:31:24
 */
@RestController
public class StudentLoanStepFourController extends BaseControllor {
    @Autowired
    StudentLoanStepFourService studentloanstepfourService;

    @ApiOperation(value = "添加学生借款申请第四步数据", notes = "添加学生借款申请第四步数据")
    @PostMapping(value = StudentLoanStepFourServiceApi.path_studentStepFourAdd)
    public BaseResponse stepOneAdd(@RequestBody StudentLoanStepFourRo ro) throws BusinessException {
        this.studentloanstepfourService.stepFourInfoAdd(ro);
        return new BaseResponse().successResponse();
    }

    @ApiOperation(value = "查询反显学生第四步数据", notes = "查询反显学生第四步数据")
    @PostMapping(value = StudentLoanStepFourServiceApi.path_studentStepFourInfo)
    public BaseResponse<StudentLoanStepFourBo> getInfo(@RequestBody BaseSessionIdRo ro) throws BusinessException{
        StudentLoanStepFourBo response = this.studentloanstepfourService.getInfo(ro,0,"");
        return new BaseResponse<StudentLoanStepFourBo>().successResponse(response);
    }

    @ApiOperation(value = "管理后台反显学生借款第四步", notes = "管理后台反显学生借款第四步")
    @PostMapping(value = StudentLoanStepFourServiceApi.path_studentStepFourInfoSys)
    public BaseResponse<StudentLoanStepFourBo> getInfoSys(@RequestBody StudentLoanStepSearchRo ro) throws BusinessException{
        StudentLoanStepFourBo response = this.studentloanstepfourService.getInfoSys(ro);
        return new BaseResponse<StudentLoanStepFourBo>().successResponse(response);
    }

}