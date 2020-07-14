package com.yqg.user.service.userbank.impl;

import com.yqg.api.system.sysbankbasicinfo.bo.SysBankBasicInfoBo;
import com.yqg.api.system.sysbankbasicinfo.ro.SysBankBasicInfoRo;
import com.yqg.api.user.enums.UserAuthStatusEnum;
import com.yqg.api.user.userbank.ro.UserBankRo;
import com.yqg.api.user.userbank.ro.UserBindBankCardRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.common.utils.SignUtils;
import com.yqg.user.dao.UserBankDao;
import com.yqg.user.dao.UserDao;
import com.yqg.user.entity.UserBank;
import com.yqg.user.entity.UserUser;
import com.yqg.api.user.enums.UserBankCardBinEnum;
import com.yqg.api.user.enums.UserExceptionEnums;
import com.yqg.user.service.UserCommonServiceImpl;
import com.yqg.user.service.userbank.UserBankService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户银行卡信息
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:20:30
 */
@Log4j
@Service("userBankService")
public class UserBankServiceImpl extends UserCommonServiceImpl implements UserBankService {
    @Autowired
    protected UserBankDao userBankDao;
    @Autowired
    protected UserDao userDao;


    @Override
    public UserBank findById(String id) throws BusinessException {
        return this.userBankDao.findOneById(id, new UserBank());
    }

    @Override
    public <E> E findById(String id, Class<E> boClass) throws BusinessException {
        UserBank userbank = findById(id);
        E bo = BeanCoypUtil.copyToNewObject(userbank, boClass);
        return bo;
    }

    @Override
    public UserBank findOne(UserBank entity) throws BusinessException {
        return this.userBankDao.findOne(entity);
    }

    @Override
    public <E> E findOne(UserBank entity, Class<E> boClass) throws BusinessException {
        UserBank userbank = findOne(entity);
        E bo = BeanCoypUtil.copyToNewObject(userbank, boClass);
        return bo;
    }


    @Override
    public List<UserBank> findList(UserBank entity) throws BusinessException {
        return this.userBankDao.findForList(entity);
    }

    @Override
    public <E> List<E> findList(UserBank entity, Class<E> boClass) throws BusinessException {
        List<UserBank> userbankList = findList(entity);
        List<E> boList = new ArrayList<>();
        for (UserBank userbank : userbankList) {
            E bo = BeanCoypUtil.copyToNewObject(userbank, boClass);
            boList.add(bo);
        }
        return boList;
    }

    @Override
    @Transactional
    public String addOne(UserBank entity) throws BusinessException {
        return userBankDao.addOne(entity);
    }

    @Override
    @Transactional
    public void deleteById(String id) throws BusinessException {
        userBankDao.deleteOne(findById(id));
    }

    @Override
    @Transactional
    public void deleteOne(UserBank entity) throws BusinessException {
        this.userBankDao.deleteOne(entity);
    }

    @Override
    @Transactional
    public void updateOne(UserBank entity) throws BusinessException {
        this.userBankDao.updateOne(entity);
    }

    @Override
    @Transactional
    public void bindBankCard(UserBindBankCardRo bindBankCardRo) throws BusinessException {
        if(redisUtil.tryLock(bindBankCardRo,1000)){
            String userId = bindBankCardRo.getUserId();
            logger.info("UserBindBankCardRo userId {}", userId);
            if(StringUtils.isEmpty(userId)){
                throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
            }

            UserUser userInfo = userService.findById(userId);   //查询用户信息
            if(userInfo ==null){
                throw new BusinessException(UserExceptionEnums.USER_NOT_EXIST);
            }

            if(StringUtils.isEmpty(userInfo.getRealName())){    //判断用户是否实名
                throw new BusinessException(UserExceptionEnums.USER_NOT_AUTH);
            }

            UserBank search = new UserBank();
            search.setUserUuid(userId);
            UserBank userBank = this.userBankDao.findOne(search);   //查询用户是否已经绑卡
            if(userBank == null){   //未绑卡
                logger.info("userId: {} BankCode()：{}", userId, bindBankCardRo.getBankCode());
                if(!StringUtils.isEmpty(bindBankCardRo.getBankCode())){
                    this.cheakBankCardBin(userInfo, bindBankCardRo);
                }
            }else {  //银行卡已存在
                if(userBank.getStatus() == UserBankCardBinEnum.PENDING.getType() &&
                        (  userInfo.getAuthStatus() == UserAuthStatusEnum.MANAGE_PASS.getType() ||
                                userInfo.getAuthStatus() == UserAuthStatusEnum.PASS.getType())){
                    userBank.setStatus(UserBankCardBinEnum.SUCCESS.getType());
                    userBank.setBankNumberNo(bindBankCardRo.getBankNumberNo());
                    userBank.setBankCardName(bindBankCardRo.getBankHolderName());
                    userBankDao.updateOne(userBank);
                }else if (userBank.getStatus() == UserBankCardBinEnum.SUCCESS.getType() || userBank.getStatus() == UserBankCardBinEnum.NOT.getType()) {
                    throw new BusinessException(UserExceptionEnums.USER_BANKCARD_EXIST);
                }else if(userBank.getStatus() == UserBankCardBinEnum.FAILED.getType()){
                    throw new BusinessException(UserExceptionEnums.USER_BINDCARD_ERROR);
                }
            }
        }else {
            throw new BusinessException(UserExceptionEnums.REPEAT_SUBMIT);
        }

    }

    @Override
    public UserBank getUserBankInfo(UserBankRo userBankRo) throws BusinessException {
        UserBank userBank=new UserBank();
        userBank.setUserUuid(userBankRo.getUserUuid());
        return this.findOne(userBank);
    }

    @Override
    @Transactional
    public void updateBankCard(UserBindBankCardRo ro) throws BusinessException {
        updateBankCardControl(ro, ro.getUserId());
    }

    @Override
    @Transactional
    public void updateBankCardControl(UserBindBankCardRo ro, String userId) throws BusinessException {
        
        if(StringUtils.isEmpty(userId)){
            throw new BusinessException(UserExceptionEnums.UPDATE_BANKCARD_FAILED);
        }
        else{
            UserBank userBank=new UserBank();
            userBank.setUserUuid(userId);
            //userBank.setBankCode(ro.getBankCode());
            UserBank bank = this.findOne(userBank);
            if(bank!=null){
                bank.setBankNumberNo(ro.getBankNumberNo());
                this.updateOne(bank);
            }else {
                // throw new BusinessException(UserExceptionEnums.UPDATE_BANKCARD_FAILED);
                bindBankCard(ro);
            }
        }
    }

    /**
     * 校验用户绑卡信息*/
    @Transactional
    public void cheakBankCardBin(UserUser userInfo, UserBindBankCardRo bindBankCardRo) throws BusinessException {
        String bankHolderName = bindBankCardRo.getBankHolderName().toUpperCase();
        String userName = userInfo.getRealName().toUpperCase();
        if(!userName.equals(bankHolderName)){       //Determine whether the card-binding user and the login user name are the same
            throw new BusinessException(UserExceptionEnums.USER_NAME_ERROR);
        }

        logger.info("SysBankBasicInfoRo");
        SysBankBasicInfoRo search = new SysBankBasicInfoRo();
        search.setBankCode(bindBankCardRo.getBankCode());
        search.setSessionId(bindBankCardRo.getSessionId());

        logger.info("bank code: {} sessionid: {}", bindBankCardRo.getBankCode(), bindBankCardRo.getSessionId());
        //Query bank information by bank code
        BaseResponse<SysBankBasicInfoBo> bankResponse = sysBankBasicThirdService.bankInfoByCode(search);
        if(bankResponse ==null || !bankResponse.isSuccess() || bankResponse.getCode() != 0){
            log.error("Failed to query bank information");
            throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
        }

        logger.info("cardSearch");
        UserBank cardSearch = new UserBank();
        cardSearch.setUserUuid(userInfo.getId());
        Sort sort = new Sort(Sort.Direction.DESC, UserBank.sort_Field);
        List<UserBank> userBankList = this.userBankDao.findForList(cardSearch, sort);   //查询最新的一条绑卡记录的bankOrder
        Integer maxOrder = 0;
        if(!CollectionUtils.isEmpty(userBankList)){
            maxOrder = userBankList.get(0).getBankorder() + 1;
        }

        logger.info("addUserBankCard");
        this.addUserBankCard(userInfo, bindBankCardRo,bankResponse.getData(), maxOrder);

    }

    /**
     * 添加用户绑卡信息*/
    @Transactional
    public void addUserBankCard(UserUser userInfo, UserBindBankCardRo bindBankCardRo,SysBankBasicInfoBo bankResponse, Integer maxOrder) throws BusinessException {
        UserBank addInfo = new UserBank();
        addInfo.setUserUuid(userInfo.getId());
        addInfo.setBankCode(bankResponse.getBankCode());
        addInfo.setBankName(bankResponse.getBankName());
        addInfo.setBankId(bankResponse.getId());
        addInfo.setBankCardName(userInfo.getRealName());
        addInfo.setIsRecent(1);
        addInfo.setBankorder(maxOrder);
        addInfo.setType(userInfo.getUserType());
        Integer authStatus = userInfo.getAuthStatus();
        if(UserAuthStatusEnum.MANAGE_PASS.getType()==authStatus || UserAuthStatusEnum.PASS.getType()==authStatus){
            addInfo.setStatus(UserBankCardBinEnum.SUCCESS.getType());
        }else{
            addInfo.setStatus(UserBankCardBinEnum.PENDING.getType());
        }
        addInfo.setBankNumberNo(bindBankCardRo.getBankNumberNo());

        this.addOne(addInfo);

    }
}