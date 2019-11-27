package com.yqg.api.pay.payaccounthistory;

/**
 * 资金流水表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 17:28:20
 */
public class PayAccountHistoryServiceApi {
    public static final String serviceName = "service-pay";

    public static final String path_payAccountListByPage = "/api-pay/pay/payAccountListByPage";
    public static final String path_payListByPage = "/api-pay/pay/payListByPage";
    public static final String path_updatePayAccountHistoryById = "/api-pay/pay/updatePayAccountHistoryById";

    public static final String path_paymentCode = "/api-pay/pay/paymentCode";
    public static final String path_getPayAccountHistoryByType = "/api-pay/pay/getPayAccountHistoryByType";
    public static final String path_addPayAccountHistory = "/api-pay/pay/addPayAccountHistory";

    public static final String path_getBranchClearList = "/api-pay/pay/getBranchClearList";
    public static final String path_updatePayAccountHistoryByIdForBranchClear = "/api-pay/pay/updatePayAccountHistoryByIdForBranchClear";
}