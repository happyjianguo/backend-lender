package com.yqg.order.entity;

import com.yqg.common.dao.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 借款人信息表
 *
 * @author wu
 * @email wu@yishufu.com
 * @date 2018-09-03 11:15:41
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "lenderUser" )
public class LenderUser extends BaseEntity implements Serializable{
    private static final long serialVersionUID=1L;

//借款人用户id
    private String lenderId;
    public static final String lenderId_field="lenderId";
//姓名
    private String name;
    public static final String name_field="name";
//身份证号
    private String idCardNo;
    public static final String idCardNo_field="idCardNo";
//性别
    private String sex;
    public static final String sex_field="sex";
//年龄
    private Integer age;
    public static final String age_field="age";
//婚姻状况
    private String isMarried;
    public static final String isMarried_field="isMarried";
//借款人身份
    private String identidy;
    public static final String identidy_field="identidy";
//手机号
    private String mobile;
    public static final String mobile_field="mobile";
//邮箱地址
    private String email;
    public static final String email_field="email";
//学历
    private String acdemic;
    public static final String acdemic_field="acdemic";
//出生日期
    private String birty;
    public static final String birty_field="birty";
//宗教
    private String religion;
    public static final String religion_field="religion";
//居住地址
    private String address;
    public static final String address_field="address";
//身份信息是否认证
    private String isIdentidyAuth;
    public static final String isIdentidyAuth_field="isIdentidyAuth";
//银行卡信息是否认证
    private String isBankCardAuth;
    public static final String isBankCardAuth_field="isBankCardAuth";
//联系人信息是否认证
    private String isLindManAuth;
    public static final String isLindManAuth_field="isLindManAuth";
//保险卡信息是否认证
    private String isInsuranceCardAuth;
    public static final String isInsuranceCardAuth_field="isInsuranceCardAuth";
//家庭卡信息是否认证
    private String isFamilyCardAuth;
    public static final String isFamilyCardAuth_field="isFamilyCardAuth";
}
