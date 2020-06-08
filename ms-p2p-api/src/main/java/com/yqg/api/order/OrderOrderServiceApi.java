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

    public static final String path_send_creditorinfo = "/api-order/doit/sendCreditorinfo";
    //借款人用户是否有在投
    public static final String path_have_investIng = "/api-order/doit/haveInvesting";
    //还款
    public static final String path_userRepay = "/api-order/doit/userRepay";
    //查理财用户是否有在贷
    public static final String path_isLoaning = "/api-order/doit/isLoaning";
    //向doit推送订单状态
    public static final String path_pushOrderStatusToDoit = "/api-order/doit/pushOrderStatusToDoit";
    //查询散标状态
    public static final String path_select_Scatterstandard = "/api-order/doit/selectScatterstandard";


    public static final String path_addAllToCart = "/api-order/shoppingCart/addAll";
    public static final String path_getShoppingCartList = "/api-order/shoppingCart/getList";
    public static final String path_deleteFullForShoppingCart = "/api-order/shoppingCart/deleteFull";
    public static final String path_deleteOneGoodsForShoppingCart = "/api-order/shoppingCart/deleteOne";
    public static final String path_addListGoodsForShoppingCart = "/api-order/shoppingCart/addList";
    public static final String path_updateGoodsAmount = "/api-order/shoppingCart/updateAmount";
    public static final String path_resetCart = "/api-order/shoppingCart/resetCart";


    public static final String path_selectUsrOrderList = "/api-order/order/selectUsrOrderList";//查询用户订单记录
//    public static final String path_selectUsrOrderSize = "/api-order/order/selectUsrOrderSize";//查询用户订单笔数
    public static final String path_orderListByPage = "/api-order/order/orderListByPage";     //分页查询用户理财记录


    public static final String path_orderSubmit = "/api-order/order/orderSubmit";     //提交订单
    public static final String path_orderPay = "/api-order/order/orderPay";     //订单支付
    public static final String path_checkPayPWD = "/api-order/order/checkPayPWD";
    public static final String path_successOrder = "/api-order/order/successOrder";     //支付成功订单
    public static final String path_failOrder = "/api-order/order/failOrder";     //支付失败订单
    public static final String path_expireOrder = "/api-order/order/expireOrder";     //失效订单(机构投资人未填写密码)

    public static final String path_selectOrderDetail = "/api-order/order/selectOrderDetail";


    //查询散标列表
    public static final String path_select_ScatterstandardList = "/api-order/scatterStandard/selectScatterstandardList";
    //查询散标详情
    public static final String path_select_scatterstandard_by_id = "/api-order/scatterStandard/selectScatterstandardById";

    public static final String path_selectLoanHistory = "/api-order/scatterStandard/selectLoanHistory";//查询历史借款记录

    //定时任务放款
    public static final String path_batchLoan = "/api-order/scatterStandard/batchLoan";
    public static final String path_batchLoanWating = "/api-order/scatterStandard/batchLoanWating";
    public static final String path_handleBreachDisburse = "/api-order/scatterStandard/handleBreachDisburse";      //机构投资人清分收入处理定时任务
    //散标还款成功
    public static final String path_repaySuccess = "/api-order/scatterStandard/repaySuccess";
    public static final String path_repayFail = "/api-order/scatterStandard/repayFail";
    public static final String path_serviceFeeSuccess = "/api-order/scatterStandard/serviceFeeSuccess";

    public static final String path_getRepayRate = "/api-order/order/getRepayRate";//查询回款率

    //更新还款计划的还款状态
    public static final String path_updateStatus = "/api-order/repaymentPlan/updateStatus";
    //存入还款计划
    public static final String path_addRepaymentPlan = "/api-order/doit/repaymentPlan/addRepaymentPlan";
    //查看还款计划
    public static final String path_findRepaymentPlanList = "/api-order/doit/repaymentPlan/findRepaymentPlanList";
}