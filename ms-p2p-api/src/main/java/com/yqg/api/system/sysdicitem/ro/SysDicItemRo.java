package com.yqg.api.system.sysdicitem.ro;

import com.yqg.common.core.request.BasePageRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

/**
 * 字典项表 请求对象
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@Data
public class SysDicItemRo {
//字典父级ID 对应sysDict表
@ApiModelProperty(value = "字典父级ID 对应sysDict表", required = true)
private Integer dicId;
//字典项值
@ApiModelProperty(value = "字典项值", required = true)
private String dicItemValue;
//字典项名
@ApiModelProperty(value = "字典项名", required = true)
private String dicItemName;
//语言 中文zh_CN 印尼语ID
@ApiModelProperty(value = "语言 中文zh_CN 印尼语ID", required = true)
private String language;
}

