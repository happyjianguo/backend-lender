package com.yqg.task.service;

import com.alibaba.fastjson.JSONObject;
import com.yqg.api.order.scatterstandard.ScatterstandardServiceApi;
import com.yqg.api.pay.PayServiceApi;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.task.service.impl.OrderTaskServiceFallbackImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 支付结果任务
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@FeignClient(value = PayServiceApi.serviceName, fallback = OrderTaskServiceFallbackImpl.class)
public interface OrderTaskService {
    @PostMapping(value = PayServiceApi.path_payResult)
    public BaseResponse payResult() throws BusinessException;

    @PostMapping(value = ScatterstandardServiceApi.path_deal6HoursLaterOrder)
    public BaseResponse deal6HoursLaterOrder() throws BusinessException;

    @PostMapping(value = ScatterstandardServiceApi.path_loan_flow_standard)
    public BaseResponse loanFlowStandard() throws BusinessException;

    @PostMapping(value = ScatterstandardServiceApi.path_deal30MinLaterOrder)
    public BaseResponse deal30MinLaterOrder() throws BusinessException;

    @ApiOperation("满标清分")
    @PostMapping(value = PayServiceApi.path_loan)
    public BaseResponse<JSONObject> fullClarify() throws Exception;

    @ApiOperation("满标清分处理中")
    @PostMapping(value = PayServiceApi.path_loan_waiting)
    public BaseResponse<JSONObject> loanWaiting() throws Exception ;


    @ApiOperation("还款清分")
    @PostMapping(value = PayServiceApi.path_refundClarify)
    public BaseResponse<JSONObject> refundClarify() throws Exception ;

    @ApiOperation("还款清分处理中")
    @PostMapping(value = PayServiceApi.path_refundWatingTask)
    public BaseResponse<JSONObject> refundWating() throws Exception ;


    @ApiOperation("债权打包")
    @PostMapping(value = PayServiceApi.path_buildPackage)
    public BaseResponse buildPackage() throws Exception;


    @ApiOperation("购买债权包")
    @PostMapping(value = PayServiceApi.path_buyPackage)
    public BaseResponse buyPackage() throws Exception ;

    @ApiOperation("超级投资人购买债权包")
    @PostMapping(value = PayServiceApi.path_superBuyPackage)
    public BaseResponse superBuyPackage() throws Exception ;

    @ApiOperation("还款清分复投")
    @PostMapping(value = PayServiceApi.path_refundClarify)
    public BaseResponse path_refundClarify() throws Exception ;

    @ApiOperation("理财用户回款金额计算")
    @PostMapping(value = PayServiceApi.path_calculationBackAmount)
    public BaseResponse path_calculationBackAmount() throws Exception ;

    @ApiOperation("理财用户利息打回waiting")
    @PostMapping(value = PayServiceApi.path_backInterestAmountWating)
    public BaseResponse path_backInterestAmountWating() throws Exception ;

    @ApiOperation("理财用户回款")
    @PostMapping(value = PayServiceApi.path_backAmount)
    public BaseResponse path_backAmount() throws Exception ;

    @ApiOperation("理财用户回款Waiting")
    @PostMapping(value = PayServiceApi.path_backAmountWating)
    public BaseResponse path_backAmountWating() throws Exception ;

}