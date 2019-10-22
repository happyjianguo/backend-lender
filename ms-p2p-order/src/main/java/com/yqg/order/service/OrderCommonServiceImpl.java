package com.yqg.order.service;

import com.yqg.api.pay.exception.PayExceptionEnums;
import com.yqg.api.user.enums.UserTypeEnum;
import com.yqg.api.user.useraccount.bo.UserAccountBo;
import com.yqg.api.user.useruser.bo.UserBankAuthStatus;
import com.yqg.api.user.useruser.ro.UserAuthBankStatusRo;
import com.yqg.api.user.useruser.ro.UserTypeSearchRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.redis.RedisUtil;
import com.yqg.common.utils.JsonUtils;
import com.yqg.order.config.DoitConfig;
import com.yqg.order.service.orderScatterStandardRel.OrderScatterStandardRelService;
import com.yqg.order.service.third.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 公共服务注入类
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:20:30
 */
@Component
@Slf4j
public abstract class OrderCommonServiceImpl {

    /**
     * 日志组件
     */
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 缓存工具
     */
    @Autowired
    protected RedisUtil redisUtil;

    @Autowired
    protected UserService userService;

    @Autowired
    protected SysParamService sysParamService;

    @Autowired
    protected PayService payService;

    @Autowired
    protected UserAccountService userAccountService;

    @Autowired
    protected UserBankService userBankService;

    @Autowired
    protected OrderScatterStandardRelService orderScatterStandardRelService;

    @Autowired
    protected DoitConfig doitConfig;


    @Bean(name = "remoteRestTemplate")
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    /**
     * 是否实名、绑卡
     *
     * @param userUuid 投资用户ID
     * @throws BusinessException
     */
    protected void userAuthBankInfo(String userUuid) throws BusinessException {
        UserAuthBankStatusRo ro = new UserAuthBankStatusRo();
        ro.setUserId(userUuid);
        BaseResponse<UserBankAuthStatus> userBankAuthStatus = userService.userAuthBankInfo(ro);
        log.info("投资人信息" + userBankAuthStatus.getData());
        if (!userBankAuthStatus.isSuccess() || userBankAuthStatus.getCode() != 0) {
            throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
        } else {
            if (userBankAuthStatus.getData().getAuthStatus() == 0) {
                throw new BusinessException(PayExceptionEnums.USER_NOT_REALNAME);
            }
            if (userBankAuthStatus.getData().getAuthStatus() == 2) {
                throw new BusinessException(PayExceptionEnums.USER_REALNAME_ING);
            }
            if (userBankAuthStatus.getData().getBankStatus() != 1) {
                throw new BusinessException(PayExceptionEnums.USER_NOT_BINDCARD);
            }
            if (userBankAuthStatus.getData().getIdentity() != 0) {
                throw new BusinessException(PayExceptionEnums.STUDENT_CANNOT_BUY);
            }
        }
    }

    /**
     * 获取托管账户
     *
     * @return
     * @throws BusinessException
     */
    public UserAccountBo getEscrowAccount(String bankType) throws BusinessException {
        UserTypeSearchRo userTypeSearchRo = new UserTypeSearchRo();
        userTypeSearchRo.setUserType(UserTypeEnum.ESCROW_ACCOUNT.getType());
        userTypeSearchRo.setBankType(bankType);
        BaseResponse<UserAccountBo> baseResponse = this.userService.userListByType(userTypeSearchRo);
        if (!baseResponse.isSuccess() || baseResponse.getCode() != 0) {
            throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
        }
        UserAccountBo userBankBo = baseResponse.getData();
        if (userBankBo == null) {
            throw new BusinessException(BaseExceptionEnums.CPPCHA_ERROR.setCustomMessage("获取托管账户异常"));
        }
        log.info("获取托管账户信息:{}", JsonUtils.serialize(userBankBo));
        return userBankBo;
    }

    /**
     * 获取收入账户
     *
     * @return
     * @throws BusinessException
     */
    public UserAccountBo getIncomeAccount(String bankType) throws BusinessException {
        log.info("获取收入账户-----【{}】",bankType);
        UserTypeSearchRo userTypeSearchRo = new UserTypeSearchRo();
        userTypeSearchRo.setUserType(UserTypeEnum.INCOME_ACCOUNT.getType());
        userTypeSearchRo.setBankType(bankType);
        BaseResponse<UserAccountBo> baseResponse = this.userService.userListByType(userTypeSearchRo);
        if (!baseResponse.isSuccess() || baseResponse.getCode() != 0) {
            log.info("baseResponse========"+baseResponse.toString());
            throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
        }
        UserAccountBo userBankBo = baseResponse.getData();
        if (userBankBo == null) {
            throw new BusinessException(BaseExceptionEnums.CPPCHA_ERROR.setCustomMessage("获取收入账户异常"));
        }
        log.info("获取收入账户信息:{}", JsonUtils.serialize(userBankBo));
        return userBankBo;
    }

}
