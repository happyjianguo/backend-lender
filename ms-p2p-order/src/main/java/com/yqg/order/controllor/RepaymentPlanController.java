package com.yqg.order.controllor;

import com.alibaba.fastjson.JSON;
import com.yqg.api.order.OrderOrderServiceApi;
import com.yqg.api.order.orderorder.bo.RepaymentPlanBo;
import com.yqg.api.order.orderorder.ro.OrderPayRo;
import com.yqg.api.order.orderorder.ro.RepaymentPlanRo;
import com.yqg.common.core.BaseControllor;
import com.yqg.common.core.annocation.NotNeedLogin;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.order.service.repaymentplan.RepaymentPlanService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Lixiangjun on 2019/7/1.
 */
@RestController
public class RepaymentPlanController extends BaseControllor {

    @Autowired
    RepaymentPlanService repaymentPlanService;

    /**
     * 存入还款计划
     * @param ro
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "addRepaymentPlan", notes = "addRepaymentPlan")
    @NotNeedLogin
    @PostMapping(value = OrderOrderServiceApi.path_addRepaymentPlan)
    public BaseResponse addRepaymentPlan(@RequestBody RepaymentPlanRo ro)throws Exception{
        logger.info("推标接口请求参数:"+ JSON.toJSONString(ro));
        this.repaymentPlanService.addRepaymentPlan(ro);
        return new BaseResponse().successResponse();
    }


    /**
     * 查询还款计划
     * @param ro
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "findRepaymentPlanList", notes = "findRepaymentPlanList")
    @NotNeedLogin
    @PostMapping(value = OrderOrderServiceApi.path_findRepaymentPlanList)
    public BaseResponse<List<RepaymentPlanBo>> findRepaymentPlanList(@RequestBody RepaymentPlanRo ro)throws Exception{
        return new BaseResponse<List<RepaymentPlanBo>>().successResponse(this.repaymentPlanService.findRepaymentPlanList(ro));
    }


    /**
     * 更新还款计划
     * @param ro
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "updateStatus", notes = "updateStatus")
    @NotNeedLogin
    @PostMapping(value = OrderOrderServiceApi.path_updateStatus)
    public BaseResponse updateStatus(@RequestBody RepaymentPlanRo ro) throws Exception {
        this.repaymentPlanService.updateStatus(ro);
        return new BaseResponse().successResponse();
    }
}
