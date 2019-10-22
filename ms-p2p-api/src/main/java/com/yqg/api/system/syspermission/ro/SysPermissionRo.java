package com.yqg.api.system.syspermission.ro;

import com.yqg.common.core.request.BasePageRo;
import com.yqg.common.core.request.BaseSessionIdRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

/**
 * 系统权限表 请求对象
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-12-04 16:58:25
 */
@Data
public class SysPermissionRo extends BaseSessionIdRo {
    //父权限ID
    @ApiModelProperty(value = "父权限ID", required = true)
    private String parentId;
    //权限名称
    @ApiModelProperty(value = "权限名称", required = true)
    private String permissionName;
    //权限码
    @ApiModelProperty(value = "权限码", required = true)
    private String permissionCode;
    //权限路径
    @ApiModelProperty(value = "权限路径", required = true)
    private String permissionUrl;
    //权限路径
    @ApiModelProperty(value = "id", required = true)
    private String id;
}

