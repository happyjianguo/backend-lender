package com.yqg.order.controllor;

import com.yqg.api.order.OrderOrderServiceApi;
import com.yqg.api.order.scatterstandard.bo.ScatterstandardDetailBo;
import com.yqg.api.order.scatterstandard.ro.ScatterstandardRo;
import com.yqg.common.core.BaseControllor;
import com.yqg.common.core.annocation.NotNeedLogin;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.order.service.scatterstandard.ScatterstandardService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 散标表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 11:02:36
 */
@RestController
public class ScatterstandardController extends BaseControllor {
    @Autowired
    ScatterstandardService scatterstandardService;

    @ApiOperation(value = "查询散标详情", notes = "查询散标详情")
    @PostMapping(value = OrderOrderServiceApi.path_select_scatterstandard_by_id)
    public BaseResponse selectOneById(@RequestBody ScatterstandardRo ro) throws Exception {

        ScatterstandardDetailBo detail =  scatterstandardService.finDetaileById(ro);

        return new BaseResponse<ScatterstandardDetailBo>().successResponse(detail);

    }



    @ApiOperation(value = "散标放款--定时任务", notes = "散标放款--定时任务")
    @NotNeedLogin
    @PostMapping(value = OrderOrderServiceApi.path_batchLoan)
    public BaseResponse batchLoan() throws Exception {
        scatterstandardService.batchLoan();
        return new BaseResponse().successResponse();
    }

    @ApiOperation(value = "散标放款处理中--定时任务", notes = "散标放款处理中--定时任务")
    @NotNeedLogin
    @PostMapping(value = OrderOrderServiceApi.path_batchLoanWating)
    public BaseResponse batchLoanWating() throws Exception {
        scatterstandardService.loanWatingTask();
        return new BaseResponse().successResponse();
    }



    @ApiOperation(value = "散标还款成功", notes = "散标还款成功")
    @NotNeedLogin
    @PostMapping(value = OrderOrderServiceApi.path_repaySuccess+"/{creditorNo}")
    public BaseResponse repaySuccess(@PathVariable(value = "creditorNo") String creditorNo) throws Exception {
        scatterstandardService.repaySuccess(creditorNo);
        return new BaseResponse().successResponse();
    }


    @ApiOperation(value = "散标还款失败", notes = "散标还款失败")
    @NotNeedLogin
    @PostMapping(value = OrderOrderServiceApi.path_repayFail+"/{creditorNo}")
    public BaseResponse repayFail(@PathVariable(value = "creditorNo") String creditorNo) throws Exception {
        scatterstandardService.repayFail(creditorNo);
        return new BaseResponse().successResponse();
    }

    @ApiOperation(value = "服务费放款成功", notes = "服务费放款成功")
    @NotNeedLogin
    @PostMapping(value = OrderOrderServiceApi.path_serviceFeeSuccess+"/{creditorNo}")
    public BaseResponse serviceFeeSuccess(@PathVariable(value = "creditorNo") String creditorNo) throws Exception {
        scatterstandardService.serviceFeeSuccess(creditorNo);
        return new BaseResponse().successResponse();
    }


    @ApiOperation(value = "机构投资人清分收入处理定时任务", notes = "机构投资人清分收入处理定时任务")
    @NotNeedLogin
    @PostMapping(value = OrderOrderServiceApi.path_handleBreachDisburse)
    public BaseResponse handleBreachDisburse() throws Exception{
        scatterstandardService.handleBreachDisburse();
        return new BaseResponse().successResponse();
    }


}