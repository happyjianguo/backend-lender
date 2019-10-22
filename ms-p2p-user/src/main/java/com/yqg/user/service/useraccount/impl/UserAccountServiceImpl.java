package com.yqg.user.service.useraccount.impl;

import com.alibaba.fastjson.JSONObject;
import com.yqg.api.pay.loan.ro.LoanRo;
import com.yqg.api.pay.loan.vo.LoanResponse;
import com.yqg.api.pay.payaccounthistory.bo.PayAccountHistoryBo;
import com.yqg.api.pay.payaccounthistory.ro.PayAccountHistoryRo;
import com.yqg.api.system.sysuser.bo.SysUserBo;
import com.yqg.api.user.enums.AccountTransTypeEnum;
import com.yqg.api.user.enums.UserExceptionEnums;
import com.yqg.api.user.enums.UserTypeEnum;
import com.yqg.api.user.useraccount.bo.OrganOrSuperUserBo;
import com.yqg.api.user.useraccount.bo.UserAccountAndBankInfoBo;
import com.yqg.api.user.useraccount.bo.UserAccountBo;
import com.yqg.api.user.useraccount.ro.UserAccountNotSessionRo;
import com.yqg.api.user.useraccount.ro.UseraccountRo;
import com.yqg.api.user.useraccount.ro.WithdrawDepositRo;
import com.yqg.api.user.useraccounthistory.ro.UserAccountChangeRo;
import com.yqg.api.user.useraccounthistory.ro.UserAccountChangeSessionRo;
import com.yqg.api.user.useraccounthistory.ro.UserAccounthistoryRo;
import com.yqg.api.user.userbank.ro.UserBankRo;
import com.yqg.api.user.useruser.ro.UserPageRo;
import com.yqg.api.user.useruser.ro.UserTypeSearchRo;
import com.yqg.common.core.response.BasePageResponse;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.dao.ExtendQueryCondition;
import com.yqg.common.enums.TransPreFixTypeEnum;
import com.yqg.common.enums.TransTypeEnum;
import com.yqg.common.enums.UserAccountBusinessTypeEnum;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.common.utils.DateUtils;
import com.yqg.common.utils.OrderNoCreator;
import com.yqg.common.utils.SignUtils;
import com.yqg.user.dao.*;
import com.yqg.user.entity.*;
import com.yqg.user.service.UserCommonServiceImpl;
import com.yqg.user.service.third.PayService;
import com.yqg.user.service.third.SysUserService;
import com.yqg.user.service.useraccount.UserAccountService;
import com.yqg.user.service.useruser.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.yqg.common.enums.TransTypeEnum.BUY_CREDITOR;

/**
 * 用户账户表
 *
 * @author wu
 * @email wsc@yishufu.com
 * @date 2018-08-31 14:16:46
 */
@Service("UserAccountService")
@Log
public class UserAccountServiceImpl extends UserCommonServiceImpl implements UserAccountService {
    @Autowired
    protected UserAccountDao userAccountDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserAccounthistoryDao userAccounthistoryDao;
    @Autowired
    private UserBankDao userBankDao;
    @Autowired
    private PayService payService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserWithdrawDao userWithdrawDao;
    @Autowired
    private SysUserService sysUserService;


    @Override
    public UserAccount findById(String id) throws BusinessException {
        return this.userAccountDao.findOneById(id, new UserAccount());
    }

    @Override
    public <E> E findById(String id, Class<E> boClass) throws BusinessException {
        UserAccount UserAccount = findById(id);
        E bo = BeanCoypUtil.copyToNewObject(UserAccount, boClass);
        return bo;
    }

    @Override
    public UserAccount findOne(UserAccount entity) throws BusinessException {
        return this.userAccountDao.findOne(entity);
    }

    @Override
    public <E> E findOne(UserAccount entity, Class<E> boClass) throws BusinessException {
        UserAccount UserAccount = findOne(entity);
        E bo = BeanCoypUtil.copyToNewObject(UserAccount, boClass);
        return bo;
    }


    @Override
    public List<UserAccount> findList(UserAccount entity) throws BusinessException {
        return this.userAccountDao.findForList(entity);
    }

    @Override
    public <E> List<E> findList(UserAccount entity, Class<E> boClass) throws BusinessException {
        List<UserAccount> UserAccountList = findList(entity);
        List<E> boList = new ArrayList<>();
        for (UserAccount UserAccount : UserAccountList) {
            E bo = BeanCoypUtil.copyToNewObject(UserAccount, boClass);
            boList.add(bo);
        }
        return boList;
    }

    @Override
    @Transactional
    public String addOne(UserAccount entity) throws BusinessException {
        return userAccountDao.addOne(entity);
    }

    @Override
    @Transactional
    public void deleteById(String id) throws BusinessException {
        userAccountDao.deleteOne(findById(id));
    }

    @Override
    @Transactional
    public void deleteOne(UserAccount entity) throws BusinessException {
        this.userAccountDao.deleteOne(entity);
    }

    @Override
    @Transactional
    public void updateOne(UserAccount entity) throws BusinessException {
        this.userAccountDao.updateOne(entity);
    }

    /**
     * 查询用户账户
     * */
    @Override
    @Transactional
    public UserAccount selectUserAccount(String userUuid) throws BusinessException {

        UserAccount entity = new UserAccount();
        entity.setUserUuid(userUuid);
        entity.setDisabled(0);

        UserAccount userAccountResult = this.findOne(entity);

        if(userAccountResult == null){
            UserAccount userAccountInsert = new UserAccount();
            userAccountInsert.setUserUuid(userUuid);
            userAccountInsert.setCreateTime(new Date());
            userAccountInsert.setUpdateTime(new Date());
            userAccountInsert.setCurrentBalance(new BigDecimal(0));
            userAccountInsert.setLockedBalance(new BigDecimal(0));
            userAccountInsert.setInvestingBanlance(new BigDecimal(0));
            this.addOne(userAccountInsert);

            return this.findOne(entity);
        }else {
            return userAccountResult;
        }

    }

    @Override
    public List<UserAccount> selectAllUserAccount(UseraccountRo useraccountRo) throws BusinessException {

        UserAccount entity = new UserAccount();
        entity.setDisabled(0);
        List<UserAccount> userAccountList = this.findList(entity);

        return userAccountList;
    }


    //插入 账户流水
    public void insertUserAccountHistory(UserAccountChangeRo ro) throws BusinessException{
        UserAccountChangeRo userAccounthistoryRo = new UserAccountChangeRo();
        userAccounthistoryRo.setPayType(ro.getPayType());
        userAccounthistoryRo.setBusinessType(ro.getBusinessType());
        userAccounthistoryRo.setUserUuid(ro.getUserUuid());
        userAccounthistoryRo.setAmount(ro.getAmount());
        userAccounthistoryRo.setTradeInfo(ro.getTradeInfo());

        userAccounthistoryRo.setLastBanlance(ro.getLastBanlance());
        userAccounthistoryRo.setCurrentBanlance(ro.getCurrentBanlance());
        userAccounthistoryRo.setLockedBanlance(ro.getLockedBanlance());
        userAccounthistoryRo.setInvestBanlance(ro.getInvestBanlance());
        userAccounthistoryRo.setType(ro.getType());
        userAccounthistoryRo.setDealTime(new Date());

        userAccounthistoryService.insterUserAccountHistory(userAccounthistoryRo);
    }

    //插入 账户流水
    public void insertUserAccountHistorySession(UserAccountChangeSessionRo ro) throws BusinessException{
        UserAccountChangeRo userAccounthistoryRo = new UserAccountChangeRo();
        userAccounthistoryRo.setCreateUser(ro.getCreateUser());
        userAccounthistoryRo.setUserUuid(ro.getUserUuid());
        userAccounthistoryRo.setType(ro.getType());
        userAccounthistoryRo.setPayType(ro.getPayType());
        userAccounthistoryRo.setBusinessType(ro.getBusinessType());
        userAccounthistoryRo.setTradeInfo(ro.getTradeInfo());
        userAccounthistoryRo.setDealTime(new Date());

        userAccounthistoryRo.setAmount(ro.getAmount());
        userAccounthistoryRo.setLastBanlance(ro.getLastBanlance());
        userAccounthistoryRo.setCurrentBanlance(ro.getCurrentBanlance());
        userAccounthistoryRo.setLockedBanlance(ro.getLockedBanlance());
        userAccounthistoryRo.setInvestBanlance(ro.getInvestBanlance());

        userAccounthistoryService.insterUserAccountHistory(userAccounthistoryRo);
    }

    /**
     * 可用余额金额++
     * */
    @Override
    @Transactional
    public void addUserAccountCurrent(UserAccountChangeRo ro) throws BusinessException {

        logger.info("可用余额金额++,userUuid---{},金额{}",ro.getUserUuid(),ro.getAmount());
        if(redisUtil.tryLock(userParamContants.LOCK_USERACCOUNT_USERUUID + ro.getUserUuid(), 5000)){
            logger.info("通过锁校验");
            UserAccount userAccount = this.selectUserAccount(ro.getUserUuid());

            logger.info("可用余额金额++ 初始账户实体{}",userAccount);
            BigDecimal lastMoney = userAccount.getCurrentBalance();
            BigDecimal lockMoney = userAccount.getLockedBalance();
            BigDecimal investMoney = userAccount.getInvestingBanlance();
            BigDecimal money = userAccount.getCurrentBalance().add(ro.getAmount());
            userAccount.setCurrentBalance(money);

            logger.info("可用余额金额++ 前 待更新实体{}",userAccount);
            this.updateOne(userAccount);

            ro.setCurrentBanlance(money);
            ro.setLockedBanlance(lockMoney);
            ro.setLastBanlance(lastMoney);
            ro.setInvestBanlance(investMoney);

            ro.setType(AccountTransTypeEnum.CURRENT_ADD.getDisburseType());

            this.insertUserAccountHistory(ro);
            redisUtil.releaseLock(userParamContants.LOCK_USERACCOUNT_USERUUID + ro.getUserUuid());

        }
    }

    /**
     * 可用余额金额手动变动session
     * */
    @Override
    @Transactional
    public void addUserAccountCurrentSession(UserAccountChangeSessionRo ro) throws BusinessException {

        logger.info("可用余额金额session++,userUuid---{},金额{}",ro.getUserUuid(),ro.getAmount());
        if(redisUtil.tryLock(userParamContants.LOCK_USERACCOUNT_USERUUID + ro.getUserUuid(), 5000)){
            logger.info("通过锁校验");
            UserAccount userAccount = this.selectUserAccount(ro.getUserUuid());

            BigDecimal lastMoney = userAccount.getCurrentBalance();
            BigDecimal lockMoney = userAccount.getLockedBalance();
            BigDecimal investMoney = userAccount.getInvestingBanlance();
            BigDecimal money = new BigDecimal("0.00");
            if (ro.getBusinessType().equals(UserAccountBusinessTypeEnum.CHARGE.getEnname())){
                logger.info("可用余额金额session++ 初始账户实体{}",userAccount);
                money = userAccount.getCurrentBalance().add(ro.getAmount());
                ro.setType(AccountTransTypeEnum.CURRENT_ADD.getDisburseType());
                ro.setTradeInfo("手动充值");
            }else if (ro.getBusinessType().equals(UserAccountBusinessTypeEnum.RECHARGE_SUCCESS.getEnname())){
                logger.info("可用余额金额session-- 初始账户实体{}",userAccount);
                if(userAccount.getCurrentBalance().compareTo(ro.getAmount()) == -1){
                    throw new BusinessException(UserExceptionEnums.AMOUNT_ERROR);
                }
                money = userAccount.getCurrentBalance().subtract(ro.getAmount());
                ro.setType(AccountTransTypeEnum.CURRENT_SUB.getDisburseType());
                ro.setTradeInfo("手动提现");
            }

            userAccount.setCurrentBalance(money);

            logger.info("可用余额金额++ 前 待更新实体{}",userAccount);
            this.updateOne(userAccount);

            ro.setCurrentBanlance(money);
            ro.setLockedBanlance(lockMoney);
            ro.setLastBanlance(lastMoney);
            ro.setInvestBanlance(investMoney);

//            UserUser user = this.userService.findById(ro.getUserId());//操作人 id
            UserAccountNotSessionRo accountNotSessionRo = new UserAccountNotSessionRo();
            accountNotSessionRo.setUserId(ro.getUserId());
            logger.info("请求system user参数{}",accountNotSessionRo);
            BaseResponse<SysUserBo> userBoBaseResponse = this.sysUserService.getSysUserById(accountNotSessionRo);
            logger.info("返回system user参数{}",userBoBaseResponse.getData());
            if (userBoBaseResponse.isSuccess() && userBoBaseResponse.getData() != null){
                SysUserBo user = userBoBaseResponse.getData();
                ro.setCreateUser(user.getRealname());
            }
            this.insertUserAccountHistorySession(ro);
            redisUtil.releaseLock(userParamContants.LOCK_USERACCOUNT_USERUUID + ro.getUserUuid());

        }
    }

    /**
     * 订单失败 冻结金额- 转 可用余额+
     */
    @Override
    @Transactional
    public void releaseUserCurrentLockBlance(UserAccountChangeRo ro) throws BusinessException {

        logger.info("冻结金额- 转 可用余额+,userUuid---{},金额{}",ro.getUserUuid(),ro.getAmount());
        if(redisUtil.tryLock(userParamContants.LOCK_USERACCOUNT_USERUUID + ro.getUserUuid(), 5000)){

            logger.info("通过锁校验");
            UserAccount userAccount = this.selectUserAccount(ro.getUserUuid());

            logger.info("冻结金额- 转 可用余额+ 初始账户实体{}",userAccount);
            if(userAccount.getLockedBalance().compareTo(ro.getAmount()) == -1){
                throw new BusinessException(UserExceptionEnums.AMOUNT_ERROR);
            }

            BigDecimal investMoney = userAccount.getInvestingBanlance();
            BigDecimal lastMoney = userAccount.getCurrentBalance();
            BigDecimal lockMoney = userAccount.getLockedBalance().subtract(ro.getAmount());
            BigDecimal balanceMoney = userAccount.getCurrentBalance().add(ro.getAmount());

            userAccount.setCurrentBalance(balanceMoney);
            userAccount.setLockedBalance(lockMoney);

            logger.info("冻结金额- 转 可用余额+ 前 待更新实体{}",userAccount);
            this.updateOne(userAccount);

            ro.setCurrentBanlance(balanceMoney);
            ro.setLockedBanlance(lockMoney);
            ro.setLastBanlance(lastMoney);
            ro.setInvestBanlance(investMoney);

            ro.setType(AccountTransTypeEnum.LOCK_TO_CURRENT.getDisburseType());

            this.insertUserAccountHistory(ro);

            redisUtil.releaseLock(userParamContants.LOCK_USERACCOUNT_USERUUID + ro.getUserUuid());

        }

    }


    /**
     * 用户余额购买债券减余额金额，增加冻结金额
     * */
    @Override
    @Transactional
    public void subtractUserAccountCurrent(UserAccountChangeRo ro) throws BusinessException {

        logger.info("减余额金额，增加冻结金额,userUuid---{},金额{}",ro.getUserUuid(),ro.getAmount());
        if(redisUtil.tryLock(userParamContants.LOCK_USERACCOUNT_USERUUID + ro.getUserUuid(), 5000)){

            logger.info("通过锁校验");
            UserAccount userAccount = this.selectUserAccount(ro.getUserUuid());

            logger.info("减余额金额，增加冻结金额 初始账户实体{}",userAccount);
            if(userAccount.getCurrentBalance().compareTo(ro.getAmount()) == -1){
                throw new BusinessException(UserExceptionEnums.AMOUNT_ERROR);
            }

            BigDecimal lastMoney = userAccount.getCurrentBalance();
            BigDecimal balanceMoney = userAccount.getCurrentBalance().subtract(ro.getAmount());
            BigDecimal lockMoney = userAccount.getLockedBalance().add(ro.getAmount());
            BigDecimal investMoney = userAccount.getInvestingBanlance();

            userAccount.setCurrentBalance(balanceMoney);
            userAccount.setLockedBalance(lockMoney);

            logger.info("减余额金额，增加冻结金额 前 待更新实体{}",userAccount);
            this.updateOne(userAccount);

            ro.setCurrentBanlance(balanceMoney);
            ro.setLockedBanlance(lockMoney);
            ro.setLastBanlance(lastMoney);
            ro.setInvestBanlance(investMoney);

            ro.setType(AccountTransTypeEnum.CURRENT_TO_LOCK.getDisburseType());

            this.insertUserAccountHistory(ro);

            redisUtil.releaseLock(userParamContants.LOCK_USERACCOUNT_USERUUID + ro.getUserUuid());

        }
    }

    /**
     * 用户充值，增加冻结金额
     * */
    @Override
    @Transactional
    public void userCharge(UserAccountChangeRo ro)throws BusinessException {

        logger.info("增加冻结金额,userUuid---{},金额{}",ro.getUserUuid(),ro.getAmount());
        if(redisUtil.tryLock(userParamContants.LOCK_USERACCOUNT_USERUUID + ro.getUserUuid(), 5000)){

            logger.info("通过锁校验");
            UserAccount userAccount = this.selectUserAccount(ro.getUserUuid());

            logger.info("用户充值，增加冻结金额 初始账户实体{}",userAccount);
            ro.setCurrentBanlance(userAccount.getCurrentBalance().add(ro.getAmount()));
            ro.setLockedBanlance(userAccount.getLockedBalance());
            ro.setLastBanlance(userAccount.getCurrentBalance());
            ro.setInvestBanlance(userAccount.getInvestingBanlance());
            ro.setType(AccountTransTypeEnum.CURRENT_ADD.getDisburseType());
            ro.setBusinessType(UserAccountBusinessTypeEnum.CHARGE.getEnname());//"充值"
            ro.setTradeInfo("用户充值");
            this.insertUserAccountHistory(ro);

            ro.setCurrentBanlance(userAccount.getCurrentBalance());
            ro.setLockedBanlance(userAccount.getLockedBalance().add(ro.getAmount()));
            ro.setLastBanlance(userAccount.getCurrentBalance().add(ro.getAmount()));
            ro.setInvestBanlance(userAccount.getInvestingBanlance());
            ro.setType(AccountTransTypeEnum.CURRENT_TO_LOCK.getDisburseType());
            ro.setBusinessType(UserAccountBusinessTypeEnum.BUY_CREDITOR.getEnname());//"购买债权"
            ro.setTradeInfo("用户购买债权");
            this.insertUserAccountHistory(ro);

            userAccount.setLockedBalance(userAccount.getLockedBalance().add(ro.getAmount()));

            logger.info("用户充值，增加冻结金额 前 待更新实体{}",userAccount);
            this.updateOne(userAccount);

            redisUtil.releaseLock(userParamContants.LOCK_USERACCOUNT_USERUUID + ro.getUserUuid());

        }
    }

    /**
     * 放款 冻结金额- 转 在投金额+
     * */
    @Override
    @Transactional
    public void addUserAccountInvesting(UserAccountChangeRo ro) throws BusinessException {

        logger.info("冻结金额- 转 在投金额+,userUuid---{},金额{}",ro.getUserUuid(),ro.getAmount());
        if(redisUtil.tryLock(userParamContants.LOCK_USERACCOUNT_USERUUID + ro.getUserUuid(), 5000)){
            logger.info("通过锁校验");
            UserAccount userAccount = this.selectUserAccount(ro.getUserUuid());

            logger.info("冻结金额- 转 在投金额+ 初始账户实体{}",userAccount);
            BigDecimal lastMoney = userAccount.getCurrentBalance();
            BigDecimal lockMoney = userAccount.getLockedBalance().subtract(ro.getAmount());
            BigDecimal money = userAccount.getInvestingBanlance().add(ro.getAmount());

            userAccount.setInvestingBanlance(money);
            userAccount.setLockedBalance(lockMoney);

            logger.info("冻结金额- 转 在投金额+ 前 待更新实体{}",userAccount);
            this.updateOne(userAccount);

            ro.setCurrentBanlance(lastMoney);
            ro.setLockedBanlance(lockMoney);
            ro.setLastBanlance(lastMoney);
            ro.setInvestBanlance(money);

            ro.setType(AccountTransTypeEnum.LOCK_TO_INVEST.getDisburseType());

            this.insertUserAccountHistory(ro);

            redisUtil.releaseLock(userParamContants.LOCK_USERACCOUNT_USERUUID + ro.getUserUuid());

        }
    }

    /**
     * 放款失败 在投金额- 转 可用金额+
     * */
    @Override
    @Transactional
    public void addUserAccountForFailed(UserAccountChangeRo ro) throws BusinessException {

        logger.info("在投金额- 转 可用金额+,userUuid---{},金额{}",ro.getUserUuid(),ro.getAmount());
        if(redisUtil.tryLock(userParamContants.LOCK_USERACCOUNT_USERUUID + ro.getUserUuid(), 5000)){
            logger.info("通过锁校验");
            UserAccount userAccount = this.selectUserAccount(ro.getUserUuid());

            logger.info("在投金额- 转 可用金额+ 初始账户实体{}",userAccount);
            BigDecimal lastMoney = userAccount.getCurrentBalance();
            BigDecimal lockMoney = userAccount.getLockedBalance();
            BigDecimal money = userAccount.getInvestingBanlance().subtract(ro.getAmount());
            BigDecimal currentMoney = userAccount.getCurrentBalance().add(ro.getAmount());


            userAccount.setInvestingBanlance(money);
            userAccount.setCurrentBalance(currentMoney);

            logger.info("在投金额- 转 可用金额+ 前 待更新实体{}",userAccount);
            this.updateOne(userAccount);

            ro.setCurrentBanlance(currentMoney);
            ro.setLockedBanlance(lockMoney);
            ro.setLastBanlance(lastMoney);
            ro.setInvestBanlance(money);

            ro.setType(AccountTransTypeEnum.INVEST_TO_CURRENT.getDisburseType());

            this.insertUserAccountHistory(ro);

            redisUtil.releaseLock(userParamContants.LOCK_USERACCOUNT_USERUUID + ro.getUserUuid());

        }
    }


    /**
     * 还款时 减用户 在投金额
     * */
    @Override
    @Transactional
    public void subtractUserAccountInvesting(UserAccountChangeRo ro) throws BusinessException {

        logger.info("减用户 在投金额,userUuid---{},金额{}",ro.getUserUuid(),ro.getAmount());
        if(redisUtil.tryLock(userParamContants.LOCK_USERACCOUNT_USERUUID + ro.getUserUuid(), 5000)){
            logger.info("通过锁校验");

            UserAccount userAccount = this.selectUserAccount(ro.getUserUuid());

            logger.info("减用户 在投金额 初始账户实体{}",userAccount);
            if(userAccount.getInvestingBanlance().compareTo(ro.getAmount()) == -1){
                throw new BusinessException(UserExceptionEnums.AMOUNT_ERROR);
            }

            BigDecimal lastMoney = userAccount.getCurrentBalance();
            BigDecimal lockMoney = userAccount.getLockedBalance();
            BigDecimal investMoney = userAccount.getInvestingBanlance().subtract(ro.getAmount());

            userAccount.setInvestingBanlance(investMoney);

            logger.info("减用户 在投金额 前 待更新实体{}",userAccount);
            this.updateOne(userAccount);

            ro.setCurrentBanlance(lastMoney);
            ro.setLockedBanlance(lockMoney);
            ro.setLastBanlance(lastMoney);
            ro.setInvestBanlance(investMoney);

            ro.setType(AccountTransTypeEnum.INVEST_SUB.getDisburseType());

            this.insertUserAccountHistory(ro);

            redisUtil.releaseLock(userParamContants.LOCK_USERACCOUNT_USERUUID + ro.getUserUuid());

        }

    }

    /**
     * 提现时时 减用户 冻结金额
     * */
    @Override
    @Transactional
    public void subtractUserAccountbLocked(UserAccountChangeRo ro) throws BusinessException {

        logger.info("减用户 冻结金额,userUuid---{},金额{}",ro.getUserUuid(),ro.getAmount());
        if(redisUtil.tryLock(userParamContants.LOCK_USERACCOUNT_USERUUID + ro.getUserUuid(), 5000)){

            logger.info("通过锁校验");
            UserAccount userAccount = this.selectUserAccount(ro.getUserUuid());

            logger.info("减用户 冻结金额 初始账户实体{}",userAccount);
            if(userAccount.getLockedBalance().compareTo(ro.getAmount()) == -1){
                throw new BusinessException(UserExceptionEnums.AMOUNT_ERROR);
            }

            BigDecimal lastMoney = userAccount.getCurrentBalance();
            BigDecimal lockMoney = userAccount.getLockedBalance().subtract(ro.getAmount());
            BigDecimal investMoney = userAccount.getInvestingBanlance();

            userAccount.setLockedBalance(lockMoney);

            logger.info("减用户 冻结金额 前 待更新实体{}",userAccount);
            this.updateOne(userAccount);

            ro.setCurrentBanlance(lastMoney);
            ro.setLockedBanlance(lockMoney);
            ro.setLastBanlance(lastMoney);
            ro.setInvestBanlance(investMoney);

            ro.setType(AccountTransTypeEnum.LOCK_SUB.getDisburseType());

            this.insertUserAccountHistory(ro);

            redisUtil.releaseLock(userParamContants.LOCK_USERACCOUNT_USERUUID + ro.getUserUuid());

        }

    }

    /**
     * 根据用户类型type查询 机构投资人 和 超级投资人 列表
     * */
    @Override
    @Transactional
    public List<OrganOrSuperUserBo> getOrganOrSuperAccount(UserTypeSearchRo ro) throws BusinessException{
        List<OrganOrSuperUserBo> userBos = new ArrayList<>();

//        userBos = this.userDao.getOrganOrSuperAccount(ro.getUserType());

        UserUser userUser = new UserUser();
        userUser.setUserType(ro.getUserType());
        userUser.setDisabled(0);
        List<UserUser> userList = this.userDao.findForList(userUser);

        for (UserUser user:userList){
            OrganOrSuperUserBo userBo = BeanCoypUtil.copyToNewObject(user,OrganOrSuperUserBo.class);//用户信息

            UserBank userBank = new UserBank();
            userBank.setUserUuid(user.getId());
            userBank.setDisabled(0);
            userBank = this.userBankDao.findOne(userBank);
            if (userBank !=null){
                userBo.setBankCarkNo(userBank.getBankNumberNo());//银行信息
                userBo.setBankCode(userBank.getBankCode());//银行code
            }

            UserAccount account = new UserAccount();
            account.setUserUuid(user.getId());
            account = this.userAccountDao.findOne(account);
            if (account != null){
                userBo.setCurrentBalance(account.getCurrentBalance());
                userBo.setLockedBalance(account.getInvestingBanlance());////账户信息
            }


            userBos.add(userBo);
        }

        return userBos;
    }

    /**
     * 账户管理--账户查询
     * */
    @Override
    @Transactional
    public BasePageResponse<UserAccounthistory> getAccountHistoryByNameOrMobile(UserPageRo ro) throws BusinessException{
        //BasePageResponse<UserAccounthistory> response = new BasePageResponse<>();
        String name = ro.getRealName();
        String mobile = ro.getMobileNumber();
        UserUser userUser = new UserUser();
        if(!StringUtils.isEmpty(mobile)){
            userUser.setMobileNumber(mobile);
        }
        if(!StringUtils.isEmpty(name)){
            userUser.setRealName(name);
        }
        List<UserUser> userList = this.userDao.findForList(userUser);

        if (!userList.isEmpty()){
            userUser = userList.get(0);
            UserAccounthistory accounthistory = new UserAccounthistory();
            accounthistory.setUserUuid(userUser.getId());
            accounthistory.setDisabled(0);

            ro.setSortProperty("sort");
            ro.setSortDirection(Sort.Direction.DESC);
            Page<UserAccounthistory> page = this.userAccounthistoryDao.findForPage(accounthistory,ro.convertPageRequest());

            BasePageResponse<UserAccounthistory> response = new BasePageResponse<>(page);
            response.setContent(page.getContent());
            return response;
        }

        return null;
    }

    @Override
    @Transactional
    public BasePageResponse<UserAccounthistory> getAccountHistoryByType(UserAccounthistoryRo ro) throws BusinessException{
        //BasePageResponse<UserAccounthistory> response = new BasePageResponse<>(null);

        UserAccounthistory accounthistory = new UserAccounthistory();
        accounthistory.setUserUuid(ro.getUserUuid());
        accounthistory.setDisabled(0);

        if(!StringUtils.isEmpty(ro.getBusinessType())){
            ExtendQueryCondition extendQueryCondition = new ExtendQueryCondition();
            List<Object> businessTypes = new ArrayList<>();
            String[] types = ro.getBusinessType().split("#");
            for (String s:types){
                businessTypes.add(s);
            }
            extendQueryCondition.addInQueryMap(UserAccounthistory.businessType_field, businessTypes);//in查询
            accounthistory.setExtendQueryCondition(extendQueryCondition);
        }

        ro.setSortProperty("sort");
        ro.setSortDirection(Sort.Direction.DESC);
        Page<UserAccounthistory> page = this.userAccounthistoryDao.findForPage(accounthistory,ro.convertPageRequest());

        BasePageResponse<UserAccounthistory> response = new BasePageResponse<>(page);
        response.setContent(page.getContent());
        return response;
    }

    @Override
    @Transactional
    public void withdrawDeposit(WithdrawDepositRo ro) throws Exception{

        //首先验证交易密码
        UserUser userUser = this.userDao.findOne(ro.getUserId());
        if (!userUser.getPayPwd().equals(SignUtils.generateMd5(ro.getPassWord()))){
            throw new BusinessException(UserExceptionEnums.PAY_PASSWORD_ERROR);
        }

        if (userUser.getUserType().equals("1") ||userUser.getUserType().equals("2")){//托管账户 和收入账户 不可提现
            return;
        }
        UserBank userBank = new UserBank();
        userBank.setUserUuid(ro.getUserId());
        userBank.setDisabled(0);
        userBank = this.userBankDao.findOne(userBank);
        logger.info("待提现用户 银行卡信息{}",userBank);
        if (userBank ==null){
            throw new BusinessException(UserExceptionEnums.BANKINFO_IS_NULL);
        }

        UserWithdraw userWithdraw = new UserWithdraw();
        userWithdraw.setDisabled(0);
        userWithdraw.setUserUuid(ro.getUserId());
        UserWithdraw userWithdraw1 = this.userWithdrawDao.findOne(userWithdraw);
        if (userWithdraw1 == null){ //未创建提现信息表
            userWithdraw.setWithdrawBalance(ro.getAmount());//主动提现金额
            this.userWithdrawDao.addOne(userWithdraw);
            logger.info("创建提现信息表完成");
            //用户账户处理
            UserAccountChangeRo req = new UserAccountChangeRo();
            req.setAmount(ro.getAmount());
            req.setUserUuid(ro.getUserId());
            req.setTradeInfo("主动提现申请");
            req.setPayType(userBank.getBankCode());
            req.setType(AccountTransTypeEnum.CURRENT_TO_LOCK.getDisburseType());
            req.setBusinessType("提现申请");
            this.subtractUserAccountCurrent(req);
        }else {
            userWithdraw1.setWithdrawBalance(userWithdraw1.getWithdrawBalance().add(ro.getAmount()));
            this.userWithdrawDao.updateOne(userWithdraw1);
            logger.info("更新提现信息表完成");
            //用户账户处理
            UserAccountChangeRo req = new UserAccountChangeRo();
            req.setAmount(ro.getAmount());
            req.setUserUuid(ro.getUserId());
            req.setTradeInfo("主动提现申请");
            req.setPayType(userBank.getBankCode());
            req.setType(AccountTransTypeEnum.CURRENT_TO_LOCK.getDisburseType());
            req.setBusinessType("提现申请");
            this.subtractUserAccountCurrent(req);
        }

    }

    public static void main(String[] args) {
//        Object[] bo = new Object[0];
//        Object[] bo1 = null;
//
//        if (bo1 != null ){
//            System.out.println("bo != null");
//        }
//
////        if (bo.equals(null)){
////            System.out.println("bo.equals(null)");
////        }
//
//        if (StringUtils.isEmpty(bo1)){
//            System.out.println("StringUtils.isEmpty(bo1)");
//        }
//
//        if (bo1 == null){
//            System.out.println("bo == null");
//        }
//
        //switch 具有穿透性，注意break的使用
//        int aa = 2;
//        switch (aa){
//            case 1:
//                System.out.println("1");
//                break;
//            case 2:
//                System.out.println("2");
////                break;
//            case 3:
//                System.out.println("3");
//                break;
//            default:
//                System.out.println("defult");
//
//        }
        //冒泡排序算法
        int[] aa = {2,6,5,7,3,4,8,};
        for (int i = 0;i<aa.length;i++){
            for (int j = i + 1;j<aa.length;j++){
                if (aa[i]>aa[j]){
                    //交換aa[i]和aa[j]
                    int tmp = aa[j];
                    aa[j] = aa[i];
                    aa[i] = tmp;
                }
            }
        }
        System.out.println(Arrays.toString(aa));
    }

    @Override
    @Transactional
    public void autoWithdrawDeposit() throws Exception{

        //查出体现信息表所有数据，轮询
        UserWithdraw userWithdraw = new UserWithdraw();
        userWithdraw.setDisabled(0);
        List<UserWithdraw> list = this.userWithdrawDao.findForList(userWithdraw);
        logger.info("开始打款请求，待处理个数{}",list.size());
        for (UserWithdraw ro:list){
            UserUser user = this.userService.findById(ro.getUserUuid(),UserUser.class);
            logger.info("user-----{}",user);
            if ("1".equals(user.getUserType()) ||"2".equals(user.getUserType())){//托管账户 和收入账户 不自动提现
                continue;
            }
            //然后获取银行卡信息  银行code  银行卡号  持卡人姓名
            UserBank userBank = new UserBank();
            userBank.setUserUuid(ro.getUserUuid());
            userBank.setDisabled(0);
            userBank = this.userBankDao.findOne(userBank);
            logger.info("待提现用户 银行卡信息{}",userBank);
            //调用提现接口
            LoanRo loanRo = new LoanRo();
            String bankCode = "b";
            if (userBank != null){
                loanRo.setBankCode(userBank.getBankCode());
                loanRo.setBankNumberNo(userBank.getBankNumberNo());
                loanRo.setCardholder(userBank.getBankCardName());
                loanRo.setDisburseChannel(userBank.getBankCode());
                bankCode = userBank.getBankCode();
            }else {
                logger.info("用户{}，银行信息为空",ro.getUserUuid());
                continue;
            }

            //查昨天拍的那条快照记录与投资成功作比对 >=0+主动提现金额    <成功-快照余额+主动提现金额  。算出总提现金额后，调用提现服务
            //计算应提现金额
            logger.info("开始查询 投资成功金额dao ");
            Date date = DateUtils.addDate(new Date(),-1);//提前一天
            Object[] bo = this.userAccounthistoryDao.getSuccessInvest(date,ro.getUserUuid());
            logger.info("查询 投资成功金额dao 结束,BO----{}",bo);//数组的长度为0，是一个空数组;;但数组bo并不为null
            BigDecimal successInvest = new BigDecimal("0.00");//购买债权金额
            if (bo[0] != null){ // bo != null 报空指针；； !bo.equals(null) 报空指针;; java中对equals的定义：对于任何非空引用值 x变量不能为空（x.equals()），否则会报空指针异常
                successInvest = new BigDecimal(bo[0].toString());//购买债权金额
            }

            BigDecimal balance = ro.getFirstBalance();//余额快照
            BigDecimal allAmount = new BigDecimal("0.00");//全部提现金额
            BigDecimal autoAmount = new BigDecimal("0.00");//自动提现金额
            UserAccountChangeRo req = new UserAccountChangeRo();
            //successInvest.compareTo(balance) >=0 || ro.getWithdrawBalance().compareTo(balance) >=0
            if ((successInvest.add(ro.getWithdrawBalance())).compareTo(balance) >=0){
                //只计算主动提现金额
                allAmount = ro.getWithdrawBalance();
            }else {
                //主动提现金额 + 自动提现（余额-购买债权金额-主动提现金额）
                autoAmount = balance.subtract(successInvest).subtract(ro.getWithdrawBalance());
                allAmount = ro.getWithdrawBalance().add(autoAmount);
                //用户账户处理 首先 自动提现金额 从 可用余额账户 转入到 冻结账户（主动提现金额 在每次提交提现申请时 已经做账户处理）
                req.setAmount(autoAmount);
                req.setUserUuid(ro.getUserUuid());
                req.setTradeInfo("自动提现申请");
                req.setPayType(userBank.getBankCode());

                req.setType(AccountTransTypeEnum.CURRENT_TO_LOCK.getDisburseType());
                req.setBusinessType(UserAccountBusinessTypeEnum.RECHARGE_APPLY.getEnname());//"提现申请"
                this.subtractUserAccountCurrent(req);
            }
            logger.info("本次用户{},申请提现总金额{},主动{}",ro.getUserUuid(),allAmount,ro.getWithdrawBalance());
            loanRo.setAmount(allAmount);
//            loanRo.setDescription("自动提现任务");
            loanRo.setDescription(TransTypeEnum.RECHARGE.getDisburseType());
            loanRo.setTransType(TransTypeEnum.RECHARGE.getDisburseType());

            UserAccountBo escrowAccount = this.getEscrowAccount(bankCode);
            if (escrowAccount != null){
                loanRo.setFromUserId(escrowAccount.getUserUuid());
            }
            loanRo.setToUserId(ro.getUserUuid());
            String transNo = TransPreFixTypeEnum.LOAN.getType() + OrderNoCreator.createOrderNo();//交易流水号 //TransPreFixTypeEnum.LOAN.getType() +
            loanRo.setCreditorNo(transNo);
            loanRo.setOrderNo(transNo);

            if (allAmount.compareTo(new BigDecimal("0")) >0){//提现金额大于零 调提现服务，否则跳过
                //提现请求
                logger.info("提现请求参数req{}",loanRo);
                BaseResponse<JSONObject> response = this.payService.loan(loanRo);
                logger.info("提现请求结果res{}",response);

                if (!response.isSuccess()){
                    logger.info("提现服务请求失败，冻结金额退回可用余额,用户ID{}",ro.getUserUuid());
                    //throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
                    //todo 调用服务失败 是佛需要 释放锁定金额 待定---hyy
                    req.setAmount(allAmount);
                    req.setType(AccountTransTypeEnum.LOCK_TO_CURRENT.getDisburseType());
                    req.setTradeInfo("提现请求失败");
                    req.setBusinessType(UserAccountBusinessTypeEnum.RECHARGE_FAIL.getEnname());
                    this.releaseUserCurrentLockBlance(req);//释放锁定到余额
                    continue;
                }
            }else {
                continue;
            }
        }
        logger.info("打款请求结束");

        //然后 拍快照。 对当前所有用户（普通账户，超级投资人账户，机构账户）的 可用余额拍照。清空 主动提现 和 总提现数据；
        logger.info("开始查所有账户 dao");
        List<UserAccount> userAccounts = this.userAccountDao.getAccount();
        logger.info("查所有账户结束");
        for (UserAccount account:userAccounts){
            if (account.getType().equals("1") || account.getType().equals("2")){
                continue;
            }
            UserWithdraw userWithdrawP = new UserWithdraw();
            userWithdrawP.setDisabled(0);
            userWithdrawP.setUserUuid(account.getUserUuid());
            UserWithdraw userWithdrawP1 = this.userWithdrawDao.findOne(userWithdrawP);
            if (userWithdrawP1 ==null){
                logger.info("新建快照，userID---{}",account.getUserUuid());
                userWithdrawP.setFirstBalance(account.getCurrentBalance());//可用余额拍照
                this.userWithdrawDao.addOne(userWithdrawP);
            }else {
                logger.info("更新快照，userID---{}",account.getUserUuid());
                userWithdrawP1.setFirstBalance(account.getCurrentBalance());//可用余额拍照
                userWithdrawP1.setUpdateTime(new Date());
                this.userWithdrawDao.updateOne(userWithdrawP1);
            }
        }

    }

    @Transactional
    public void autoWithdrawDepositCheck() throws BusinessException{
        //体现结果查询
        PayAccountHistoryRo payAccountHistoryRo = new PayAccountHistoryRo();
        payAccountHistoryRo.setTradeType(TransTypeEnum.RECHARGE.getDisburseType());
        List<PayAccountHistoryBo> boList = this.payService.getPayAccountHistoryByType(payAccountHistoryRo);
        for (PayAccountHistoryBo bo :boList){
            logger.info("开始处理查询提现结果，数量{}",boList.size());
            LoanRo loanRo = new LoanRo();
            loanRo.setCreditorNo(bo.getOrderNo());
            loanRo.setTransType(bo.getTradeType());
            //提现请求
            logger.info("提现查询请求参数req{}",loanRo);
            BaseResponse<LoanResponse> response = this.payService.queryLoanResult(loanRo);
            logger.info("提现查询请求结果res{}",response);
            if (!response.isSuccess()){
                logger.info("提现查询服务请求失败,订单号{}",bo.getOrderNo());
                continue;
            }
            //提现结果处理
            logger.info("LoanResponse---------{}",response);
            LoanResponse loanResponse = response.getData();
            //支付状态:'PENDING'=处理中,COMPLETED=成功,'FAILED'=失败
            if (loanResponse !=null && loanResponse.getDisburseStatus().equals("COMPLETED")) {
                //用户账户处理 提现成功后 冻结--；处理中或失败后 暂时不管
                UserAccountChangeRo req = new UserAccountChangeRo();
                req.setAmount(bo.getAmount());
                req.setUserUuid(bo.getToUserId());
                req.setTradeInfo("自动提现任务处理成功");
                req.setType(AccountTransTypeEnum.LOCK_SUB.getDisburseType());
                req.setPayType(bo.getPaychannel());
                req.setBusinessType(UserAccountBusinessTypeEnum.RECHARGE_SUCCESS.getEnname());//提现成功
                this.subtractUserAccountbLocked(req);
            }
        }
        logger.info("处理查询提现结果 结束");
    }

    /**
     * 根据银行类型 获取托管账户
     *
     * @return
     * @throws BusinessException
     */
    public UserAccountBo getEscrowAccount(String bankType) throws BusinessException {
        UserTypeSearchRo userTypeSearchRo = new UserTypeSearchRo();
        userTypeSearchRo.setUserType(UserTypeEnum.ESCROW_ACCOUNT.getType());
        userTypeSearchRo.setBankType(bankType);
        UserAccountBo baseResponse = this.userService.userListByType(userTypeSearchRo);

        if (baseResponse == null) {
            throw new BusinessException(BaseExceptionEnums.CPPCHA_ERROR.setCustomMessage("获取托管账户异常"));
        }
        logger.info("获取托管账户信息:{}",baseResponse);
        return baseResponse;
    }

    public UserAccountAndBankInfoBo userBankAndAccountInfo(UserBankRo ro) throws BusinessException{
        UserAccountAndBankInfoBo accountAndBankInfoBo = new UserAccountAndBankInfoBo();
        accountAndBankInfoBo.setCurrentBalance(this.selectUserAccount(ro.getUserUuid()).getCurrentBalance());
        UserBank userBank = this.userBankService.getUserBankInfo(ro);
        UserBank userBank1 = new UserBank();
        if (userBank != null){
            accountAndBankInfoBo.setBankCarkNo(userBank.getBankNumberNo());
            userBank1.setBankCode(userBank.getBankCode());
        }

        userBank1.setDisabled(0);
        userBank1.setType(1);
        List<UserBank> userBanks = this.userBankDao.findForList(userBank1);
        if (!userBanks.isEmpty()){
            accountAndBankInfoBo.setCompanyBankCarkNo(userBanks.get(0).getBankNumberNo());
        }

        return accountAndBankInfoBo;
    }


}