package com.yqg.pay.controller;

import com.alibaba.fastjson.JSONObject;
import com.yqg.api.pay.PayServiceApi;
import com.yqg.api.pay.income.ro.IncomeRo;
import com.yqg.api.pay.income.ro.InvestmentRo;
import com.yqg.common.core.BaseControllor;
import com.yqg.common.core.annocation.NotNeedLogin;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.pay.service.income.PayIncomeService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation("收款")
    @PostMapping(value = PayServiceApi.path_incomeRequest)
    @ResponseBody
    public BaseResponse<JSONObject> incomeRequest(@RequestBody IncomeRo incomeRo) throws Exception {
//        JSONObject jsonObject = payService.incomeRequest(tradeNo, needPay, user.getData().getUserName(), TransTypeEnum.CHARGE.getDisburseType());
        JSONObject jsonObject = payService.incomeRequest(incomeRo);
        return new BaseResponse<JSONObject>().successResponse(jsonObject);
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
    @NotNeedLogin
    @PostMapping(value = PayServiceApi.path_payResult)
    @ResponseBody
    public BaseResponse payResult() throws Exception {
        log.info("payResult开始");
        payService.payResult();
        log.info("payResult结束");
        return successResponse();
    }
}