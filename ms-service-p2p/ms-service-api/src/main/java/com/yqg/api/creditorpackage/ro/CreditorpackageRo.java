package com.yqg.api.creditorpackage.ro;

import com.yqg.common.core.request.BasePageRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

/**
 * 债权包表 请求对象
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-11-15 18:21:38
 */
@Data
public class CreditorpackageRo{
//产品uuid
@ApiModelProperty(value = "产品uuid", required = true)
private Integer productUuid;
//债权编号
@ApiModelProperty(value = "债权编号", required = true)
private String code;
//状态
@ApiModelProperty(value = "状态", required = true)
private Integer status;
}

