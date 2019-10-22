package com.yqg.system.service.syssms;

import com.yqg.api.system.sms.bo.SysSmsMessageInfoBo;
import com.yqg.api.system.sms.ro.UserSmsCodeRo;
import com.yqg.common.core.BaseService;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.sender.CaptchaUtils;
import com.yqg.system.entity.SysSmsMessageInfo;

/**
 * Created by Lixiangjun on 2019/6/5.
 */

public interface SysSmsMessageService extends BaseService<SysSmsMessageInfo> {

    /**
     * 手机号处理
     * @param phone
     * @return
     */
    public String getSmsCodeMobile(String phone);

    /**
     * 判断用户发几次验证码
     * @param mobile
     * @param smsType
     * @param smsCount
     * @return
     */
    public Boolean getSmsCodeCount(String mobile, CaptchaUtils.CaptchaType smsType, int smsCount);

     SysSmsMessageInfoBo getUserLastSmsCode(UserSmsCodeRo request) throws BusinessException;
}
