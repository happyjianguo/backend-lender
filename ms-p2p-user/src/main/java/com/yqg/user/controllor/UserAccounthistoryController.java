package com.yqg.user.controllor;

import com.yqg.api.user.useraccount.UserAccountServiceApi;
import com.yqg.api.user.useraccount.ro.WithdrawDepositRo;
import com.yqg.api.user.useraccounthistory.UserAccounthistoryServiceApi;
import com.yqg.api.user.useraccounthistory.bo.UserAccountHistoryTotalBo;
import com.yqg.api.user.useraccounthistory.ro.UserAccountHistoryTotalRo;
import com.yqg.common.core.BaseControllor;
import com.yqg.common.core.annocation.NotNeedLogin;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.user.entity.UserAccounthistory;
import com.yqg.user.service.useraccounthistory.UserAccounthistoryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户账户明细表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-11 10:43:36
 */
@RestController
public class UserAccounthistoryController extends BaseControllor {
    @Autowired
    UserAccounthistoryService useraccounthistoryService;

    //查询指定日期各超级投资人的投资总金额
    @NotNeedLogin
    @ApiOperation(value = "查询指定日期各超级投资人的投资总金额", notes = "查询指定日期各超级投资人的投资总金额")
    @PostMapping(value = UserAccounthistoryServiceApi.path_getUserAccountHistoryTotal)
    public BaseResponse<List<UserAccountHistoryTotalBo>> getUserAccountHistoryTotal(@RequestBody UserAccountHistoryTotalRo ro) throws Exception {

        List<UserAccountHistoryTotalBo> list = useraccounthistoryService.getUserAccountHistoryTotal(ro);

        return new BaseResponse<List<UserAccountHistoryTotalBo>>().successResponse(list);

    }



    @NotNeedLogin
    @ApiOperation(value = "查询在此之前24小时内投资人的成功投资总金额", notes = "查询在此之前24小时内投资人的成功投资总金额")
    @PostMapping(value = UserAccounthistoryServiceApi.path_getSuccessInvest)
    public BaseResponse getSuccessInvest(@RequestBody UserAccountHistoryTotalRo ro) throws Exception {
        Object[] bo = useraccounthistoryService.getSuccessInvest(ro);
        return new BaseResponse<Object[]>().successResponse(bo);
    }
}