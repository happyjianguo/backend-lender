package com.yqg.pay.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yqg.api.pay.exception.PayExceptionEnums;
import com.yqg.api.system.systhirdlogs.bo.SysThirdLogsBo;
import com.yqg.api.user.useruser.bo.UserBo;
import com.yqg.api.user.useruser.ro.UserReq;
import com.yqg.api.user.useruser.ro.UserTypeSearchRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.httpclient.RestTemplateUtil;
import com.yqg.common.redis.RedisUtil;
import com.yqg.common.utils.DateUtils;
import com.yqg.common.utils.JsonUtils;
import com.yqg.order.dao.OrderOrderDao;
import com.yqg.order.entity.Creditorinfo;
import com.yqg.order.entity.OrderOrder;
import com.yqg.order.entity.Productconf;
import com.yqg.order.entity.Scatterstandard;
import com.yqg.order.enums.OrderStatusEnums;
import com.yqg.order.enums.ProductTypeEnums;
import com.yqg.order.service.creditorinfo.CreditorinfoService;
import com.yqg.order.service.creditorpackage.CreditorpackageService;
import com.yqg.order.service.orderorder.OrderOrderService;
import com.yqg.order.service.orderpackagerel.OrderpackagerelService;
import com.yqg.order.service.productconf.ProductconfService;
import com.yqg.order.service.scatterstandard.ScatterstandardService;
import com.yqg.pay.config.DoitConfig;
import com.yqg.pay.config.InComeConfig;
import com.yqg.pay.config.PaymentConfig;
import com.yqg.pay.constants.PayParamContants;
import com.yqg.pay.entity.PayAccountHistory;
import com.yqg.pay.enums.PayAccountStatusEnum;
import com.yqg.pay.enums.SupperUserTypeEnum;
import com.yqg.pay.enums.TransTypeEnum;
import com.yqg.pay.service.payaccounthistory.PayAccountHistoryService;
import com.yqg.pay.service.third.*;
import com.yqg.pay.util.CustomRestTemplate;
import com.yqg.pay.util.MapUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;

/**
 * 公共服务注入类
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:20:30
 */
@Component
@Slf4j
public abstract class PayCommonServiceImpl {

    /**
     * 日志组件
     */
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 缓存工具
     */
    @Autowired
    protected RedisUtil redisUtil;

    @Autowired
    protected UserService userService;
    @Autowired
    protected PayParamContants payParamContants;
    @Autowired
    protected DoitConfig doitConfig;
    @Autowired
    protected InComeConfig inComeConfig;
    @Autowired
    protected SysParamService sysParamService;
    @Autowired
    protected OrderOrderService orderOrderService;
    @Autowired
    protected CreditorinfoService creditorinfoService;
    @Autowired
    protected PayAccountHistoryService payAccountHistoryService;
    @Autowired
    protected SysThirdLogsService sysThirdLogsService;
    @Autowired
    protected RestTemplateUtil restTemplateUtil;
    @Autowired
    protected ScatterstandardService scatterstandardService;
    @Autowired
    protected UserBankService userBankService;

    @Autowired
    protected UserAccountService userAccountService;

    @Autowired
    protected PaymentConfig paymentConfig;
    @Autowired
    protected ProductconfService productconfService;
    @Autowired
    protected CreditorpackageService creditorpackageService;
    @Autowired
    protected OrderpackagerelService orderPackageRelService;
    @Autowired
    protected SysDictService sysDictService;
    @Autowired
    protected OrderOrderDao orderOrderDao;
    protected static String doItLoanUrl = "";

    @Value("${doItLoan.url}")
    public void setDoItLoanUrl(String doItLoanUrl) {
        doItLoanUrl = doItLoanUrl;
    }

    @Autowired
    @Qualifier(value = "remoteRestTemplate")
    protected RestTemplate remoteRestTemplate;

    @Autowired
    protected CustomRestTemplate customRestTemplate;

    @Bean(name = "remoteRestTemplate")
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    protected void addThirdLog(SysThirdLogsBo sysThirdLogsBo) throws BusinessException {
        try {
//            sysThirdLogsService.addThirdLog(sysThirdLogsBo);
        } catch (Exception e) {
            log.error("请求日志记录失败!"+e.getMessage());
        }

    }

    /**
     * 获取超级投资人账户
     *
     * @return
     * @throws BusinessException
     */
    public UserBo getSuperAccount() throws BusinessException {
        UserTypeSearchRo userTypeSearchRo = new UserTypeSearchRo();
        userTypeSearchRo.setUserType(SupperUserTypeEnum.SUPPER_INVESTORS.getType());
        BaseResponse<List<UserBo>> baseResponse = this.userService.userListByType(userTypeSearchRo);
        if (!baseResponse.isSuccess() || baseResponse.getCode() != 0) {
            throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
        }
        List<UserBo> userBankBo = baseResponse.getData();
        if (CollectionUtils.isEmpty(userBankBo)) {
            throw new BusinessException(BaseExceptionEnums.CPPCHA_ERROR.setCustomMessage("获取超级投资人账户异常"));
        }
        // 多个超级投资人
        int number = new Random().nextInt(userBankBo.size());
        log.info("获取超级投资人账户信息:{}", JsonUtils.serialize(userBankBo.get(number)));
        return userBankBo.get(number);
    }
    /**
     * 获取托管账户
     *
     * @return
     * @throws BusinessException
     */
    public UserBo getEscrowAccount() throws BusinessException {
        UserTypeSearchRo userTypeSearchRo = new UserTypeSearchRo();
        userTypeSearchRo.setUserType(SupperUserTypeEnum.ESCROW_ACCOUNT.getType());
        BaseResponse<List<UserBo>> baseResponse = this.userService.userListByType(userTypeSearchRo);
        if (!baseResponse.isSuccess() || baseResponse.getCode() != 0) {
            throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
        }
        List<UserBo> userBankBo = baseResponse.getData();
        if (CollectionUtils.isEmpty(userBankBo)) {
            throw new BusinessException(BaseExceptionEnums.CPPCHA_ERROR.setCustomMessage("获取托管账户异常"));
        }
        log.info("获取托管账户信息:{}", JsonUtils.serialize(userBankBo.get(0)));
        return userBankBo.get(0);
    }
    /**
     * 获取利息账户
     *
     * @return
     * @throws BusinessException
     */
    public UserBo getInterestAccount() throws BusinessException {
        UserTypeSearchRo userTypeSearchRo = new UserTypeSearchRo();
        userTypeSearchRo.setUserType(SupperUserTypeEnum.INTEREST_ACCOUNT.getType());
        BaseResponse<List<UserBo>> baseResponse = this.userService.userListByType(userTypeSearchRo);
        if (!baseResponse.isSuccess() || baseResponse.getCode() != 0) {
            throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
        }
        List<UserBo> userBankBo = baseResponse.getData();
        if (CollectionUtils.isEmpty(userBankBo)) {
            throw new BusinessException(BaseExceptionEnums.CPPCHA_ERROR.setCustomMessage("获取利息账户异常"));
        }
        log.info("获取利息账户信息:{}", JsonUtils.serialize(userBankBo.get(0)));
        return userBankBo.get(0);
    }

    /**
     * 获取收入账户
     *
     * @return
     * @throws BusinessException
     */
    public UserBo getIncomeAccount() throws BusinessException {
        UserTypeSearchRo userTypeSearchRo = new UserTypeSearchRo();
        userTypeSearchRo.setUserType(SupperUserTypeEnum.INCOME_ACCOUNT.getType());
        BaseResponse<List<UserBo>> baseResponse = this.userService.userListByType(userTypeSearchRo);
        if (!baseResponse.isSuccess() || baseResponse.getCode() != 0) {
            throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
        }
        List<UserBo> userBankBo = baseResponse.getData();
        if (CollectionUtils.isEmpty(userBankBo)) {
            throw new BusinessException(BaseExceptionEnums.CPPCHA_ERROR.setCustomMessage("获取收入账户异常"));
        }
        log.info("获取收入账户信息:{}", JsonUtils.serialize(userBankBo.get(0)));
        return userBankBo.get(0);
    }

    /**
     * 添加订单
     *
     * @param amount
     * @param userUuid
     * @param creditorNo
     * @param yearRateFin
     * @return
     * @throws BusinessException
     */
    protected String addOrder(BigDecimal amount, String userUuid, String creditorNo, BigDecimal yearRateFin, ProductTypeEnums productTypeEnums,Integer orderType) throws BusinessException {
        OrderOrder orderOrder = new OrderOrder();
        orderOrder.setCreditorNo(creditorNo);
        orderOrder.setUserUuid(userUuid);
        orderOrder.setType(orderType);
        UserReq userReq = new UserReq();
        userReq.setUserUuid(userUuid);
        BaseResponse<UserBo> user = userService.findUserById(userReq);
        log.info(user + "user--------------------");
        orderOrder.setStatus(OrderStatusEnums.INVESTMENTING.getCode());
        //复投 改为投资成功
        if (orderType==1){
            orderOrder.setStatus(OrderStatusEnums.INVESTMEN_SUCCESS.getCode());
        }
        if (productTypeEnums.getCode()==ProductTypeEnums.SCATTER_STANDARD.getCode()) {
            Creditorinfo creditorinfo = new Creditorinfo();
            creditorinfo.setCreditorNo(creditorNo);
            creditorinfo = creditorinfoService.findOne(creditorinfo);
            orderOrder.setTerm(creditorinfo.getTerm());
            orderOrder.setYearRate(creditorinfo.getBorrowerYearRate());
        }else if (productTypeEnums.getCode()==ProductTypeEnums.FINANCING.getCode()) {
            Productconf productconf = new Productconf();
            productconf.setId(creditorNo);
            productconf = productconfService.findOne(productconf);
            orderOrder.setTerm(productconf.getBorrowingTerm());
        }
        if (user != null && null != user.getData()) {
            if (user.getData().getUserType().equals(SupperUserTypeEnum.SUPPER_INVESTORS.getType())) {
                //超级投资人为预充值 所以 直接投资成功
//                改为匹配成功
//                orderOrder.setStatus(OrderStatusEnums.MATCH_SUCCESS.getCode());
                orderOrder.setStatus(OrderStatusEnums.INVESTMEN_SUCCESS.getCode());
                orderOrder.setDueTime(DateUtils.addDate(new Date(),orderOrder.getTerm()));
            }
        }
        orderOrder.setAmountBuy(amount);

        orderOrder.setProductType(productTypeEnums.getCode());

        orderOrder.setYearRateFin(yearRateFin);
        //利息 取百位
        BigDecimal income = amount.multiply(orderOrder.getYearRateFin()).multiply(new BigDecimal(orderOrder.getTerm())).divide(new BigDecimal(360), 4);
        income= new BigDecimal(income.setScale(-2, BigDecimal.ROUND_DOWN).intValue());
        orderOrder.setIncome(income);
        orderOrder.setBuyTime(new Date());
        orderOrder.setIncomeTime(new Date());
        orderOrder.setProductType(productTypeEnums.getCode());

        return orderOrderService.addOne(orderOrder);
    }





    /**
     * 债转添加订单
     * */
    protected String addZhaiZhuanOrder(BigDecimal amount, String userUuid, String creditorNo, ProductTypeEnums productTypeEnums) throws BusinessException {
        OrderOrder orderOrder = new OrderOrder();
        orderOrder.setCreditorNo(creditorNo);
        orderOrder.setUserUuid(userUuid);
        orderOrder.setType(2);
        orderOrder.setStatus(OrderStatusEnums.MATCH_SUCCESS.getCode());
        //复投 改为投资成功
        orderOrder.setAmountBuy(amount);

        orderOrder.setProductType(productTypeEnums.getCode());

        orderOrder.setYearRateFin(new BigDecimal("0.00"));
        orderOrder.setIncome(new BigDecimal("0.00"));
        orderOrder.setBuyTime(new Date());
        orderOrder.setIncomeTime(new Date());
        orderOrder.setProductType(productTypeEnums.getCode());

        return orderOrderService.addOne(orderOrder);
    }


    /**
     * 新增流水
     *
     * @param amount
     * @param fromUserId
     * @param toUserId
     * @param orderNo
     * @return
     * @throws BusinessException
     */
    protected String addPayAccountHistory(String tradeNo, BigDecimal amount, String fromUserId, String toUserId, String orderNo, TransTypeEnum tradeType) throws BusinessException {
        PayAccountHistory payAccountHistory = new PayAccountHistory();
        payAccountHistory.setOrderNo(orderNo);//订单号/债权编号
        payAccountHistory.setTradeNo(tradeNo);
        payAccountHistory.setAmount(amount);
        payAccountHistory.setFee(new BigDecimal("0"));
        payAccountHistory.setStatus(PayAccountStatusEnum.WATING.getType());
        payAccountHistory.setTradeType(tradeType.getType());
        payAccountHistory.setFromUserId(fromUserId);
        payAccountHistory.setToUserId(toUserId);
        payAccountHistory.setRemark(tradeType.getName());//备注
        payAccountHistoryService.addOne(payAccountHistory);
        return tradeNo;
    }
    /**
     * 新增流水
     *
     * @param amount
     * @param fromUserId
     * @param toUserId
     * @param orderNo
     * @return
     * @throws BusinessException
     */
    protected String addPayAccountHistory(String tradeNo, BigDecimal amount, String fromUserId, String toUserId, String orderNo, TransTypeEnum tradeType,Integer status) throws BusinessException {
        PayAccountHistory payAccountHistory = new PayAccountHistory();
        payAccountHistory.setOrderNo(orderNo);//订单号/债权编号
        payAccountHistory.setTradeNo(tradeNo);
        payAccountHistory.setAmount(amount);
        payAccountHistory.setFee(new BigDecimal("0"));
        payAccountHistory.setStatus(status);
        payAccountHistory.setTradeType(tradeType.getType());
        payAccountHistory.setFromUserId(fromUserId);
        payAccountHistory.setToUserId(toUserId);
        payAccountHistory.setRemark(tradeType.getName());//备注
        payAccountHistoryService.addOne(payAccountHistory);
        return tradeNo;
    }

    /**
     * 锁定散标金额
     *
     * @param amount
     * @param creditorNo
     * @return
     * @throws BusinessException
     */
    public Scatterstandard lockAmount(BigDecimal amount, String creditorNo) throws BusinessException {
        Scatterstandard scatterstandard = new Scatterstandard();
        if (redisUtil.tryLock(payParamContants.LOCK_SCATTERSTANDARD_CREDITORNO + creditorNo, 10)) {
            scatterstandard.setCreditorNo(creditorNo);
            scatterstandard = scatterstandardService.findOne(scatterstandard);
            //散标表锁定份额 暂定30分钟
            scatterstandard.setAmountLock(scatterstandard.getAmountLock().add(amount));
            //购买金额大于当前可购买金额
            if (scatterstandard.getAmountBuy().add(scatterstandard.getAmountLock()).compareTo(scatterstandard.getAmountApply()) == 1) {
                redisUtil.releaseLock(payParamContants.LOCK_SCATTERSTANDARD_CREDITORNO + creditorNo);
                throw new BusinessException(PayExceptionEnums.BUY_OVER_CURRENT);
            }
            scatterstandardService.updateOne(scatterstandard);
            redisUtil.releaseLock(payParamContants.LOCK_SCATTERSTANDARD_CREDITORNO + creditorNo);
        } else {
            log.info("锁定散标金额---获取锁失败{}", payParamContants.LOCK_SCATTERSTANDARD_CREDITORNO + creditorNo);
            throw new BusinessException(BaseExceptionEnums.SYSTERM_ERROR);
        }
        return scatterstandard;
    }

    protected JSONObject executeHttpGetRequest(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "*/*");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("X-AUTH-TOKEN", inComeConfig.payToken);
        logger.info("get查询URL------" + url);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(null,headers);
        JSONObject  jsonObject = customRestTemplate.getForObject(url,headers, JSONObject.class,"");

//        log.info(jsonObjectStr.toJSONString());
//        JSONObject jsonObject = remoteRestTemplate.getForObject(url, JSONObject.class,entity);
        logger.info("get查询结果----------" + jsonObject.toJSONString());
        return jsonObject;
    }

    protected JSONObject executeHttpPostRequest(String url, Object param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "*/*");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("X-AUTH-TOKEN", inComeConfig.payToken);
        String jsonPost = JSONObject.toJSONString(param);
        JSONObject object = JSONObject.parseObject(jsonPost);
        Iterator it = object.keySet().iterator();
        StringBuilder sb = new StringBuilder("{");
        while (it.hasNext()) {
            String key = (String) it.next();
            Object value = object.getString(key);
            if (value == null) {
                sb.append(JSON.toJSONString(key)).append(":").append(value).append(",");
            } else {
                List<String> list = Collections.singletonList(value.toString());
                sb.append(JSON.toJSONString(key)).append(":").append(JSON.toJSONString(list)).append(",");
            }
        }
        sb.append("}");
        sb.deleteCharAt(sb.length() - 2);
        logger.info("请求参数------url:"+url+", param:" + sb.toString());
        LinkedMultiValueMap params = JSON.parseObject(sb.toString(), LinkedMultiValueMap.class);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
        JSONObject jsonObject = remoteRestTemplate.postForObject(url, entity, JSONObject.class);
        logger.info("请求结果----------" + jsonObject.toJSONString());
        return jsonObject;
    }

    protected JSONObject executeHttpGet(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "*/*");
        headers.set("X-AUTH-TOKEN", inComeConfig.payToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        logger.info("get查询URL------" + url);
//        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(new LinkedMultiValueMap<String, String>(), headers);
//        ResponseEntity<String> response = remoteRestTemplate.exchange(url, HttpMethod.GET, requestEntity,String.class);
//        JSONObject jsonObject=JSONObject.parseObject(response.getBody());
        JSONObject  jsonObject = customRestTemplate.getForObject(url,headers, JSONObject.class,"");
        logger.info("get查询结果----------" + jsonObject.toJSONString());
        return jsonObject;
    }

    protected JSONObject executeHttpPost(String url, Object param) throws IllegalAccessException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "*/*");
        headers.set("X-AUTH-TOKEN", inComeConfig.payToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        Map<String, Object> objectMap = MapUtil.objectToMap(param);
        StringBuilder sb = new StringBuilder();
        List<String> keys = new ArrayList<String>(objectMap.keySet());
        Collections.sort(keys);
        for(String key : keys){
            sb.append(key).append("=");
            sb.append(objectMap.get(key));
            sb.append("&");
        }
        sb.setLength(sb.length() - 1);
        logger.info("请求参数---------" + sb.toString());
        String urlStr=url+"?"+sb.toString();
        logger.info("请求URL------" + urlStr);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(new LinkedMultiValueMap<String, String>(), headers);
        ResponseEntity<String> response = remoteRestTemplate.exchange(urlStr, HttpMethod.POST, entity,String.class);
        JSONObject jsonObject=JSONObject.parseObject(response.getBody());
        logger.info("请求结果返回----------" + jsonObject.toJSONString());
        return jsonObject;
    }

}
