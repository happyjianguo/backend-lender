package com.yqg.api.user.student.studentloanstepthree.ro;

import com.yqg.api.user.student.studentaddressdetail.ro.StudentAddressBasicRo;
import com.yqg.common.core.annocation.ReqStringNotEmpty;
import com.yqg.common.core.request.BasePageRo;
import com.yqg.common.core.request.BaseSessionIdRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

/**
 * 学生借款申请步骤3(担保人信息) 请求对象
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-10-08 10:31:24
 */
@Data
public class StudentLoanStepThreeRo extends BaseSessionIdRo {
//用户id
@ApiModelProperty(value = "用户id", required = true)
private String userUuid;

@ApiModelProperty(value = "订单编号", required = true)
private String orderNo;

//担保人姓名
@ReqStringNotEmpty
@ApiModelProperty(value = "担保人姓名", required = true)
private String userName;

//担保人身份证号
@ReqStringNotEmpty
@ApiModelProperty(value = "担保人身份证号", required = true)
private String idCardNo;

//担保人与借款人关系
@ReqStringNotEmpty
@ApiModelProperty(value = "担保人与借款人关系", required = true)
private String relationship;

//担保人身份证地址
@ApiModelProperty(value = "担保人身份证地址", required = true)
private StudentAddressBasicRo idAddressRo;

//担保人现居地地址
@ApiModelProperty(value = "担保人现居地地址", required = true)
private StudentAddressBasicRo liveAddressRo;

//担保人税卡号
@ApiModelProperty(value = "担保人税卡号", required = true)
private String taxCardNo;

//担保人邮箱
@ApiModelProperty(value = "担保人邮箱", required = true)
private String email;

//担保人手机号
@ReqStringNotEmpty
@ApiModelProperty(value = "担保人手机号", required = true)
private String mobileNumber;

//担保人公司名称
@ReqStringNotEmpty
@ApiModelProperty(value = "担保人公司名称", required = true)
private String companyName;

//担保人公司联系方式
@ReqStringNotEmpty
@ApiModelProperty(value = "担保人公司联系方式", required = true)
private String companyMobile;

//担保人公司地址
@ApiModelProperty(value = "担保人公司地址", required = true)
private StudentAddressBasicRo liveCompanyAddressRo;

//职位
@ReqStringNotEmpty
@ApiModelProperty(value = "职位", required = true)
private String position;

//担保人月收入
@ReqStringNotEmpty
@ApiModelProperty(value = "担保人月收入", required = true)
private String income;

//是否在doit借过款(0否,1是)
@ReqStringNotEmpty
@ApiModelProperty(value = "是否在doit借过款(0否,1是)", required = true)
private Integer doitLoan;

//身份证照片url
@ReqStringNotEmpty
@ApiModelProperty(value = "身份证照片url", required = true)
private String idCardUrl;

//工资单url
/*@ReqStringNotEmpty*/
@ApiModelProperty(value = "工资单url", required = true)
private String payDetailUrl;

//担保人声明照片url
@ReqStringNotEmpty
@ApiModelProperty(value = "担保人声明照片url", required = true)
private String aggrementUrl;

//担保人持有声明照片
@ReqStringNotEmpty
@ApiModelProperty(value = "担保人持有声明照片", required = true)
private String aggrementInHandUrl;
}

