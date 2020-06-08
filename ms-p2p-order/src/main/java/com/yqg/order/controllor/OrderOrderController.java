package com.yqg.order.controllor;

import com.yqg.api.order.OrderOrderServiceApi;
import com.yqg.api.order.orderorder.bo.OrderOrderBo;
import com.yqg.api.order.orderorder.bo.OrderPageListBo;
import com.yqg.api.order.orderorder.ro.ManOrderPageRo;
import com.yqg.api.order.orderorder.ro.OrderOrderPageRo;
import com.yqg.api.order.orderorder.ro.OrderOrderRo;
import com.yqg.api.order.orderorder.ro.OrderPayRo;
import com.yqg.api.order.scatterstandard.bo.ScatterstandardDetailBo;
import com.yqg.api.pay.income.ro.InvestmentRo;
import com.yqg.common.core.BaseControllor;
import com.yqg.common.core.annocation.NotNeedLogin;
import com.yqg.common.core.response.BasePageResponse;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.enums.OrderStatusEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.DateUtils;
import com.yqg.order.entity.OrderOrder;
import com.yqg.order.service.orderorder.OrderOrderService;
import com.yqg.order.service.scatterstandard.ScatterstandardService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

/**
 * 债权人的基本信息表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 10:40:02
 */
@RestController
public class OrderOrderController extends BaseControllor {
    @Autowired
    OrderOrderService orderOrderService;
    @Autowired
    ScatterstandardService scatterstandardService;



    //查询用户投资记录
    @ApiOperation(value = "查询用户投资记录", notes = "查询用户投资记录")
    @PostMapping(value = OrderOrderServiceApi.path_selectUsrOrderList)
    public BaseResponse selectUsrOrderList(@RequestBody OrderOrderPageRo ro) throws Exception {

        BasePageResponse<OrderOrderBo> list = orderOrderService.selectUsrOrderList(ro);

        return new BaseResponse<BasePageResponse<OrderOrderBo>>().successResponse(list);
    }


    @ApiOperation(value = "查询订单对应债权", notes = "查询订单对应债权")
    @PostMapping(value = OrderOrderServiceApi.path_selectOrderDetail)
    public BaseResponse selectOrderDetail(@RequestBody OrderOrderPageRo ro) throws Exception {

        BasePageResponse<ScatterstandardDetailBo> list = orderOrderService.selectOrderDetail(ro);
        return new BaseResponse<BasePageResponse<ScatterstandardDetailBo>>().successResponse(list);
    }

    //查询用户投资笔数
//    @ApiOperation(value = "查询用户投资笔数", notes = "查询用户投资笔数")
//    @PostMapping(value = OrderOrderServiceApi.path_selectUsrOrderSize)
//    public BaseResponse selectUsrOrderSize(@RequestBody OrderOrderPageRo ro) throws Exception {
//
//
//        OrderSizeBo orderSizeBo = orderOrderService.selectOrderSize(ro);
//
//
//        return new BaseResponse<Integer>().successResponse(orderSizeBo.getSize());
//
//    }

    @ApiOperation(value = "分页查询理财记录", notes = "分页查询理财记录")
    @PostMapping(value = OrderOrderServiceApi.path_orderListByPage)
    public BaseResponse<BasePageResponse<OrderPageListBo>> orderListByPage(@RequestBody ManOrderPageRo ro) throws Exception {

        return new BaseResponse<BasePageResponse<OrderPageListBo>>().successResponse(this.orderOrderService.orderListByPage(ro));

    }

    @ApiOperation(value = "提交订单", notes = "提交订单")
    @PostMapping(value = OrderOrderServiceApi.path_orderSubmit)
    public BaseResponse orderSubmit(@RequestBody InvestmentRo investmentRo) throws Exception {
        return new BaseResponse<>().successResponse(this.scatterstandardService.immediateInvestment(investmentRo));

    }

    @ApiOperation(value = "支付", notes = "支付")
    @PostMapping(value = OrderOrderServiceApi.path_orderPay)
    public BaseResponse orderPay(@RequestBody OrderOrderRo orderOrderRo) throws Exception {
        String orderNo = orderOrderRo.getOrderNo();
        return new BaseResponse<>().successResponse(this.orderOrderService.orderPay(orderNo));

    }

    @ApiOperation(value = "checkPayPWD", notes = "checkPayPWD")
    @PostMapping(value = OrderOrderServiceApi.path_checkPayPWD)
    public BaseResponse checkPayPWD(@RequestBody OrderPayRo orderPayRo) throws Exception {
        return new BaseResponse<>().successResponse(this.scatterstandardService.checkPayPWD(orderPayRo));

    }

    @ApiOperation(value = "successOrder", notes = "successOrder")
    @NotNeedLogin
    @PostMapping(value = OrderOrderServiceApi.path_successOrder+"/{orderNo}")
    public BaseResponse successOrder(@PathVariable String orderNo) throws Exception {
        this.scatterstandardService.successOrder(orderNo);
        return new BaseResponse().successResponse();

    }

    @ApiOperation(value = "failOrder", notes = "failOrder")
    @NotNeedLogin
    @PostMapping(value = OrderOrderServiceApi.path_failOrder+"/{orderNo}")
    public BaseResponse failOrder(@PathVariable String orderNo) throws Exception {
        this.scatterstandardService.failOrder(orderNo);
        return new BaseResponse().successResponse();

    }


    @ApiOperation(value = "expireOrder", notes = "expireOrder")
    @NotNeedLogin
    @PostMapping(value = OrderOrderServiceApi.path_expireOrder)
    public BaseResponse expireOrder() throws Exception {
        OrderOrder order = new OrderOrder();
        order.setStatus(OrderStatusEnums.INVESTMENTING.getCode());
        List<OrderOrder> orderOrders = orderOrderService.findList(order);
        orderOrders.forEach(orderOrder -> {
            try {
                if(DateUtils.redMin(30).after(orderOrder.getBuyTime())){
                    this.scatterstandardService.failOrder(orderOrder.getId());
                }
            } catch (ParseException | BusinessException e) {
                e.printStackTrace();
            }
        });


        return new BaseResponse().successResponse();

    }



}