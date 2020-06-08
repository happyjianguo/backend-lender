package com.yqg.order.controllor;

import com.yqg.api.order.OrderOrderServiceApi;
import com.yqg.api.order.creditorinfo.bo.LoanHistiryBo;
import com.yqg.api.order.creditorinfo.bo.ScatterstandardListBo;
import com.yqg.api.order.scatterstandard.ro.LoanHistoryRo;
import com.yqg.api.order.scatterstandard.ro.ScatterstandardPageRo;
import com.yqg.api.pay.exception.PayExceptionEnums;
import com.yqg.common.core.BaseControllor;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.order.service.creditorinfo.CreditorinfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 债权表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 11:02:36
 */
@RestController
public class CreditorinfoController extends BaseControllor {
    @Autowired
    CreditorinfoService creditorInfoService;

    //查询散标列表
    @ApiOperation(value = "查询散标列表", notes = "查询散标列表")
    @PostMapping(value = OrderOrderServiceApi.path_select_ScatterstandardList)
    public BaseResponse selectList(@RequestBody ScatterstandardPageRo ro) throws Exception {

        ScatterstandardListBo list =  creditorInfoService.queryForPage(ro);
        if(list.getBo().getTotalPages()<1){
            throw new BusinessException(PayExceptionEnums.NO_AVAILABLE_ORDER);
        }

        return new BaseResponse<ScatterstandardListBo>().successResponse(list);

    }

    @ApiOperation(value = "查询历史借款记录", notes = "查询历史借款记录")
    @PostMapping(value = OrderOrderServiceApi.path_selectLoanHistory)
    public BaseResponse selectLoanHistory(@RequestBody LoanHistoryRo ro) throws BusinessException {
        List<LoanHistiryBo> list = creditorInfoService.selectLoanHistoryByNumber(ro);
        return new BaseResponse().successResponse(list);
    }

}