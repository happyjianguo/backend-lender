package com.yqg.api.system.sysuser.ro;

import com.yqg.common.core.request.BaseSessionIdRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysUserEditRo extends BaseSessionIdRo {
    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "真实姓名")

    private String realname;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "roleIds")
    private String roleIds;

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "是否为第三方人员")
    private Integer third;
}
