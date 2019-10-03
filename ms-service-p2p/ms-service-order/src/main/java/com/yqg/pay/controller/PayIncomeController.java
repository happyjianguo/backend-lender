package com.yqg.pay.controller;

import com.alibaba.fastjson.JSONObject;
import com.yqg.api.pay.PayServiceApi;
import com.yqg.api.pay.income.ro.InvestmentFinancingRo;
import com.yqg.api.pay.income.ro.InvestmentRo;
import com.yqg.common.core.BaseControllor;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.pay.service.income.PayIncomeService;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * 
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@Slf4j
@RestController
public class PayIncomeController extends BaseControllor {

    @Autowired
    private PayIncomeService payService;

    @ApiOperation("充值")
    @PostMapping(value = PayServiceApi.path_charge)
    @ResponseBody
    public BaseResponse<JSONObject> charge(@RequestBody ChargeParam param) throws Exception {
        JSONObject jsonObject = payService.charge(param.getUserUuid(),param.getAmount());
        return new BaseResponse().successResponse(jsonObject);
    }
    @ApiOperation("立即投资-散标")
    @PostMapping(value = PayServiceApi.path_immediateInvestment)
    @ResponseBody
    public BaseResponse<JSONObject> immediateInvestment(@RequestBody InvestmentRo investmentRo) throws Exception {
        JSONObject jsonObject = payService.immediateInvestment(investmentRo);
        return new BaseResponse().successResponse(jsonObject);
    }
    @ApiOperation("立即投资-理财")
    @PostMapping(value = PayServiceApi.path_immediateInvestmentFinancing)
    @ResponseBody
    public BaseResponse<JSONObject> immediateInvestmentFinancing(@RequestBody InvestmentFinancingRo investmentFinancingRo) throws Exception {
        JSONObject jsonObject = payService.immediateInvestmentFinancing(investmentFinancingRo);
        return new BaseResponse().successResponse(jsonObject);
    }
    @ApiOperation("立即还款")
    @PostMapping(value = PayServiceApi.path_repayment)
    @ResponseBody
    public BaseResponse<JSONObject> repayment(@RequestBody InvestmentRo investmentRo) throws Exception {
        JSONObject jsonObject = payService.repayment(investmentRo);
        return new BaseResponse().successResponse(jsonObject);
    }
    @ApiOperation("入账通知")
    @PostMapping(value = PayServiceApi.path_incomeNotify)
    @ResponseBody
    public BaseResponse incomeNotify(@RequestBody InvestmentRo investmentRo) throws Exception {
        log.info("incomeNotify:{}",investmentRo.toString());
//        payService.incomeNotify(investmentRo);
        return successResponse();
    }
    @ApiOperation("支付结果")
    @PostMapping(value = PayServiceApi.path_payResult)
    @ResponseBody
    public BaseResponse payResult() throws Exception {
        log.info("payResult开始");
        payService.payResult();
        log.info("payResult结束");
        return successResponse();
    }
    @ApiOperation("债权打包")
    @PostMapping(value = PayServiceApi.path_buildPackage)
    @ResponseBody
    public BaseResponse buildPackage() throws Exception {
        log.info("buildPackage开始");
        payService.buildPackage();
        log.info("buildPackage结束");
        return successResponse();
    }
    @ApiOperation("购买债权包")
    @PostMapping(value = PayServiceApi.path_buyPackage)
    @ResponseBody
    public BaseResponse buyPackage() throws Exception {
        log.info("buyPackage开始");
        payService.buyPackage();
        log.info("buyPackage结束");
        return successResponse();
    }
    @ApiOperation("超级投资人购买债权包")
    @PostMapping(value = PayServiceApi.path_superBuyPackage)
    @ResponseBody
    public BaseResponse superBuyPackage() throws Exception {
        log.info("path_superBuyPackage开始");

        payService.superBuyPackage();
        log.info("path_superBuyPackage结束");
        return successResponse();
    }

    @Data
    private class ChargeParam {
        private String userUuid;
        private BigDecimal amount;
    }
}