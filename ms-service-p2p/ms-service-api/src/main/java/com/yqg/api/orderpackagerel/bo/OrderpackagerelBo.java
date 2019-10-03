package com.yqg.api.orderpackagerel.bo;

import lombok.Data;

import java.math.BigDecimal;
/**
 * 订单债权包关系表 业务对象
 *
 * @author admin
 * @email admin@yishufu.com
 * @date 2018-11-27 14:57:50
 */
@Data
public class OrderpackagerelBo {
    private String orderNo;
    private String code;
    private Integer sort;
    private BigDecimal amount;
    private String buyUser;
    private Integer userType;
}

