package com.yqg.api.user.useruser.ro;

import com.yqg.common.core.request.BasePageRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserAuthSearchRo extends BasePageRo {
    @ApiModelProperty(value = "用户名", required = true)
    private String realname;

    @ApiModelProperty(value = "手机号", required = true)
    private String mobile;

    @ApiModelProperty(value = "时间极小值", required = true)
    private String timeMin;

    @ApiModelProperty(value = "时间极大值", required = true)
    private String timeMax;

    @ApiModelProperty(value = "是否通过", required = true)
    private Integer pass;

    @ApiModelProperty(value = "用户id", required = true)
    private String userUuid;

    @ApiModelProperty(value = "认证失败原因", required = true)
    private String cause;
    @ApiModelProperty(value = "备注", required = true)
    private String remark;
}
