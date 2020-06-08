package com.yqg.order.controllor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yqg.api.order.OrderOrderServiceApi;
import com.yqg.api.order.creditorinfo.ro.CreditorinfoRo;
import com.yqg.api.order.scatterstandard.bo.ScatterstandardBo;
import com.yqg.api.order.scatterstandard.ro.ScatterstandardRo;
import com.yqg.api.pay.income.bo.IncomeBo;
import com.yqg.api.user.useruser.bo.LenderUsrBo;
import com.yqg.api.user.useruser.ro.LenderUsrRo;
import com.yqg.api.user.useruser.ro.UserReq;
import com.yqg.api.user.useruser.ro.UserRo;
import com.yqg.common.core.BaseControllor;
import com.yqg.common.core.annocation.NotNeedLogin;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.enums.OrderExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.order.entity.Scatterstandard;
import com.yqg.order.service.creditorinfo.CreditorinfoService;
import com.yqg.order.service.orderorder.OrderOrderService;
import com.yqg.order.service.scatterstandard.ScatterstandardService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2018/9/3.
 */
@RestController
public class ForDoitLoanController  extends BaseControllor {


    @Autowired
    CreditorinfoService creditorInfoService;

    @Autowired
    OrderOrderService orderOrderService;
    @Autowired
    ScatterstandardService scatterstandardService;

    //1.接收推标
    @ApiOperation(value = "接收推标", notes = "接收推标")
    @PostMapping(value = OrderOrderServiceApi.path_send_creditorinfo)
    @NotNeedLogin
    public BaseResponse sendCreditorinfo(@RequestBody CreditorinfoRo ro) throws Exception {
        logger.info("推标接口请求参数:"+JSON.toJSONString(ro));
        creditorInfoService.sendCreditorinfo(ro);

        return successResponse();

    }

    //2.查询散标的状态

    @ApiOperation(value = "查询标的状态", notes = "查询标的状态")
    @PostMapping(value = OrderOrderServiceApi.path_select_Scatterstandard)
    @NotNeedLogin
    public BaseResponse selectScatterstandard(@RequestBody ScatterstandardRo ro) throws Exception {
            // TODO: 2019/5/21 查询标的状态
        logger.info("查询标的状态接口请求参数:"+JSON.toJSONString(ro));
        Scatterstandard scatterstandard = new Scatterstandard();
        scatterstandard.setDisabled(0);
        scatterstandard.setCreditorNo(ro.getCreditorNo());

        Scatterstandard scatterstandardResult = scatterstandardService.findOne(scatterstandard);
        ScatterstandardBo scatterstandardBo = new ScatterstandardBo();
        if(scatterstandardResult == null){
            throw new BusinessException(OrderExceptionEnums.SELECT_SCATTERSTANDARD_ERROR);
        }else {
            int status =1;
            scatterstandardBo.setCreditorNo(scatterstandardResult.getCreditorNo());
            switch (scatterstandardResult.getStatus()) {
                case 1:
                case 2:
                case 3:
                    status = 1;//1.投标中
                    break;
                case 4:
                    status = 2;//2.放款中
                    break;
                case 6:
                case 7:
                case 8:
                    status = 3;//3.放款成功
                    break;
                case 5:
                    status = 4;//4.放款失败
                    break;
                case 9:
                    status = 5;//5.还款处理中
                    break;
                case 10:
                case 11:
                case 12:
                case 13:
                    status = 6;//还款成功
                    //  查询订单状态的接口如果是还款成功的状态还需要把还款的paymentCode以及实际还款金额返回 参数 paymentCode amountActual 与status字段并列 and 以下字段
//            depositStatus： 还款状态
//            externalId：
//            transactionId
//            depositMethod：还款方式
//            depositChannel：还款渠道
                    scatterstandardBo.setPaymentcode(scatterstandardResult.getPaymentcode());
                    scatterstandardBo.setAmountActual(scatterstandardResult.getAmountActual());
                    scatterstandardBo.setDepositStatus(scatterstandardResult.getDepositStatus());
                    scatterstandardBo.setExternalId(scatterstandardResult.getExternalId());
                    scatterstandardBo.setTransactionId(scatterstandardResult.getTransactionId());
                    scatterstandardBo.setDepositMethod(scatterstandardResult.getDepositMethod());
                    scatterstandardBo.setDepositChannel(scatterstandardResult.getDepositChannel());

                    break;
            }
            scatterstandardBo.setStatus(status);

        }

        return new BaseResponse<ScatterstandardBo>().successResponse(scatterstandardBo);

    }


    @NotNeedLogin
    @ApiOperation(value = "查询是否有在投", notes = "查询是否有在投")
    @PostMapping(value = OrderOrderServiceApi.path_have_investIng)
    public BaseResponse haveInvestIng(@RequestBody LenderUsrRo ro) throws Exception {
        logger.info("查询是否有在投接口请求参数:"+JSON.toJSONString(ro));
        LenderUsrBo userBo= orderOrderService.haveInvestIng(ro);
        return new BaseResponse<LenderUsrBo>().successResponse(userBo);
    }
    @NotNeedLogin
    @ApiOperation(value = "用户还款", notes = "用户还款")
    @PostMapping(value = OrderOrderServiceApi.path_userRepay)
    public BaseResponse<IncomeBo> userRepay(@RequestBody ScatterstandardRo ro) throws Exception {
        logger.info("用户还款接口请求参数:"+JSON.toJSONString(ro));
        return new BaseResponse<IncomeBo>().successResponse(scatterstandardService.repayment(ro));
    }


    @NotNeedLogin
    @ApiOperation(value = "查询是否有在贷(doit提供)", notes = "查询是否有在贷")
    @PostMapping(value = OrderOrderServiceApi.path_isLoaning)
    public BaseResponse isLoaning(@RequestBody UserReq req) throws Exception {
        logger.info("查询是否有在贷接口请求参数:"+JSON.toJSONString(req));
        UserRo ro = new UserRo();
        BeanCoypUtil.copy(req,ro);
        LenderUsrBo lenderUsrRo = orderOrderService.isLoaning(ro);
        return new BaseResponse<LenderUsrBo>().successResponse(lenderUsrRo);
    }

    @NotNeedLogin
    @ApiOperation(value = "向doit推送订单状态(doit提供)", notes = "推送订单状态")
    @PostMapping(value = OrderOrderServiceApi.path_pushOrderStatusToDoit)
    public BaseResponse path_pushOrderStatusToDoit(@RequestBody UserReq req) throws Exception {
        logger.info("推送订单状态接口请求参数:"+JSON.toJSONString(req));
        UserRo ro = new UserRo();
        BeanCoypUtil.copy(req,ro);
        orderOrderService.pushOrderStatusToDoit(new Scatterstandard());
        return new BaseResponse<LenderUsrBo>().successResponse();
    }

    @NotNeedLogin
    @ApiOperation(value = "查询回款率(doit提供)", notes = "查询回款率")
    @PostMapping(value = OrderOrderServiceApi.path_getRepayRate)
    public BaseResponse getRepayRate() throws Exception{
        logger.info("调doit 查询回款率开始");
        LenderUsrBo lenderUsrRo = orderOrderService.getRepayRate();
        return new BaseResponse<LenderUsrBo>().successResponse(lenderUsrRo);
    }


}
