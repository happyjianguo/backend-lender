package com.yqg.api.system.sysparam.ro;

import com.yqg.common.core.request.BasePageRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

/**
 * 系统参数 请求对象
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@Data
public class SysParamRo {
//系统key
@ApiModelProperty(value = "系统key", required = true)
private String sysKey;
//数值
@ApiModelProperty(value = "数值", required = true)
private String sysValue;
//描述
@ApiModelProperty(value = "描述", required = true)
private String description;
//语言 中文zh_CN 印尼语ID
@ApiModelProperty(value = "语言 中文zh_CN 印尼语ID", required = true)
private String language;
}

