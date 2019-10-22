package com.yqg.api.order.orderorder.bo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 债权人的基本信息表 业务对象
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 10:40:02
 */
@Data
public class OrderOrderBo {
    private String userUuid;
    //购买金额
    private BigDecimal applyBuy;
    private BigDecimal amountBuy;
    //购买金额
    private BigDecimal chargeBuy;

    private String buyTime;
    private String userName;
    //付款时间
    private Date incomeTime;
    private Integer status;
    private String id;
    private String createTime;
}

