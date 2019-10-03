package com.yqg.api.system.syspermission.bo;

import lombok.Data;
import java.util.List;
/**
 * 系统权限表 业务对象
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-12-04 16:58:25
 */
@Data
public class SysPermissionBo {
    private Integer parentId;
    private String permissionName;
    private String permissionCode;
    private String permissionUrl;
}

