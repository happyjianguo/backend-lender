package com.yqg.api.system.sysrole.ro;

import com.yqg.common.core.request.BasePageRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

/**
 * 系统角色表 请求对象
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-12-04 16:58:25
 */
@Data
public class SysRoleRo {
//角色名称
@ApiModelProperty(value = "角色名称", required = true)
private String roleName;
//状态,0=有效，1=无效
@ApiModelProperty(value = "状态,0=有效，1=无效", required = true)
private Integer status;
}

