package com.yqg.user.entity.student;

import com.yqg.common.dao.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 学生借款申请步骤1
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-10-08 10:31:24
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "studentLoanStepOne" )
public class StudentLoanStepOne extends BaseEntity implements Serializable{
    private static final long serialVersionUID=1L;

//用户id
    private String userUuid;
    public static final String userUuid_field="userUuid";
    //订单id
    private String orderNo;
    public static final String orderNo_field="orderNo";
//姓名
    private String userName;
    public static final String userName_field="userName";
//身份证号
    private String idCard;
    public static final String idCard_field="idCard";
//性别，0：未知，1：男，2：女
    private Integer sex;
    public static final String sex_field="sex";
//年龄
    private Integer age;
    public static final String age_field="age";
//宗教
    private String religion;
    public static final String religion_field="religion";
//邮箱
    private String email;
    public static final String email_field="email";
//手机号
    private String mobileNumber;
    public static final String mobileNumber_field="mobileNumber";
//家庭年收入
    private String familyYearSalary;
    public static final String familyYearSalary_field="familyYearSalary";
//家庭成员数量
    private String familyMember;
    public static final String familyMember_field="familyMember";
//银行名称
    private String bankName;
    public static final String bankName_field="bankName";
//银行卡号
    private String bankNo;
    public static final String bankNo_field="bankNo";
//是否有分期贷款(0无,1有)
    private Integer loanAble;
    public static final String loanAble_field="loanAble";
//每月分期付款金额
    private String loanAmount;
    public static final String loanAmount_field="loanAmount";
//身份证照片url
    private String idCardUrl;
    public static final String idCardUrl_field="idCardUrl";
//家庭卡照片url
    private String familyCardUrl;
    public static final String familyCardUrl_field="familyCardUrl";
//学生证照片
    private String studentCardUrl;
    public static final String studentCardUrl_field="studentCardUrl";
//手持身份证自拍照url
    private String idCardInHandUrl;
    public static final String idCardInHandUrl_field="idCardInHandUrl";
//大学学生证明照片
    private String collageStudentUrl;
    public static final String collageStudentUrl_field="collageStudentUrl";
}
