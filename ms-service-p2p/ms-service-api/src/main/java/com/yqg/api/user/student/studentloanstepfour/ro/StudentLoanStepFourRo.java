package com.yqg.api.user.student.studentloanstepfour.ro;

import com.yqg.common.core.request.BasePageRo;
import com.yqg.common.core.request.BaseSessionIdRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

/**
 * 学生借款申请步骤4(联系人信息) 请求对象
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-10-08 10:31:24
 */
@Data
public class StudentLoanStepFourRo extends BaseSessionIdRo {
//用户id
@ApiModelProperty(value = "用户id", required = true)
private String userUuid;

@ApiModelProperty(value = "订单编号", required = true)
private String orderNo;

//
@ApiModelProperty(value = "", required = true)
private String username;
//担保人与借款人关系
@ApiModelProperty(value = "担保人与借款人关系", required = true)
private String relationship;
//担保人手机号
@ApiModelProperty(value = "担保人手机号", required = true)
private String mobileNumber;
}

