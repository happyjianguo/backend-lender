package com.yqg.api.user.useruser.bo;

import lombok.Data;

/**
 * 用户登录成功业务对象
 * Created by gao on 2018/6/20.
 */
@Data
public class UserLoginBo {
    /**
     * 登录成功会话id
     */
    String sessionId;
    /**
     * 用户手机号
     */
    String mobileNumber;
    /**
     * 用户是否实名认证*/
    Integer authStatus;

    /**
     * 用户id*/
    String userId;

    /*
    * 用户姓名*/
    String username;

    /**
     * 用户头像url
     */
    String headImage;
}
