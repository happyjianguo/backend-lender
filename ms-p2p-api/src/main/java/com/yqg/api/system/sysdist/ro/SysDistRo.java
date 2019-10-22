package com.yqg.api.system.sysdist.ro;

import com.yqg.common.core.request.BasePageRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

/**
 * 行政区划表 请求对象
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@Data
public class SysDistRo {
//多语言UUID可重复
@ApiModelProperty(value = "多语言UUID可重复", required = true)
private String uuid;
//区划名称
@ApiModelProperty(value = "区划名称", required = true)
private String distName;
//区划编号
@ApiModelProperty(value = "区划编号", required = true)
private String distCode;
//区划级别
@ApiModelProperty(value = "区划级别", required = true)
private String distLevel;
//上级区划编号
@ApiModelProperty(value = "上级区划编号", required = true)
private String parentCode;
//语言 中文zh_CN 印尼语ID
@ApiModelProperty(value = "语言 中文zh_CN 印尼语ID", required = true)
private String language;
}

