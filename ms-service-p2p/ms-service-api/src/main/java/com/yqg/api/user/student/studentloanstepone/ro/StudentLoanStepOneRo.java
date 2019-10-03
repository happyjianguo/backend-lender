package com.yqg.api.user.student.studentloanstepone.ro;

import com.yqg.api.user.student.studentaddressdetail.ro.StudentAddressBasicRo;
import com.yqg.common.core.annocation.ReqStringNotEmpty;
import com.yqg.common.core.request.BasePageRo;
import com.yqg.common.core.request.BaseSessionIdRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

/**
 * 学生借款申请步骤1 请求对象
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-10-08 10:31:24
 */
@Data
public class StudentLoanStepOneRo extends BaseSessionIdRo {
//用户id
/*@ReqStringNotEmpty
@ApiModelProperty(value = "用户id", required = true)
private String userUuid;*/
@ApiModelProperty(value = "订单编号", required = true)
private String orderNo;

//姓名
@ReqStringNotEmpty
@ApiModelProperty(value = "姓名", required = true)
private String userName;

//身份证号
@ReqStringNotEmpty
@ApiModelProperty(value = "身份证号", required = true)
private String idCard;

//性别，0：未知，1：男，2：女
@ReqStringNotEmpty
@ApiModelProperty(value = "性别，0：未知，1：男，2：女", required = true)
private Integer sex;

//年龄
@ReqStringNotEmpty
@ApiModelProperty(value = "年龄", required = true)
private Integer age;

//宗教
@ReqStringNotEmpty
@ApiModelProperty(value = "宗教", required = true)
private String religion;

//身份证地址
@ApiModelProperty(value = "身份证地址", required = true)
private StudentAddressBasicRo idAddressRo;

//现居地地址
@ApiModelProperty(value = "现居地地址", required = true)
private StudentAddressBasicRo liveAddressRo;

//邮箱
@ReqStringNotEmpty
@ApiModelProperty(value = "邮箱", required = true)
private String email;

//手机号
@ReqStringNotEmpty
@ApiModelProperty(value = "手机号", required = true)
private String mobileNumber;

//家庭年收入
@ReqStringNotEmpty
@ApiModelProperty(value = "家庭年收入", required = true)
private String familyYearSalary;

//家庭成员数量
@ReqStringNotEmpty
@ApiModelProperty(value = "家庭成员数量", required = true)
private String familyMember;

//银行名称
@ReqStringNotEmpty
@ApiModelProperty(value = "银行名称", required = true)
private String bankName;

//银行卡号
@ReqStringNotEmpty
@ApiModelProperty(value = "银行卡号", required = true)
private String bankNo;

//是否有分期贷款(0无,1有)
@ReqStringNotEmpty
@ApiModelProperty(value = "是否有分期贷款(0无,1有)", required = true)
private Integer loanAble;

//每月分期付款金额
//@ReqStringNotEmpty
@ApiModelProperty(value = "每月分期付款金额", required = true)
private String loanAmount;

//身份证照片url
@ReqStringNotEmpty
@ApiModelProperty(value = "身份证照片url", required = true)
private String idCardUrl;

//家庭卡照片url
@ReqStringNotEmpty
@ApiModelProperty(value = "家庭卡照片url", required = true)
private String familyCardUrl;

//学生证照片
@ReqStringNotEmpty
@ApiModelProperty(value = "学生证照片", required = true)
private String studentCardUrl;

//手持身份证自拍照url
@ReqStringNotEmpty
@ApiModelProperty(value = "手持身份证自拍照url", required = true)
private String idCardInHandUrl;

//大学学生证明照片
@ReqStringNotEmpty
@ApiModelProperty(value = "大学学生证明照片", required = true)
private String collageStudentUrl;

@ApiModelProperty(value = "邀请码", required = true)
private String inviteCode;
}

