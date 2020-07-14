package com.yqg.api.order.shoppingcart.bo;

import lombok.Data;

import java.util.List;

@Data
public class CartWMessageBo {
    List<ShoppingCartBo> shoppingCartBoList;
    String update;
    String removedOrders;
}
