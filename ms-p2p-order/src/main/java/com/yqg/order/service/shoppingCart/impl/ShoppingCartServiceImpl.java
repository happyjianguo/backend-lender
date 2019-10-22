package com.yqg.order.service.shoppingCart.impl;

import com.yqg.api.order.shoppingcart.bo.ShoppingCartBo;
import com.yqg.api.order.shoppingcart.ro.Goods;
import com.yqg.api.order.shoppingcart.ro.ShoppingCartListRo;
import com.yqg.api.order.shoppingcart.ro.ShoppingCartRo;
import com.yqg.api.user.useruser.bo.UserBo;
import com.yqg.api.user.useruser.ro.UserReq;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.enums.OrderExceptionEnums;
import com.yqg.common.enums.RedisKeyEnums;
import com.yqg.common.enums.ScatterStandardStatusEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.order.entity.Scatterstandard;
import com.yqg.order.service.OrderCommonServiceImpl;
import com.yqg.order.service.scatterstandard.ScatterstandardService;
import com.yqg.order.service.shoppingCart.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * Remark:
 * Created by huwei on 19.5.14.
 */
@Service("shoppingCartService")
public class ShoppingCartServiceImpl extends OrderCommonServiceImpl implements ShoppingCartService {
    @Autowired
    protected ScatterstandardService scatterstandardService;

    @Override
    public List<ShoppingCartBo> getShoppingCartList(ShoppingCartRo ro) throws BusinessException {
        Map<String, String> map = getCartByUserId(ro.getUserId());
        Set<String> set = map.keySet();
        LinkedList<ShoppingCartBo> list = new LinkedList<>();
        boolean flag = false;
        for (String ss: set){
            Scatterstandard scatterstandard = scatterstandardService.findOneByCreditorNo(ss);
            if (null==scatterstandard){
                logger.info("未找到指定标的 creditorNo:{}", ss);
                continue;
            }
            ShoppingCartBo bo = new ShoppingCartBo();
            bo.setBorrowingPurposes(scatterstandard.getBorrowingPurposes());
            bo.setLaveAmount(scatterstandard.getAmountBuy().add(scatterstandard.getAmountLock()).toString());
            bo.setTerm(scatterstandard.getTerm().toString());
            bo.setYearRateFin(scatterstandard.getYearRateFin().toString());
            bo.setTotalAmount(scatterstandard.getAmountApply().toString());
            bo.setGoodsId(scatterstandard.getCreditorNo());
            String count = "0";
            if (scatterstandard.getStatus()==ScatterStandardStatusEnums.FULL_SCALE.getCode()){
            }else if(Integer.parseInt(map.get(ss))> (scatterstandard.getAmountApply().subtract(scatterstandard.getAmountBuy()).subtract(scatterstandard.getAmountLock()).divide(new BigDecimal(10000))).intValue()){
                count = scatterstandard.getAmountApply().subtract(scatterstandard.getAmountBuy()).subtract(scatterstandard.getAmountLock()).divide(new BigDecimal(10000)).intValue()+"";
                map.put(ss,count);
                flag = true;
            }else{
                count = map.get(ss);
            }
            bo.setCount(count);
//            bo.setCount(scatterstandard.getStatus()==ScatterStandardStatusEnums.FULL_SCALE.getCode()?"0":map.get(ss));
            list.add(bo);
        }
        if (flag){
            updateCart(ro.getUserId(),map);
        }
        return list;
    }

    @Override
    public void addAllToShoppingCart(ShoppingCartRo ro) throws BusinessException {
        UserBo user = getUserById(ro.getUserId());
        //判断是否已实名认证
        logger.info("是否实名、绑卡");
        userAuthBankInfo(ro.getUserId());

        Map<String, String> map = getCartByUserId(ro.getUserId());
        //获取当前散标列表查询缓存
        List<String> list = redisUtil.getList(RedisKeyEnums.SCATTERSTANDARD_QUERY_CACHE_KEY.appendToDefaultKey(user.getMobileNumber()), String.class);
        for (String s: list){
//            Scatterstandard scatterstandard = this.scatterstandardService.findOneByCreditorNo(s);
            map.put(s,getLastAmountByCreditorNo(s).divide(new BigDecimal(10000)).intValue()+"");
        }
        updateCart(ro.getUserId(), map);
    }

    @Override
    public void deleteFullForShoppingCart(ShoppingCartRo ro) throws BusinessException {
        Map<String, String> map = getCartByUserId(ro.getUserId());
        Set<String> set = map.keySet();
        for (String ss: set){
            Scatterstandard scatterstandard = scatterstandardService.findOneByCreditorNo(ss);
            if (scatterstandard==null){
                continue;
            }
            if (scatterstandard.getStatus()>=ScatterStandardStatusEnums.FULL_SCALE.getCode()){
                logger.info("scatterstandardNo：{}，is full，remove from cart, userUuid:{}", ss, ro.getUserId());
                map.remove(ss);
            }
        }
        updateCart(ro.getUserId(),map);
    }

    @Override
    public void deleteOneGoodsForShoppingCart(ShoppingCartRo ro) throws BusinessException {
        Map<String, String> map = getCartByUserId(ro.getUserId());
        map.remove(ro.getGoodsId());
        updateCart(ro.getUserId(), map);
    }

    @Override
    public void addListGoodsForShoppingCart(ShoppingCartListRo ro) throws BusinessException {
        UserBo user = getUserById(ro.getUserId());
        //判断是否已实名认证
        logger.info("是否实名、绑卡");
        userAuthBankInfo(ro.getUserId());

        Map<String, String> map = getCartByUserId(ro.getUserId());
        List<Goods> goodsList = ro.getGoodsList();
        for (Goods goods: goodsList){
            if (map.containsKey(goods.getGoodsId())){
                map.put(goods.getGoodsId(),(Integer.parseInt(map.get(goods.getGoodsId()))+Integer.parseInt(goods.getAmount()))+"");
            }else{
                map.put(goods.getGoodsId(), goods.getAmount());
            }
        }
        updateCart(ro.getUserId(), map);
    }

    @Override
    public void updateGoodsAmount(ShoppingCartRo ro) throws BusinessException {
        Map<String, String> map = getCartByUserId(ro.getUserId());
        if (!map.containsKey(ro.getGoodsId())){
            throw new BusinessException(OrderExceptionEnums.CART_NOT_FOUND_GOODS_ERROR);
        }
        map.put(ro.getGoodsId(), ro.getAmount());
        updateCart(ro.getUserId(), map);
    }

    public Map<String, String> getCartByUserId(String userUuid){
        Map map = this.redisUtil.get(RedisKeyEnums.USER_CART_KEY.appendToDefaultKey(userUuid), Map.class);
        if (null == map){
            map = new LinkedHashMap<>();
            redisUtil.set(RedisKeyEnums.USER_CART_KEY.appendToDefaultKey(userUuid),map);
            return getCartByUserId(userUuid);
        }
        return (Map<String, String>)map;
    }

    @Override
    public void resetCart(ShoppingCartRo ro) throws BusinessException {
        Map<String, String> map = getCartByUserId(ro.getUserId());
        Set<String> set = map.keySet();
        for (String s: set){
            map.replace(s, "1");
        }
        updateCart(ro.getUserId(),map);
    }

    public void updateCart(String userUuid, Map map){
        redisUtil.set(RedisKeyEnums.USER_CART_KEY.appendToDefaultKey(userUuid), map);
    }

    public UserBo getUserById(String userUuid) throws BusinessException {
        UserReq userReq = new UserReq();
        userReq.setUserUuid(userUuid);
        BaseResponse<UserBo> userById = this.userService.findUserById(userReq);
        return userById.getData();
    }

    public BigDecimal getLastAmountByCreditorNo(String creditorNo) throws BusinessException {
        Scatterstandard one = scatterstandardService.findOneByCreditorNo(creditorNo);
        BigDecimal subtract = one.getAmountApply().subtract(one.getAmountBuy()).subtract(one.getAmountLock());
        return subtract;
    }
}
