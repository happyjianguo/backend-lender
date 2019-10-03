package com.yqg.pay.controller;

import com.yqg.api.pay.payaccounthistory.PayAccountHistoryServiceApi;
import com.yqg.api.pay.payaccounthistory.ro.PayAccountPageRo;
import com.yqg.common.core.BaseControllor;
import com.yqg.common.core.response.BasePageResponse;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.pay.entity.PayAccountHistory;
import com.yqg.pay.service.payaccounthistory.PayAccountHistoryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 资金流水表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 17:28:20
 */
@RestController
public class PayAccountHistoryController extends BaseControllor {
    @Autowired
    PayAccountHistoryService payaccounthistoryService;

    @ApiOperation(value = "分页查询理财记录", notes = "分页查询理财记录")
    @PostMapping(value = PayAccountHistoryServiceApi.path_payAccountListByPage)
    public BaseResponse<BasePageResponse<PayAccountHistory>> payAccountListByPage(@RequestBody PayAccountPageRo ro) throws Exception {

        return new BaseResponse<BasePageResponse<PayAccountHistory>>().successResponse(this.payaccounthistoryService.payAccountListByPage(ro));

    }
}