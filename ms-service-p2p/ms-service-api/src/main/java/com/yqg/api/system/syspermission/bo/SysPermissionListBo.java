package com.yqg.api.system.syspermission.bo;

import lombok.Data;

@Data
public class SysPermissionListBo {
    private String id;

    private Boolean isCheck;

    private String permissionName;

    private String permissionCode;

    private String permissionUrl;

    private Object children;
}
