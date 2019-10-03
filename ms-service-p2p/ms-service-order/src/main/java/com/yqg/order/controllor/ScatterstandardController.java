package com.yqg.order.controllor;

import com.yqg.api.order.scatterstandard.ScatterstandardServiceApi;
import com.yqg.api.order.scatterstandard.bo.ScatterstandardBo;
import com.yqg.api.order.scatterstandard.ro.ScatterstandardPageRo;
import com.yqg.api.order.scatterstandard.ro.ScatterstandardRo;
import com.yqg.common.core.BaseControllor;
import com.yqg.common.core.annocation.NotNeedLogin;
import com.yqg.common.core.response.BasePageResponse;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.order.entity.Scatterstandard;
import com.yqg.order.service.scatterstandard.ScatterstandardService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    //查询散标列表
    @NotNeedLogin
    @ApiOperation(value = "查询散标列表", notes = "查询散标列表")
    @PostMapping(value = ScatterstandardServiceApi.path_select_ScatterstandardList)
    public BaseResponse selectList(@RequestBody ScatterstandardPageRo ro) throws Exception {

        BasePageResponse<ScatterstandardBo> list =  scatterstandardService.queryForPage(ro);

        return new BaseResponse<BasePageResponse<ScatterstandardBo>>().successResponse(list);

    }


    //查询债权
    @ApiOperation(value = "查询债权", notes = "查询债权")
    @PostMapping(value = ScatterstandardServiceApi.path_select_creditorinfo)
    public BaseResponse selectCreditorinfo(@RequestBody ScatterstandardRo ro) throws Exception {


        return new BaseResponse<List<Scatterstandard>>().successResponse(scatterstandardService.selectCreditorinfo(ro));

    }


    @ApiOperation(value = "六小时处理标定时器", notes = "六小时处理标定时器")
    @PostMapping(value = ScatterstandardServiceApi.path_deal6HoursLaterOrder)
    @NotNeedLogin
    public BaseResponse deal6HoursLaterOrder() throws Exception {
        logger.info("六小时处理标定时器");
        scatterstandardService.deal6HoursLaterOrder();

        return new BaseResponse<BasePageResponse>().successResponse();
    }

    @ApiOperation(value = "流标处理定时任务", notes = "流标处理定时任务")
    @PostMapping(value = ScatterstandardServiceApi.path_loan_flow_standard)
    @NotNeedLogin
    public BaseResponse loanFlowStandard() throws Exception {
        logger.info("流标处理定时任务");
        scatterstandardService.loanFlowStandard();

        return new BaseResponse<BasePageResponse>().successResponse();
    }
    @ApiOperation(value = "半小时购买订单失效定时器", notes = "半小时购买订单失效定时器")
    @PostMapping(value = ScatterstandardServiceApi.path_deal30MinLaterOrder)
    @NotNeedLogin
    public BaseResponse deal30MinLaterOrder() throws Exception {
        logger.info("半小时购买订单失效定时器");
        scatterstandardService.deal30MinLaterOrder();
        return new BaseResponse<BasePageResponse>().successResponse();
    }


}