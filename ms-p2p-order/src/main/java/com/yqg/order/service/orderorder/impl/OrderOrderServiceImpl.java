package com.yqg.order.service.orderorder.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yqg.api.order.orderorder.bo.OrderOrderBo;
import com.yqg.api.order.orderorder.bo.OrderPageListBo;
import com.yqg.api.order.orderorder.bo.OrderSizeBo;
import com.yqg.api.order.orderorder.ro.ManOrderPageRo;
import com.yqg.api.order.orderorder.ro.OrderOrderPageRo;
import com.yqg.api.order.scatterstandard.bo.ScatterstandardDetailBo;
import com.yqg.api.pay.exception.PayExceptionEnums;
import com.yqg.api.pay.payaccounthistory.ro.PayAccountHistoryRo;
import com.yqg.api.user.enums.UserTypeEnum;
import com.yqg.api.user.userbank.bo.UserBankBo;
import com.yqg.api.user.userbank.ro.UserBankRo;
import com.yqg.api.user.useruser.bo.LenderUsrBo;
import com.yqg.api.user.useruser.bo.UserBo;
import com.yqg.api.user.useruser.ro.LenderUsrRo;
import com.yqg.api.user.useruser.ro.UserReq;
import com.yqg.api.user.useruser.ro.UserRo;
import com.yqg.common.core.response.BasePageResponse;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.dao.ExtendQueryCondition;
import com.yqg.common.enums.OrderExceptionEnums;
import com.yqg.common.enums.OrderStatusEnums;
import com.yqg.common.enums.RedisKeyEnums;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.httpclient.RestTemplateUtil;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.common.utils.DESUtils;
import com.yqg.common.utils.DateUtils;
import com.yqg.common.utils.JsonUtils;
import com.yqg.order.dao.OrderOrderDao;
import com.yqg.order.entity.Creditorinfo;
import com.yqg.order.entity.OrderOrder;
import com.yqg.order.entity.OrderScatterStandardRel;
import com.yqg.order.entity.Scatterstandard;
import com.yqg.order.service.OrderCommonServiceImpl;
import com.yqg.order.service.creditorinfo.CreditorinfoService;
import com.yqg.order.service.orderorder.OrderOrderService;
import com.yqg.order.service.scatterstandard.ScatterstandardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * 债权人的基本信息表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 10:40:02
 */
@Slf4j
@Service("orderOrderService")
public class OrderOrderServiceImpl extends OrderCommonServiceImpl implements OrderOrderService {
    @Autowired
    protected OrderOrderDao orderorderDao;
    @Autowired
    @Qualifier(value = "remoteRestTemplate")
    protected RestTemplate remoteRestTemplate;
    @Autowired
    protected RestTemplateUtil restTemplateUtil;

    @Autowired
    protected ScatterstandardService scatterstandardService;
    @Autowired
    protected CreditorinfoService creditorinfoService;

    @Transactional
    public String initOrder(String userUuid) throws BusinessException {
        OrderOrder orderOrder = new OrderOrder();
        orderOrder.setUserUuid(userUuid);
        orderOrder.setStatus(OrderStatusEnums.INVESTMENTING.getCode());
        orderOrder.setBuyTime(new Date());
        return orderorderDao.addOne(orderOrder);

    }

    @Override
    public void pushOrderStatusToDoit(Scatterstandard sca) {
        // TODO: 2019/5/21  pushOrderStatusToDoit

        int status = sca.getStatus();
        try {
            switch (status) {
                case 5:
                    status = 4;
                    break;
                case 6:
                    status = 3;
                    break;
                case 8:
                    status = 6;
                    break;
                case 12:
                    status = 2;//流标 状态为放款中
                    break;
            }
            Map map = new HashMap();
            map.put("creditorNo", sca.getCreditorNo());
            map.put("status", status);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(map), headers);
            log.info("发送推送订单状态" + JSONObject.toJSONString(map));
            JSONObject jsonObject = remoteRestTemplate.postForObject(doitConfig.getDoItLoanUrl() + "/p2p/getOrderStatus", entity, JSONObject.class);
            log.info("返回推送订单状态" + jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询用户订单记录
     */
    @Override
    public BasePageResponse<OrderOrderBo> selectUsrOrderList(OrderOrderPageRo orderOrderPageRo) throws BusinessException {
        log.info("orderOrderPageRo:[{}]", orderOrderPageRo);
        Page<OrderOrder> orderPage = null;
        OrderOrder entity = new OrderOrder();
        ExtendQueryCondition extendQueryCondition = new ExtendQueryCondition();
        entity.setDisabled(0);
        if (!orderOrderPageRo.getIsAdmin()) {
            entity.setUserUuid(orderOrderPageRo.getUserId());
        }
        if (orderOrderPageRo.getStatus() != null && orderOrderPageRo.getStatus() != 0) {
            entity.setStatus(orderOrderPageRo.getStatus());
        }
        if (orderOrderPageRo.getPayStatus() != null && orderOrderPageRo.getPayStatus() != 0) {
            if (orderOrderPageRo.getPayStatus() == 1) {
                //支付中
                entity.setStatus(1);
            } else if (orderOrderPageRo.getPayStatus() == 2) {
                //支付成功
                List<Object> list = new ArrayList<>();
                list.add(2);
                list.add(3);
                extendQueryCondition.addInQueryMap(OrderOrder.status_field, list);
            } else if (orderOrderPageRo.getPayStatus() == 3) {
                //支付失败
                entity.setStatus(9);
            }

        }
        //  姓名手机号 支付区间 
        UserReq userReq = new UserReq();
        if (!StringUtils.isEmpty(orderOrderPageRo.getUserName()) || !StringUtils.isEmpty(orderOrderPageRo.getMobile())) {
            if (!StringUtils.isEmpty(orderOrderPageRo.getUserName())) {
                userReq.setRealName(orderOrderPageRo.getUserName());
            }
            if (!StringUtils.isEmpty(orderOrderPageRo.getMobile())) {
                userReq.setMobileNumber(orderOrderPageRo.getMobile());
            }
            BaseResponse<UserBo> userBoBaseResponse = userService.findOneByMobileOrName(userReq);
            if (userBoBaseResponse.isSuccess()) {
                String userId = userBoBaseResponse.getData().getId();
                //0默认普通用户 3.超级投资人 4.机构投资人
                if (StringUtils.isEmpty(userId) || !"034".contains(userId)) {
                    userId = "EMPTY";
                }
                entity.setUserUuid(userId);
            }
        }


        Map<ExtendQueryCondition.CompareType, Object> map = new HashMap<>();
        Date startDate = null;
        Date endDate = null;
        try {
            if (!StringUtils.isEmpty(orderOrderPageRo.getStartTime())) {
                startDate = DateUtils.stringToDate(orderOrderPageRo.getStartTime());
                map.put(ExtendQueryCondition.CompareType.GTE_TIME, startDate);
            }
            if (!StringUtils.isEmpty(orderOrderPageRo.getEndTime())) {
                endDate = DateUtils.stringToDate(orderOrderPageRo.getEndTime());
                map.put(ExtendQueryCondition.CompareType.LTE_TIME, endDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        extendQueryCondition.addCompareQueryMap(OrderOrder.createTime_Field, map);


//        if (orderOrderPageRo.getFlag() != null) {
//
//            Date startDate = null;
//            Date endDate = null;
//            PageRequest pageRequest = new PageRequest(0, 5);
//
////            try {
////
////                if (orderOrderPageRo.getFlag() == 0) {
////                    startDate = DateUtils.stringToDate(orderOrderPageRo.getStartTime());
////                    endDate = DateUtils.stringToDate(orderOrderPageRo.getEndTime());
////                } else if (orderOrderPageRo.getFlag() == 2) {
////                    startDate = DateUtils.stringToDate(DateUtils.DateToString(new Date()) + " 00:00:00");
////                    endDate = new Date();
////                } else if (orderOrderPageRo.getFlag() == 3) {
////                    startDate = DateUtils.addDate(new Date(), -30);
////                    endDate = new Date();
////                } else if (orderOrderPageRo.getFlag() == 4) {
////                    startDate = DateUtils.addDate(new Date(), -90);
////                    endDate = new Date();
////                }
////
////
////                if (orderOrderPageRo.getFlag() != 1) {
////                    Map<ExtendQueryCondition.CompareType, Object> map = new HashMap<>();
////                    map.put(ExtendQueryCondition.CompareType.GTE_TIME, startDate);
////                    map.put(ExtendQueryCondition.CompareType.LTE_TIME, endDate);
////                    extendQueryCondition.addCompareQueryMap(OrderOrder.createTime_Field, map);
////                }
////
////
////                entity.setExtendQueryCondition(extendQueryCondition);
////
////                orderPage = orderorderDao.findForPage(entity, orderOrderPageRo.convertPageRequest());
////
//////                if (orderOrderPageRo.getFlag() == 1) {
//////                    orderPage = orderorderDao.findForPage(entity, orderOrderPageRo.convertPageRequest());
//////                } else if (orderOrderPageRo.getStatus() != null && orderOrderPageRo.getProductType() != null) {
//////                    orderPage = orderorderDao.findByUserUuid("111", orderOrderPageRo.getProductType(), orderOrderPageRo.getStatus(), startDate, endDate, pageRequest);
//////                } else if (orderOrderPageRo.getStatus() != null && orderOrderPageRo.getProductType() == null) {
//////                    orderPage = orderorderDao.findByStatus("111", orderOrderPageRo.getStatus(), startDate, endDate, pageRequest);
//////                } else if (orderOrderPageRo.getStatus() == null && orderOrderPageRo.getProductType() != null) {
//////                    orderPage = orderorderDao.findByProductType("111", orderOrderPageRo.getProductType(), startDate, endDate, pageRequest);
//////                } else {
//////                    orderPage = orderorderDao.findByUserUuid("111", startDate, endDate, pageRequest);
//////                }
////
////
////            } catch (ParseException e) {
////                e.printStackTrace();
////            }
////        } else {
        entity.setExtendQueryCondition(extendQueryCondition);
        orderPage = orderorderDao.findForPage(entity, orderOrderPageRo.convertPageRequest());

//        }


        List<OrderOrderBo> OrderOrderBoList = new ArrayList<>();
        for (OrderOrder order : orderPage) {
            OrderOrderBo orderBo = new OrderOrderBo();

            orderBo.setApplyBuy(order.getApplyBuy());
            orderBo.setChargeBuy(order.getChargeBuy());
            orderBo.setCreateTime(DateUtils.DateToString2(order.getCreateTime()));
            if (null != order.getIncomeTime()) {
                orderBo.setBuyTime(DateUtils.DateToString2(order.getIncomeTime()));
            }
            orderBo.setAmountBuy(order.getAmountBuy());
            orderBo.setStatus(order.getStatus());
            orderBo.setId(order.getId());

            if (orderOrderPageRo.getIsAdmin()) {
                BaseResponse<UserBo> userBoBaseResponse = userService.findOneByMobileOrName(userReq);
                if (userBoBaseResponse.isSuccess()) {
                    orderBo.setUserName(userBoBaseResponse.getData().getUserName());
                }
            }

            OrderOrderBoList.add(orderBo);
        }
        BasePageResponse<OrderOrderBo> orderVOs = new BasePageResponse<>(orderPage);
        orderVOs.setContent(OrderOrderBoList);

        return orderVOs;
    }

    @Override
    public BasePageResponse<ScatterstandardDetailBo> selectOrderDetail(OrderOrderPageRo orderOrderPageRo) throws BusinessException {
//        OrderOrder order = this.findById(orderOrderPageRo.getOrderNo());
        Page<OrderScatterStandardRel> orderScatterStandardRelPage = null;
        OrderScatterStandardRel entity = new OrderScatterStandardRel();
        ExtendQueryCondition extendQueryCondition = new ExtendQueryCondition();
        entity.setDisabled(0);
        entity.setOrderNo(orderOrderPageRo.getOrderNo());
        entity.setExtendQueryCondition(extendQueryCondition);

        orderScatterStandardRelPage = orderScatterStandardRelService.findForPage(entity, orderOrderPageRo.convertPageRequest());


        List<ScatterstandardDetailBo> scatterstandardDetailBoList = new ArrayList<>();
        for (OrderScatterStandardRel rel : orderScatterStandardRelPage) {
            ScatterstandardDetailBo scatterstandardDetailBo = new ScatterstandardDetailBo();
            scatterstandardDetailBo.setCreditorNo(rel.getCreditorNo());
            scatterstandardDetailBo.setAmountBuy(rel.getAmount());
//            scatterstandardDetailBo.setOrderStatus(order.getStatus());

            Scatterstandard scatterstandard = new Scatterstandard();
            scatterstandard.setCreditorNo(rel.getCreditorNo());
            scatterstandard = scatterstandardService.findOne(scatterstandard);

            scatterstandardDetailBo.setAmountApply(scatterstandard.getAmountApply());
            scatterstandardDetailBo.setYearRateFin(scatterstandard.getYearRateFin());
            scatterstandardDetailBo.setStatus(scatterstandard.getStatus());

            Creditorinfo creditorinfo = creditorinfoService.findByCreditorNo(rel.getCreditorNo());
            scatterstandardDetailBo.setRealName(creditorinfo.getName());


            scatterstandardDetailBoList.add(scatterstandardDetailBo);
        }

        BasePageResponse<ScatterstandardDetailBo> scatterstandardBos = new BasePageResponse<>(orderScatterStandardRelPage);
        scatterstandardBos.setContent(scatterstandardDetailBoList);

        return scatterstandardBos;
    }

    @Override
    public OrderSizeBo selectOrderSize(OrderOrderPageRo orderOrderPageRo) throws BusinessException {

        OrderSizeBo orderSizeBo = new OrderSizeBo();

        OrderOrder entity = new OrderOrder();
        ExtendQueryCondition extendQueryCondition = new ExtendQueryCondition();
        entity.setDisabled(0);
        entity.setUserUuid(orderOrderPageRo.getUserId());
        //投资中
        List<Object> list = new ArrayList<>();
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        extendQueryCondition.addInQueryMap(OrderOrder.status_field, list);

        entity.setExtendQueryCondition(extendQueryCondition);

        orderSizeBo.setSize(this.findList(entity).size());


        return orderSizeBo;
    }


    @Override
    public LenderUsrBo isLoaning(UserRo ro) throws BusinessException {
        LenderUsrBo usr = new LenderUsrBo();
//        ro.setUserId(ro.getUserId());
//        UserReq userReq = new UserReq();
//        userReq.setUserUuid(ro.getUserId());
//        UserBo userBo = restTemplateUtil.callPostService(
//                UserServiceApi.serviceName, UserServiceApi.path_findUserById, userReq, UserBo.class);


        UserReq userReq = new UserReq();
        userReq.setUserUuid(ro.getUserId());
        BaseResponse<UserBo> userBo = userService.findUserById(userReq);
        if (userBo == null || null == userBo.getData()) {
            throw new BusinessException(OrderExceptionEnums.USER_NOT_EXIST);
        } else {
            log.info("当前操作用户:[{}]", userBo.getData());
        }

        UserBo userBo1 = new UserBo();
        userBo1.setIdCardNo(userBo.getData().getIdCardNo());
        userBo1.setMobileNumber(DESUtils.decrypt(userBo.getData().getMobileNumberDES()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(userBo1), headers);
        logger.info("发送查询投资人是否有在贷" + JSONObject.toJSONString(userBo1));
        log.info("------------{}----------" + doitConfig.getDoItLoanUrl());
        JSONObject jsonObject = remoteRestTemplate.postForObject(doitConfig.getDoItLoanUrl() + "/p2p/checkUserIsHaveLoan", entity, JSONObject.class);
        log.info("返回查询投资人是否有在贷" + jsonObject);
        if (StringUtils.isEmpty(jsonObject.getString("code")) || !jsonObject.getString("code").equals("0")) {
            throw new BusinessException(BaseExceptionEnums.SYSTERM_ERROR);
        }
        String s = JsonUtils.serialize(jsonObject.get("data"));
        usr.setIsExist(JsonUtils.deserialize(s, LinkedHashMap.class).get("isExit") + "");
        return usr;
    }

    @Override
    public JSONObject orderPay(String orderNo) throws Exception {
        JSONObject jsonObject = new JSONObject();
        OrderOrder order = this.findById(orderNo);
        if (order.getStatus().equals(OrderStatusEnums.INVESTMENTING.getCode())) {
            if (order.getChargeBuy().compareTo(new BigDecimal("0.0")) > 0) {
                log.info("需要充值购买");
                UserReq userReq = new UserReq();
                userReq.setUserUuid(order.getUserUuid());
                BaseResponse<UserBo> user = userService.findUserById(userReq);
                if (user == null || null == user.getData()) {
                    log.info("无此用户");
                    throw new BusinessException(PayExceptionEnums.ACCOUNT_ERROR);
                } else {
                    log.info("当前操作用户:[{}]", user.getData());
                }
                if (user.getData().getUserType().equals(UserTypeEnum.SUPPER_INVESTORS.getType())) {
                    log.info("超级投资人账户");
                    jsonObject.put("needPwd", false);
                    jsonObject.put("amountPay", order.getAmountBuy());
                    jsonObject.put("orderAmount", order.getApplyBuy());
                    jsonObject.put("needPay", order.getChargeBuy());
                    jsonObject.put("orderNo", orderNo);
                    UserBankRo userBankRo = new UserBankRo();
                    userBankRo.setUserUuid(order.getUserUuid());
                    BaseResponse<UserBankBo> userBankBoBaseResponse = userBankService.getUserBankInfo(userBankRo);
                    String bankCode = userBankBoBaseResponse.getData().getBankCode();
                    jsonObject.put("bankCode", bankCode);
                    PayAccountHistoryRo payAccountHistoryRo = new PayAccountHistoryRo();
                    payAccountHistoryRo.setOrderNo(orderNo);
                    jsonObject.put("paymentCode", payService.paymentCodeByOrderNo(payAccountHistoryRo));
                    jsonObject.put("timestamp", redisUtil.get(RedisKeyEnums.PAY_EXPRIRESECONDS.appendToDefaultKey(orderNo)));
                } else if (user.getData().getUserType().equals(UserTypeEnum.BRANCH_INVESTORS.getType())) {
                    log.info("机构投资人账户");
                    // TODO: 2019/6/28 0不代扣 1 代扣
                    if (user.getData().getWithholding()==1) {
                        jsonObject.put("needPwd", true);
                        jsonObject.put("amountPay", order.getAmountBuy());
                        jsonObject.put("orderAmount", order.getApplyBuy());
                        jsonObject.put("needPay", order.getChargeBuy());
                        jsonObject.put("orderNo", orderNo);
                    }else if (user.getData().getWithholding()==0) {
                        jsonObject.put("needPwd", false);
                        jsonObject.put("amountPay", order.getAmountBuy());
                        jsonObject.put("orderAmount", order.getApplyBuy());
                        jsonObject.put("needPay", order.getChargeBuy());
                        jsonObject.put("orderNo", orderNo);
                        UserBankRo userBankRo = new UserBankRo();
                        userBankRo.setUserUuid(order.getUserUuid());
                        BaseResponse<UserBankBo> userBankBoBaseResponse = userBankService.getUserBankInfo(userBankRo);
                        String bankCode = userBankBoBaseResponse.getData().getBankCode();
                        jsonObject.put("bankCode", bankCode);
                        PayAccountHistoryRo payAccountHistoryRo = new PayAccountHistoryRo();
                        payAccountHistoryRo.setOrderNo(orderNo);
                        jsonObject.put("paymentCode", payService.paymentCodeByOrderNo(payAccountHistoryRo));
                        jsonObject.put("timestamp", redisUtil.get(RedisKeyEnums.PAY_EXPRIRESECONDS.appendToDefaultKey(orderNo)));

                    }


                } else {
                    log.info("普通投资人 暂无法购买");
                    throw new BusinessException(PayExceptionEnums.BUY_OVER_CURRENT);
                }

            } else {
                if (order.getAmountBuy().compareTo(new BigDecimal("0.0")) > 0) {

                    log.info("输入密码");
                    jsonObject.put("needPwd", true);
                    jsonObject.put("needPay", order.getAmountBuy());
                    jsonObject.put("orderNo", orderNo);

                }
            }
        }

        return jsonObject;
    }


    @Override
    public LenderUsrBo haveInvestIng(LenderUsrRo ro) throws BusinessException {
        //根据手机号和身份证号查询用户是否有在投
        UserReq userReq = new UserReq();
        userReq.setMobileNumber(ro.getMobileNumber());
        BaseResponse<UserBo> userBo = userService.findOneByMobileOrId(userReq);
        logger.info("查询用户服务结果:{}", JSON.toJSONString(userBo));
        LenderUsrBo lenderUserBo = new LenderUsrBo();
        //1.投资处理中 2.投资失败 3,投资成功  4.过期 5.回款成功 6.回款失败 7.回款处理中
        OrderOrder order = new OrderOrder();
        order.setUserUuid(userBo.getData().getId());
        ExtendQueryCondition extendQueryCondition = new ExtendQueryCondition();
        List list = new ArrayList();
        list.add(1);
        list.add(3);
        extendQueryCondition.addInQueryMap(OrderOrder.status_field, list);//in查询
        order.setExtendQueryCondition(extendQueryCondition);
        List<OrderOrder> orderList = this.findList(order);

        if (CollectionUtils.isEmpty(orderList)) {
            lenderUserBo.setIsExist("0");
        } else {
            lenderUserBo.setIsExist("1");
        }
        //   }else{
        lenderUserBo.setIsExist("0");
        //    }

        return lenderUserBo;
    }


    @Override
    public OrderOrder findById(String id) throws BusinessException {
        return this.orderorderDao.findOneById(id, new OrderOrder());
    }

    @Override
    public <E> E findById(String id, Class<E> boClass) throws BusinessException {
        OrderOrder orderorder = findById(id);
        E bo = BeanCoypUtil.copyToNewObject(orderorder, boClass);
        return bo;
    }

    @Override
    public OrderOrder findOne(OrderOrder entity) throws BusinessException {
        return this.orderorderDao.findOne(entity);
    }

    @Override
    public <E> E findOne(OrderOrder entity, Class<E> boClass) throws BusinessException {
        OrderOrder orderorder = findOne(entity);
        E bo = BeanCoypUtil.copyToNewObject(orderorder, boClass);
        return bo;
    }


    @Override
    public List<OrderOrder> findList(OrderOrder entity) throws BusinessException {
        return this.orderorderDao.findForList(entity);
    }

    @Override
    public <E> List<E> findList(OrderOrder entity, Class<E> boClass) throws BusinessException {
        List<OrderOrder> orderorderList = findList(entity);
        List<E> boList = new ArrayList<>();
        for (OrderOrder orderorder : orderorderList) {
            E bo = BeanCoypUtil.copyToNewObject(orderorder, boClass);
            boList.add(bo);
        }
        return boList;
    }

    @Override
    @Transactional
    public String addOne(OrderOrder entity) throws BusinessException {
        return orderorderDao.addOne(entity);
    }

    @Override
    @Transactional
    public void deleteById(String id) throws BusinessException {
        orderorderDao.deleteOne(findById(id));
    }

    @Override
    @Transactional
    public void deleteOne(OrderOrder entity) throws BusinessException {
        this.orderorderDao.deleteOne(entity);
    }

    @Override
    @Transactional
    public void updateOne(OrderOrder entity) throws BusinessException {
        this.orderorderDao.updateOne(entity);
    }

    @Override
    public BasePageResponse<OrderPageListBo> orderListByPage(ManOrderPageRo ro) throws BusinessException, ParseException {
        String buyTimeMin = ro.getBuyTimeMin();
        String buyTimeMax = ro.getBuyTimeMax();
        String dueTimeMin = ro.getDueTimeMin();
        String dueTimeMax = ro.getDueTimeMax();
        String mobile = ro.getMobile();

        ExtendQueryCondition extendQueryCondition = new ExtendQueryCondition();

        OrderOrder entity = new OrderOrder();
        Map<ExtendQueryCondition.CompareType, Object> buyTimeMap = new HashMap<>();     //购买时间
        if (!StringUtils.isEmpty(buyTimeMax)) {
            buyTimeMap.put(ExtendQueryCondition.CompareType.LTE_TIME, DateUtils.stringToDate(buyTimeMax + " 23:59:59"));
        }
        if (!StringUtils.isEmpty(buyTimeMin)) {
            buyTimeMap.put(ExtendQueryCondition.CompareType.GTE_TIME, DateUtils.stringToDate(buyTimeMin + " 00:00:00"));
        }
        if (!StringUtils.isEmpty(buyTimeMax) || !StringUtils.isEmpty(buyTimeMin)) {
            extendQueryCondition.addCompareQueryMap(OrderOrder.buyTime_field, buyTimeMap);
            entity.setExtendQueryCondition(extendQueryCondition);
        }

        Map<ExtendQueryCondition.CompareType, Object> dueTimeMap = new HashMap<>();       //到期日
        if (!StringUtils.isEmpty(dueTimeMax)) {
            dueTimeMap.put(ExtendQueryCondition.CompareType.LTE_TIME, DateUtils.stringToDate(dueTimeMax + " 23:59:59"));
        }
        if (!StringUtils.isEmpty(dueTimeMin)) {
            dueTimeMap.put(ExtendQueryCondition.CompareType.GTE_TIME, DateUtils.stringToDate(dueTimeMin + " 00:00:00"));
        }
        if (!StringUtils.isEmpty(dueTimeMax) || !StringUtils.isEmpty(dueTimeMin)) {
//            extendQueryCondition.addCompareQueryMap(OrderOrder.dueTime_field, dueTimeMap);
            entity.setExtendQueryCondition(extendQueryCondition);
        }

        if (!StringUtils.isEmpty(ro.getId())) {
            entity.setId(ro.getId());
        }


        if (!StringUtils.isEmpty(ro.getYearRateFin())) {          //年化收益率
//            entity.setYearRateFin(new BigDecimal(ro.getYearRateFin()));
        }

        if (!StringUtils.isEmpty(mobile)) {       //理财人手机号
            UserReq search = new UserReq();
            search.setMobileNumber(mobile);
//            BaseResponse<UserBo> result = this.userService.findOneByMobileOrId(search);
//            if (!result.isSuccess() || result.getCode() != 0) {
//                throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
//            }
//            UserBo userResult = result.getData();
//            if(userResult != null){
//                entity.setUserUuid(userResult.getId());
//            }
        }

        ro.setSortProperty("sort");
        ro.setSortDirection(Sort.Direction.DESC);

        Page<OrderOrder> orderResult = this.orderorderDao.findForPage(entity, ro.convertPageRequest());

        BasePageResponse<OrderPageListBo> response = new BasePageResponse<>(orderResult);
        List<OrderPageListBo> pageListBos = new ArrayList<>();

        List<OrderOrder> orderList = orderResult.getContent();
        for (OrderOrder cell : orderList) {
            OrderPageListBo item = new OrderPageListBo();
            BeanUtils.copyProperties(cell, item);

            UserReq search = new UserReq();
            search.setUserUuid(cell.getUserUuid());
//            BaseResponse<UserBo> result = this.userService.findOneByMobileOrId(search);     //查询用户手机号
//            if (!result.isSuccess() || result.getCode() != 0) {
//                throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
//            }
//            UserBo userResult = result.getData();
//            if(userResult != null){
//                item.setMobile(userResult.getMobileNumber());
//            }
            pageListBos.add(item);
        }
        response.setContent(pageListBos);

        return response;

    }

    @Override
    public LenderUsrBo getRepayRate() throws BusinessException {
        LenderUsrBo usrBo = new LenderUsrBo();
        logger.info("发送查询回款率");
        String jsonObject = remoteRestTemplate.getForObject(doitConfig.getDoItLoanUrl() + "/web/users/getRepayRate", String.class);
        logger.info("返回查询回款率" + jsonObject);
        usrBo.setIsExist(jsonObject);
        return usrBo;

    }


}