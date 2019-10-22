package com.yqg.api.order;

/**
 * 债权/订单/散标 相关接口
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 10:40:02
 */
public class OrderOrderServiceApi {
    public static final String serviceName = "service-order";

    public static final String path_send_creditorinfo = "/doit/sendCreditorinfo";
    //借款人用户是否有在投
    public static final String path_have_investIng = "/doit/haveInvesting";
    //还款
    public static final String path_userRepay = "/doit/userRepay";
    //查理财用户是否有在贷
    public static final String path_isLoaning = "/doit/isLoaning";
    //向doit推送订单状态
    public static final String path_pushOrderStatusToDoit = "/doit/pushOrderStatusToDoit";
    //查询散标状态
    public static final String path_select_Scatterstandard = "/doit/selectScatterstandard";


    public static final String path_addAllToCart = "/shoppingCart/addAll";
    public static final String path_getShoppingCartList = "/shoppingCart/getList";
    public static final String path_deleteFullForShoppingCart = "/shoppingCart/deleteFull";
    public static final String path_deleteOneGoodsForShoppingCart = "/shoppingCart/deleteOne";
    public static final String path_addListGoodsForShoppingCart = "/shoppingCart/addList";
    public static final String path_updateGoodsAmount = "/shoppingCart/updateAmount";
    public static final String path_resetCart = "/shoppingCart/resetCart";


    public static final String path_selectUsrOrderList = "/order/selectUsrOrderList";//查询用户订单记录
//    public static final String path_selectUsrOrderSize = "/order/selectUsrOrderSize";//查询用户订单笔数
//    public static final String path_orderListByPage = "/order/orderListByPage";     //分页查询用户理财记录


    public static final String path_orderSubmit = "/order/orderSubmit";     //提交订单
    public static final String path_orderPay = "/order/orderPay";     //订单支付
    public static final String path_checkPayPWD = "/order/checkPayPWD";
    public static final String path_successOrder = "/order/successOrder";     //支付成功订单
    public static final String path_failOrder = "/order/failOrder";     //支付失败订单
    public static final String path_expireOrder = "/order/expireOrder";     //失效订单(机构投资人未填写密码)

    public static final String path_selectOrderDetail = "/order/selectOrderDetail";


    //查询散标列表
    public static final String path_select_ScatterstandardList = "/scatterStandard/selectScatterstandardList";
    //查询散标详情
    public static final String path_select_scatterstandard_by_id = "/scatterStandard/selectScatterstandardById";

    public static final String path_selectLoanHistory = "/scatterStandard/selectLoanHistory";//查询历史借款记录

    //定时任务放款
    public static final String path_batchLoan = "/scatterStandard/batchLoan";
    public static final String path_batchLoanWating = "/scatterStandard/batchLoanWating";
    public static final String path_handleBreachDisburse = "/scatterStandard/handleBreachDisburse";      //机构投资人清分收入处理定时任务
    //散标还款成功
    public static final String path_repaySuccess = "/scatterStandard/repaySuccess";
    public static final String path_repayFail = "/scatterStandard/repayFail";
    public static final String path_serviceFeeSuccess = "/scatterStandard/serviceFeeSuccess";

    public static final String path_getRepayRate = "/order/getRepayRate";//查询回款率

    //更新还款计划的还款状态
    public static final String path_updateStatus = "/repaymentPlan/updateStatus";
    //存入还款计划
    public static final String path_addRepaymentPlan = "/doit/repaymentPlan/addRepaymentPlan";
    //查看还款计划
    public static final String path_findRepaymentPlanList = "/doit/repaymentPlan/findRepaymentPlanList";
}