package com.yqg.api.user.useraddressdetail.ro;

import com.yqg.common.core.request.BasePageRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

/**
 * 用户地址信息表 请求对象
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:20:30
 */
@Data
public class UserAddressDetailRo {
//userUuid
@ApiModelProperty(value = "userUuid", required = true)
private String userUuid;
//地址类型：0下单地址；1居住地址；2公司地址；3学校地址
@ApiModelProperty(value = "地址类型：0下单地址；1居住地址；2公司地址；3学校地址", required = true)
private Integer addressType;
//省
@ApiModelProperty(value = "省", required = true)
private String province;
//市
@ApiModelProperty(value = "市", required = true)
private String city;
//大区
@ApiModelProperty(value = "大区", required = true)
private String bigDirect;
//小区
@ApiModelProperty(value = "小区", required = true)
private String smallDirect;
//详细地址
@ApiModelProperty(value = "详细地址", required = true)
private String detailed;
//是否有效：0有效；1无效
@ApiModelProperty(value = "是否有效：0有效；1无效", required = true)
private Integer status;
//排序自增字段
@ApiModelProperty(value = "排序自增字段", required = true)
private Integer sort;
}

