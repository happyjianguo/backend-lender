package com.yqg.order.service.orderpackagerel;

import com.yqg.common.core.BaseService;
import com.yqg.order.entity.Orderpackagerel;

import java.util.List;

/**
 * 订单债权包关系表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-11-27 15:04:24
 */
public interface OrderpackagerelService extends BaseService<Orderpackagerel> {

    List<Orderpackagerel> findByPackageNo(String packageNo)  throws Exception;
}