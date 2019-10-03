package com.yqg.api.order.orderorder;

/**
 * 债权人的基本信息表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 10:40:02
 */
public class OrderOrderServiceApi {
    public static final String serviceName = "service-order";
    public static final String path_have_investIng = "/order/haveInvesting";//借款人用户是否有在投
    public static final String path_userRepay = "/order/userRepay";//还款
    public static final String path_queryLenderInfo = "/order/queryLenderInfo";//查询借款人信息
    public static final String path_isLoaning = "/order/isLoaning";//查理财用户是否有在贷
    public static final String path_pushOrderStatusToDoit = "/order/pushOrderStatusToDoit";//向doit推送订单状态
    public static final String path_selectUsrOrderList = "/order/selectUsrOrderList";//查询用户订单记录
    public static final String path_selectUsrOrderSize = "/order/selectUsrOrderSize";//查询用户订单笔数

    public static final String path_orderListByPage = "/order/orderListByPage";     //分页查询用户理财记录
    public static final String path_getRepayRate = "/order/getRepayRate";//查理回款率
}