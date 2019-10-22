package com.yqg.api.system.sysuser.bo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * Created by Lixiangjun on 2019/6/14.
 */
@Data
public class SysUserImageBo {

    @JSONField(serialize=false)
    String imgBase64;
}
