package com.yqg.api.order.shoppingcart.ro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Remark:
 * Created by huwei on 19.6.21.
 */
@Data
@ApiModel("添加购物车模型")
public class ShoppingCartListRo extends ShoppingCartRo {
    @ApiModelProperty(value = "商品列表")
    private List<Goods> goodsList;

}
