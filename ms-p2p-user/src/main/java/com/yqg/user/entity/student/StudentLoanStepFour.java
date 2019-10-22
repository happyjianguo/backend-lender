package com.yqg.user.entity.student;

import com.yqg.common.dao.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 学生借款申请步骤4(联系人信息)
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-10-08 10:31:24
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "studentLoanStepFour" )
public class StudentLoanStepFour extends BaseEntity implements Serializable{
    private static final long serialVersionUID=1L;

//用户id
    private String userUuid;
    public static final String userUuid_field="userUuid";
    //订单id
    private String orderNo;
    public static final String orderNo_field="orderNo";
//
    private String username;
    public static final String username_field="username";
//担保人与借款人关系
    private String relationship;
    public static final String relationship_field="relationship";
//担保人手机号
    private String mobileNumber;
    public static final String mobileNumber_field="mobileNumber";
}
