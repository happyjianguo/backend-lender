package com.yqg.api.system.sms;

/**
 * Created by Lixiangjun on 2019/5/16.
 */
public class SmsServiceApi {
    public static final String serviceName = "service-system";

    public static final String path_systemSendRegisterCapcha = "/public/api-system/system/sendSmsCode";   //获取短信验证码

    public static final String path_getUserLastSmsCode = "/api-system/system/getUserLastSmsCode";   //查询短信验证码

}
