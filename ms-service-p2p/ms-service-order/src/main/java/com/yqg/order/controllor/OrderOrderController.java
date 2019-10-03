package com.yqg.order.controllor;

import com.yqg.api.order.orderorder.OrderOrderServiceApi;
import com.yqg.api.order.orderorder.bo.OrderOrderBo;
import com.yqg.api.order.orderorder.bo.OrderPageListBo;
import com.yqg.api.order.orderorder.bo.OrderSizeBo;
import com.yqg.api.order.orderorder.ro.ManOrderPageRo;
import com.yqg.api.order.orderorder.ro.OrderOrderPageRo;
import com.yqg.common.core.BaseControllor;
import com.yqg.common.core.annocation.NotNeedLogin;
import com.yqg.common.core.response.BasePageResponse;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.order.service.orderorder.OrderOrderService;
import com.yqg.pay.service.loan.PayLoanService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    PayLoanService payLoanService;


    //查询用户投资记录
    @ApiOperation(value = "查询用户投资记录", notes = "查询用户投资记录")
    @PostMapping(value = OrderOrderServiceApi.path_selectUsrOrderList)
    public BaseResponse selectUsrOrderList(@RequestBody OrderOrderPageRo ro) throws Exception {


        BasePageResponse<OrderOrderBo> list = orderOrderService.selectUsrOrderList(ro);


        return new BaseResponse<BasePageResponse<OrderOrderBo>>().successResponse(list);

    }

    //查询用户投资笔数
    @ApiOperation(value = "查询用户投资笔数", notes = "查询用户投资笔数")
    @PostMapping(value = OrderOrderServiceApi.path_selectUsrOrderSize)
    public BaseResponse selectUsrOrderSize(@RequestBody OrderOrderPageRo ro) throws Exception {


        OrderSizeBo orderSizeBo = orderOrderService.selectOrderSize(ro);


        return new BaseResponse<Integer>().successResponse(orderSizeBo.getSize());

    }

    @ApiOperation(value = "分页查询理财记录", notes = "分页查询理财记录")
    @PostMapping(value = OrderOrderServiceApi.path_orderListByPage)
    public BaseResponse<BasePageResponse<OrderPageListBo>> orderListByPage(@RequestBody ManOrderPageRo ro) throws Exception {

        return new BaseResponse<BasePageResponse<OrderPageListBo>>().successResponse(this.orderOrderService.orderListByPage(ro));

    }


    @ApiOperation(value = "测试Controller", notes = "测试Controller")
    @PostMapping(value = "/test")
    @NotNeedLogin
    public BaseResponse test(@RequestBody ManOrderPageRo ro) throws Exception {

//        if(ro.getMobile().equals("0")){
//            this.payLoanService.calculationBackAmount();
//        }else if(ro.getMobile().equals("1")){
//            this.payLoanService.backInterestAmountWating();
//        }else if(ro.getMobile().equals("2")){
//            this.payLoanService.backAmount();
//        }else if(ro.getMobile().equals("3")){
//            this.payLoanService.backAmountWating();
//        }


        return new BaseResponse<BasePageResponse<OrderPageListBo>>().successResponse();

    }

}