package com.yqg.pay.service.third;

import com.yqg.api.user.useraccount.UserAccountServiceApi;
import com.yqg.api.user.useraccounthistory.ro.UserAccountChangeRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.pay.service.third.impl.UserAccountServiceFallbackImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Created by liuhuanhuan on 2018/9/10.
 */
//@FeignClient(value = UserAccountServiceApi.serviceName, fallback = UserAccountServiceFallbackImpl.class)
public interface UserAccountService {


//    @PostMapping(value = UserAccountServiceApi.path_addUserCurrentBlance)
    public BaseResponse addUserCurrentBlance(@RequestBody UserAccountChangeRo ro) throws BusinessException;

//    @PostMapping(value = UserAccountServiceApi.path_subtractUserCurrentBlance)
    public BaseResponse current2lock(@RequestBody UserAccountChangeRo ro) throws BusinessException;

//    @PostMapping(value = UserAccountServiceApi.path_releaseUserCurrentLockBlance)
    public BaseResponse lock2current(@RequestBody UserAccountChangeRo ro) throws BusinessException ;
//    @PostMapping(value = UserAccountServiceApi.path_userCharge)
    public BaseResponse userCharge(@RequestBody UserAccountChangeRo ro) throws BusinessException ;


}
