package com.yqg.api.user.usrloginhistory.ro;

import com.yqg.common.core.request.BasePageRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

/**
 * 用户登录历史表 请求对象
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:20:30
 */
@Data
public class UsrLoginHistoryRo {
//用户uuid
@ApiModelProperty(value = "用户uuid", required = true)
private String userUuid;
//设备号
@ApiModelProperty(value = "设备号", required = true)
private String deviceNomber;
//设备类型
@ApiModelProperty(value = "设备类型", required = true)
private String deviceType;
//mac地址
@ApiModelProperty(value = "mac地址", required = true)
private String macAddress;
//ip地址
@ApiModelProperty(value = "ip地址", required = true)
private String ipAddress;
//网络类型(0=4G,1=WiFi)
@ApiModelProperty(value = "网络类型(0=4G,1=WiFi)", required = true)
private String networkType;
//手机系统版本号
@ApiModelProperty(value = "手机系统版本号", required = true)
private String mobileSysVersionNo;
//市场渠道号
@ApiModelProperty(value = "市场渠道号", required = true)
private String marketChannelNo;
//应用版本号
@ApiModelProperty(value = "应用版本号", required = true)
private String applicationVersionNo;
//经度
@ApiModelProperty(value = "经度", required = true)
private String lbsX;
//纬度
@ApiModelProperty(value = "纬度", required = true)
private String lbsY;
//排序自增字段
@ApiModelProperty(value = "排序自增字段", required = true)
private Integer sort;
}

