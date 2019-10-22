package com.yqg.api.system.sysrole.ro;

import com.yqg.common.core.request.BaseSessionIdRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ManSysRoleRo extends BaseSessionIdRo {
    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "角色名称")
    private String roleName;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "权限id集合")
    private String permissionIds;
    @ApiModelProperty(value = "status")
    private Integer status;
}
