package com.yqg.user.entity.student;

import com.yqg.common.dao.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 学生借款申请步骤3(担保人信息)
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-10-08 10:31:24
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "studentLoanStepThree" )
public class StudentLoanStepThree extends BaseEntity implements Serializable{
    private static final long serialVersionUID=1L;

//用户id
    private String userUuid;
    public static final String userUuid_field="userUuid";
    //订单id
    private String orderNo;
    public static final String orderNo_field="orderNo";
//担保人姓名
    private String userName;
    public static final String userName_field="userName";
//担保人身份证号
    private String idCardNo;
    public static final String idCardNo_field="idCardNo";
//担保人与借款人关系
    private String relationship;
    public static final String relationship_field="relationship";
//担保人税卡号
    private String taxCardNo;
    public static final String taxCardNo_field="taxCardNo";
//担保人邮箱
    private String email;
    public static final String email_field="email";
//担保人手机号
    private String mobileNumber;
    public static final String mobileNumber_field="mobileNumber";
//担保人公司名称
    private String companyName;
    public static final String companyName_field="companyName";
//担保人公司联系方式
    private String companyMobile;
    public static final String companyMobile_field="companyMobile";
//职位
    private String position;
    public static final String position_field="position";
//担保人月收入
    private String income;
    public static final String income_field="income";
//是否在doit借过款(0否,1是)
    private Integer doitLoan;
    public static final String doitLoan_field="doitLoan";
//身份证照片url
    private String idCardUrl;
    public static final String idCardUrl_field="idCardUrl";
//工资单url
    private String payDetailUrl;
    public static final String payDetailUrl_field="payDetailUrl";
//担保人声明照片url
    private String aggrementUrl;
    public static final String aggrementUrl_field="aggrementUrl";
//担保人持有声明照片
    private String aggrementInHandUrl;
    public static final String aggrementInHandUrl_field="aggrementInHandUrl";
}
