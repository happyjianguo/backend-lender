package com.yqg.order.entity;

import com.yqg.common.dao.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 债权表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 11:02:36
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "creditorInfo" )
public class Creditorinfo extends BaseEntity implements Serializable{
    private static final long serialVersionUID=1L;

//借款用途
    private String borrowingPurposes;
    public static final String borrowingPurposes_field="borrowingPurposes";
//风险等级
    private Integer riskLevel;
    public static final String riskLevel_field="riskLevel";
//债权编号
    private String creditorNo;
    public static final String creditorNo_field="creditorNo";
//申请金额
    private BigDecimal amountApply;
    public static final String amountApply_field="amountApply";
//期限
    private String term;
    public static final String term_field="term";
//借款年化利率
    private BigDecimal borrowerYearRate;
    public static final String borrowerYearRate_field="borrowerYearRate";
//前置服务费
    private BigDecimal serviceFee;
    public static final String serviceFee_field="serviceFee";
//发标时间
    private Date biddingTime;
    public static final String biddingTime_field="biddingTime";
//来源
    private Integer channel;
    public static final String channel_field="channel";
//银行code
    private String bankCode;
    public static final String bankCode_field="bankCode";
//银行名称
    private String bankName;
    public static final String bankName_field="bankName";
//银行卡号
    private String bankNumber;
    public static final String bankNumber_field="bankNumber";
//银行卡持卡人
    private String bankCardholder;
    public static final String bankCardholder_field="bankCardholder";
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
    //详细地址
    private String inhabit;
    public static final String inhabit_field="inhabit";
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
    //信用分
    private String creditScore;
    public static final String creditScore_field="creditScore";
    //信用分
    private Integer status;
    public static final String status_field="status";
    //债权类型
    private Integer creditorType;
    public static final String creditorType_field="creditorType";
    //详情
    private String detail;
    public static final String detail_field="detail";
}
