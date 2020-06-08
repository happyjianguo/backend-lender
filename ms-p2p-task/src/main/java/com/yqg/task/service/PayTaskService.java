package com.yqg.task.service;

import com.yqg.api.pay.PayServiceApi;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.task.service.impl.PayTaskServiceFallbackImpl;
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
//@FeignClient(value = PayServiceApi.serviceName, fallback = PayTaskServiceFallbackImpl.class)
public interface PayTaskService {

    /**
     *
     * This method use to check order in payaccounthistory and run the suitable task for it based on status
     * and trade type ( PAYDAYINCOME and P2P_INCOME )
     *
     * @return none
     * @throws Exception general exception
     */
//    @ApiOperation("支付结果")
//    @PostMapping(value = PayServiceApi.path_payResult)
    public BaseResponse payResult() throws Exception ;


    /**
     *
     * This method use to check order in payaccounthistory and run the suitable task for it based on status
     * and trade type ( INTERNAL_TCC and PRE_SERVICE_FEE )
     *
     * @return none
     * @throws Exception general exception
     */
//    @PostMapping(value = PayServiceApi.path_loanResult)
    public BaseResponse loanResult() throws Exception;

}