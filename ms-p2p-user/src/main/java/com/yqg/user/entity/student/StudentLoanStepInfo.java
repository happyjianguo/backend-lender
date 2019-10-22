package com.yqg.user.entity.student;

import com.yqg.common.dao.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 学生借款信息步骤表
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-10-08 10:31:24
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "studentLoanStepInfo" )
public class StudentLoanStepInfo extends BaseEntity implements Serializable{
    private static final long serialVersionUID=1L;

//学生借款信息步骤
    private Integer step;
    public static final String step_field="step";
//用户id
    private String userUuid;
    public static final String userUuid_field="userUuid";
    //真实姓名
    private String realName;
    public static final String realName_field="realName";
    //订单状态
    private Integer status;
    public static final String status_field="status";
    //真实姓名
    private Integer isAgain;
    public static final String isAgain_field="isAgain";
    //复借次数
    private Integer passTime;
    public static final String passTime_field="passTime";
    //申请金额
    private BigDecimal amountApply;
    public static final String amountApply_field="amountApply";
    //申请期限
    private Integer term;
    public static final String term_field="term";
    //
    private String inviteCode;
    public static final String inviteCode_field="inviteCode";
}
