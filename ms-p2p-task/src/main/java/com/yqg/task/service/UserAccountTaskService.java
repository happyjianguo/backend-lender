package com.yqg.task.service;

import com.yqg.api.user.useraccount.UserAccountServiceApi;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BusinessException;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @Author: hyy
 * @Date: 2019/5/21 16:20
 * @Version 1.0
 * @EMAIL: hanyangyang@yishufu.com
 */
//@FeignClient(value = UserAccountServiceApi.serviceName, fallback = UserAccountTaskServiceImpl.class)
public interface UserAccountTaskService {

    /**
     * 用户账户自动提现 = Automatic withdrawal of user account
     * @throws BusinessException
     */
    @ApiOperation("用户账户自动提现")
    @PostMapping(value = UserAccountServiceApi.path_autoWithdrawDeposit)
    public BaseResponse autoWithdrawDeposit() throws Exception ;

    /**
     * 用户账户自动提现 结果查询 = User account automatic withdrawal results query
     * @throws BusinessException
     */
    @ApiOperation("用户账户自动提现 结果查询")
    @PostMapping(value = UserAccountServiceApi.path_autoWithdrawDepositCheck)
    public BaseResponse autoWithdrawDepositCheck() throws Exception ;
}