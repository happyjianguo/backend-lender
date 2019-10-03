package com.yqg.user.service.useraccount.impl;

import com.yqg.api.user.useraccount.ro.UseraccountRo;
import com.yqg.api.user.useraccounthistory.ro.UserAccounthistoryRo;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.user.dao.UserAccountDao;
import com.yqg.user.entity.UserAccount;
import com.yqg.user.enums.UserExceptionEnums;
import com.yqg.user.service.UserCommonServiceImpl;
import com.yqg.user.service.useraccount.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户账户表
 *
 * @author wu
 * @email wsc@yishufu.com
 * @date 2018-08-31 14:16:46
 */
@Service("UserAccountService")
public class UserAccountServiceImpl extends UserCommonServiceImpl implements UserAccountService {
    @Autowired
    protected UserAccountDao userAccountDao;


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
            userAccountInsert.setSanbiaoBalance(new BigDecimal(0));
            userAccountInsert.setSanbiaoLockBalance(new BigDecimal(0));
            userAccountInsert.setDepositLockBalance(new BigDecimal(0));
            userAccountInsert.setDepositBalance(new BigDecimal(0));
            userAccountInsert.setCurrentLockBalance(new BigDecimal(0));
            userAccountInsert.setCurrentBalance(new BigDecimal(0));
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

    /**
     * 加用户散标金额
     * */
    @Override
    @Transactional
    public void addUserAccountSanbiao(String userUuid, BigDecimal amount) throws BusinessException {

        if(redisUtil.tryLock(userParamContants.LOCK_USERACCOUNT_USERUUID + userUuid, 10)){
            UserAccount userAccount = this.selectUserAccount(userUuid);

            BigDecimal money = userAccount.getSanbiaoBalance().add(amount);

            userAccount.setSanbiaoBalance(money);

            this.updateOne(userAccount);
            UserAccounthistoryRo userAccounthistoryRo = new UserAccounthistoryRo();
            userAccounthistoryRo.setProductType(1);
            userAccounthistoryRo.setType(2);
            userAccounthistoryRo.setUserUuid(userUuid);
            userAccounthistoryRo.setTradeBalance(amount);
            userAccounthistoryService.insterUserAccountHistory(userAccounthistoryRo);

            redisUtil.releaseLock(userParamContants.LOCK_USERACCOUNT_USERUUID + userUuid);

        }



    }

    /**
     * 减用户散标金额
     * */
    @Override
    @Transactional
    public void subtractUserAccountSanbiao(String userUuid, BigDecimal amount) throws BusinessException {

        if(redisUtil.tryLock(userParamContants.LOCK_USERACCOUNT_USERUUID + userUuid, 10)){

            UserAccount userAccount = this.selectUserAccount(userUuid);
            if(userAccount.getSanbiaoBalance().compareTo(amount) == -1){
                throw new BusinessException(UserExceptionEnums.AMOUNT_ERROR);
            }

            BigDecimal money = userAccount.getSanbiaoBalance().subtract(amount);

            userAccount.setSanbiaoBalance(money);

            this.updateOne(userAccount);

            UserAccounthistoryRo userAccounthistoryRo = new UserAccounthistoryRo();
            userAccounthistoryRo.setProductType(1);
            userAccounthistoryRo.setType(4);
            userAccounthistoryRo.setUserUuid(userUuid);
            userAccounthistoryRo.setTradeBalance(amount);
            userAccounthistoryService.insterUserAccountHistory(userAccounthistoryRo);

            redisUtil.releaseLock(userParamContants.LOCK_USERACCOUNT_USERUUID + userUuid);

        }



    }

    /**
     * 用户充值活期金额
     * */
    @Override
    @Transactional
    public void addUserAccountCurrent(String userUuid, BigDecimal amount) throws BusinessException {

        if(redisUtil.tryLock(userParamContants.LOCK_USERACCOUNT_USERUUID + userUuid, 10)){
            UserAccount userAccount = this.selectUserAccount(userUuid);

            BigDecimal money = userAccount.getCurrentBalance().add(amount);

            userAccount.setCurrentBalance(money);

            this.updateOne(userAccount);
            UserAccounthistoryRo userAccounthistoryRo = new UserAccounthistoryRo();
            userAccounthistoryRo.setProductType(2);
            userAccounthistoryRo.setType(1);
            userAccounthistoryRo.setUserUuid(userUuid);
            userAccounthistoryRo.setTradeBalance(amount);
            userAccounthistoryService.insterUserAccountHistory(userAccounthistoryRo);

            redisUtil.releaseLock(userParamContants.LOCK_USERACCOUNT_USERUUID + userUuid);

        }
    }
    /**
     * 用户活期购买债券减余额金额，增加活期冻结金额
     * */
    @Override
    @Transactional
    public void subtractUserAccountCurrent(String userUuid, BigDecimal amount) throws BusinessException {

        if(redisUtil.tryLock(userParamContants.LOCK_USERACCOUNT_USERUUID + userUuid, 10)){

            UserAccount userAccount = this.selectUserAccount(userUuid);

            if(userAccount.getCurrentBalance().compareTo(amount) == -1){
                throw new BusinessException(UserExceptionEnums.AMOUNT_ERROR);
            }

            BigDecimal balanceMoney = userAccount.getCurrentBalance().subtract(amount);
            BigDecimal lockMoney = userAccount.getCurrentLockBalance().add(amount);

            userAccount.setCurrentBalance(balanceMoney);
            userAccount.setCurrentLockBalance(lockMoney);

            this.updateOne(userAccount);

            UserAccounthistoryRo userAccounthistoryRo = new UserAccounthistoryRo();
            userAccounthistoryRo.setProductType(2);
            userAccounthistoryRo.setType(2);
            userAccounthistoryRo.setUserUuid(userUuid);
            userAccounthistoryRo.setTradeBalance(amount);
            userAccounthistoryService.insterUserAccountHistory(userAccounthistoryRo);


            redisUtil.releaseLock(userParamContants.LOCK_USERACCOUNT_USERUUID + userUuid);

        }
    }

    /**
     *用户活期释放债券，增加活期余额，减少活期冻结金额
     */
    @Override
    @Transactional
    public void releaseUserCurrentLockBlance(String userUuid, BigDecimal amount) throws BusinessException {

        if(redisUtil.tryLock(userParamContants.LOCK_USERACCOUNT_USERUUID + userUuid, 10)){

            UserAccount userAccount = this.selectUserAccount(userUuid);

            if(userAccount.getCurrentLockBalance().compareTo(amount) == -1){
                throw new BusinessException(UserExceptionEnums.AMOUNT_ERROR);
            }

            BigDecimal balanceMoney = userAccount.getCurrentBalance().add(amount);
            BigDecimal lockMoney = userAccount.getCurrentLockBalance().subtract(amount);

            userAccount.setCurrentBalance(balanceMoney);
            userAccount.setCurrentLockBalance(lockMoney);

            this.updateOne(userAccount);

            UserAccounthistoryRo userAccounthistoryRo = new UserAccounthistoryRo();
            userAccounthistoryRo.setProductType(2);
            userAccounthistoryRo.setType(3);
            userAccounthistoryRo.setUserUuid(userUuid);
            userAccounthistoryRo.setTradeBalance(amount);
            userAccounthistoryService.insterUserAccountHistory(userAccounthistoryRo);

            redisUtil.releaseLock(userParamContants.LOCK_USERACCOUNT_USERUUID + userUuid);

        }

    }

    /**
     * 用户活期回款，减少活期余额金额(相当于提现)
     * */
    @Override
    @Transactional
    public void returnMoneyUserCurrentBlance(String userUuid, BigDecimal amount) throws BusinessException {

        if(redisUtil.tryLock(userParamContants.LOCK_USERACCOUNT_USERUUID + userUuid, 10)){

            UserAccount userAccount = this.selectUserAccount(userUuid);

            if(userAccount.getCurrentBalance().compareTo(amount) == -1){
                throw new BusinessException(UserExceptionEnums.AMOUNT_ERROR);
            }

            BigDecimal balanceMoney = userAccount.getCurrentBalance().subtract(amount);

            userAccount.setCurrentBalance(balanceMoney);

            this.updateOne(userAccount);

            UserAccounthistoryRo userAccounthistoryRo = new UserAccounthistoryRo();
            userAccounthistoryRo.setProductType(2);
            userAccounthistoryRo.setType(4);
            userAccounthistoryRo.setUserUuid(userUuid);
            userAccounthistoryRo.setTradeBalance(amount);
            userAccounthistoryService.insterUserAccountHistory(userAccounthistoryRo);

            redisUtil.releaseLock(userParamContants.LOCK_USERACCOUNT_USERUUID + userUuid);

        }

    }


    /**
     * 活期转定期
     * */
    @Override
    @Transactional
    public void huo2dingqi(String userUuid, BigDecimal amount) throws BusinessException {

        if(redisUtil.tryLock(userParamContants.LOCK_USERACCOUNT_USERUUID + userUuid, 10)){

            UserAccount userAccount = this.selectUserAccount(userUuid);

            if(userAccount.getCurrentBalance().compareTo(amount) == -1){
                throw new BusinessException(UserExceptionEnums.AMOUNT_ERROR);
            }

            BigDecimal huoMoney = userAccount.getCurrentBalance().subtract(amount);
            BigDecimal dingqiMoney = userAccount.getDepositLockBalance().add(amount);

            userAccount.setCurrentBalance(huoMoney);
            userAccount.setDepositLockBalance(dingqiMoney);

            this.updateOne(userAccount);

            UserAccounthistoryRo userAccounthistoryRo = new UserAccounthistoryRo();
            userAccounthistoryRo.setProductType(3);
            userAccounthistoryRo.setType(2);
            userAccounthistoryRo.setUserUuid(userUuid);
            userAccounthistoryRo.setTradeBalance(amount);
            userAccounthistoryService.insterUserAccountHistory(userAccounthistoryRo);

            redisUtil.releaseLock(userParamContants.LOCK_USERACCOUNT_USERUUID + userUuid);

        }

    }

    /**
     * 活期转散标
     * */
    @Override
    @Transactional
    public void huo2sanbiao(String userUuid, BigDecimal amount) throws BusinessException {
        if(redisUtil.tryLock(userParamContants.LOCK_USERACCOUNT_USERUUID + userUuid, 10)){

            UserAccount userAccount = this.selectUserAccount(userUuid);

            if(userAccount.getCurrentBalance().compareTo(amount) == -1){
                throw new BusinessException(UserExceptionEnums.AMOUNT_ERROR);
            }

            BigDecimal huoMoney = userAccount.getCurrentBalance().subtract(amount);
            BigDecimal sanbiaoMoney = userAccount.getSanbiaoBalance().add(amount);

            userAccount.setCurrentBalance(huoMoney);
            userAccount.setSanbiaoBalance(sanbiaoMoney);

            this.updateOne(userAccount);

            UserAccounthistoryRo userAccounthistoryRo = new UserAccounthistoryRo();
            userAccounthistoryRo.setProductType(1);
            userAccounthistoryRo.setType(2);
            userAccounthistoryRo.setUserUuid(userUuid);
            userAccounthistoryRo.setTradeBalance(amount);
            userAccounthistoryService.insterUserAccountHistory(userAccounthistoryRo);

            redisUtil.releaseLock(userParamContants.LOCK_USERACCOUNT_USERUUID + userUuid);

        }
    }

    /**
     * 用户充值定期金额
     * */
    @Override
    @Transactional
    public void addUserAccountDeposit(String userUuid, BigDecimal amount) throws BusinessException {

        if(redisUtil.tryLock(userParamContants.LOCK_USERACCOUNT_USERUUID + userUuid, 10)){

            UserAccount userAccount = this.selectUserAccount(userUuid);

            BigDecimal money = userAccount.getDepositBalance().add(amount);

            userAccount.setDepositBalance(money);

            this.updateOne(userAccount);
            UserAccounthistoryRo userAccounthistoryRo = new UserAccounthistoryRo();
            userAccounthistoryRo.setProductType(3);
            userAccounthistoryRo.setType(1);
            userAccounthistoryRo.setUserUuid(userUuid);
            userAccounthistoryRo.setTradeBalance(amount);
            userAccounthistoryService.insterUserAccountHistory(userAccounthistoryRo);

            redisUtil.releaseLock(userParamContants.LOCK_USERACCOUNT_USERUUID + userUuid);

        }

    }

    /**
     * 用户定期购买债券减余额金额，增加定期冻结金额
     * */
    @Override
    @Transactional
    public void subtractUserAccountDeposit(String userUuid, BigDecimal amount) throws BusinessException {

        if(redisUtil.tryLock(userParamContants.LOCK_USERACCOUNT_USERUUID + userUuid, 10)){


            UserAccount userAccount = this.selectUserAccount(userUuid);

            if(userAccount.getDepositBalance().compareTo(amount) == -1){
                throw new BusinessException(UserExceptionEnums.AMOUNT_ERROR);
            }

            BigDecimal balanceMoney = userAccount.getDepositBalance().subtract(amount);
            BigDecimal lockMoney = userAccount.getDepositLockBalance().add(amount);

            userAccount.setDepositBalance(balanceMoney);
            userAccount.setDepositLockBalance(lockMoney);

            this.updateOne(userAccount);

            UserAccounthistoryRo userAccounthistoryRo = new UserAccounthistoryRo();
            userAccounthistoryRo.setProductType(3);
            userAccounthistoryRo.setType(2);
            userAccounthistoryRo.setUserUuid(userUuid);
            userAccounthistoryRo.setTradeBalance(amount);
            userAccounthistoryService.insterUserAccountHistory(userAccounthistoryRo);

            redisUtil.releaseLock(userParamContants.LOCK_USERACCOUNT_USERUUID + userUuid);

        }

    }

    /**
     *用户定期释放债券，增加定期余额，减少定期冻结金额
     */
    @Override
    @Transactional
    public void releaseUserDepositLockBlance(String userUuid, BigDecimal amount) throws BusinessException {

        if(redisUtil.tryLock(userParamContants.LOCK_USERACCOUNT_USERUUID + userUuid, 10)){


            UserAccount userAccount = this.selectUserAccount(userUuid);

            if(userAccount.getDepositLockBalance().compareTo(amount) == -1){
                throw new BusinessException(UserExceptionEnums.AMOUNT_ERROR);
            }

            BigDecimal balanceMoney = userAccount.getDepositBalance().add(amount);
            BigDecimal lockMoney = userAccount.getDepositLockBalance().subtract(amount);

            userAccount.setDepositBalance(balanceMoney);
            userAccount.setDepositLockBalance(lockMoney);

            this.updateOne(userAccount);

            UserAccounthistoryRo userAccounthistoryRo = new UserAccounthistoryRo();
            userAccounthistoryRo.setProductType(3);
            userAccounthistoryRo.setType(3);
            userAccounthistoryRo.setUserUuid(userUuid);
            userAccounthistoryRo.setTradeBalance(amount);
            userAccounthistoryService.insterUserAccountHistory(userAccounthistoryRo);
            redisUtil.releaseLock(userParamContants.LOCK_USERACCOUNT_USERUUID + userUuid);

        }

    }


    /**
     * 用户定期回款，减少定期余额金额(相当于提现)
     * */
    @Override
    @Transactional
    public void returnMoneyUserDepositLockBlance(String userUuid, BigDecimal amount) throws BusinessException {

        if(redisUtil.tryLock(userParamContants.LOCK_USERACCOUNT_USERUUID + userUuid, 10)){

            UserAccount userAccount = this.selectUserAccount(userUuid);

            if(userAccount.getDepositLockBalance().compareTo(amount) == -1){
                throw new BusinessException(UserExceptionEnums.AMOUNT_ERROR);
            }

            BigDecimal lockBalanceMoney = userAccount.getDepositLockBalance().subtract(amount);

            userAccount.setDepositLockBalance(lockBalanceMoney);

            this.updateOne(userAccount);

            UserAccounthistoryRo userAccounthistoryRo = new UserAccounthistoryRo();
            userAccounthistoryRo.setProductType(3);
            userAccounthistoryRo.setType(4);
            userAccounthistoryRo.setUserUuid(userUuid);
            userAccounthistoryRo.setTradeBalance(amount);
            userAccounthistoryService.insterUserAccountHistory(userAccounthistoryRo);
            redisUtil.releaseLock(userParamContants.LOCK_USERACCOUNT_USERUUID + userUuid);
        }

    }


}