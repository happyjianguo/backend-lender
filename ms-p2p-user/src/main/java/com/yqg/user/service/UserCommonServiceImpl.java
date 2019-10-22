package com.yqg.user.service;

import com.yqg.common.httpclient.RestTemplateUtil;
import com.yqg.common.redis.RedisUtil;
import com.yqg.common.sender.CaptchaUtils;
import com.yqg.user.config.ControlConfig;
import com.yqg.user.constants.UserParamContants;
import com.yqg.user.service.student.studentaddressdetail.StudentAddressDetailService;
import com.yqg.user.service.student.studentloanstepfour.StudentLoanStepFourService;
import com.yqg.user.service.student.studentloanstepinfo.StudentLoanStepInfoService;
import com.yqg.user.service.student.studentloanstepone.StudentLoanStepOneService;
import com.yqg.user.service.student.studentloanstepthree.StudentLoanStepThreeService;
import com.yqg.user.service.student.studentloansteptwo.StudentLoanStepTwoService;
import com.yqg.user.service.third.SysBankBasicThirdService;
import com.yqg.user.service.third.SysOperateHistoryThirdService;
import com.yqg.user.service.third.SysParamService;
import com.yqg.user.service.useraccounthistory.UserAccounthistoryService;
import com.yqg.user.service.useraddressdetail.UserAddressDetailService;
import com.yqg.user.service.userbank.UserBankService;
import com.yqg.user.service.useruser.UserService;
import com.yqg.user.service.usrloginhistory.UsrLoginHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 公共服务注入类
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:20:30
 */
@Component
public abstract class UserCommonServiceImpl {

    /**
     * 日志组件
     */
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected UserService userService;
    @Autowired
    protected UserBankService userBankService;
    @Autowired
    protected UserAddressDetailService userAddressDetailService;
    @Autowired
    protected UsrLoginHistoryService usrLoginHistoryService;

    @Autowired
    protected RedisUtil redisUtil;

    @Autowired
    protected RestTemplateUtil restTemplateUtil;

    @Autowired
    protected UserParamContants userParamContants;

    @Autowired
    protected UserAccounthistoryService userAccounthistoryService;

    @Autowired
    protected SysBankBasicThirdService sysBankBasicThirdService;

    @Autowired
    protected CaptchaUtils captchaUtils;

    @Autowired
    protected StudentLoanStepOneService studentLoanStepOneService;

    @Autowired
    protected StudentLoanStepTwoService studentLoanStepTwoService;

    @Autowired
    protected StudentLoanStepThreeService studentLoanStepThreeService;

    @Autowired
    protected StudentLoanStepFourService studentLoanStepFourService;

    @Autowired
    protected StudentLoanStepInfoService studentLoanStepInfoService;

    @Autowired
    protected StudentAddressDetailService studentAddressDetailService;

    @Autowired
    protected SysOperateHistoryThirdService sysOperateHistoryThirdService;

    @Autowired
    protected ControlConfig controlConfig;

    @Autowired
    protected SysParamService sysParamService;
}
