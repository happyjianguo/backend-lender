package com.yqg.user.controllor;

import com.yqg.api.order.orderorder.ro.OrderOrderPageRo;
import com.yqg.api.user.useraccount.UserAccountServiceApi;
import com.yqg.api.user.useraccount.bo.OrganOrSuperUserBo;
import com.yqg.api.user.useraccount.bo.UserAccountBo;
import com.yqg.api.user.useraccount.ro.UserAccountNotSessionRo;
import com.yqg.api.user.useraccount.ro.UserAccountSessionRo;
import com.yqg.api.user.useraccount.ro.UseraccountRo;
import com.yqg.api.user.useraccount.ro.WithdrawDepositRo;
import com.yqg.api.user.useraccounthistory.ro.UserAccountChangeRo;
import com.yqg.api.user.useraccounthistory.ro.UserAccountChangeSessionRo;
import com.yqg.api.user.useraccounthistory.ro.UserAccounthistoryRo;
import com.yqg.api.user.userbank.ro.UserBankRo;
import com.yqg.api.user.useruser.ro.UserPageRo;
import com.yqg.api.user.useruser.ro.UserTypeSearchRo;
import com.yqg.common.core.BaseControllor;
import com.yqg.common.core.annocation.NotNeedLogin;
import com.yqg.common.core.response.BasePageResponse;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.httpclient.RestTemplateUtil;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.user.entity.UserAccount;
import com.yqg.user.entity.UserAccounthistory;
import com.yqg.user.service.order.OrderOrderService;
import com.yqg.user.service.useraccount.UserAccountService;
import io.swagger.annotations.ApiOperation;
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

//        String data = orderOrderService.selectUsrOrderSize(orderOrderPageRo);
//        JSONObject jsonObject = new JSONObject(data);
//        userAccountBo.setSize(jsonObject.getInt("data"));
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

    //查询用户账户 要sessionId
    @ApiOperation(value = "查询用户账户", notes = "查询用户账户")
    @PostMapping(value = UserAccountServiceApi.path_selectUserAccountSession)
    public BaseResponse selectUserAccountSessionId(@RequestBody UserAccountSessionRo ro) throws Exception {


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

    //增加余额金额
    @NotNeedLogin
    @ApiOperation(value = "增加余额金额", notes = "增加余额金额")
    @PostMapping(value = UserAccountServiceApi.path_addUserCurrentBlance)
    public BaseResponse addUserCurrentBlance(@RequestBody UserAccountChangeRo ro) throws Exception {

        useraccountService.addUserAccountCurrent(ro);

        return successResponse();

    }

    //手动增加充值或提现流水 余额金额带流水 需要session的
    @ApiOperation(value = "可用余额金额手动变动session", notes = "可用余额金额手动变动session")
    @PostMapping(value = UserAccountServiceApi.path_addUserCurrentBlanceSession)
    public BaseResponse addUserCurrentBlanceSession(@RequestBody UserAccountChangeSessionRo ro) throws Exception {

        useraccountService.addUserAccountCurrentSession(ro);

        return successResponse();

    }

    //订单失败 冻结金额 转 余额
    @NotNeedLogin
    @ApiOperation(value = "订单失败 冻结金额 转 余额", notes = "订单失败 冻结金额 转 余额")
    @PostMapping(value = UserAccountServiceApi.path_releaseUserCurrentLockBlance)
    public BaseResponse releaseUserCurrentLockBlance(@RequestBody UserAccountChangeRo ro) throws Exception {

        useraccountService.releaseUserCurrentLockBlance(ro);

        return successResponse();

    }

    //用户购买债券减余额金额，增加冻结金额
    @NotNeedLogin
    @ApiOperation(value = "用户购买债券减余额金额，增加冻结金额", notes = "用户购买债券减余额金额，增加冻结金额")
    @PostMapping(value = UserAccountServiceApi.path_subtractUserCurrentBlance)
    public BaseResponse subtractUserCurrentBlance(@RequestBody UserAccountChangeRo ro) throws Exception {
        logger.info("--------------------"+ro);
        useraccountService.subtractUserAccountCurrent(ro);

        return successResponse();

    }

    //用户充值，增加冻结金额（流水正常记）
    @NotNeedLogin
    @ApiOperation(value = "用户充值，增加冻结金额", notes = "用户充值，增加冻结金额")
    @PostMapping(value = UserAccountServiceApi.path_userCharge)
    public BaseResponse userCharge(@RequestBody UserAccountChangeRo ro) throws Exception {

        useraccountService.userCharge(ro);

        return successResponse();

    }


    //放款 冻结金额- 转 在投金额+
    @NotNeedLogin
    @ApiOperation(value = "放款 冻结金额- 转 在投金额+", notes = "放款 冻结金额- 转 在投金额+")
    @PostMapping(value = UserAccountServiceApi.path_addUserAccountInvesting)
    public BaseResponse addUserAccountInvesting(@RequestBody UserAccountChangeRo ro) throws Exception {

        useraccountService.addUserAccountInvesting(ro);

        return successResponse();

    }

    //减用户在投金额
    @NotNeedLogin
    @ApiOperation(value = "减用户在投金额", notes = "减用户在投金额")
    @PostMapping(value = UserAccountServiceApi.path_subtractUserAccountInvesting)
    public BaseResponse subtractUserAccountInvesting(@RequestBody UserAccountChangeRo ro) throws Exception {

        useraccountService.subtractUserAccountInvesting(ro);

        return successResponse();

    }

    //放款失败 在投金额- 转 可用金额+
    @NotNeedLogin
    @ApiOperation(value = "放款失败 在投金额- 转 可用金额+", notes = "放款失败 在投金额- 转 可用金额+")
    @PostMapping(value = UserAccountServiceApi.path_addUserAccountForFailed)
    public BaseResponse addUserAccountForFailed(@RequestBody UserAccountChangeRo ro) throws Exception {

        useraccountService.addUserAccountForFailed(ro);

        return successResponse();

    }

    //机构投资者 huo 超级投资人 账户列表
    @NotNeedLogin
    @ApiOperation(value = "机构投资者 huo 超级投资人 账户列表", notes = "机构投资者 huo 超级投资人 账户列表")
    @PostMapping(value = UserAccountServiceApi.path_getOrganAccount)
    public BaseResponse getOrganOrSuperAccount(@RequestBody UserTypeSearchRo ro) throws Exception {

        List<OrganOrSuperUserBo> organOrSuperUserBos = useraccountService.getOrganOrSuperAccount(ro);

        return new BaseResponse<List<OrganOrSuperUserBo>>().successResponse(organOrSuperUserBos);

    }

    //账户管理--账户查询(根据姓名和手机号 查询用户账户流水)
    @ApiOperation(value = "账户管理--账户查询", notes = "账户管理--账户查询")
    @PostMapping(value = UserAccountServiceApi.path_getAccountHistoryByNameOrMobile)
    public BaseResponse<BasePageResponse<UserAccounthistory>> getAccountHistoryByNameOrMobile(@RequestBody UserPageRo ro) throws Exception {

        return new BaseResponse<BasePageResponse<UserAccounthistory>>().successResponse(useraccountService.getAccountHistoryByNameOrMobile(ro));

    }

    //我的资产--账户流水展示(根据交易类型BusinessType 查询用户账户流水)
    @ApiOperation(value = "我的资产--账户流水展示", notes = "我的资产--账户流水展示")
    @PostMapping(value = {UserAccountServiceApi.path_getAccountHistoryByType,UserAccountServiceApi.path_getAccountHistoryByType})
    public BaseResponse<BasePageResponse<UserAccounthistory>> getAccountHistoryByType(@RequestBody UserAccounthistoryRo ro) throws Exception {

        return new BaseResponse<BasePageResponse<UserAccounthistory>>().successResponse(useraccountService.getAccountHistoryByType(ro));

    }

    //用户提现前 预先填入银行卡号
    @NotNeedLogin
    @ApiOperation(value = "用户银行卡信息和账户信息", notes = "用户银行卡信息和账户信息")
    @PostMapping(value = UserAccountServiceApi.path_userBankAndAccountInfo)
    public BaseResponse userBankAndAccountInfo(@RequestBody UserBankRo ro) throws BusinessException {
        return new BaseResponse<>().successResponse(this.useraccountService.userBankAndAccountInfo(ro));
    }

    //当天购买债权总金额。 分别统计每个用户从上次拍快照到现在 购买债权总金额
    //点击提现按钮接口。
    //自动提现任务 查昨天拍的那条快照记录与购买债权作比对 >=0+主动提现金额    <购买债权-快照余额+主动提现金额  。算出总提现金额后，调用提现服务
    //然后 拍快照。 对当前所有用户的 可用余额拍照。清空 主动提现 和 总提现数据；
    //查询 提现结果以及相应处理

    //用户提现 点击提现按钮 只涉及到 创建或更新提现表 '当天主动提现累计金额'withdrawBalance
    @ApiOperation(value = "用户提现 点击提现按钮", notes = "用户提现 点击提现按钮")
    @PostMapping(value = UserAccountServiceApi.path_withdrawDeposit)
    public BaseResponse withdrawDeposit(@RequestBody WithdrawDepositRo ro) throws Exception {
        useraccountService.withdrawDeposit(ro);
        return successResponse();
    }

    @NotNeedLogin
    @ApiOperation(value = "自动提现任务", notes = "自动提现任务")
    @PostMapping(value = UserAccountServiceApi.path_autoWithdrawDeposit)
    public BaseResponse autoWithdrawDeposit() throws Exception {
        useraccountService.autoWithdrawDeposit();
        return successResponse();
    }

    @NotNeedLogin
    @ApiOperation(value = "自动提现任务查询", notes = "自动提现任务查询")
    @PostMapping(value = UserAccountServiceApi.path_autoWithdrawDepositCheck)
    public BaseResponse autoWithdrawDepositCheck() throws Exception {
        useraccountService.autoWithdrawDepositCheck();
        return successResponse();
    }


    @NotNeedLogin
    @ApiOperation(value = "减锁定账户", notes = "减锁定账户")
    @PostMapping(value = UserAccountServiceApi.path_subtractUserAccountbLocked)
    public BaseResponse subtractUserAccountbLocked(@RequestBody UserAccountChangeRo ro) throws Exception {
        useraccountService.subtractUserAccountbLocked(ro);
        return successResponse();
    }


}