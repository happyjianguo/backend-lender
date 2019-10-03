package com.yqg.api.order.productconf.bo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
/**
 * 投资产品表 业务对象
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-11-14 14:45:36
 */
@Data
public class ProductconfBo {
    private Integer borrowingTerm;
    private String productName;
    private BigDecimal interestRate;
    private BigDecimal lowAmount;
    private BigDecimal maxAmount;
}

