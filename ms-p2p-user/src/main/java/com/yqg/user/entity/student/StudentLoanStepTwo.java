package com.yqg.user.entity.student;

import com.yqg.common.dao.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 学生借款申请步骤2
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-10-08 10:31:24
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "studentLoanStepTwo" )
public class StudentLoanStepTwo extends BaseEntity implements Serializable{
    private static final long serialVersionUID=1L;

//用户id
    private String userUuid;
    public static final String userUuid_field="userUuid";
    //订单id
    private String orderNo;
    public static final String orderNo_field="orderNo";
//学校名称
    private String schoolName;
    public static final String schoolName_field="schoolName";
//学校电话
    private String schoolMobile;
    public static final String schoolMobile_field="schoolMobile";
    //学校电话
    private String level;
    public static final String level_field="level";
//入学年份
    private String enterYear;
    public static final String enterYear_field="enterYear";
//学院和专业
    private String subject;
    public static final String subject_field="subject";
//学号
    private String studentId;
    public static final String studentId_field="studentId";
//平均基点
    private String averageBasePoint;
    public static final String averageBasePoint_field="averageBasePoint";
//学费
    private String schoolFee;
    public static final String schoolFee_field="schoolFee";
//学费支付期
    private String schoolFeeTerm;
    public static final String schoolFeeTerm_field="schoolFeeTerm";
//学费支付方式
    private String schoolFeePayWay;
    public static final String schoolFeePayWay_field="schoolFeePayWay";
//支付学费银行名称
    private String schoolFeePayBank;
    public static final String schoolFeePayBank_field="schoolFeePayBank";
//支付学费银行卡号
    private String schoolFeePayBankNo;
    public static final String schoolFeePayBankNo_field="schoolFeePayBankNo";
//学费支付描述详情
    private String schoolFeePayDetail;
    public static final String schoolFeePayDetail_field="schoolFeePayDetail";
}
