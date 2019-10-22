package com.yqg.order.controllor;

import com.yqg.api.order.OrderOrderServiceApi;
import com.yqg.api.order.shoppingcart.bo.ShoppingCartBo;
import com.yqg.api.order.shoppingcart.ro.ShoppingCartListRo;
import com.yqg.api.order.shoppingcart.ro.ShoppingCartRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.order.service.shoppingCart.ShoppingCartService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Remark:
 * Created by huwei on 19.5.9.
 */
@RestController
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @ApiOperation(value = "获取购物车列表", notes = "获取购物车列表")
    @PostMapping(value = OrderOrderServiceApi.path_getShoppingCartList)
    public BaseResponse getShoppingCartList(@RequestBody ShoppingCartRo ro) throws BusinessException {
        List<ShoppingCartBo> list = this.shoppingCartService.getShoppingCartList(ro);
        return new BaseResponse<>().successResponse(list);
    }

    @ApiOperation(value = "一键加入购物车", notes = "一键加入购物车")
    @PostMapping(value = OrderOrderServiceApi.path_addAllToCart)
    public BaseResponse addAllToShoppingCart(@RequestBody ShoppingCartRo ro) throws BusinessException {
        this.shoppingCartService.addAllToShoppingCart(ro);
        return new BaseResponse<>().successResponse();
    }

    @ApiOperation(value = "一键剔除满标", notes = "一键剔除满标")
    @PostMapping(value = OrderOrderServiceApi.path_deleteFullForShoppingCart)
    public BaseResponse deleteFullForShoppingCart(@RequestBody ShoppingCartRo ro) throws BusinessException {
        this.shoppingCartService.deleteFullForShoppingCart(ro);
        return new BaseResponse<>().successResponse();
    }

    @ApiOperation(value = "删除购物车内单个商品", notes = "删除购物车内单个商品")
    @PostMapping(value = OrderOrderServiceApi.path_deleteOneGoodsForShoppingCart)
    public BaseResponse deleteOneGoodsForShoppingCart(@RequestBody ShoppingCartRo ro) throws BusinessException {
        this.shoppingCartService.deleteOneGoodsForShoppingCart(ro);
        return new BaseResponse<>().successResponse();
    }

    @ApiOperation(value = "批量商品加入购物车", notes = "批量商品加入购物车")
    @PostMapping(value = OrderOrderServiceApi.path_addListGoodsForShoppingCart)
    public BaseResponse addListGoodsForShoppingCart(@RequestBody ShoppingCartListRo ro) throws BusinessException {
        this.shoppingCartService.addListGoodsForShoppingCart(ro);
        return new BaseResponse<>().successResponse();
    }


    @ApiOperation(value = "更改商品数量", notes = "更改商品数量")
    @PostMapping(value = OrderOrderServiceApi.path_updateGoodsAmount)
    public BaseResponse updateGoodsAmount(@RequestBody ShoppingCartRo ro) throws BusinessException {
        this.shoppingCartService.updateGoodsAmount(ro);
        return new BaseResponse<>().successResponse();
    }

    @ApiOperation(value = "重置购物车商品数量", notes = "重置购物车商品数量")
    @PostMapping(value = OrderOrderServiceApi.path_resetCart)
    public BaseResponse resetCart(@RequestBody ShoppingCartRo ro) throws BusinessException {
        this.shoppingCartService.resetCart(ro);
        return new BaseResponse<>().successResponse();
    }

}
