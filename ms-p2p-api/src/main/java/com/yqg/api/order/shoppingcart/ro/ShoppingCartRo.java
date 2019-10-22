package com.yqg.api.order.shoppingcart.ro;

import com.yqg.common.core.request.BaseSessionIdRo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Remark:
 * Created by huwei on 19.5.10.
 */
@Data
@ApiModel("购物车模型")
public class ShoppingCartRo extends BaseSessionIdRo {

    @ApiModelProperty(value = "商品id")
    private String goodsId;
    @ApiModelProperty(value = "商品數量")
    private String amount;
}
