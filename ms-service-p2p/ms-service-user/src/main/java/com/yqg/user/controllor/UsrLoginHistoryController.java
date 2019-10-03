package com.yqg.user.controllor;

import com.yqg.api.user.usrloginhistory.UsrLoginHistoryServiceApi;
import com.yqg.api.user.usrloginhistory.ro.UsrLoginHistoryRo;
import com.yqg.common.core.BaseControllor;
import com.yqg.common.core.annocation.NotNeedLogin;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.user.service.usrloginhistory.UsrLoginHistoryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户登录历史表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:20:30
 */
@RestController
//@RequestMapping("usrloginhistory" )
public class UsrLoginHistoryController extends BaseControllor {
    @Autowired
    UsrLoginHistoryService usrLoginHistoryService;

    @NotNeedLogin
    @ApiOperation(value = "添加用户登录记录", notes = "添加用户登录记录")
    @PostMapping(value = UsrLoginHistoryServiceApi.path_sysUserLoginLog)
    public BaseResponse addSysUserLoginLog(@RequestBody UsrLoginHistoryRo ro) throws BusinessException {
        this.usrLoginHistoryService.addLoginHistory(ro);
        return new BaseResponse().successResponse();
    }
}