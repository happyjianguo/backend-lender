package com.yqg.api.user.userbank;

/**
 * 用户银行卡信息
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:20:30
 */
public class UserBankServiceApi {
    public static final String serviceName = "service-user";
    public static final String path_bindBankCard = "/user/bindBankCard";
    //根据userId查询用户银行卡信息
    public static final String path_getUserBankInfo = "/user/getUserBankInfo";
    //更换用户银行卡
    public static final String path_updateUserBankInfo = "/user/updateUserBankInfo";
}