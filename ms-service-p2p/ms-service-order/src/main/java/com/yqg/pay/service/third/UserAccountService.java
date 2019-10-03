package com.yqg.pay.service.third;

import com.yqg.api.user.useraccount.UserAccountServiceApi;
import com.yqg.api.user.useraccount.bo.UserAccountBo;
import com.yqg.api.user.useraccount.ro.UserAccountNotSessionRo;
import com.yqg.api.user.useraccount.ro.UseraccountRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.pay.service.third.impl.SysParamServiceFallbackImpl;
import com.yqg.pay.service.third.impl.UserAccountServiceFallbackImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Created by liuhuanhuan on 2018/9/10.
 */
@FeignClient(value = UserAccountServiceApi.serviceName, fallback = UserAccountServiceFallbackImpl.class)
public interface UserAccountService {
    @PostMapping(value = UserAccountServiceApi.path_subtractUserSanbiaoBlance)
    public BaseResponse subtractScatterstandardBlance(@RequestBody UseraccountRo ro) throws BusinessException;

    //增加散标金额
    @PostMapping(value = UserAccountServiceApi.path_addUserSanbiaoBlance)
    public BaseResponse addScatterstandardBlance(@RequestBody UserAccountNotSessionRo ro) throws BusinessException;

    @PostMapping(value = UserAccountServiceApi.path_selectUserAccountNotSession)
    public BaseResponse<UserAccountBo> selectUserAccount(@RequestBody UseraccountRo ro) throws BusinessException;
    //查询所有用户账户
    @PostMapping(value = UserAccountServiceApi.path_selectAllUserAccount)
    public List<UserAccountBo> selectAllUserAccount() throws BusinessException;

    //增加定期金额
    @PostMapping(value = UserAccountServiceApi.path_addUserDepositBlance)
    public BaseResponse<UserAccountBo> addUserDepositBlance(@RequestBody UseraccountRo ro) throws BusinessException;

    //充值增加活期金额
    @PostMapping(value = UserAccountServiceApi.path_addUserCurrentBlance)
    public BaseResponse addUserCurrentBlance(@RequestBody UseraccountRo ro) throws BusinessException;

    //定期转定期冻结
    @PostMapping(value = UserAccountServiceApi.path_subtractUserDepositBlance)
    public BaseResponse dingqi2dongjie(@RequestBody UseraccountRo ro) throws BusinessException;

    //活期转定期
    @PostMapping(value = UserAccountServiceApi.path_huo2dingqi)
    public BaseResponse huoqi2dingqi(@RequestBody UseraccountRo ro) throws BusinessException;

    //活期转散标
    @PostMapping(value = UserAccountServiceApi.path_huo2sanbiao)
    public BaseResponse huoqi2sanbiao(@RequestBody UseraccountRo ro) throws BusinessException;

    //定期转定期冻结
    @PostMapping(value = UserAccountServiceApi.path_dongjie2huoqi)
    public void dongjie2dingqi(@RequestBody UseraccountRo ro) throws BusinessException;

    //给用户回款，减冻结金额
    @PostMapping(value = UserAccountServiceApi.path_returnMoneyUserDepositLockBlance)
    public BaseResponse returnMoneyUserDepositLockBlance(@RequestBody UseraccountRo ro) throws BusinessException;
}
