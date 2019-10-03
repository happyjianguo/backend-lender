package com.yqg.api.user.useruser;

/**
 * 用户基本信息表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:20:30
 */
public class UserServiceApi {
    public static final String serviceName = "service-user";
    //用户是否存在
    public static final String path_userIsExist = "/user/userIsExist";

    public static final String path_checkUserExist = "/user/checkUserExist";

    public static final String path_userLogin = "/user/login";

    public static final String path_getImageCapcha = "/user/getImageCapcha";    //获取图片验证码

    public static final String path_sendRegisterCapcha = "/user/sendSmsCode";   //获取短信验证码

    public static final String path_findUserById = "/user/findUserById";   //根据userId查用户信息

    public static final String path_userBasicInfo = "/user/userBasicInfo";     //查询用户银行卡及个人信息

    public static final String path_userAuthBankStatus = "/user/userAuthBankStatus";      //通过用户id查询用户实名绑卡状态

    public static final String path_userAdvanceVerify = "/user/userAdvanceVerify";      //advance认证

    public static final String path_userListByType = "/user/userListByType";        //通过type查询超级投资人,资金托管账户,收入账户

    public static final String path_userAuthFailedListByPage = "/user/userAuthFailedListByPage";        //分页查询审核失败用户列表

    public static final String path_checkUserAuthStatus = "/user/checkUserAuthStatus";        //分页查询审核失败用户列表

    public static final String path_userAuthCheckInfo = "/user/userAuthCheckInfo";        //分页查询审核失败用户列表

    public static final String path_userAuthBankInfoBySession = "/user/userAuthBankInfoBySession";        //session查询用户实名绑卡状态

    public static final String path_superUserAccountAdd = "/user/superUserAccountAdd";        //为超级用户增加资金

    public static final String path_findUserByMobileOrId = "/user/findUserByMobileOrId";

    public static final String path_findUserByRealNameOrId = "/user/findUserByRealNameOrId";        //根据用户姓名查询
}