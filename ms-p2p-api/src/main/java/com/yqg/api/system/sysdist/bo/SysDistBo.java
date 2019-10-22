package com.yqg.api.system.sysdist.bo;

import lombok.Data;
import java.util.List;
/**
 * 行政区划表 业务对象
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@Data
public class SysDistBo {
    private String uuid;
    private String distName;
    private String distCode;
    private String distLevel;
    private String parentCode;
    private String language;
}

