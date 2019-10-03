package com.yqg.api.system.sysapph5.ro;

import com.yqg.common.core.request.BasePageRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

/**
 * app H5 url集合表 请求对象
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@Data
public class SysAppH5Ro {
//url Key
@ApiModelProperty(value = "url Key", required = true)
private String urlKey;
//url 值
@ApiModelProperty(value = "url 值", required = true)
private String urlValue;
//url描述
@ApiModelProperty(value = "url描述", required = true)
private String urlDesc;
}

