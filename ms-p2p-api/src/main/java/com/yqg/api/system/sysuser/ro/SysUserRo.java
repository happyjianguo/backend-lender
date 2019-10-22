package com.yqg.api.system.sysuser.ro;

import com.yqg.common.core.request.BasePageRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 系统用户表 请求对象
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-09-20 15:52:10
 */
@Data
public class SysUserRo {
//用户名
@ApiModelProperty(value = "用户名", required = true)
private String username;
//密码
@ApiModelProperty(value = "密码", required = true)
private String password;
//真实姓名
@ApiModelProperty(value = "真实姓名", required = true)
private String realname;
//手机
@ApiModelProperty(value = "手机", required = true)
private String mobile;
//电子邮件
@ApiModelProperty(value = "电子邮件", required = true)
private String email;
//ip地址
@ApiModelProperty(value = "ip地址", required = true)
private String ipAddress;
//最后登陆时间
@ApiModelProperty(value = "最后登陆时间", required = true)
private Date lastLoginTime;
//状态 0启用 1禁用
@ApiModelProperty(value = "状态 0启用 1禁用", required = true)
private Integer status;
//是否为第三方人员(0是;1否)
@ApiModelProperty(value = "是否为第三方人员(0是;1否)", required = true)
private Integer third;
}

