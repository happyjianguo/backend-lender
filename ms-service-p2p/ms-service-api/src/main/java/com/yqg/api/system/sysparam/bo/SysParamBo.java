package com.yqg.api.system.sysparam.bo;

import lombok.Data;
import java.util.List;
/**
 * 系统参数 业务对象
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@Data
public class SysParamBo {
    private String sysKey;
    private String sysValue;
    private String description;
    private String language;
}

