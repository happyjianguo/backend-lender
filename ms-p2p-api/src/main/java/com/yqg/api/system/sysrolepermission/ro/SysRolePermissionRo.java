package com.yqg.api.system.sysrolepermission.ro;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yqg.common.core.request.BasePageRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

/**
 * 系统角色权限表 请求对象
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-12-04 16:58:25
 */
@Data
public class SysRolePermissionRo {

    @ApiModelProperty(value = "父权限id")
    @JsonProperty
    private String parentId;
    @ApiModelProperty(value = "权限编号")
    @JsonProperty
    private String permissionCode;
    @ApiModelProperty(value = "权限名称")
    @JsonProperty
    private String permissionName;
    @ApiModelProperty(value = "权限url")
    @JsonProperty
    private String permissionUrl;
    //角色ID
    @ApiModelProperty(value = "角色ID", required = true)
    private Integer roleId;
    //权限ID
    @ApiModelProperty(value = "权限ID", required = true)
    private Integer permissionId;
}

