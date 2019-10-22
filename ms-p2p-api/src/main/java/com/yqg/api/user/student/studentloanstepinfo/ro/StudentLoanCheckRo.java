package com.yqg.api.user.student.studentloanstepinfo.ro;

import com.yqg.common.core.annocation.ReqStringNotEmpty;
import com.yqg.common.core.request.BaseSessionIdRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 学生借款信息审核 请求对象
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-10-08 10:31:24
 */
@Data
public class StudentLoanCheckRo extends BaseSessionIdRo {
    //是否拒绝
    @ReqStringNotEmpty
    @ApiModelProperty(value = "是否拒绝", required = true)
    private Integer pass;

    @ApiModelProperty(value = "拒绝原因", required = true)
    private String reason;

    @ReqStringNotEmpty
    @ApiModelProperty(value = "操作人姓名", required = true)
    private String username;

    @ReqStringNotEmpty
    @ApiModelProperty(value = "订单编号", required = true)
    private String id;
}
