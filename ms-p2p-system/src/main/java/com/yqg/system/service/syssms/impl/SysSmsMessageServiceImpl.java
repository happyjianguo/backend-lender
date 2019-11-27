package com.yqg.system.service.syssms.impl;

import com.yqg.api.system.sms.bo.SysSmsMessageInfoBo;
import com.yqg.api.system.sms.ro.UserSmsCodeRo;
import com.yqg.api.user.enums.UserRedisKeyEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.redis.IRedisKeyEnum;
import com.yqg.common.redis.RedisUtil;
import com.yqg.common.sender.CaptchaUtils;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.system.dao.SysSmsMessageDao;
import com.yqg.system.entity.SysSmsMessageInfo;
import com.yqg.system.enums.SystemExceptionEnums;
import com.yqg.system.service.syssms.SysSmsMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lixiangjun on 2019/6/5.
 */
@Service("sysSmsMessageService")
public class SysSmsMessageServiceImpl implements SysSmsMessageService {

    @Autowired
    private SysSmsMessageDao sysSmsMessageDao;
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    protected RedisUtil redisUtil;

    @Override
    public String addOne(SysSmsMessageInfo sysSmsMessageInfo) throws BusinessException {
        return this.sysSmsMessageDao.addOne(sysSmsMessageInfo);
    }

    @Override
    public SysSmsMessageInfo findById(String id) throws BusinessException {
        return this.sysSmsMessageDao.findOneById(id, new SysSmsMessageInfo());
    }

    @Override
    public <E> E findById(String id, Class<E> aClass) throws BusinessException {
        SysSmsMessageInfo sysSmsMessageInfo = findById(id);
        E bo = BeanCoypUtil.copyToNewObject(sysSmsMessageInfo, aClass);
        return bo;
    }

    @Override
    public SysSmsMessageInfo findOne(SysSmsMessageInfo sysSmsMessageInfo) throws BusinessException {
        return this.sysSmsMessageDao.findOne(sysSmsMessageInfo);
    }

    @Override
    public <E> E findOne(SysSmsMessageInfo entity, Class<E> aClass) throws BusinessException {
        SysSmsMessageInfo sysSmsMessageInfo = findOne(entity);
        E bo = BeanCoypUtil.copyToNewObject(sysSmsMessageInfo, aClass);
        return bo;
    }

    @Override
    public List<SysSmsMessageInfo> findList(SysSmsMessageInfo sysSmsMessageInfo) throws BusinessException {
        return this.findList(sysSmsMessageInfo);
    }

    @Override
    public <E> List<E> findList(SysSmsMessageInfo entity, Class<E> aClass) throws BusinessException {
        List<SysSmsMessageInfo> sysSmsMessageInfoList = findList(entity);
        List<E> boList = new ArrayList<>();
        for (SysSmsMessageInfo messageInfo : sysSmsMessageInfoList) {
            E bo = BeanCoypUtil.copyToNewObject(messageInfo, aClass);
            boList.add(bo);
        }
        return boList;
    }

    @Override
    public void updateOne(SysSmsMessageInfo sysSmsMessageInfo) throws BusinessException {
        this.sysSmsMessageDao.updateOne(sysSmsMessageInfo);
    }

    @Override
    public void deleteById(String s) throws BusinessException {
        this.sysSmsMessageDao.delete(s);
    }

    @Override
    public void deleteOne(SysSmsMessageInfo sysSmsMessageInfo) throws BusinessException {
        this.sysSmsMessageDao.deleteOne(sysSmsMessageInfo);
    }

    @Override
    public String getSmsCodeMobile(String phone) {
        phone = phone.replaceAll("\\s", "")
                .replaceAll("-", "");

        if (phone.startsWith("08")) {
            return "62"+phone.substring(1);
        } else if (phone.startsWith("8")) {
            return "62"+phone;
        } else if (phone.startsWith("+628")) {
            return phone.substring(1);
        }
        return phone;
    }

    @Override
    public Boolean getSmsCodeCount(String mobile, CaptchaUtils.CaptchaType smsType, int smsCount) {
        IRedisKeyEnum iRedisKeyEnum = UserRedisKeyEnums.USER_SESSION_SMS_KEY_COUNT;
        String paramValue = redisUtil.get(iRedisKeyEnum.appendToDefaultKey(mobile+smsType));
        logger.info("sms count from redis: {}",paramValue);
        int count=0;
        Boolean f ;
        if(!StringUtils.isEmpty(paramValue) && Integer.valueOf(paramValue) >= smsCount){
            return false;
        }else if(StringUtils.isEmpty(paramValue)){
            count++;
            f = true;
        }else {
            count=Integer.valueOf(paramValue)+1;
            f = true;
        }
        this.redisUtil.set(UserRedisKeyEnums.USER_SESSION_SMS_KEY_COUNT.appendToDefaultKey(mobile+smsType), count);
        return f;
    }

    public SysSmsMessageInfoBo getUserLastSmsCode(UserSmsCodeRo request) throws BusinessException {
        String mobile = request.getMobileNumber();
        if(StringUtils.isEmpty(mobile)){
            throw new BusinessException(SystemExceptionEnums.MOBILE_NOT_EXIST);
        }

        SysSmsMessageInfoBo bo = new SysSmsMessageInfoBo();
        List<SysSmsMessageInfo> sysSmsMessageInfoList = this.sysSmsMessageDao.getUserSmsCode(mobile);
        if(!CollectionUtils.isEmpty(sysSmsMessageInfoList)){
            BeanUtils.copyProperties(sysSmsMessageInfoList.get(0),bo);
        }

        return bo;
    }

}
