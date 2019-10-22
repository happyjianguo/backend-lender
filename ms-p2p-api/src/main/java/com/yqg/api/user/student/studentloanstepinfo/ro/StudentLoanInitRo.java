package com.yqg.api.user.student.studentloanstepinfo.ro;

import com.yqg.common.core.annocation.ReqStringNotEmpty;
import com.yqg.common.core.request.BaseSessionIdRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 学生借款信息步骤表 请求对象
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-10-08 10:31:24
 */
@Data
public class StudentLoanInitRo extends BaseSessionIdRo {
    //学生借款信息步骤
    @ReqStringNotEmpty
    @ApiModelProperty(value = "借款金额", required = true)
    private BigDecimal amountApply;

    //学生借款信息步骤
    @ReqStringNotEmpty
    @ApiModelProperty(value = "借款期限", required = true)
    private Integer term;
}
