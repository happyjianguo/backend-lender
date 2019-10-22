package com.yqg.api.system.sysdicitem.bo;

import lombok.Data;
import java.util.List;
/**
 * 字典项表 业务对象
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@Data
public class SysDicItemBo {
    private Integer dicId;
    private String dicItemValue;
    private String dicItemName;
    private String language;
}

