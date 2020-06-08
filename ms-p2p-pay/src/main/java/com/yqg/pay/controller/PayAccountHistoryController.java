package com.yqg.pay.controller;

import com.alibaba.fastjson.JSONObject;
import com.yqg.api.pay.payaccounthistory.PayAccountHistoryServiceApi;
import com.yqg.api.pay.payaccounthistory.bo.BankCodeBo;
import com.yqg.api.pay.payaccounthistory.bo.BreanchClearPageBo;
import com.yqg.api.pay.payaccounthistory.bo.PayAccountHistoryBo;
import com.yqg.api.pay.payaccounthistory.bo.PayAccountListPageBo;
import com.yqg.api.pay.payaccounthistory.ro.*;
import com.yqg.common.core.BaseControllor;
import com.yqg.common.core.response.BasePageResponse;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.pay.entity.PayAccountHistory;
import com.yqg.pay.service.payaccounthistory.PayAccountHistoryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @ApiOperation(value = "分页查询资金列表（资金管理 三个列表通用）", notes = "分页查询资金列表")
    @PostMapping(value = PayAccountHistoryServiceApi.path_payListByPage)
    public BaseResponse<BasePageResponse<PayAccountListPageBo>> payListByPage(@RequestBody PayAccountListPageRo ro) throws Exception {

        return new BaseResponse<BasePageResponse<PayAccountListPageBo>>().successResponse(this.payaccounthistoryService.payListByPage(ro));

    }


    @ApiOperation(value = "更新提现失败列表 订单情况", notes = "更新提现失败列表 订单情况")
    @PostMapping(value = PayAccountHistoryServiceApi.path_updatePayAccountHistoryById)
    public BaseResponse<JSONObject> updatePayAccountHistoryById(@RequestBody PayAccountHistoryUpdateRo ro) throws Exception {

        this.payaccounthistoryService.updatePayAccountHistoryById(ro);
        return new BaseResponse().successResponse();

    }

    @ApiOperation(value = "查询资金流水--paymentCode", notes = "查询资金流水--paymentCode")
    @PostMapping(value = PayAccountHistoryServiceApi.path_paymentCode)
    public BaseResponse<BankCodeBo> paymentCodeByOrderNo(@RequestBody PayAccountHistoryRo payAccountHistoryRo) throws Exception {

        BankCodeBo bankCodeBo = new BankCodeBo();
        bankCodeBo.setBankCode(this.payaccounthistoryService.paymentCodeByOrderNo(payAccountHistoryRo));

        return new BaseResponse<BankCodeBo>().successResponse(bankCodeBo);

    }

    @ApiOperation(value = "根据type 查询资金流水", notes = "根据type 查询资金流水")
    @PostMapping(value = PayAccountHistoryServiceApi.path_getPayAccountHistoryByType)
    public List<PayAccountHistoryBo> getPayAccountHistoryByType(@RequestBody PayAccountHistoryRo payAccountHistoryRo) throws Exception {

        return this.payaccounthistoryService.getPayAccountHistoryByType(payAccountHistoryRo);

    }

    @ApiOperation(value = "添加新的资金流水", notes = "添加新的资金流水")
    @PostMapping(value = PayAccountHistoryServiceApi.path_addPayAccountHistory)
    public BaseResponse addPayAccountHistory(@RequestBody PayAccountHistoryRo payAccountHistoryRo) throws Exception {

        this.payaccounthistoryService.addPayAccountHistory(payAccountHistoryRo);
        return new BaseResponse().successResponse();

    }

    @ApiOperation(value = "查询机构清分列表", notes = "查询机构清分列表")
    @PostMapping(value = PayAccountHistoryServiceApi.path_getBranchClearList)
    public BaseResponse<BasePageResponse<BreanchClearPageBo>> getBranchClearList(@RequestBody BreanchClearPageRo breanchClearPageRo) throws Exception {

        BasePageResponse<BreanchClearPageBo> list = this.payaccounthistoryService.getBranchClearList(breanchClearPageRo);
        return new BaseResponse<BasePageResponse<BreanchClearPageBo>>().successResponse(list);
    }

    @ApiOperation(value = "机构清分资金流水编辑操作", notes = "机构清分资金流水编辑操作")
    @PostMapping(value = PayAccountHistoryServiceApi.path_updatePayAccountHistoryByIdForBranchClear)
    public BaseResponse<BasePageResponse<BreanchClearPageBo>> updatePayAccountHistoryByIdForBranchClear(@RequestBody PayAccountHistoryUpdateRo ro) throws Exception {

        this.payaccounthistoryService.updatePayAccountHistoryByIdForBranchClear(ro);
        return new BaseResponse<BasePageResponse<BreanchClearPageBo>>().successResponse();
    }


}