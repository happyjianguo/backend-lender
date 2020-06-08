package com.yqg.order.service.third;

import com.yqg.api.user.useraccount.UserAccountServiceApi;
import com.yqg.api.user.useraccount.bo.UserAccountBo;
import com.yqg.api.user.useraccount.ro.UserAccountNotSessionRo;
import com.yqg.api.user.useraccounthistory.ro.UserAccountChangeRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.order.service.third.impl.UserAccountServiceFallbackImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Created by liuhuanhuan on 2018/9/10.
 */
//@FeignClient(value = UserAccountServiceApi.serviceName, fallback = UserAccountServiceFallbackImpl.class)
public interface UserAccountService {

//    @PostMapping(value = UserAccountServiceApi.path_addUserCurrentBlance)
    public BaseResponse addCurrentBlance(@RequestBody UserAccountChangeRo ro) throws BusinessException ;


    //    @PostMapping(value = UserAccountServiceApi.path_addUserCurrentBlance)
    public BaseResponse subtractLockedBlance(@RequestBody UserAccountChangeRo ro) throws BusinessException ;

//    @PostMapping(value = UserAccountServiceApi.path_subtractUserCurrentBlance)
    public BaseResponse current2lock(@RequestBody UserAccountChangeRo ro) throws BusinessException ;

//    @PostMapping(value = UserAccountServiceApi.path_releaseUserCurrentLockBlance)
    public BaseResponse lock2current(@RequestBody UserAccountChangeRo ro) throws BusinessException ;

//    @PostMapping(value = UserAccountServiceApi.path_addUserAccountInvesting)
    public BaseResponse lock2used(@RequestBody UserAccountChangeRo ro) throws BusinessException ;

//    @ApiOperation(value = "放款失败 or 用户还款 在投金额- 转 可用金额+", notes = "放款失败 or 用户还款 在投金额- 转 可用金额+")
//    @PostMapping(value = UserAccountServiceApi.path_addUserAccountForFailed)
    public BaseResponse used2current(@RequestBody UserAccountChangeRo ro) throws Exception ;




//    @PostMapping(value = UserAccountServiceApi.path_selectUserAccountNotSession)
    public BaseResponse<UserAccountBo> selectUserAccountNotSession(@RequestBody UserAccountNotSessionRo ro) throws BusinessException;
}
