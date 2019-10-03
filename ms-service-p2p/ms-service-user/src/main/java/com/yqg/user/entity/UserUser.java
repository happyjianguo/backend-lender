package com.yqg.user.entity;

import com.yqg.common.dao.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 用户基本信息表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:20:30
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "userUser" )
public class UserUser extends BaseEntity implements Serializable{
    private static final long serialVersionUID=1L;

    //用户名
    private String userName;
    public static final String userName_field="userName";
    //手机号
    private String mobileNumber;
    public static final String mobileNumber_field="mobileNumber";
    //手机号
    private String mobileNumberDES;
    public static final String mobileNumberDES_field="mobileNumberDES";
    //真实姓名
    private String realName;
    public static final String realName_field="realName";
    //身份证号
    private String idCardNo;
    public static final String idCardNo_field="idCardNo";
    //出生日期
    private String birthDate;
    public static final String birthDate_field="birthDate";
    //年龄
    private Integer age;
    public static final String age_field="age";
    //性别，0：未知，1：男，2：女
    private Integer sex;
    public static final String sex_field="sex";
    //学历
    private String education;
    public static final String education_field="education";
    //工作信息
    private String job;
    public static final String job_field="job";
    //行业
    private String workField;
    public static final String workField_field="workField";
    //宗教
    private String religion;
    public static final String religion_field="religion";
    //公司营业执照号
    private String companyBusinessCode;
    public static final String companyBusinessCode_field="companyBusinessCode";
    //公司类型
    private Integer companyType;
    public static final String companyType_field="companyType";
    //注册法人名称
    private String registeredLegalName;
    public static final String registeredLegalName_field="registeredLegalName";
    //组织编号
    private String organizerCode;
    public static final String organizerCode_field="organizerCode";
    //税号
    private String npwpNo;
    public static final String npwpNo_field="npwpNo";
    //年收入
    private String yearSalary;
    public static final String yearSalary_field="yearSalary";
    //印尼税务居籍
    private String salaryHomeValue;
    public static final String salaryHomeValue_field="salaryHomeValue";
    //工作年限
    private String workTime;
    public static final String workTime_field="workTime";
    //其他资产来源
    private String otherSalaryFrom;
    public static final String otherSalaryFrom_field="otherSalaryFrom";
    //用户类型
    private Integer userType;
    public static final String userType_field="userType";
    //实名状态
    private Integer authStatus;
    public static final String authStatus_field="authStatus";
    //是否拉黑
    private Integer status;
    public static final String status_field="status";
}
