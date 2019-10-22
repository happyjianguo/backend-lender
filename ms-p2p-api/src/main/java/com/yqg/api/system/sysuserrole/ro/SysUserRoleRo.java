package com.yqg.api.system.sysuserrole.ro;

import com.yqg.common.core.request.BasePageRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

/**
 * 用户角色中间表 请求对象
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-12-04 16:58:25
 */
@Data
public class SysUserRoleRo {
//用户ID
@ApiModelProperty(value = "用户ID", required = true)
private Integer userId;
//角色ID
@ApiModelProperty(value = "角色ID", required = true)
private Integer roleId;
//状态,0=有效，1=无效
@ApiModelProperty(value = "状态,0=有效，1=无效", required = true)
private Integer status;
}

