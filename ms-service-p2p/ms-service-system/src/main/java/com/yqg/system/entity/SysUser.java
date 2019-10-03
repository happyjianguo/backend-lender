package com.yqg.system.entity;

import com.yqg.common.dao.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 系统用户表
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-09-20 15:52:10
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "sysUser" )
public class SysUser extends BaseEntity implements Serializable{
    private static final long serialVersionUID=1L;

//用户名
    private String username;
    public static final String username_field="username";
//密码
    private String password;
    public static final String password_field="password";
//真实姓名
    private String realname;
    public static final String realname_field="realname";
//手机
    private String mobile;
    public static final String mobile_field="mobile";
//电子邮件
    private String email;
    public static final String email_field="email";
//ip地址
    private String ipAddress;
    public static final String ipAddress_field="ipAddress";
//最后登陆时间
    private Date lastLoginTime;
    public static final String lastLoginTime_field="lastLoginTime";
//状态 0启用 1禁用
    private Integer status;
    public static final String status_field="status";
//是否为第三方人员(0是;1否)
    private Integer third;
    public static final String third_field="third";

    /*@OneToMany(mappedBy = "sysUser")
    @JoinColumn(name = "userId")
    private List<SysUser> sysUserList;*/

}
