package com.yqg.pay.controller;

import com.alibaba.fastjson.JSONObject;
import com.yqg.api.pay.PayServiceApi;
import com.yqg.api.pay.income.ro.InvestmentRo;
import com.yqg.api.pay.loan.ro.LoanRo;
import com.yqg.common.core.BaseControllor;
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

    @ApiOperation("满标清分")
    @PostMapping(value = PayServiceApi.path_loan)
    @ResponseBody
    public BaseResponse<JSONObject> fullClarify() throws Exception {
        payLoanService.fullScaleDistributionTask();
        return new BaseResponse().successResponse();
    }

    @ApiOperation("满标清分处理中")
    @PostMapping(value = PayServiceApi.path_loan_waiting)
    @ResponseBody
    public BaseResponse<JSONObject> loanWaiting() throws Exception {
        payLoanService.loanWatingTask();
        return new BaseResponse().successResponse();
    }

    @ApiOperation("还款清分")
    @PostMapping(value = PayServiceApi.path_refundClarify)
    @ResponseBody
    public BaseResponse<JSONObject> refundClarify() throws Exception {
        payLoanService.refundClarify();
        return new BaseResponse().successResponse();
    }

    @ApiOperation("还款清分处理中")
    @PostMapping(value = PayServiceApi.path_refundWatingTask)
    @ResponseBody
    public BaseResponse<JSONObject> refundWating() throws Exception {
        payLoanService.refundWatingTask();
        return new BaseResponse().successResponse();
    }


    @ApiOperation("理财用户回款计算")
    @PostMapping(value = PayServiceApi.path_calculationBackAmount)
    @ResponseBody
    public BaseResponse<JSONObject> calculationBackAmount() throws Exception {

        this.payLoanService.calculationBackAmount();

        return new BaseResponse().successResponse();
    }

    @ApiOperation("理财用户利息打回waiting")
    @PostMapping(value = PayServiceApi.path_backInterestAmountWating)
    @ResponseBody
    public BaseResponse<JSONObject> backInterestAmountWating() throws Exception {

        this.payLoanService.backInterestAmountWating();

        return new BaseResponse().successResponse();
    }

    @ApiOperation("理财用户回款")
    @PostMapping(value = PayServiceApi.path_backAmount)
    @ResponseBody
    public BaseResponse<JSONObject> backAmount() throws Exception {

        this.payLoanService.backAmount();

        return new BaseResponse().successResponse();
    }

    @ApiOperation("理财用户回款Waiting")
    @PostMapping(value = PayServiceApi.path_backAmountWating)
    @ResponseBody
    public BaseResponse<JSONObject> backAmountWating() throws Exception {

        this.payLoanService.backAmountWating();

        return new BaseResponse().successResponse();
    }

}

