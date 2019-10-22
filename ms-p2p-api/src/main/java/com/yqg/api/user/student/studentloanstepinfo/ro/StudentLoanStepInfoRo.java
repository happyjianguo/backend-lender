package com.yqg.api.user.student.studentloanstepinfo.ro;

import com.yqg.common.core.request.BasePageRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

/**
 * 学生借款信息步骤表 请求对象
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-10-08 10:31:24
 */
@Data
public class StudentLoanStepInfoRo extends BasePageRo {
//学生借款信息步骤
@ApiModelProperty(value = "学生借款信息步骤", required = true)
private Integer step;
//用户id
@ApiModelProperty(value = "用户id", required = true)
private String userUuid;
    @ApiModelProperty(value = "订单号", required = true)
    private String id;

    @ApiModelProperty(value = "姓名", required = true)
    private String realName;

    @ApiModelProperty(value = "订单状态", required = true)
    private Integer status;

    @ApiModelProperty(value = "申请金额", required = true)
    private String amountApply;

    @ApiModelProperty(value = "申请期限", required = true)
    private Integer term;

    @ApiModelProperty(value = "申请时间极小值", required = true)
    private String createTimeMin;

    @ApiModelProperty(value = "申请时间极大值", required = true)
    private String createTimeMax;


}

