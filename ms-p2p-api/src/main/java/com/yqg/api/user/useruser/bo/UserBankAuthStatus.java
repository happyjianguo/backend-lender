package com.yqg.api.user.useruser.bo;

import lombok.Data;

/**
 * 用户认证状态
 * Created by alan on 2018/9/29.
 */
@Data
public class UserBankAuthStatus {
    private Integer authStatus = 0; //0未认证,1已认证,2认证中

    private Integer bankStatus = 0; //0未绑卡,1已绑卡

    private Integer identity = 0;   //0理财用户,1学生
}
