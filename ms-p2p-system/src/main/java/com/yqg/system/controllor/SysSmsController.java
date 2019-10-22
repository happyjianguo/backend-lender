package com.yqg.system.controllor;

import com.yqg.api.system.sms.SmsServiceApi;
import com.yqg.api.system.sms.bo.SysSmsMessageInfoBo;
import com.yqg.api.system.sms.ro.UserSmsCodeRo;
import com.yqg.api.user.useruser.ro.SmsCodeRo;
import com.yqg.common.config.CommonConfig;
import com.yqg.common.context.ApplicationContextProvider;
import com.yqg.common.utils.SmsServiceUtil;
import com.yqg.common.core.BaseControllor;
import com.yqg.common.core.annocation.NotNeedLogin;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.sender.CaptchaUtils;
import com.yqg.system.service.syssms.CommonSmsSender;
import com.yqg.system.service.syssms.SmsContentTemplate;
import com.yqg.system.service.syssms.SysSmsMessageService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



/**
 * Created by Lixiangjun on 2019/5/14.
 */
@RestController
public class SysSmsController extends BaseControllor {

    @Autowired
    private SmsServiceUtil smsServiceUtil;
    @Autowired
    protected CaptchaUtils captchaUtils;
    @Autowired
    CommonSmsSender commonSmsSender;
    @Autowired
    private SysSmsMessageService sysSmsMessageService;
    @Autowired
    CommonConfig commonConfig;

    @Value("${count.smsCount}")
    private String smsCount;

    @NotNeedLogin
    @ApiOperation(value = "短信验证码", notes = "获取短信验证码")
    @PostMapping(value = SmsServiceApi.path_systemSendRegisterCapcha)
    public BaseResponse getSmsCode(@RequestBody SmsCodeRo ro) throws Exception {

        if (!ApplicationContextProvider.isProdProfile() && !commonConfig.isSendSmsCaptcha()) {
            //非生产环境 且 配置了不发送验证码,则跳过校验
            logger.info("非生产环境 且 配置了不发送验证码, 跳过校验");
            return successResponse();
        }

        String mobile = sysSmsMessageService.getSmsCodeMobile(ro.getMobileNumber());
        switch(ro.getSmsType()){
            case LOGIN:
                String imgSessionId = ro.getImgSessionId();
                String imgCaptch = ro.getImgCaptch();
                captchaUtils.checkImgCaptcha(imgSessionId, imgCaptch);  //校验图片验证码
                int smsCount = Integer.valueOf(this.smsCount);//配置表
                Boolean smsCodeCount = sysSmsMessageService.getSmsCodeCount(mobile, ro.getSmsType(), smsCount);
                //同一天内前两次用TWILIO 两次之后用ZENZIVA
                if(smsCodeCount){
                    smsServiceUtil.sendSmsLoginCode(mobile);
                }else {
                    captchaUtils.sendSmsCaptcha(commonSmsSender,ro.getSmsType(), SmsContentTemplate.LOGIN_TEMP, mobile);
                }
                break;
            case PASSWORD_REST:
                captchaUtils.sendSmsCaptcha(commonSmsSender,ro.getSmsType(), SmsContentTemplate.PAYPSSWORD_RESET_TEMP, mobile);
                break;
        }
        return successResponse();
    }

    @ApiOperation(value = "查询短信验证码", notes = "查询短信验证码")
    @PostMapping(value = SmsServiceApi.path_getUserLastSmsCode)
    public BaseResponse<SysSmsMessageInfoBo> querySmsCode(@RequestBody UserSmsCodeRo ro) throws Exception {
        String mobile = sysSmsMessageService.getSmsCodeMobile(ro.getMobileNumber());
        ro.setMobileNumber(mobile);
        SysSmsMessageInfoBo response = sysSmsMessageService.getUserLastSmsCode(ro);
        return new BaseResponse<SysSmsMessageInfoBo>().successResponse(response);
    }


}
