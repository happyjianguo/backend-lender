package com.yqg.user.controllor;

import com.yqg.api.user.userbank.UserBankServiceApi;
import com.yqg.api.user.userbank.ro.UserBankRo;
import com.yqg.api.user.userbank.ro.UserBindBankCardRo;
import com.yqg.common.core.BaseControllor;
import com.yqg.common.core.annocation.NotNeedLogin;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.user.service.userbank.UserBankService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户银行卡信息
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:20:30
 */
@RestController
//@RequestMapping("userbank" )
public class UserBankController extends BaseControllor {
    @Autowired
    UserBankService userBankService;

    @ApiOperation(value = "用户绑卡", notes = "用户绑卡")
    @PostMapping(value = UserBankServiceApi.path_bindBankCard)
    public BaseResponse bindBankCard(@RequestBody UserBindBankCardRo ro) throws Exception{
        this.userBankService.bindBankCard(ro);
        return new BaseResponse<>().successResponse();
    }

    @NotNeedLogin
    @ApiOperation(value = "通过用户id查询用户绑卡信息", notes = "通过用户id查询用户绑卡信息")
    @PostMapping(value = UserBankServiceApi.path_getUserBankInfo)
    public BaseResponse userBankInfo(@RequestBody UserBankRo ro) throws BusinessException{
        return new BaseResponse<>().successResponse(this.userBankService.getUserBankInfo(ro));
    }

    @ApiOperation(value = "用户换绑卡", notes = "用户换绑卡")
    @PostMapping(value = UserBankServiceApi.path_updateUserBankInfo)
    public BaseResponse updateBankCard(@RequestBody UserBindBankCardRo ro) throws BusinessException{
        this.userBankService.updateBankCard(ro);
        return new BaseResponse<>().successResponse();
    }

}