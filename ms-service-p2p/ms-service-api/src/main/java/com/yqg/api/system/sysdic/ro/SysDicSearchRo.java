package com.yqg.api.system.sysdic.ro;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysDicSearchRo {
    //字典名
    @ApiModelProperty(value = "字典名", required = true)
    private String dicName;
    //字典编码
    @ApiModelProperty(value = "字典编码", required = true)
    private String dicCode;
    //语言 中文zh_CN 印尼语ID
    @ApiModelProperty(value = "语言 中文zh_CN 印尼语ID", required = true)
    private String language;
}
