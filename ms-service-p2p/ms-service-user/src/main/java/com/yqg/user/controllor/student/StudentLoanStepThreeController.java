package com.yqg.user.controllor.student;

import com.yqg.api.user.student.studentloanstepinfo.ro.StudentLoanStepSearchRo;
import com.yqg.api.user.student.studentloanstepthree.StudentLoanStepThreeServiceApi;
import com.yqg.api.user.student.studentloanstepthree.bo.StudentLoanStepThreeBo;
import com.yqg.api.user.student.studentloanstepthree.ro.StudentLoanStepThreeRo;
import com.yqg.common.core.BaseControllor;
import com.yqg.common.core.request.BaseSessionIdRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.user.service.student.studentloanstepthree.StudentLoanStepThreeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

/**
 * 学生借款申请步骤3(担保人信息)
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-10-08 10:31:24
 */
@RestController
public class StudentLoanStepThreeController extends BaseControllor {
    @Autowired
    StudentLoanStepThreeService studentloanstepthreeService;

    @ApiOperation(value = "添加学生借款申请第三步数据", notes = "添加学生借款申请第三步数据")
    @PostMapping(value = StudentLoanStepThreeServiceApi.path_studentStepThreeAdd)
    public BaseResponse stepOneAdd(@RequestBody StudentLoanStepThreeRo ro) throws BusinessException {
        this.studentloanstepthreeService.stepThreeInfoAdd(ro);
        return new BaseResponse().successResponse();
    }

    @ApiOperation(value = "查询反显学生第三步数据", notes = "查询反显学生第三步数据")
    @PostMapping(value = StudentLoanStepThreeServiceApi.path_studentStepThreeInfo)
    public BaseResponse<StudentLoanStepThreeBo> getInfo(@RequestBody BaseSessionIdRo ro) throws BusinessException{
        StudentLoanStepThreeBo response = this.studentloanstepthreeService.getInfo(ro,0, "");
        return new BaseResponse<StudentLoanStepThreeBo>().successResponse(response);
    }

    //
    @ApiOperation(value = "管理后台查询反显学生第三步数据", notes = "管理后台查询反显学生第三步数据")
    @PostMapping(value = StudentLoanStepThreeServiceApi.path_studentStepThreeInfoSys)
    public BaseResponse<StudentLoanStepThreeBo> getInfoSys(@RequestBody StudentLoanStepSearchRo ro) throws BusinessException{
        StudentLoanStepThreeBo response = this.studentloanstepthreeService.getInfoSys(ro);
        return new BaseResponse<StudentLoanStepThreeBo>().successResponse(response);
    }
}