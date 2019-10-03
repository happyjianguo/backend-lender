package com.yqg.pay.service.third;

import com.yqg.api.user.userbank.UserBankServiceApi;
import com.yqg.api.user.userbank.bo.UserBankBo;
import com.yqg.api.user.userbank.ro.UserBankRo;
import com.yqg.api.user.useruser.UserServiceApi;
import com.yqg.api.user.useruser.bo.UserBankAuthStatus;
import com.yqg.api.user.useruser.ro.UserAuthBankStatusRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.pay.service.third.impl.SysParamServiceFallbackImpl;
import com.yqg.pay.service.third.impl.UserBankServiceImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 用户
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@FeignClient(value = UserBankServiceApi.serviceName, fallback = UserBankServiceImpl.class)
public interface UserBankService {

    /**
     * 根据userId获取银行卡信息
     * @param ro
     * @return
     * @throws BusinessException
     */
    @PostMapping(value = UserBankServiceApi.path_getUserBankInfo)
    public BaseResponse<UserBankBo> getUserBankInfo(@RequestBody UserBankRo ro) throws BusinessException;
}