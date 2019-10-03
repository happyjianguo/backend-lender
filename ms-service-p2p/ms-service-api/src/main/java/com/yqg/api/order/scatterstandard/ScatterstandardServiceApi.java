package com.yqg.api.order.scatterstandard;

/**
 * 散标表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 11:02:36
 */
public class ScatterstandardServiceApi {
    public static final String serviceName = "service-order";
    //查询散标状态
    public static final String path_select_Scatterstandard = "/order/selectScatterstandard";

    //查询散标列表
    public static final String path_select_ScatterstandardList = "/order/selectScatterstandardList";
    public static final String path_deal6HoursLaterOrder = "/order/deal6HoursLaterOrder";
    public static final String path_deal30MinLaterOrder = "/order/deal30MinLaterOrder";


    public static final String path_select_creditorinfo = "/order/selectCreditorinfo";

    public static final String path_loan_flow_standard = "/order/loanFlowStandard";
}