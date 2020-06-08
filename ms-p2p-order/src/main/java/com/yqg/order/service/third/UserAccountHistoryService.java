package com.yqg.order.service.third;

import com.yqg.api.user.useraccounthistory.UserAccounthistoryServiceApi;
import com.yqg.api.user.useraccounthistory.bo.UserAccountHistoryTotalBo;
import com.yqg.api.user.useraccounthistory.ro.UserAccountHistoryTotalRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.order.service.third.impl.UserAccountServiceFallbackImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Remark:
 * Created by huwei on 19.5.24.
 */
//@FeignClient(value = UserAccounthistoryServiceApi.serviceName, fallback = UserAccountServiceFallbackImpl.class)
public interface UserAccountHistoryService {

//    @ApiOperation(value = "查询指定日期各超级投资人的投资总金额", notes = "查询指定日期各超级投资人的投资总金额")
//    @PostMapping(value = UserAccounthistoryServiceApi.path_getUserAccountHistoryTotal)
    BaseResponse<List<UserAccountHistoryTotalBo>> getUserAccountHistoryTotal(@RequestBody UserAccountHistoryTotalRo ro) throws Exception;
}
