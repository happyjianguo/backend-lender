package com.yqg.order.service.orderorder.impl;

import com.alibaba.fastjson.JSONObject;
import com.yqg.api.order.orderorder.bo.OrderOrderBo;
import com.yqg.api.order.orderorder.bo.OrderPageListBo;
import com.yqg.api.order.orderorder.bo.OrderSizeBo;
import com.yqg.api.order.orderorder.ro.ManOrderPageRo;
import com.yqg.api.order.orderorder.ro.OrderOrderPageRo;
import com.yqg.api.user.useruser.bo.LenderUsrBo;
import com.yqg.api.user.useruser.bo.UserBo;
import com.yqg.api.user.useruser.ro.LenderUsrRo;
import com.yqg.api.user.useruser.ro.UserReq;
import com.yqg.api.user.useruser.ro.UserRo;
import com.yqg.common.core.response.BasePageResponse;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.dao.ExtendQueryCondition;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.common.utils.DateUtils;
import com.yqg.common.utils.JsonUtils;
import com.yqg.order.dao.OrderOrderDao;
import com.yqg.order.dao.OrderpackagerelDao;
import com.yqg.order.dao.ProductconfDao;
import com.yqg.order.entity.OrderOrder;
import com.yqg.order.entity.Orderpackagerel;
import com.yqg.order.entity.Productconf;
import com.yqg.order.entity.Scatterstandard;
import com.yqg.order.enums.OrderStatusEnums;
import com.yqg.order.enums.ProductTypeEnums;
import com.yqg.order.service.orderorder.OrderOrderService;
import com.yqg.pay.service.PayCommonServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
@Service("orderOrderService")
public class OrderOrderServiceImpl extends PayCommonServiceImpl implements OrderOrderService {
    @Autowired
    protected OrderOrderDao orderorderDao;
    @Autowired
    private OrderpackagerelDao orderpackagerelDao;
    @Autowired
    private ProductconfDao productconfDao;

    @Transactional
    public void createOrder(OrderOrder order) throws BusinessException{

        orderorderDao.addOne(order);
    }

    @Override
    public void pushOrderStatusToDoit(Scatterstandard sca) {
        //1.投标中 2.放款中 3.放款成功 4.放款失败 5.还款处理中 6.还款成功 7.还款失败 8流标
//5,6,8,12
        //1.投标中 2锁定中 3.满标  4.放款中 5.放款失败 6.放款成功(待还款)
        // 7.还款处理中 8.已还款 9.还款清分处理中  10.还款清分处理成功 11.还款清分处理失败 12.流标'
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
            logger.info("发送推送订单状态" + JSONObject.toJSONString(map));
            JSONObject jsonObject = remoteRestTemplate.postForObject(doitConfig.getDoItLoanUrl() + "/p2p/getOrderStatus", entity, JSONObject.class);
            logger.info("返回推送订单状态" + jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 查询用户订单记录
     */
    @Override
    public BasePageResponse<OrderOrderBo> selectUsrOrderList(OrderOrderPageRo orderOrderPageRo) throws BusinessException {

        Page<OrderOrder> orderPage = null;
        OrderOrder entity = new OrderOrder();
        ExtendQueryCondition extendQueryCondition = new ExtendQueryCondition();
        entity.setDisabled(0);
        entity.setUserUuid(orderOrderPageRo.getUserId());
        if (orderOrderPageRo.getProductType() != null) {
            entity.setProductType(orderOrderPageRo.getProductType());
            extendQueryCondition.addLikeQueyField(OrderOrder.productType_field);
        }
        if (orderOrderPageRo.getStatus() != null) {
            if(orderOrderPageRo.getStatus()==1){
                //投资中
                List<Object> list = new ArrayList<>();
                list.add(3);
                list.add(4);
                extendQueryCondition.addInQueryMap(OrderOrder.status_field,list);
            }else if(orderOrderPageRo.getStatus()==2){
                //已结束
                List<Object> list = new ArrayList<>();
                list.add(8);
                extendQueryCondition.addInQueryMap(OrderOrder.status_field,list);
            }else if(orderOrderPageRo.getStatus()==3){
                //待付款
                List<Object> list = new ArrayList<>();
                list.add(1);
                extendQueryCondition.addInQueryMap(OrderOrder.status_field,list);
            }

        }

        if (orderOrderPageRo.getFlag() != null) {

            Date startDate = null;
            Date endDate = null;
            PageRequest pageRequest = new PageRequest(0, 5);

            try {

                if (orderOrderPageRo.getFlag() == 0) {
                    startDate = DateUtils.stringToDate(orderOrderPageRo.getStartTime());
                    endDate = DateUtils.stringToDate(orderOrderPageRo.getEndTime());
                } else if (orderOrderPageRo.getFlag() == 2) {
                    startDate = DateUtils.stringToDate(DateUtils.DateToString(new Date()) + " 00:00:00");
                    endDate = new Date();
                } else if (orderOrderPageRo.getFlag() == 3) {
                    startDate = DateUtils.addDate(new Date(), -30);
                    endDate = new Date();
                } else if (orderOrderPageRo.getFlag() == 4) {
                    startDate = DateUtils.addDate(new Date(), -90);
                    endDate = new Date();
                }



                if(orderOrderPageRo.getFlag() != 1){
                    Map<ExtendQueryCondition.CompareType, Object> map = new HashMap<>();
                    map.put(ExtendQueryCondition.CompareType.GTE_TIME, startDate);
                    map.put(ExtendQueryCondition.CompareType.LTE_TIME, endDate);
                    extendQueryCondition.addCompareQueryMap(OrderOrder.createTime_Field, map);
                }


                entity.setExtendQueryCondition(extendQueryCondition);

                orderPage = orderorderDao.findForPage(entity,orderOrderPageRo.convertPageRequest());

//                if (orderOrderPageRo.getFlag() == 1) {
//                    orderPage = orderorderDao.findForPage(entity, orderOrderPageRo.convertPageRequest());
//                } else if (orderOrderPageRo.getStatus() != null && orderOrderPageRo.getProductType() != null) {
//                    orderPage = orderorderDao.findByUserUuid("111", orderOrderPageRo.getProductType(), orderOrderPageRo.getStatus(), startDate, endDate, pageRequest);
//                } else if (orderOrderPageRo.getStatus() != null && orderOrderPageRo.getProductType() == null) {
//                    orderPage = orderorderDao.findByStatus("111", orderOrderPageRo.getStatus(), startDate, endDate, pageRequest);
//                } else if (orderOrderPageRo.getStatus() == null && orderOrderPageRo.getProductType() != null) {
//                    orderPage = orderorderDao.findByProductType("111", orderOrderPageRo.getProductType(), startDate, endDate, pageRequest);
//                } else {
//                    orderPage = orderorderDao.findByUserUuid("111", startDate, endDate, pageRequest);
//                }


            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            entity.setExtendQueryCondition(extendQueryCondition);
            orderPage = orderorderDao.findForPage(entity, orderOrderPageRo.convertPageRequest());

        }


        List<OrderOrderBo> OrderOrderBoList = new ArrayList<>();
        for (OrderOrder order : orderPage) {
            OrderOrderBo orderBo = new OrderOrderBo();
            orderBo.setCreditorNo(order.getCreditorNo());
            orderBo.setCreateTime(DateUtils.DateToString2(order.getCreateTime()));
            orderBo.setProductType(order.getProductType());
            orderBo.setAmountBuy(order.getAmountBuy());
            orderBo.setIncome(order.getIncome());
            orderBo.setTerm(order.getTerm());
            orderBo.setYearRateFin(order.getYearRateFin());
            orderBo.setStatus(order.getStatus());
            orderBo.setId(order.getId());
            orderBo.setProductType(order.getProductType());

            OrderOrderBoList.add(orderBo);
        }
        BasePageResponse<OrderOrderBo> orderVOs = new BasePageResponse<>(orderPage);
        orderVOs.setContent(OrderOrderBoList);

        return orderVOs;
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
        extendQueryCondition.addInQueryMap(OrderOrder.status_field,list);

        entity.setExtendQueryCondition(extendQueryCondition);

        orderSizeBo.setSize(this.findList(entity).size());


        return orderSizeBo;
    }

    /**
     * 查询到期订单
     * */
    @Override
    public List<OrderOrder> selectDueTimeOrderList() throws BusinessException {


        try {
            OrderOrder entity = new OrderOrder();
            ExtendQueryCondition extendQueryCondition = new ExtendQueryCondition();
            entity.setDisabled(0);
            entity.setProductType(ProductTypeEnums.FINANCING.getCode());
            entity.setStatus(OrderStatusEnums.MATCH_SUCCESS.getCode());
            entity.setType(0);//查询首投的订单
            Map<ExtendQueryCondition.CompareType, Object> map = new HashMap<>();
            map.put(ExtendQueryCondition.CompareType.LTE_TIME, DateUtils.stringToDate(DateUtils.DateToString2(new Date())));
            extendQueryCondition.addCompareQueryMap(OrderOrder.dueTime_field, map);
            entity.setExtendQueryCondition(extendQueryCondition);
            return this.findList(entity);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }


    }

    @Override
    public LenderUsrBo getRepayRate() throws BusinessException{
        LenderUsrBo usrBo = new LenderUsrBo();
        logger.info("发送查询回款率");
        String jsonObject = remoteRestTemplate.getForObject(doitConfig.getDoItLoanUrl() + "/web/users/getRepayRate", String.class);
        logger.info("返回查询回款率" + jsonObject);
        usrBo.setIsExist(jsonObject);
        return usrBo;

    }

    @Override
    public LenderUsrBo isLoaning(UserRo ro) throws BusinessException {
        LenderUsrBo usr = new LenderUsrBo();
        ro.setUserId(ro.getUserId());

      /*  UserBo userBo= restTemplateUtil.callPostService(
                UserServiceApi.serviceName, UserServiceApi.path_findUserById, ro, UserBo.class);
        //LinkedHashMap resultMap = JsonUtils.deserialize(result, LinkedHashMap.class);
        if(userBo == null){
            throw new BusinessException(OrderExceptionEnums.USER_NOT_EXIST);
        }*/
        UserBo userBo1 = new UserBo();
        userBo1.setIdCardNo("12");
        //userBo1.setMobileNumber(DESUtils.decrypt("23") );
        userBo1.setMobileNumber("43");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(userBo1), headers);
        logger.info("发送查询投资人是否有在贷" + JSONObject.toJSONString(userBo1));
        JSONObject jsonObject = remoteRestTemplate.postForObject(doitConfig.getDoItLoanUrl() + "/p2p/checkUserIsHaveLoan", entity, JSONObject.class);
        logger.info("返回查询投资人是否有在贷" + jsonObject);
        /*if (StringUtils.isEmpty(jsonObject.getString("code")) || !jsonObject.getString("code").equals("0")) {
            throw new BusinessException(BaseExceptionEnums.SYSTERM_ERROR);
        }*/
        String s = JsonUtils.serialize(jsonObject.get("data"));
        usr.setIsExist(JsonUtils.deserialize(s, LinkedHashMap.class).get("isExit") + "");
        return usr;
    }

    @Override
    public JSONObject queryLenderInfo(String userUuid) {
        UserReq req = new UserReq();
        req.setUserUuid(userUuid);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(req), headers);
        logger.info("发送借款人信息" + JSONObject.toJSONString(JSONObject.toJSONString(req)));
        JSONObject jsonObject = remoteRestTemplate.postForObject(doitConfig.getDoItLoanUrl() + "/p2p/queryUserInfo", entity, JSONObject.class);
        logger.info("返回借款人信息" + JSONObject.toJSONString(jsonObject));
        return jsonObject;
    }


    @Override
    public LenderUsrBo haveInvestIng(LenderUsrRo ro) throws BusinessException {
        //根据手机号和身份证号查询用户是否有在投
     /*   UserBo userBo= restTemplateUtil.callPostService(
                UserServiceApi.serviceName, UserServiceApi.path_userIsExist, ro, UserBo.class);
        logger.info("查询用户服务结果:{}", JSON.toJSONString(userBo));*/
        LenderUsrBo lenderUserBo = new LenderUsrBo();
        //   if("1".equals(userBo.getIsExist())){
        //1.投资处理中 2.投资失败 3,投资成功  4.过期 5.回款成功 6.回款失败 7.回款处理中
        OrderOrder order = new OrderOrder();
        //         order.setUserUuid(userBo.getId());
        ExtendQueryCondition extendQueryCondition = new ExtendQueryCondition();
        List list = new ArrayList();
        list.add(1);
        list.add(3);
        list.add(7);
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
    public BasePageResponse<OrderPageListBo> orderListByPage(ManOrderPageRo ro) throws BusinessException,ParseException {
        String buyTimeMin = ro.getBuyTimeMin();
        String buyTimeMax = ro.getBuyTimeMax();
        String dueTimeMin = ro.getDueTimeMin();
        String dueTimeMax = ro.getDueTimeMax();
        String incomeTimeMin = ro.getIncomeTimeMin();
        String incomeTimeMax = ro.getIncomeTimeMax();
        String bondXatchingTimeMin = ro.getBondXatchingTimeMin();
        String bondXatchingTimeMax = ro.getBondXatchingTimeMax();
        String mobile = ro.getMobile();
        String realName = ro.getRealName();

        ExtendQueryCondition extendQueryCondition = new ExtendQueryCondition();

        OrderOrder entity = new OrderOrder();
        Map<ExtendQueryCondition.CompareType, Object> buyTimeMap = new HashMap<>();     //购买时间
        if(!StringUtils.isEmpty(buyTimeMax)){
            buyTimeMap.put(ExtendQueryCondition.CompareType.LTE_TIME, DateUtils.stringToDate(buyTimeMax + " 23:59:59"));
        }
        if(!StringUtils.isEmpty(buyTimeMin)){
            buyTimeMap.put(ExtendQueryCondition.CompareType.GTE_TIME, DateUtils.stringToDate(buyTimeMin + " 00:00:00"));
        }
        if(!StringUtils.isEmpty(buyTimeMax) || !StringUtils.isEmpty(buyTimeMin)){
            extendQueryCondition.addCompareQueryMap(OrderOrder.buyTime_field, buyTimeMap);
            entity.setExtendQueryCondition(extendQueryCondition);
        }

        Map<ExtendQueryCondition.CompareType, Object> dueTimeMap = new HashMap<>();       //到期日
        if(!StringUtils.isEmpty(dueTimeMax)){
            dueTimeMap.put(ExtendQueryCondition.CompareType.LTE_TIME, DateUtils.stringToDate(dueTimeMax + " 23:59:59"));
        }
        if(!StringUtils.isEmpty(dueTimeMin)){
            dueTimeMap.put(ExtendQueryCondition.CompareType.GTE_TIME, DateUtils.stringToDate(dueTimeMin + " 00:00:00"));
        }
        if(!StringUtils.isEmpty(dueTimeMax) || !StringUtils.isEmpty(dueTimeMin)){
            extendQueryCondition.addCompareQueryMap(OrderOrder.dueTime_field, dueTimeMap);
            entity.setExtendQueryCondition(extendQueryCondition);
        }


        Map<ExtendQueryCondition.CompareType, Object> incomeTimeMap = new HashMap<>();     //付款时间
        if(!StringUtils.isEmpty(incomeTimeMax)){
            incomeTimeMap.put(ExtendQueryCondition.CompareType.LTE_TIME, DateUtils.stringToDate(incomeTimeMax + " 23:59:59"));
        }
        if(!StringUtils.isEmpty(incomeTimeMin)){
            incomeTimeMap.put(ExtendQueryCondition.CompareType.GTE_TIME, DateUtils.stringToDate(incomeTimeMin + " 00:00:00"));
        }
        if(!StringUtils.isEmpty(incomeTimeMax) || !StringUtils.isEmpty(incomeTimeMin)){
            extendQueryCondition.addCompareQueryMap(OrderOrder.incomeTime_field, incomeTimeMap);
            entity.setExtendQueryCondition(extendQueryCondition);
        }


        if(!StringUtils.isEmpty(ro.getId())){
            entity.setId(ro.getId());
        }
        //状态
        if(!StringUtils.isEmpty(ro.getStatus())){
            entity.setStatus(ro.getStatus());
        }

        if(ro.getProductType() != null){        //产品类型
            entity.setProductType(ro.getProductType());
        }

        if(!StringUtils.isEmpty(ro.getYearRateFin())){          //年化收益率
            entity.setYearRateFin(new BigDecimal(ro.getYearRateFin()));
        }

        if(!StringUtils.isEmpty(mobile)){       //理财人手机号
            UserReq search = new UserReq();
            search.setMobileNumber(mobile);
            BaseResponse<UserBo> result = this.userService.findOneByMobileOrId(search);
            if (!result.isSuccess() || result.getCode() != 0) {
                throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
            }
            UserBo userResult = result.getData();
            if(userResult != null){
                entity.setUserUuid(userResult.getId());
            }
        }

        if(!StringUtils.isEmpty(realName)){       //理财人姓名
            UserReq search = new UserReq();
            search.setRealName(realName);
            BaseResponse<UserBo> result = this.userService.findOneByRealNameOrId(search);
            if (!result.isSuccess() || result.getCode() != 0) {
                throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
            }
            UserBo userResult = result.getData();
            if(userResult != null){
                entity.setUserUuid(userResult.getId());
            }
        }

        ro.setSortProperty("sort");
        ro.setSortDirection(Sort.Direction.DESC);
        entity.setType(0);
        entity.setProductType(3);
        Page<OrderOrder> orderResult = this.orderorderDao.findForPage(entity, ro.convertPageRequest());

        BasePageResponse<OrderPageListBo> response = new BasePageResponse<>(orderResult);
        List<OrderPageListBo> pageListBos = new ArrayList<>();

        List<OrderOrder> orderList = orderResult.getContent();
        for (OrderOrder cell:orderList){

            OrderPageListBo item = new OrderPageListBo();
            BeanUtils.copyProperties(cell,item);

            //处理订单状态： 待支付=投资处理中     待匹配=投资成功       待回款=匹配成功       回款完成=回款成功
            //(1.投资处理中 2.投资失败 3,投资成功 4匹配成功 5回款清分中 6.回款处理中 7.回款失败 8.回款成功 9.失效订单)
      /*      switch (cell.getStatus()){
                case 1:
                    item.setStatus("待支付");
                    break;
                case 2:
                    item.setStatus("投资失败");
                    break;
                case 3:
                    item.setStatus("待匹配");
                    break;
                case 4:
                    item.setStatus("待回款");
                    break;
                case 5:
                    item.setStatus("回款清分中");
                    break;
                case 6:
                    item.setStatus("回款处理中");
                    break;
                case 7:
                    item.setStatus("回款失败");
                    break;
                case 8:
                    item.setStatus("回款完成");
                    break;
                case 9:
                    item.setStatus("失效订单");
                    break;
                default:
                    item.setStatus("待支付");
                    break;
            }
*/

            UserReq search = new UserReq();
            search.setUserUuid(cell.getUserUuid());
            BaseResponse<UserBo> result = this.userService.findOneByMobileOrId(search);     //查询用户手机号
            if (!result.isSuccess() || result.getCode() != 0) {
                throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
            }
            UserBo userResult = result.getData();
            if(userResult != null){
                //用户真实姓名
                item.setRealName(userResult.getRealName());
                item.setMobile(userResult.getMobileNumber());
            }

            //债匹时间--查询订单与债权关系表的创建时间
            Orderpackagerel orderpackagerel = new Orderpackagerel();

            Map<ExtendQueryCondition.CompareType, Object> bondXatchingTimeMap = new HashMap<>();     //债券匹配时间
            if(!StringUtils.isEmpty(bondXatchingTimeMax)){
                bondXatchingTimeMap.put(ExtendQueryCondition.CompareType.LTE_TIME, DateUtils.stringToDate(bondXatchingTimeMax + " 23:59:59"));
            }
            if(!StringUtils.isEmpty(bondXatchingTimeMin)){
                bondXatchingTimeMap.put(ExtendQueryCondition.CompareType.GTE_TIME, DateUtils.stringToDate(bondXatchingTimeMin + " 00:00:00"));
            }
            if(!StringUtils.isEmpty(bondXatchingTimeMax) || !StringUtils.isEmpty(bondXatchingTimeMin)){
                extendQueryCondition.addCompareQueryMap(Orderpackagerel.createTime_Field, bondXatchingTimeMap);
                orderpackagerel.setExtendQueryCondition(extendQueryCondition);
            }


            orderpackagerel.setOrderNo(cell.getId());
            orderpackagerel.setDisabled(0);
            Page<Orderpackagerel> orderpackagerelList = orderpackagerelDao.findForPage(orderpackagerel,ro.convertPageRequest());
            List<Orderpackagerel> orderpackagerels = orderpackagerelList.getContent();
            if(!CollectionUtils.isEmpty(orderpackagerels)){
                item.setBondXatchingTime(orderpackagerels.get(0).getCreateTime());
            }else {
                response.setNumberOfElements(0);
                response.setTotalPages(0);
                response.setTotalElements(0);
                response.setContent(null);
                return response;
            }

            //产品类型--根据订单表里的产品id查询产品表
            Productconf productConf = new Productconf();
            productConf.setId(cell.getCreditorNo());
            productConf.setDisabled(0);
            List<Productconf> productconfList = productconfDao.findForList(productConf);
            if(!CollectionUtils.isEmpty(productconfList)){
                item.setBorrowingTerm(productconfList.get(0).getBorrowingTerm());
            }
            pageListBos.add(item);
        }
        response.setContent(pageListBos);

        return response;

    }


}