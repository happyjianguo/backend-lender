package com.yqg.api.system.sysdic.ro;

import com.yqg.common.core.request.BasePageRo;
import com.yqg.common.core.request.BaseSessionIdRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

/**
 * 字典表 请求对象
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@Data
public class SysDicRo extends BaseSessionIdRo {
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

