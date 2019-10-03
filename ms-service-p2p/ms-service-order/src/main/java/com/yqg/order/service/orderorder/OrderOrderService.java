package com.yqg.order.service.orderorder;

import com.alibaba.fastjson.JSONObject;
import com.yqg.api.order.orderorder.OrderOrderServiceApi;
import com.yqg.api.order.orderorder.bo.OrderOrderBo;
import com.yqg.api.order.orderorder.bo.OrderPageListBo;
import com.yqg.api.order.orderorder.bo.OrderSizeBo;
import com.yqg.api.order.orderorder.ro.ManOrderPageRo;
import com.yqg.api.order.orderorder.ro.OrderOrderPageRo;
import com.yqg.api.order.scatterstandard.bo.ScatterstandardBo;
import com.yqg.api.user.useruser.bo.LenderUsrBo;
import com.yqg.api.user.useruser.ro.LenderUsrRo;
import com.yqg.api.user.useruser.ro.UserReq;
import com.yqg.api.user.useruser.ro.UserRo;
import com.yqg.common.core.BaseService;
import com.yqg.common.core.response.BasePageResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.order.entity.OrderOrder;
import com.yqg.order.entity.Scatterstandard;
import com.yqg.order.service.orderorder.impl.OrderOrderServiceImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.ParseException;
import java.util.List;

/**
 * 债权人的基本信息表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 10:40:02
 */
public interface OrderOrderService extends BaseService<OrderOrder> {

    //查询借款人是否有在投
    LenderUsrBo haveInvestIng(LenderUsrRo ro) throws BusinessException ;

    //查询投资人是否有在贷
    LenderUsrBo isLoaning(UserRo ro) throws BusinessException ;

    //查询回款率
    LenderUsrBo getRepayRate() throws BusinessException ;

    //查询借款人信息
    JSONObject queryLenderInfo(String userUuid);

    //向doit推送订单状态
    void pushOrderStatusToDoit(Scatterstandard entity);

    BasePageResponse<OrderOrderBo> selectUsrOrderList(OrderOrderPageRo orderOrderPageRo) throws BusinessException;

    OrderSizeBo selectOrderSize(OrderOrderPageRo orderOrderPageRo) throws BusinessException;

    //创建订单
    void createOrder(OrderOrder entity) throws BusinessException;

    //查询到期订单
    List<OrderOrder> selectDueTimeOrderList()throws BusinessException;

    /*分页查询理财记录*/
    BasePageResponse<OrderPageListBo> orderListByPage(ManOrderPageRo ro) throws BusinessException,ParseException;

}