package com.yqg.api.order.lenderuser.ro;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 借款人信息表 请求对象
 *
 * @author wu
 * @email wu@yishufu.com
 * @date 2018-09-03 11:15:41
 */
@Data
public class LenderUserRo{
//借款人用户id
@ApiModelProperty(value = "借款人用户id", required = true)
private String lenderId;
//姓名
@ApiModelProperty(value = "姓名", required = true)
private String name;
//身份证号
@ApiModelProperty(value = "身份证号", required = true)
private String idCardNo;
//性别
@ApiModelProperty(value = "性别", required = true)
private String sex;
//年龄
@ApiModelProperty(value = "年龄", required = true)
private Integer age;
//婚姻状况
@ApiModelProperty(value = "婚姻状况", required = true)
private String isMarried;
//借款人身份
@ApiModelProperty(value = "借款人身份", required = true)
private String identidy;
//手机号
@ApiModelProperty(value = "手机号", required = true)
private String mobile;
//邮箱地址
@ApiModelProperty(value = "邮箱地址", required = true)
private String email;
//学历
@ApiModelProperty(value = "学历", required = true)
private String acdemic;
//出生日期
@ApiModelProperty(value = "出生日期", required = true)
private String birty;
//宗教
@ApiModelProperty(value = "宗教", required = true)
private String religion;
//居住地址
@ApiModelProperty(value = "居住地址", required = true)
private String address;
//身份信息是否认证
@ApiModelProperty(value = "身份信息是否认证", required = true)
private String isIdentidyAuth;
//银行卡信息是否认证
@ApiModelProperty(value = "银行卡信息是否认证", required = true)
private String isBankCardAuth;
//联系人信息是否认证
@ApiModelProperty(value = "联系人信息是否认证", required = true)
private String isLindManAuth;
//保险卡信息是否认证
@ApiModelProperty(value = "保险卡信息是否认证", required = true)
private String isInsuranceCardAuth;
//家庭卡信息是否认证
@ApiModelProperty(value = "家庭卡信息是否认证", required = true)
private String isFamilyCardAuth;
}

