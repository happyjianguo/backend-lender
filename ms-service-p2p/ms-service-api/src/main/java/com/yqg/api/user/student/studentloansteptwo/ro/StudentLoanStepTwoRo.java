package com.yqg.api.user.student.studentloansteptwo.ro;

import com.yqg.api.user.student.studentaddressdetail.ro.StudentAddressBasicRo;
import com.yqg.common.core.annocation.ReqStringNotEmpty;
import com.yqg.common.core.request.BasePageRo;
import com.yqg.common.core.request.BaseSessionIdRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

/**
 * 学生借款申请步骤2 请求对象
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-10-08 10:31:24
 */
@Data
public class StudentLoanStepTwoRo extends BaseSessionIdRo {
//用户id
@ApiModelProperty(value = "用户id", required = true)
private String userUuid;

@ApiModelProperty(value = "订单编号", required = true)
private String orderNo;

//学校名称
@ReqStringNotEmpty
@ApiModelProperty(value = "学校名称", required = true)
private String schoolName;

//学生学校地址
@ApiModelProperty(value = "学生学校地址", required = true)
private StudentAddressBasicRo schoolAddressRo;

//教育程度
@ApiModelProperty(value = "教育程度", required = true)
private String level;

//学校电话
@ReqStringNotEmpty
@ApiModelProperty(value = "学校电话", required = true)
private String schoolMobile;

//入学年份
@ReqStringNotEmpty
@ApiModelProperty(value = "入学年份", required = true)
private String enterYear;

//学院和专业
@ReqStringNotEmpty
@ApiModelProperty(value = "学院和专业", required = true)
private String subject;

//学号
@ReqStringNotEmpty
@ApiModelProperty(value = "学号", required = true)
private String studentId;

//平均基点
@ReqStringNotEmpty
@ApiModelProperty(value = "平均基点", required = true)
private String averageBasePoint;

//学费
@ReqStringNotEmpty
@ApiModelProperty(value = "学费", required = true)
private String schoolFee;

//学费支付期
@ReqStringNotEmpty
@ApiModelProperty(value = "学费支付期", required = true)
private String schoolFeeTerm;

//学费支付方式
@ReqStringNotEmpty
@ApiModelProperty(value = "学费支付方式", required = true)
private String schoolFeePayWay;

//支付学费银行名称
@ApiModelProperty(value = "支付学费银行名称", required = true)
private String schoolFeePayBank;

//支付学费银行卡号
@ApiModelProperty(value = "支付学费银行卡号", required = true)
private String schoolFeePayBankNo;

//学费支付描述详情
@ApiModelProperty(value = "学费支付描述详情", required = true)
private String schoolFeePayDetail;
}

