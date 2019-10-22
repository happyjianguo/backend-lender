package com.yqg.api.system.sysuser.bo;

import lombok.Data;

import java.util.Date;
import java.util.List;
/**
 * 系统用户表 业务对象
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-09-20 15:52:10
 */
@Data
public class SysUserBo {
    private String username;
    private String password;
    private String realname;
    private String mobile;
    private String email;
    private String ipAddress;
    private Date lastLoginTime;
    private Integer status;
    private Integer third;

    private String id;

    private String uuid;

    private Date createTime;

    private Date updateTime;

    private String roles;
}

