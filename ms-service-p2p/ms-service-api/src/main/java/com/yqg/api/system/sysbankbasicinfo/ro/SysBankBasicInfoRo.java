package com.yqg.api.system.sysbankbasicinfo.ro;

import com.yqg.common.core.request.BaseSessionIdRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 银行基础信息 请求对象
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@Data
public class SysBankBasicInfoRo extends BaseSessionIdRo {
//银行名称
@ApiModelProperty(value = "银行名称", required = true)
private String bankName;
//银行简称
@ApiModelProperty(value = "银行简称", required = true)
private String bankCode;
//是否可用(1=是，0=否)
@ApiModelProperty(value = "是否可用(1=是，0=否)", required = true)
private Integer isUsed;
//维护开始时间
@ApiModelProperty(value = "维护开始时间", required = true)
private Date protectStartTime;
//维护结束时间
@ApiModelProperty(value = "维护结束时间", required = true)
private Date protectEndTime;
//单笔限额
@ApiModelProperty(value = "单笔限额", required = true)
private BigDecimal singleLimit;
//单日限额
@ApiModelProperty(value = "单日限额", required = true)
private BigDecimal oneDayLimit;
}

