package com.yqg.api.order.shoppingcart.bo;

import lombok.Data;

/**
 * Remark:
 * Created by huwei on 19.5.10.
 */
@Data
public class ShoppingCartBo {
    private String borrowingPurposes;//借款用途
    private String term;//期限
    private String yearRateFin;//逾期年化收益率
    private String totalAmount;//总金额
    private String laveAmount;//剩余金额
    private String count;//购买数量
    private String goodsId;//商品编号
}
