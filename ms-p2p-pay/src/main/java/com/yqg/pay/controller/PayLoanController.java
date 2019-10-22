package com.yqg.pay.controller;

import com.yqg.api.pay.PayServiceApi;
import com.yqg.api.pay.loan.ro.LoanRo;
import com.yqg.api.pay.loan.vo.LoanResponse;
import com.yqg.common.core.BaseControllor;
import com.yqg.common.core.annocation.NotNeedLogin;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.pay.service.loan.PayLoanService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhaoruifeng on 2018/9/3.
 */
@Slf4j
@RestController
public class PayLoanController extends BaseControllor {


    @Autowired
    private PayLoanService payLoanService;

    @ApiOperation("放款")
    @PostMapping(value = PayServiceApi.path_loan)
    @ResponseBody
    public BaseResponse<LoanResponse> loan(@RequestBody LoanRo loanRo) throws Exception{

        return  new BaseResponse().successResponse(payLoanService.loan(loanRo));

    }

    @ApiOperation("放款结果查询")
    @NotNeedLogin
    @PostMapping(value = PayServiceApi.path_queryLoanResult)
    @ResponseBody
    public BaseResponse<LoanResponse> queryLoanResult(@RequestBody LoanRo loanRo) throws Exception {

        return new BaseResponse().successResponse(payLoanService.queryLoanResult(loanRo.getCreditorNo(),loanRo.getTransType()));
    }



    @ApiOperation("放款结果查询--定时")
    @NotNeedLogin
    @PostMapping(value = PayServiceApi.path_loanResult)
    @ResponseBody
    public BaseResponse loanResult() throws Exception {
        payLoanService.loanResult();
        return new BaseResponse().successResponse();
    }

//    @ApiOperation("满标清分处理中")
//    @PostMapping(value = PayServiceApi.path_loan_waiting)
//    @ResponseBody
//    public BaseResponse<JSONObject> loanWaiting() throws Exception {
//        payLoanService.loanWatingTask();
//        return new BaseResponse().successResponse();
//    }

//    @ApiOperation("还款清分")
//    @PostMapping(value = PayServiceApi.path_refundClarify)
//    @ResponseBody
//    public BaseResponse<JSONObject> refundClarify() throws Exception {
//        payLoanService.refundClarify();
//        return new BaseResponse().successResponse();
//    }

//    @ApiOperation("还款清分处理中")
//    @PostMapping(value = PayServiceApi.path_refundWatingTask)
//    @ResponseBody
//    public BaseResponse<JSONObject> refundWating() throws Exception {
//        payLoanService.refundWatingTask();
//        return new BaseResponse().successResponse();
//    }



}

