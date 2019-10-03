package com.yqg.api.system.sysuser.bo;

import lombok.Data;

@Data
public class SysUserLoginBo {
    /**
     * 登录成功会话id
     */
    String sessionId;
    /**
     * 用户手机号
     */
    String username;

    /**
     * 用户id*/
    String userId;
}
