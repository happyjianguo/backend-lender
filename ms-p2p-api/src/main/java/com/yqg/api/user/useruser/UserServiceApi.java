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
    
    public static final String path_healthcheck = "/api-user/healthcheck";
    //用户是否存在
    public static final String path_userIsExist = "/api-user/user/userIsExist";

    public static final String path_checkUserExist = "/api-user/user/checkUserExist";

    public static final String path_userLogin = "/public/api-user/user/login";

    public static final String path_userDeactivate = "/api-user/user/deactivate";

    public static final String path_userDeactivateSession = "/public/api-user/user/deactivateSession";

    public static final String path_userActivate = "/api-user/user/activate";

    public static final String path_getImageCapcha = "/public/api-user/user/getImageCapcha";    //获取图片验证码

    public static final String path_sendRegisterCapcha = "/api-user/user/sendSmsCode";   //获取短信验证码

    public static final String path_findUserById = "/api-user/user/findUserById";   //根据userId查用户信息

    public static final String path_userBasicInfo = "/public/api-user/user/userBasicInfo";     //查询用户银行卡及个人信息

    public static final String path_userBasicInfoView = "/api-user/user/userBasicInfoView";

    public static final String path_userUsers = "/api-user/user/allUsers";  

    public static final String path_userAuthBankStatus = "/api-user/user/userAuthBankStatus";      //通过用户id查询用户实名绑卡状态

    public static final String path_userAdvanceVerify = "/public/api-user/user/userAdvanceVerify";      //advance认证

    public static final String path_userAdvanceVerifyEdit = "/api-user/user/userAdvanceVerifyEdit";

    public static final String path_userAdvanceVerifyEditControl = "/api-user/user/userAdvanceVerifyEditControl";

    public static final String path_userListByType = "/api-user/user/userListByType";        //通过type查询超级投资人,资金托管账户,收入账户

    public static final String path_userAuthFailedListByPage = "/api-user/user/userAuthFailedListByPage";        //分页查询审核失败用户列表

    public static final String path_checkUserAuthStatus = "/api-user/user/checkUserAuthStatus";        //分页查询审核失败用户列表

    public static final String path_userAuthCheckInfo = "/api-user/user/userAuthCheckInfo";        //分页查询审核失败用户列表

    public static final String path_userAuthBankInfoBySession = "/public/api-user/user/userAuthBankInfoBySession";        //session查询用户实名绑卡状态

    public static final String path_superUserAccountAdd = "/api-user/user/superUserAccountAdd";        //为超级用户增加资金

    public static final String path_findUserByMobileOrId = "/api-user/user/findUserByMobileOrId";        //为超级用户增加资金

    public static final String path_findOneByMobileOrName = "/api-user/user/findOneByMobileOrName";//根据手机号和姓名 查询用户

    public static final String path_resetPayPassword = "/public/api-user/users/resetPayPassword";        //重置交易密码

    public static final String path_uploadHeadImage = "/public/api-user/users/uploadHeadImage";        //上传头像图片

    public static final String path_addSuperInvestor = "/api-user/users/addSuperInvestor";        //添加超级投资人

    public static final String path_addCompanyInvestor = "/api-user/users/addCompanyInvestor";        //添加机构投资人

    public static final String path_addUserMessage = "/api-user/users/addUserMessage";        //添加通知消息

    public static final String path_messageList = "/public/api-user/users/getMessageList";        //通知消息列表

    public static final String path_updateUserMessage = "/public/api-user/users/updateUserMessage";        //通知消息标记已读

}