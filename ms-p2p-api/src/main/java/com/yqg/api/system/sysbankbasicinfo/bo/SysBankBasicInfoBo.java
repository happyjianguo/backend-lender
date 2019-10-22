package com.yqg.api.system.sysbankbasicinfo.bo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 银行基础信息 业务对象
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@Data
public class SysBankBasicInfoBo {
    private String id;
    private String bankName;
    private String bankCode;
    private Integer isUsed;
    private Date protectStartTime;
    private Date protectEndTime;
    private BigDecimal singleLimit;
    private BigDecimal oneDayLimit;
}

