package com.yqg.order.service.third;

import com.yqg.api.user.useraccount.bo.UserAccountBo;
import com.yqg.api.user.useruser.UserServiceApi;
import com.yqg.api.user.useruser.bo.UserBankAuthStatus;
import com.yqg.api.user.useruser.bo.UserBo;
import com.yqg.api.user.useruser.ro.MessageRo;
import com.yqg.api.user.useruser.ro.UserAuthBankStatusRo;
import com.yqg.api.user.useruser.ro.UserReq;
import com.yqg.api.user.useruser.ro.UserTypeSearchRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.order.service.third.impl.UserServiceFallbackImpl;
import io.swagger.annotations.ApiOperation;
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
@FeignClient(value = UserServiceApi.serviceName, fallback = UserServiceFallbackImpl.class)
public interface UserService {
    @PostMapping(value = UserServiceApi.path_userAuthBankStatus)
    public BaseResponse<UserBankAuthStatus> userAuthBankInfo(@RequestBody UserAuthBankStatusRo ro) throws BusinessException;

    @PostMapping(value = UserServiceApi.path_userListByType)
    public BaseResponse<UserAccountBo> userListByType(@RequestBody UserTypeSearchRo ro) throws BusinessException;

    @PostMapping(value = UserServiceApi.path_findUserById)
    public BaseResponse<UserBo> findUserById(@RequestBody UserReq ro) throws BusinessException;

    @PostMapping(value =UserServiceApi.path_findUserByMobileOrId)
    public BaseResponse<UserBo> findOneByMobileOrId(@RequestBody UserReq ro) throws BusinessException;

    @PostMapping(value =UserServiceApi.path_findOneByMobileOrName)
    public BaseResponse<UserBo> findOneByMobileOrName(@RequestBody UserReq ro) throws BusinessException;

    @ApiOperation(value = "发送通知消息")
    @PostMapping(value = UserServiceApi.path_addUserMessage)
    public BaseResponse<Object> addUserMessage(@RequestBody MessageRo ro) throws BusinessException;
}