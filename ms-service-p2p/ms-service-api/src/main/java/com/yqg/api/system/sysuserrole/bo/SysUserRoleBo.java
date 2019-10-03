package com.yqg.api.system.sysuserrole.bo;

import lombok.Data;
import java.util.List;
/**
 * 用户角色中间表 业务对象
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-12-04 16:58:25
 */
@Data
public class SysUserRoleBo {
    private Integer userId;
    private Integer roleId;
    private Integer status;
}

