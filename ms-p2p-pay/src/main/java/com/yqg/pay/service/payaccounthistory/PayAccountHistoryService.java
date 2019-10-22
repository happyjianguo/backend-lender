package com.yqg.pay.service.payaccounthistory;

import com.yqg.api.pay.payaccounthistory.bo.BreanchClearPageBo;
import com.yqg.api.pay.payaccounthistory.bo.PayAccountHistoryBo;
import com.yqg.api.pay.payaccounthistory.bo.PayAccountListPageBo;
import com.yqg.api.pay.payaccounthistory.ro.*;
import com.yqg.common.core.BaseService;
import com.yqg.common.core.response.BasePageResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.pay.entity.PayAccountHistory;

import java.text.ParseException;
import java.util.List;

/**
 * 资金流水表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 17:28:20
 */
public interface PayAccountHistoryService extends BaseService<PayAccountHistory> {

    /*理财资金流水查询*/
    BasePageResponse<PayAccountHistory> payAccountListByPage(PayAccountPageRo ro) throws BusinessException,ParseException;

    /**
     * 分页查询资金列表
     * @param ro
     * @return
     * @throws Exception
     */
    BasePageResponse<PayAccountListPageBo> payListByPage(PayAccountListPageRo ro) throws Exception;

    /**
     * 更新 资金流水的 处理情况
     * @param ro
     * @throws Exception
     */
    void updatePayAccountHistoryById(PayAccountHistoryUpdateRo ro) throws Exception;
    String paymentCodeByOrderNo(PayAccountHistoryRo ro) throws Exception;

    /**
     * 根据 类型查询资金流水
     * @param ro
     * @return
     * @throws Exception
     */
    List<PayAccountHistoryBo> getPayAccountHistoryByType(PayAccountHistoryRo ro) throws Exception;

    void addPayAccountHistory(PayAccountHistoryRo payAccountHistoryRo) throws BusinessException;

    BasePageResponse<BreanchClearPageBo> getBranchClearList(BreanchClearPageRo ro) throws Exception;

    void updatePayAccountHistoryByIdForBranchClear(PayAccountHistoryUpdateRo ro) throws BusinessException;
}