package com.yqg.api.system.systhirdlogs.ro;

import com.yqg.common.core.request.BasePageRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

/**
 * 第三方日志信息 请求对象
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@Data
public class SysThirdLogsRo {
//用户id
@ApiModelProperty(value = "用户id", required = true)
private String userUuid;
//订单号
@ApiModelProperty(value = "订单号", required = true)
private String orderNo;
//第三方类型(1、短信服务，2、advance，3、依图，4、支付服务,5、卡bin)
@ApiModelProperty(value = "第三方类型(1、短信服务，2、advance，3、依图，4、支付服务,5、卡bin)", required = true)
private Integer thirdType;
//根据不同的第三方接口类型自定义
@ApiModelProperty(value = "根据不同的第三方接口类型自定义", required = true)
private String logType;
//请求参数
@ApiModelProperty(value = "请求参数", required = true)
private String request;
//响应参数
@ApiModelProperty(value = "响应参数", required = true)
private String response;
//耗时（毫秒）
@ApiModelProperty(value = "耗时（毫秒）", required = true)
private Integer timeUsed;
}

