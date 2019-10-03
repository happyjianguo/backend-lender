package com.yqg.user.service.useraccount;

import com.yqg.api.user.useraccount.ro.UseraccountRo;
import com.yqg.common.core.BaseService;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.user.entity.UserAccount;

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
     * 增加用户散标金额
     * */
    void addUserAccountSanbiao(String userUuid, BigDecimal amount)throws BusinessException;

    /**
     * 减用户散标金额
     * */
    void subtractUserAccountSanbiao(String userUuid, BigDecimal amount)throws BusinessException;

    /**
     * 用户充值活期增加余额金额
     * */
    void addUserAccountCurrent(String userUuid, BigDecimal amount)throws BusinessException;

    /**
     * 用户活期购买债券减余额金额，增加活期冻结金额
     * */
    void subtractUserAccountCurrent(String userUuid, BigDecimal amount)throws BusinessException;

    /**
     * 用户活期释放债券，增加活期余额，减少活期冻结金额
     * */
    void releaseUserCurrentLockBlance(String userUuid, BigDecimal amount)throws BusinessException;


    /**
     * 用户活期回款，减少活期余额金额(相当于提现)
     * */
    void returnMoneyUserCurrentBlance(String userUuid, BigDecimal amount)throws BusinessException;


    /**
     * 活期转定期
     * */
    void huo2dingqi(String userUuid, BigDecimal amount)throws BusinessException;

    /**
     * 活期转散标
     * */
    void huo2sanbiao(String userUuid, BigDecimal amount)throws BusinessException;

    /**
     * 用户充值定期增加余额金额
     * */
    void addUserAccountDeposit(String userUuid, BigDecimal amount)throws BusinessException;

    /**
     * 用户定期购买债券减余额金额，增加定期冻结金额
     * */
    void subtractUserAccountDeposit(String userUuid, BigDecimal amount)throws BusinessException;

    /**
     * 用户定期释放债券，增加定期余额，减少定期冻结金额
     * */
    void releaseUserDepositLockBlance(String userUuid, BigDecimal amount)throws BusinessException;


    /**
     * 用户定期回款，减少定期余额金额(相当于提现)
     * */
    void returnMoneyUserDepositLockBlance(String userUuid, BigDecimal amount)throws BusinessException;


}