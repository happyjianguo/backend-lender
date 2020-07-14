package com.yqg.order.service.shoppingCart;

import com.yqg.api.order.shoppingcart.bo.CartWMessageBo;
import com.yqg.api.order.shoppingcart.bo.ShoppingCartBo;
import com.yqg.api.order.shoppingcart.ro.ShoppingCartListRo;
import com.yqg.api.order.shoppingcart.ro.ShoppingCartRo;
import com.yqg.common.core.request.BaseSessionIdRo;
import com.yqg.common.exceptions.BusinessException;

import java.util.List;
import java.util.Map;

/**
 * Remark:
 * Created by huwei on 19.5.14.
 */
public interface ShoppingCartService {
    /**
     * 获取购物车列表
     */
    CartWMessageBo getShoppingCartList(ShoppingCartRo ro) throws BusinessException;

    /**
     * 一键加入购物车
     */
    void addAllToShoppingCart(ShoppingCartRo ro) throws BusinessException;

    /**
     * 一键剔除
     */
    void deleteFullForShoppingCart(ShoppingCartRo ro) throws BusinessException;

    /**
     * 删除单个商品
     */
    void deleteOneGoodsForShoppingCart(ShoppingCartRo ro) throws BusinessException;

    /**
     * 添加N个商品
     */
    void addListGoodsForShoppingCart(ShoppingCartListRo ro) throws BusinessException;

    /**
     *  更改购物车中单个商品的数量
     */
    void updateGoodsAmount(ShoppingCartRo ro) throws BusinessException;

    /**
     * 获取购物车通过userUuid
     * @param mobileNumber
     * @return
     */
    Map getCartByUserId(String mobileNumber);

    /**
     * 重置购物车商品数量
     * @param ro
     * @throws BusinessException
     */
    void resetCart(ShoppingCartRo ro) throws BusinessException;
}
