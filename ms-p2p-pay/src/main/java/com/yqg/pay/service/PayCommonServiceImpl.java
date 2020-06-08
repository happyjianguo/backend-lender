package com.yqg.pay.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yqg.api.pay.income.ro.IncomeRo;
import com.yqg.api.system.systhirdlogs.bo.SysThirdLogsBo;
import com.yqg.api.user.enums.UserTypeEnum;
import com.yqg.api.user.useraccount.bo.UserAccountBo;
import com.yqg.api.user.useruser.bo.UserBo;
import com.yqg.api.user.useruser.ro.UserTypeSearchRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.enums.PayAccountStatusEnum;
import com.yqg.common.enums.TransTypeEnum;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.redis.PayParamContants;
import com.yqg.common.redis.RedisUtil;
import com.yqg.common.utils.JsonUtils;
import com.yqg.pay.config.InComeConfig;
import com.yqg.pay.config.PaymentConfig;
import com.yqg.pay.entity.PayAccountHistory;
import com.yqg.pay.service.payaccounthistory.PayAccountHistoryService;
import com.yqg.pay.service.third.*;
import com.yqg.pay.util.CustomRestTemplate;
import com.yqg.pay.util.MapUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;

//import com.yqg.pay.enums.TransTypeEnum;

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
    protected InComeConfig inComeConfig;
    @Autowired
    protected SysParamService sysParamService;

    @Autowired
    protected PayAccountHistoryService payAccountHistoryService;
    @Autowired
    protected SysThirdLogsService sysThirdLogsService;
//    @Autowired
//    protected RestTemplateUtil restTemplateUtil;

    @Autowired
    protected UserBankService userBankService;

    @Autowired
    protected UserAccountService userAccountService;

    @Autowired
    protected PaymentConfig paymentConfig;
//    @Autowired
//    protected OrderScatterStandardRelService orderScatterStandardRelService;

    @Autowired
    protected SysDictService sysDictService;
//    @Autowired
//    protected OrderOrderDao orderOrderDao;
//    protected static String doItLoanUrl = "";

//    @Value("${doItLoan.url}")
//    public void setDoItLoanUrl(String doItLoanUrl) {
//        doItLoanUrl = doItLoanUrl;
//    }

    @Autowired
    protected CustomRestTemplate customRestTemplate;

    @Autowired
    @Qualifier(value = "remoteRestTemplate")
    protected RestTemplate remoteRestTemplate;



    protected void addThirdLog(SysThirdLogsBo sysThirdLogsBo) throws BusinessException {
        try {
//            sysThirdLogsService.addThirdLog(sysThirdLogsBo);
        } catch (Exception e) {
            log.error("请求日志记录失败!" + e.getMessage());
        }

    }

    /**
     * 获取托管账户
     *
     * @return
     * @throws BusinessException
     */
    public UserAccountBo getEscrowAccount(String bankType) throws BusinessException {
        UserTypeSearchRo userTypeSearchRo = new UserTypeSearchRo();
        userTypeSearchRo.setUserType(UserTypeEnum.ESCROW_ACCOUNT.getType());
        userTypeSearchRo.setBankType(bankType);
        BaseResponse<UserAccountBo> baseResponse = this.userService.userListByType(userTypeSearchRo);
        if (!baseResponse.isSuccess() || baseResponse.getCode() != 0) {
            throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
        }
        UserAccountBo userBankBo = baseResponse.getData();
        if (userBankBo == null) {
            throw new BusinessException(BaseExceptionEnums.CPPCHA_ERROR.setCustomMessage("获取托管账户异常"));
        }
        log.info("获取托管账户信息:{}", JsonUtils.serialize(userBankBo));
        return userBankBo;
    }

    /**
     * 获取利息账户
     *
     * @return
     * @throws BusinessException
     */
    public UserAccountBo getInterestAccount() throws BusinessException {
        UserTypeSearchRo userTypeSearchRo = new UserTypeSearchRo();
        userTypeSearchRo.setUserType(UserTypeEnum.INCOME_ACCOUNT.getType());
        BaseResponse<UserAccountBo> baseResponse = this.userService.userListByType(userTypeSearchRo);
        if (!baseResponse.isSuccess() || baseResponse.getCode() != 0) {
            throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
        }
        UserAccountBo userBankBo = baseResponse.getData();
        if (userBankBo == null) {
            throw new BusinessException(BaseExceptionEnums.CPPCHA_ERROR.setCustomMessage("获取利息账户异常"));
        }
        log.info("获取利息账户信息:{}", JsonUtils.serialize(userBankBo));
        return userBankBo;
    }

    /**
     * 获取收入账户
     *
     * @return
     * @throws BusinessException
     */
    public UserBo getIncomeAccount() throws BusinessException {
//        UserTypeSearchRo userTypeSearchRo = new UserTypeSearchRo();
//        userTypeSearchRo.setUserType(SupperUserTypeEnum.INCOME_ACCOUNT.getType());
//        BaseResponse<List<UserBo>> baseResponse = this.userService.userListByType(userTypeSearchRo);
//        if (!baseResponse.isSuccess() || baseResponse.getCode() != 0) {
//            throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
//        }
//        List<UserBo> userBankBo = baseResponse.getData();
//        if (CollectionUtils.isEmpty(userBankBo)) {
//            throw new BusinessException(BaseExceptionEnums.CPPCHA_ERROR.setCustomMessage("获取收入账户异常"));
//        }
//        log.info("获取收入账户信息:{}", JsonUtils.serialize(userBankBo.get(0)));
//        return userBankBo.get(0);
        return null;
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
        payAccountHistory.setTradeType(tradeType.getDisburseType());
        payAccountHistory.setFromUserId(fromUserId);
        payAccountHistory.setToUserId(toUserId);
        payAccountHistory.setRemark(tradeType.getName());//备注
        payAccountHistoryService.addOne(payAccountHistory);
        return tradeNo;
    }

    /**
     * 新增流水
     *
     * @param incomeRo
     * @return
     * @throws BusinessException
     */
    protected String addPayAccountHistory(IncomeRo incomeRo) throws BusinessException {
        PayAccountHistory payAccountHistory = new PayAccountHistory();
        payAccountHistory.setOrderNo(incomeRo.getOrderNo());
        payAccountHistory.setStatus(PayAccountStatusEnum.WATING.getType());
        PayAccountHistory his = payAccountHistoryService.findOne(payAccountHistory);
        if(his==null) {//订单号/债权编号
            payAccountHistory.setTradeNo(incomeRo.getExternalId());
            payAccountHistory.setAmount(incomeRo.getDepositAmount());
            payAccountHistory.setFee(new BigDecimal("0"));
            payAccountHistory.setTradeType(incomeRo.getDepositType().getDisburseType());
            payAccountHistory.setFromUserId(incomeRo.getCustomerUserId());
            payAccountHistory.setToUserId(incomeRo.getToUserId());
            payAccountHistory.setRemark(incomeRo.getDepositType().getName());//备注
            payAccountHistory.setPaymentCode(incomeRo.getPaymentCode());
            payAccountHistory.setPaychannel(incomeRo.getDepositMethod());
            payAccountHistoryService.addOne(payAccountHistory);
        }
        return incomeRo.getExternalId();
    }
//    /**
//     * 新增流水
//     *
//     * @param amount
//     * @param fromUserId
//     * @param toUserId
//     * @param orderNo
//     * @return
//     * @throws BusinessException
//     */
//    protected String addPayAccountHistory(String tradeNo, BigDecimal amount, String fromUserId, String toUserId, String orderNo, TransTypeEnum tradeType,Integer status) throws BusinessException {
//        PayAccountHistory payAccountHistory = new PayAccountHistory();
//        payAccountHistory.setOrderNo(orderNo);//订单号/债权编号
//        payAccountHistory.setTradeNo(tradeNo);
//        payAccountHistory.setAmount(amount);
//        payAccountHistory.setFee(new BigDecimal("0"));
//        payAccountHistory.setStatus(status);
//        payAccountHistory.setTradeType(tradeType.getType());
//        payAccountHistory.setFromUserId(fromUserId);
//        payAccountHistory.setToUserId(toUserId);
//        payAccountHistory.setRemark(tradeType.getName());//备注
//        payAccountHistoryService.addOne(payAccountHistory);
//        return tradeNo;
//    }


    protected JSONObject executeHttpGetRequest(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "*/*");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("X-AUTH-TOKEN", inComeConfig.payToken);
        logger.info("get查询URL------" + url);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(null, headers);
        JSONObject jsonObject = customRestTemplate.getForObject(url, headers, JSONObject.class, "");

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
        logger.info("请求参数------url:" + url + ", param:" + sb.toString());
        LinkedMultiValueMap params = JSON.parseObject(sb.toString(), LinkedMultiValueMap.class);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
        JSONObject jsonObject = this.remoteRestTemplate.postForObject(url, entity, JSONObject.class);
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
        JSONObject jsonObject = customRestTemplate.getForObject(url, headers, JSONObject.class, "");
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
        for (String key : keys) {
            sb.append(key).append("=");
            if (objectMap.get(key) instanceof TransTypeEnum) {
                sb.append(((TransTypeEnum) objectMap.get(key)).getDisburseType());
            } else {
                sb.append(objectMap.get(key));
            }
            sb.append("&");
        }
        sb.setLength(sb.length() - 1);
        logger.info("请求参数---------" + sb.toString());
        String urlStr = url + "?" + sb.toString();
        logger.info("请求URL------" + urlStr);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(new LinkedMultiValueMap<String, String>(), headers);
        ResponseEntity<String> response = this.remoteRestTemplate.exchange(urlStr, HttpMethod.POST, entity, String.class);
        JSONObject jsonObject = JSONObject.parseObject(response.getBody());
        logger.info("请求结果返回----------" + jsonObject.toJSONString());
        return jsonObject;
    }

}
