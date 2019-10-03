package com.yqg.api.order.orderorder.bo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * 债权人的基本信息表 业务对象
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 10:40:02
 */
@Data
public class OrderOrderBo {
    private String creditorNo;
    private String userUuid;
    private BigDecimal amountBuy;
    private Integer term;
    private BigDecimal yearRate;
    private BigDecimal yearRateFin;
    private BigDecimal serviceFee;
    private String buyTime;
    private String dueTime;
    private String actReturnTime;
    private String createTime;
    private BigDecimal income;
    private Integer status;
    private Integer productType;
    private Integer sort;
    private String id;
}

