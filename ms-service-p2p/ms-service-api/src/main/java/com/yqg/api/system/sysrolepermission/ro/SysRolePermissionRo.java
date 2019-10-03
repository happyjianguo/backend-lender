package com.yqg.api.system.sysrolepermission.ro;

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
//角色ID
@ApiModelProperty(value = "角色ID", required = true)
private Integer roleId;
//权限ID
@ApiModelProperty(value = "权限ID", required = true)
private Integer permissionId;
}

