package com.yqg.api.system.syspaymentchannel.ro;

import com.yqg.common.core.request.BasePageRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

/**
 *  请求对象
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@Data
public class SysPaymentChannelRo {
//支付通道编码
@ApiModelProperty(value = "支付通道编码", required = true)
private String paymentChannelCode;
//支付通道名称
@ApiModelProperty(value = "支付通道名称", required = true)
private String paymentChannelName;
//通道类型
@ApiModelProperty(value = "通道类型", required = true)
private Integer type;
//是否有效(0=无效，1=有效)
@ApiModelProperty(value = "是否有效(0=无效，1=有效)", required = true)
private Integer status;
}

