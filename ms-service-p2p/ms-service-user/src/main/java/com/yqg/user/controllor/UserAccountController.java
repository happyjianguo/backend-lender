package com.yqg.user.controllor;

import com.yqg.api.order.orderorder.bo.OrderSizeBo;
import com.yqg.api.order.orderorder.ro.OrderOrderPageRo;
import com.yqg.api.user.useraccount.UserAccountServiceApi;
import com.yqg.api.user.useraccount.bo.UserAccountBo;
import com.yqg.api.user.useraccount.ro.UserAccountNotSessionRo;
import com.yqg.api.user.useraccount.ro.UseraccountRo;
import com.yqg.common.core.BaseControllor;
import com.yqg.common.core.annocation.NotNeedLogin;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.httpclient.RestTemplateUtil;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.user.entity.UserAccount;
import com.yqg.user.service.order.OrderOrderService;
import com.yqg.user.service.useraccount.UserAccountService;
import io.swagger.annotations.ApiOperation;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户账户表
 *
 * @author wu
 * @email wsc@yishufu.com
 * @date 2018-08-31 14:16:46
 */
@RestController
//@RequestMapping("userAccount" )
public class UserAccountController extends BaseControllor {
    @Autowired
    UserAccountService useraccountService;
    @Autowired
    OrderOrderService orderOrderService;
    @Autowired
    RestTemplateUtil restTemplateUtil;

    //查询用户账户
    @ApiOperation(value = "查询用户账户", notes = "查询用户账户")
    @PostMapping(value = UserAccountServiceApi.path_selectUserAccount)
    public BaseResponse selectUserAccount(@RequestBody UseraccountRo ro) throws Exception {


        UserAccount userAccount = useraccountService.selectUserAccount(ro.getUserId());

        UserAccountBo userAccountBo = new UserAccountBo();
        BeanCoypUtil.copy(userAccount, userAccountBo);

        OrderOrderPageRo orderOrderPageRo = new OrderOrderPageRo();
        orderOrderPageRo.setStatus(1);
        orderOrderPageRo.setSessionId(ro.getSessionId());

        String data = orderOrderService.selectUsrOrderSize(orderOrderPageRo);
        JSONObject jsonObject = new JSONObject(data);
        userAccountBo.setSize(jsonObject.getInt("data"));
        return new BaseResponse<UserAccountBo>().successResponse(userAccountBo);

    }


    //查询用户账户不要sessionId
    @ApiOperation(value = "查询用户账户", notes = "查询用户账户")
    @PostMapping(value = UserAccountServiceApi.path_selectUserAccountNotSession)
    @NotNeedLogin
    public BaseResponse selectUserAccountNotSessionId(@RequestBody UserAccountNotSessionRo ro) throws Exception {


        UserAccount userAccount = useraccountService.selectUserAccount(ro.getUserId());

        UserAccountBo userAccountBo = new UserAccountBo();
        BeanCoypUtil.copy(userAccount, userAccountBo);

        return new BaseResponse<UserAccountBo>().successResponse(userAccountBo);

    }



    //查询所有用户账户
    @ApiOperation(value = "查询所有用户账户", notes = "查询所有用户账户")
    @PostMapping(value = UserAccountServiceApi.path_selectAllUserAccount)
    public BaseResponse selectAllUserAccount(@RequestBody UseraccountRo ro) throws Exception {

        List<UserAccount> userAccount = useraccountService.selectAllUserAccount(ro);

        return new BaseResponse<List<UserAccount>>().successResponse(userAccount);

    }




    //用户散标增加金额
    @NotNeedLogin
    @ApiOperation(value = "用户散标增加金额", notes = "用户散标增加金额")
    @PostMapping(value = UserAccountServiceApi.path_addUserSanbiaoBlance)
    public BaseResponse addUserSanbiaoBlance(@RequestBody UserAccountNotSessionRo ro) throws Exception {
try{
    useraccountService.addUserAccountSanbiao(ro.getUserId(),ro.getAmount());
}catch (Exception e){
    e.printStackTrace();
}


        return successResponse();

    }

    //用户散标减金额
    @NotNeedLogin
    @ApiOperation(value = "用户散标减金额", notes = "用户散标减金额")
    @PostMapping(value = UserAccountServiceApi.path_subtractUserSanbiaoBlance)
    public BaseResponse subtractUserSanbiaoBlance(@RequestBody UserAccountNotSessionRo ro) throws Exception {

        useraccountService.subtractUserAccountSanbiao(ro.getUserId(),ro.getAmount());

        return successResponse();

    }

    //用户充值活期增加余额金额
    @NotNeedLogin
    @ApiOperation(value = "用户充值活期增加余额金额", notes = "用户充值活期增加余额金额")
    @PostMapping(value = UserAccountServiceApi.path_addUserCurrentBlance)
    public BaseResponse addUserCurrentBlance(@RequestBody UserAccountNotSessionRo ro) throws Exception {

        useraccountService.addUserAccountCurrent(ro.getUserId(),ro.getAmount());

        return successResponse();

    }

    //用户活期购买债券减余额金额，增加活期冻结金额
    @NotNeedLogin
    @ApiOperation(value = "用户活期购买债券减余额金额，增加活期冻结金额", notes = "用户活期购买债券减余额金额，增加活期冻结金额")
    @PostMapping(value = UserAccountServiceApi.path_subtractUserCurrentBlance)
    public BaseResponse subtractUserCurrentBlance(@RequestBody UserAccountNotSessionRo ro) throws Exception {

        useraccountService.subtractUserAccountCurrent(ro.getUserId(),ro.getAmount());

        return successResponse();

    }

    //用户活期释放债券，增加活期余额，减少活期冻结金额
    @NotNeedLogin
    @ApiOperation(value = "用户活期释放债券，增加活期余额，减少活期冻结金额", notes = "用户活期释放债券，增加活期余额，减少活期冻结金额")
    @PostMapping(value = UserAccountServiceApi.path_releaseUserCurrentLockBlance)
    public BaseResponse releaseUserCurrentLockBlance(@RequestBody UserAccountNotSessionRo ro) throws Exception {

        useraccountService.releaseUserCurrentLockBlance(ro.getUserId(),ro.getAmount());

        return successResponse();

    }


    //用户活期回款，减少活期余额金额(相当于提现)
    @NotNeedLogin
    @ApiOperation(value = "用户活期回款，减少活期余额金额(相当于提现)", notes = "用户活期回款，减少活期余额金额(相当于提现)")
    @PostMapping(value = UserAccountServiceApi.path_returnMoneyUserCurrentBlance)
    public BaseResponse returnMoneyUserCurrentBlance(@RequestBody UserAccountNotSessionRo ro) throws Exception {

        useraccountService.returnMoneyUserCurrentBlance(ro.getUserId(),ro.getAmount());

        return successResponse();

    }



    //活期转定期（超级投资人购买定期）
    @NotNeedLogin
    @ApiOperation(value = "活期转定期（超级投资人购买定期）", notes = "活期转定期（超级投资人购买定期）")
    @PostMapping(value = UserAccountServiceApi.path_huo2dingqi)
    public BaseResponse huo2dingqi(@RequestBody UserAccountNotSessionRo ro) throws Exception {

        useraccountService.huo2dingqi(ro.getUserId(),ro.getAmount());

        return successResponse();

    }

    //活期转散标（超级投资人购买散标）
    @NotNeedLogin
    @ApiOperation(value = "活期转散标（超级投资人购买散标）", notes = "活期转散标（超级投资人购买散标）")
    @PostMapping(value = UserAccountServiceApi.path_huo2sanbiao)
    public BaseResponse huo2sanbiao(@RequestBody UserAccountNotSessionRo ro) throws Exception {

        useraccountService.huo2sanbiao(ro.getUserId(),ro.getAmount());

        return successResponse();

    }


    //用户充值定期增加余额金额
    @NotNeedLogin
    @ApiOperation(value = "用户充值定期增加余额金额", notes = "用户充值定期增加余额金额")
    @PostMapping(value = UserAccountServiceApi.path_addUserDepositBlance)
    public BaseResponse addUserDepositBlance(@RequestBody UserAccountNotSessionRo ro) throws Exception {

        useraccountService.addUserAccountDeposit(ro.getUserId(),ro.getAmount());

        return successResponse();

    }

    //用户定期购买债券减余额金额，增加定期冻结金额
    @NotNeedLogin
    @ApiOperation(value = "用户定期购买债券减余额金额，增加定期冻结金额", notes = "用户定期购买债券减余额金额，增加定期冻结金额")
    @PostMapping(value = UserAccountServiceApi.path_subtractUserDepositBlance)
    public BaseResponse subtractUserDepositBlance(@RequestBody UserAccountNotSessionRo ro) throws Exception {

        useraccountService.subtractUserAccountDeposit(ro.getUserId(),ro.getAmount());

        return successResponse();

    }

    //用户定期释放债券，增加定期余额，减少定期冻结金额
    @NotNeedLogin
    @ApiOperation(value = "用户定期释放债券，增加定期余额，减少定期冻结金额", notes = "用户定期释放债券，增加定期余额，减少定期冻结金额")
    @PostMapping(value = UserAccountServiceApi.path_releaseUserDepositLockBlance)
    public BaseResponse releaseUserDepositLockBlance(@RequestBody UserAccountNotSessionRo ro) throws Exception {

        useraccountService.releaseUserDepositLockBlance(ro.getUserId(),ro.getAmount());

        return successResponse();

    }


    //用户定期回款，减少定期冻结金额(相当于提现)
    @NotNeedLogin
    @ApiOperation(value = "用户定期回款，减少定期冻结金额(相当于提现)", notes = "用户定期回款，减少定期冻结金额(相当于提现)")
    @PostMapping(value = UserAccountServiceApi.path_returnMoneyUserDepositLockBlance)
    public BaseResponse returnMoneyUserDepositLockBlance(@RequestBody UserAccountNotSessionRo ro) throws Exception {

        useraccountService.returnMoneyUserDepositLockBlance(ro.getUserId(),ro.getAmount());

        return successResponse();

    }


}