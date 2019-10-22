package com.yqg.user.service.useraccount;

import com.yqg.api.user.useraccount.bo.OrganOrSuperUserBo;
import com.yqg.api.user.useraccount.bo.UserAccountAndBankInfoBo;
import com.yqg.api.user.useraccount.ro.UseraccountRo;
import com.yqg.api.user.useraccount.ro.WithdrawDepositRo;
import com.yqg.api.user.useraccounthistory.ro.UserAccountChangeRo;
import com.yqg.api.user.useraccounthistory.ro.UserAccountChangeSessionRo;
import com.yqg.api.user.useraccounthistory.ro.UserAccounthistoryRo;
import com.yqg.api.user.userbank.ro.UserBankRo;
import com.yqg.api.user.useruser.ro.UserPageRo;
import com.yqg.api.user.useruser.ro.UserRo;
import com.yqg.api.user.useruser.ro.UserTypeSearchRo;
import com.yqg.common.core.BaseService;
import com.yqg.common.core.response.BasePageResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.user.entity.UserAccount;
import com.yqg.user.entity.UserAccounthistory;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户账户表
 *
 * @author wu
 * @email wsc@yishufu.com
 * @date 2018-08-31 14:16:46
 */
public interface UserAccountService extends BaseService<UserAccount> {


    /**
     * 查询用户账户
     * */
    UserAccount selectUserAccount(String userUuid) throws BusinessException;

    /**
     * 查询所有用户账户
     * */
    List<UserAccount> selectAllUserAccount(UseraccountRo useraccountRo) throws BusinessException;

    /**
     * 增加余额金额
     * */
    void addUserAccountCurrent(UserAccountChangeRo ro)throws BusinessException;

    /**
     * 可用余额金额手动变动session
     * */
    void addUserAccountCurrentSession(UserAccountChangeSessionRo ro)throws BusinessException;

    /**
     * 用户活期购买债券减余额金额，增加活期冻结金额
     * */
    void subtractUserAccountCurrent(UserAccountChangeRo ro)throws BusinessException;

    /**
     * 用户充值，增加冻结金额
     * @param ro
     * @throws BusinessException
     */
    void userCharge(UserAccountChangeRo ro)throws BusinessException;

    /**
     * 用户活期释放债券，增加活期余额，减少活期冻结金额
     * */
    void releaseUserCurrentLockBlance(UserAccountChangeRo ro)throws BusinessException;

    /**
     * 放款 冻结金额- 转 在投金额+
     * */
    void addUserAccountInvesting(UserAccountChangeRo ro)throws BusinessException;

    /**
     * 放款失败 在投金额- 转 可用金额+
     * */
    void addUserAccountForFailed (UserAccountChangeRo ro)throws BusinessException;

    /**
     * 减用户在投金额
     * */
    void subtractUserAccountInvesting(UserAccountChangeRo ro)throws BusinessException;

    /**
     * 减用户冻结金额
     * */
    void subtractUserAccountbLocked(UserAccountChangeRo ro)throws BusinessException;

    /**
     * 机构投资者 huo 超级投资人 账户列表
     * @param ro
     * @return
     * @throws BusinessException
     */
    List<OrganOrSuperUserBo> getOrganOrSuperAccount(UserTypeSearchRo ro) throws BusinessException;

    /**
     * 账户管理--账户查询
     * @throws BusinessException
     */
    BasePageResponse<UserAccounthistory> getAccountHistoryByNameOrMobile(UserPageRo ro) throws BusinessException;

    /**
     * 我的资产--账户流水展示
     * @param ro
     * @return
     * @throws BusinessException
     */
    BasePageResponse<UserAccounthistory> getAccountHistoryByType (UserAccounthistoryRo ro) throws BusinessException;

    /**
     * 用户提现 点击提现按钮
     * @param ro
     * @throws Exception
     */
    void withdrawDeposit(WithdrawDepositRo ro) throws Exception;

    /**
     * 自动提现任务
     * @throws Exception
     */
    void autoWithdrawDeposit() throws Exception;

    /**
     * 自动提现任务查询
     * @throws Exception
     */
    void autoWithdrawDepositCheck() throws Exception;

    /**
     * 提现前 获取账户余额 和 银行卡号
     * @param
     * @return
     * @throws BusinessException
     */
    UserAccountAndBankInfoBo userBankAndAccountInfo(UserBankRo ro) throws BusinessException;
}