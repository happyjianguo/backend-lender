package com.yqg.user.service.userbank.impl;

import com.yqg.api.system.sysbankbasicinfo.bo.SysBankBasicInfoBo;
import com.yqg.api.system.sysbankbasicinfo.ro.SysBankBasicInfoRo;
import com.yqg.api.user.userbank.ro.UserBankRo;
import com.yqg.api.user.userbank.ro.UserBindBankCardRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.user.dao.UserBankDao;
import com.yqg.user.entity.UserBank;
import com.yqg.user.entity.UserUser;
import com.yqg.user.enums.UserBankCardBinEnum;
import com.yqg.user.enums.UserExceptionEnums;
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
                this.cheakBankCardBin(userInfo, bindBankCardRo);
            }else {     //银行卡已存在
                if (userBank.getStatus() == UserBankCardBinEnum.SUCCESS.getType() || userBank.getStatus() == UserBankCardBinEnum.PENDING.getType()
                        || userBank.getStatus() == UserBankCardBinEnum.NOT.getType()) {
                    throw new BusinessException(UserExceptionEnums.USER_BANKCARD_EXIST);
                }
                if(userBank.getStatus() == UserBankCardBinEnum.FAILED.getType()){
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

    /**
     * 校验用户绑卡信息*/
    public void cheakBankCardBin(UserUser userInfo, UserBindBankCardRo bindBankCardRo) throws BusinessException {
        String bankHolderName = bindBankCardRo.getBankHolderName().toUpperCase();
        String userName = userInfo.getRealName().toUpperCase();
        if(!userName.equals(bankHolderName)){       //判断绑卡用户与登录用户名是否相同
            throw new BusinessException(UserExceptionEnums.USER_NAME_ERROR);
        }

        SysBankBasicInfoRo search = new SysBankBasicInfoRo();
        search.setBankCode(bindBankCardRo.getBankCode());
        search.setSessionId(bindBankCardRo.getSessionId());
        //通过银行code查询银行信息
        BaseResponse<SysBankBasicInfoBo> bankResponse = sysBankBasicThirdService.bankInfoByCode(search);
        if(bankResponse ==null || !bankResponse.isSuccess() || bankResponse.getCode() != 0){
            log.error("未查询到对应code的银行卡信息");
            throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
        }
        UserBank cardSearch = new UserBank();
        cardSearch.setUserUuid(userInfo.getId());
        Sort sort = new Sort(Sort.Direction.DESC, UserBank.sort_Field);
        List<UserBank> userBankList = this.userBankDao.findForList(cardSearch, sort);   //查询最新的一条绑卡记录的bankOrder
        Integer maxOrder = 0;
        if(!CollectionUtils.isEmpty(userBankList)){
            maxOrder = userBankList.get(0).getBankorder() + 1;
        }

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
        addInfo.setType(0);
        addInfo.setStatus(UserBankCardBinEnum.SUCCESS.getType());
        addInfo.setBankNumberNo(bindBankCardRo.getBankNumberNo());

        this.addOne(addInfo);

    }
}