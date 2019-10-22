package com.yqg.api.order.orderorder.bo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderPageListBo {
    private String id;

    private String mobile;

    private BigDecimal amountBuy;   //购买金额

    private Date buyTime;           //购买时间

    private BigDecimal yearRateFin; //年化收益率

    private Date dueTime;           //到期日

    private Integer productType;    //购买产品类型
}
