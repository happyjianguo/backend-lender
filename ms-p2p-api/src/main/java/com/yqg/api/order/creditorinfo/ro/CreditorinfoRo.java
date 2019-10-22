package com.yqg.api.order.creditorinfo.ro;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 债权表 请求对象
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 11:02:36
 */
@Data
public class CreditorinfoRo{
    //债权相关
    //借款用途
    @ApiModelProperty(value = "借款用途", required = true)
    private String borrowingPurposes;
    //风险等级
    @ApiModelProperty(value = "风险等级", required = true)
    private Integer riskLevel;
    //债权编号
    @ApiModelProperty(value = "债权编号", required = true)
    private String creditorNo;
    //借款人用户uuid
    @ApiModelProperty(value = "借款人用户uuid", required = true)
    private String lenderId;
    //申请金额
    @ApiModelProperty(value = "申请金额", required = true)
    private BigDecimal amountApply;
    //期限
    @ApiModelProperty(value = "期限", required = true)
    private String term;
    //借款年化利率
    @ApiModelProperty(value = "借款年化利率", required = true)
    private BigDecimal borrowerYearRate;
    //前置服务费
    @ApiModelProperty(value = "前置服务费", required = true)
    private BigDecimal serviceFee;
    //发标时间
    @ApiModelProperty(value = "发标时间", required = true)
    private String biddingTime;
    //来源
    @ApiModelProperty(value = "来源", required = true)
    private Integer channel;
    //银行code
    @ApiModelProperty(value = "银行code", required = true)
    private String bankCode;
    //银行名称
    @ApiModelProperty(value = "银行名称", required = true)
    private String bankName;
    //银行卡号
    @ApiModelProperty(value = "银行卡号", required = true)
    private String bankNumber;
    //银行卡持卡人
    @ApiModelProperty(value = "银行卡持卡人", required = true)
    private String bankCardholder;
    //签名
    @ApiModelProperty(value = "签名", required = true)
    private String sign;
    //借款人相关
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
    //身份
    @ApiModelProperty(value = "身份", required = true)
    private String identidy;
    //手机号
    @ApiModelProperty(value = "手机号", required = true)
    private String mobile;
    //邮箱
    @ApiModelProperty(value = "邮箱", required = true)
    private String email;
    //学历
    @ApiModelProperty(value = "学历", required = true)
    private String acdemic;
    //生日
    @ApiModelProperty(value = "生日", required = true)
    private String birty;
    //宗教
    @ApiModelProperty(value = "宗教", required = true)
    private String religion;
    //居住地
    @ApiModelProperty(value = "居住地", required = true)
    private String address;
    //详细地址
    @ApiModelProperty(value = "详细地址", required = true)
    private String inhabit;
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
    //信用分
    @ApiModelProperty(value = "信用分", required = true)
    private String creditScore;

    //债权类型
    @ApiModelProperty(value = "债权类型", required = true)
    private Integer creditorType;

    //展期对应债权编号
    @ApiModelProperty(value = "其他", required = true)
    private String detail;


}

