package com.yqg.api.system.sysuser.ro;

import com.yqg.common.core.request.BasePageRo;
import lombok.Data;

@Data
public class SysUserSearchRo extends BasePageRo {
    private String userName;

    private String mobile;
}
