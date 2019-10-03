package com.yqg.pay.service.loan.impl;

import com.alibaba.fastjson.JSONObject;
import com.yqg.api.pay.loan.ro.LoanRo;
import com.yqg.api.pay.loan.vo.PaymentThirdVo;
import com.yqg.api.system.systhirdlogs.bo.SysThirdLogsBo;
import com.yqg.api.user.useraccount.bo.UserAccountBo;
import com.yqg.api.user.useraccount.ro.UseraccountRo;
import com.yqg.api.user.userbank.bo.UserBankBo;
import com.yqg.api.user.userbank.ro.UserBankRo;
import com.yqg.api.user.useruser.bo.UserBo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.dao.ExtendQueryCondition;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.DateUtils;
import com.yqg.common.utils.JsonUtils;
import com.yqg.order.entity.*;
import com.yqg.order.enums.OrderStatusEnums;
import com.yqg.order.enums.ProductTypeEnums;
import com.yqg.order.enums.ScatterStandardStatusEnums;
import com.yqg.order.service.creditorpackage.CreditorpackageService;
import com.yqg.order.service.orderorder.OrderOrderService;
import com.yqg.order.service.orderpackagerel.OrderpackagerelService;
import com.yqg.pay.entity.PayAccountHistory;
import com.yqg.pay.enums.*;
import com.yqg.pay.service.PayCommonServiceImpl;
import com.yqg.pay.service.loan.PayLoanService;
import com.yqg.pay.service.third.UserAccountService;
import com.yqg.pay.util.OrderNoCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * 打款逻辑
 * Created by zhaoruifeng on 2018/9/3.
 */
@Slf4j
@Service("payLoanService")
public class PayLoanServiceImpl extends PayCommonServiceImpl implements PayLoanService {

    @Autowired
    private OrderpackagerelService orderpackagerelService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private CreditorpackageService creditorpackageService;
    @Autowired
    private OrderOrderService orderOrderService;

    /**
     * 打款
     */
    @Transactional
    public void loan(LoanRo loanRo) throws BusinessException, IllegalAccessException {
        String transNo = TransPreFixTypeEnum.LOAN.getType() + OrderNoCreator.createOrderNo();//交易流水号
        PaymentThirdVo paymentThirdVo = new PaymentThirdVo();
        if (StringUtils.isEmpty(loanRo.getCreditorNo())) {
            paymentThirdVo.setExternalId(transNo);//交易流水号
        }else{
            paymentThirdVo.setExternalId(loanRo.getCreditorNo());//交易流水号
        }
        paymentThirdVo.setBankCode(loanRo.getBankCode());//银行code
        paymentThirdVo.setAccountNumber(loanRo.getBankNumberNo());//银行卡号
        paymentThirdVo.setAccountHolderName(loanRo.getCardholder());//持卡人
        paymentThirdVo.setAmount(loanRo.getAmount());
        paymentThirdVo.setDescription(loanRo.getDescription());//描述
        paymentThirdVo.setDisburseType(this.getEnumsNameById(loanRo.getTransType()));//交易类型
        paymentThirdVo.setDisburseChannel(paymentConfig.getDisburseChannel());//支付渠道
        log.info("打款前数据处理封装:{}", JsonUtils.serialize(loanRo));
        //初始化资金表为处理中
        this.addAccountHistory(paymentThirdVo, loanRo);
        //调用打款
        this.executeLoanRequst(paymentThirdVo, loanRo);
    }


    /**
     * 初始化资金记录
     *
     * @param paymentThirdVo
     * @param loanRo
     * @throws BusinessException
     */
    private void addAccountHistory(PaymentThirdVo paymentThirdVo, LoanRo loanRo) throws BusinessException {
        PayAccountHistory payAccountHistory = new PayAccountHistory();
        payAccountHistory.setOrderNo(loanRo.getCreditorNo());//债权/订单编号
        payAccountHistory.setTradeNo(paymentThirdVo.getExternalId());//交易流水号
        payAccountHistory.setAmount(paymentThirdVo.getAmount());//金额
        payAccountHistory.setStatus(PayAccountStatusEnum.WATING.getType());//支付状态
        payAccountHistory.setTradeType(loanRo.getTransType());//交易类型
        payAccountHistory.setFromUserId(loanRo.getFromUserId());//资金出方
        payAccountHistory.setToUserId(loanRo.getToUserId());//资金入方
        payAccountHistory.setRemark(paymentThirdVo.getDescription());//备注
        log.info("记录资金表数据:{}", JsonUtils.serialize(payAccountHistory));
        this.payAccountHistoryService.addOne(payAccountHistory);
    }

    /***
     * 打款请求入口
     *
     * @param paymentThirdVo
     * @return
     * @throws BusinessException
     */
    public JSONObject executeLoanRequst(PaymentThirdVo paymentThirdVo, LoanRo loanRo) throws BusinessException, IllegalAccessException {
        log.info("准备执行打款请求。。。。");
        try {
            //记录日志
            SysThirdLogsBo sysThirdLogsBo = new SysThirdLogsBo();
            sysThirdLogsBo.setUserUuid(loanRo.getToUserId());//借款人userId
            sysThirdLogsBo.setOrderNo(loanRo.getCreditorNo());//债权编号
            sysThirdLogsBo.setTransNo(paymentThirdVo.getExternalId());//交易流水号
            sysThirdLogsBo.setLogType(ThirdLogTypeEnum.LOAN_REQUEST.name());//日志类型
            sysThirdLogsBo.setTimeUsed((int) System.currentTimeMillis());//当前时间戳
            sysThirdLogsBo.setRequest(JsonUtils.serialize(paymentThirdVo));//请求参数
            sysThirdLogsBo.setResponse("");
            sysThirdLogsBo.setThirdType(ThirdLogTypeEnum.LOAN_REQUEST.getType());//支付服务
            this.addThirdLog(sysThirdLogsBo);
        } catch (Exception e) {
            log.error("记录请求日志异常!");
        }
        JSONObject jsonObject = this.executeHttpPost(paymentConfig.getDisburseUrl(), paymentThirdVo);
        try {
            //记录日志
            SysThirdLogsBo sysThirdLogsBo = new SysThirdLogsBo();
            sysThirdLogsBo.setUserUuid(loanRo.getToUserId());//借款人userId
            sysThirdLogsBo.setOrderNo(loanRo.getCreditorNo());//债权编号
            sysThirdLogsBo.setTransNo(paymentThirdVo.getExternalId());//交易流水号
            sysThirdLogsBo.setLogType(ThirdLogTypeEnum.LOAN_REQUEST.name());//日志类型
            sysThirdLogsBo.setTimeUsed((int) System.currentTimeMillis());//当前时间戳
            sysThirdLogsBo.setRequest("");
            sysThirdLogsBo.setResponse(jsonObject.toJSONString());
            sysThirdLogsBo.setThirdType(ThirdLogTypeEnum.LOAN_REQUEST.getType());//支付服务
            this.addThirdLog(sysThirdLogsBo);
        } catch (Exception e) {
            log.error("记录响应日志异常!");
        }
        if (StringUtils.isEmpty(jsonObject.getString("code")) || !jsonObject.getString("code").equals("0")) {
            throw new BusinessException(BaseExceptionEnums.SYSTERM_ERROR);
        }
        log.info("打款请求执行结束。。。。");
        return jsonObject;
    }


    /**
     * 打款查询
     *
     * @param
     * @return
     * @throws BusinessException
     */
    public JSONObject executeLoanQuery(PayAccountHistory payAccountHistory) throws BusinessException {
        log.info("查询准备执行打款请求。。。。");
        String url = paymentConfig.getDisburseQueryUrl() + payAccountHistory.getTradeNo();
        try {
            //记录日志
            SysThirdLogsBo sysThirdLogsBo = new SysThirdLogsBo();
            sysThirdLogsBo.setUserUuid(payAccountHistory.getToUserId());//借款人userId
            sysThirdLogsBo.setOrderNo(payAccountHistory.getOrderNo());//债权编号
            sysThirdLogsBo.setTransNo(payAccountHistory.getTradeNo());//交易流水号
            sysThirdLogsBo.setLogType(ThirdLogTypeEnum.LOAN_REQUEST_QUERY.name());//日志类型
            sysThirdLogsBo.setTimeUsed((int) System.currentTimeMillis());//当前时间戳
            sysThirdLogsBo.setRequest(url);//请求参数
            sysThirdLogsBo.setResponse("");
            sysThirdLogsBo.setThirdType(ThirdLogTypeEnum.LOAN_REQUEST_QUERY.getType());//支付服务
            this.addThirdLog(sysThirdLogsBo);
        } catch (Exception e) {
            log.error("记录请求日志异常!");
        }
        JSONObject jsonObject = executeHttpGet(url);
        try {
            //记录日志
            SysThirdLogsBo sysThirdLogsBo = new SysThirdLogsBo();
            sysThirdLogsBo.setUserUuid(payAccountHistory.getToUserId());//借款人userId
            sysThirdLogsBo.setOrderNo(payAccountHistory.getOrderNo());//债权编号
            sysThirdLogsBo.setTransNo(payAccountHistory.getTradeNo());//交易流水号
            sysThirdLogsBo.setLogType(ThirdLogTypeEnum.LOAN_REQUEST_QUERY.name());//日志类型
            sysThirdLogsBo.setTimeUsed((int) System.currentTimeMillis());//当前时间戳
            sysThirdLogsBo.setRequest("");
            sysThirdLogsBo.setResponse(jsonObject.toJSONString());
            sysThirdLogsBo.setThirdType(ThirdLogTypeEnum.LOAN_REQUEST_QUERY.getType());//支付服务
            this.addThirdLog(sysThirdLogsBo);
        } catch (Exception e) {
            log.error("记录响应日志异常!");
        }
        if (StringUtils.isEmpty(jsonObject.getString("code")) || !jsonObject.getString("code").equals("0")) {
//            throw new BusinessException(BaseExceptionEnums.SYSTERM_ERROR);
        }
        logger.info("确认结果----------" + jsonObject.toString());
        if (StringUtils.isEmpty(jsonObject.getString("code")) || !jsonObject.getString("code").equals("0")) {
//            throw new BusinessException(BaseExceptionEnums.SYSTERM_ERROR);
        }
        log.info("查询打款请求执行结束。。。。");
        return jsonObject;

    }


    @Override
    @Transactional
    public void fullScaleDistributionTask() {
        log.info("************满标批量清分开始*************");
        try {
            //获取托管账户账号
            UserBo escrowAccount = this.getEscrowAccount();
            //获取收入账户账号
            UserBo incomeAccount = this.getIncomeAccount();
            //查询散标状态为满标的
            Scatterstandard scatterstandardEntity = new Scatterstandard();
            scatterstandardEntity.setDisabled(0);
            scatterstandardEntity.setStatus(ScatterStandardStatusEnums.FULL_SCALE.getCode());
            List<Scatterstandard> scatterstandardList = this.scatterstandardService.findList(scatterstandardEntity);
            if (!CollectionUtils.isEmpty(scatterstandardList)) {
                for (Scatterstandard scatterstandard : scatterstandardList) {
                    if (redisUtil.tryLock(payParamContants.LOCK_LOAN_CREDITORNO + scatterstandard.getCreditorNo(), 3600 * 24 * 7)) {
                        try {
                            //清分到借款人(打款给借款人)
                            //1.根据散标的债权编号查询债权信息
                            log.info("清分到借款人(打款给借款人) 债权编号{}",scatterstandard.getCreditorNo());
                            Creditorinfo creditorinfoPo = new Creditorinfo();
                            creditorinfoPo.setCreditorNo(scatterstandard.getCreditorNo());
                            Creditorinfo creditorinfo = this.creditorinfoService.findOne(creditorinfoPo);
                            if (null == creditorinfo) {
                                log.error("该散标对应的债权人信息不存在,债权编号:{}", scatterstandard.getCreditorNo());
                                continue;
                            }
                            //2.更新散标订单状态为放款处理中
                            log.info("更新散标订单状态为放款处理中 债权编号{}",scatterstandard.getCreditorNo());
                            scatterstandard.setStatus(ScatterStandardStatusEnums.LENDING_WATING.getCode());
                            scatterstandard.setUpdateTime(new Date());
                            this.scatterstandardService.updateOne(scatterstandard);

                            //清分到借款人
                            LoanRo loanRo = new LoanRo();
                            loanRo.setAmount(scatterstandard.getAmountApply().subtract(creditorinfo.getServiceFee()));//金额=申请金额-前置服务费
                            loanRo.setBankCode(creditorinfo.getBankCode());//银行编码
                            loanRo.setBankNumberNo(creditorinfo.getBankNumber());//银行卡号
                            loanRo.setCardholder(creditorinfo.getBankCardholder());//持卡人
                            loanRo.setCreditorNo(scatterstandard.getCreditorNo());// 债权编号
                            loanRo.setFromUserId(escrowAccount.getId());// 托管账户id
                            loanRo.setToUserId(creditorinfo.getLenderId());// 借款人userId
                            loanRo.setTransType(TransTypeEnum.LOAN.getType());// 交易类型
                            loanRo.setDescription(TransTypeEnum.LOAN.getName());//描述
                            loanRo.setCreditorNo(scatterstandard.getCreditorNo());
                            log.info("清分到借款人 债权编号{}",scatterstandard.getCreditorNo());
                            log.info("清分到借款人数据封装:{}", JsonUtils.serialize(loanRo));
                            this.loan(loanRo);
                            //清分前置服务费到收入账户
                            LoanRo loanRoFee = new LoanRo();
                            loanRoFee.setAmount(creditorinfo.getServiceFee());//金额=前置服务费
                            loanRoFee.setBankCode(incomeAccount.getBankCode());//收入账户银行编码
                            loanRoFee.setBankNumberNo(incomeAccount.getBankCardNo());//收入银行卡号
                            loanRoFee.setCardholder(incomeAccount.getBankCardName());//收入持卡人
                            loanRoFee.setCreditorNo(scatterstandard.getCreditorNo());// 债权编号
                            loanRoFee.setFromUserId(escrowAccount.getId());// 托管账户
                            loanRoFee.setToUserId(incomeAccount.getId());// 收入账户id
                            loanRoFee.setTransType(TransTypeEnum.SERVICE_FEE.getType());// 交易类型
                            loanRoFee.setDescription(TransTypeEnum.SERVICE_FEE.getName());

                            log.info("清分前置服务费到收入账户  债权编号:{}", scatterstandard.getCreditorNo());
                            log.info("清分前置服务费到收入账户数据封装:{}", JsonUtils.serialize(loanRoFee));
                            this.loan(loanRoFee);
                            redisUtil.releaseLock(payParamContants.LOCK_LOAN_CREDITORNO + scatterstandard.getCreditorNo());
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.error("放款清分异常:{}", e.getMessage());
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("批量查询满标列表获取账户异常!{}", e.getMessage());
        }
        log.info("************满标批量清分结束*************");
    }

    /**
     * 给借款人打款处理中
     */
    @Override
    @Transactional
    public void loanWatingTask() {
        log.info("************批量处理给借款人打款处理中开始*************");
        try {
            //查询所有打款处理中的信息
            Scatterstandard scatterstandardEntity = new Scatterstandard();
            scatterstandardEntity.setStatus(ScatterStandardStatusEnums.LENDING_WATING.getCode());
            List<Scatterstandard> scatterstandardList = this.scatterstandardService.findList(scatterstandardEntity);
            if (!CollectionUtils.isEmpty(scatterstandardList)) {
                for (Scatterstandard scatterstandard : scatterstandardList) {
                    log.info("************批量处理给借款人打款处理中开始*************:{}", scatterstandard.getCreditorNo());
                    //打款到借款人结果处理
                    log.info("打款到借款人结果处理,债权编号{}",scatterstandard.getCreditorNo());
                    this.queryLoanResult(scatterstandard, TransTypeEnum.LOAN.getType());
                    //前置服务费收入结果处理
                    log.info("前置服务费收入结果处理,债权编号{}",scatterstandard.getCreditorNo());
                    this.processAccountHistoryResult(scatterstandard, TransTypeEnum.SERVICE_FEE.getType());
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            log.error("批量处理给借款人打款处理中异常!{}", e.getLocalizedMessage());
        }
        log.info("************批量处理给借款人打款处理中结束*************");
    }

    @Override
    public void releaseFundsTask() {
        // TODO: 2018/11/27 释放搁置两天的资金(超级投资人资金先进先出)

    }


    /**
     * 到期债权包债转
     * */
//    public void calculationPackageZhaiZhuan(){
//        try {
//
//            //1查找到期的债权包
//            ExtendQueryCondition extendQueryCondition = new ExtendQueryCondition();
//            Creditorpackage creditorpackageEntity = new Creditorpackage();
//            creditorpackageEntity.setDisabled(0);
//            creditorpackageEntity.setStatus(CreditorPackageStatusEnum.BUY_FULL.getType());
//            Map<ExtendQueryCondition.CompareType, Object> map = new HashMap<>();
//            map.put(ExtendQueryCondition.CompareType.LTE_TIME, DateUtils.stringToDate(DateUtils.DateToString2(new Date())));
//            extendQueryCondition.addCompareQueryMap(Creditorpackage.endTime_field, map);
//            creditorpackageEntity.setExtendQueryCondition(extendQueryCondition);
//            List<Creditorpackage> creditorpackageList = creditorpackageService.findList(creditorpackageEntity);
//
//            //2判断债权包是否需要债转
//            for (Creditorpackage creditorpackage:creditorpackageList){
//
//                //查询此债权包未到期的债权
//                Scatterstandard scatterstandardEntity = new Scatterstandard();
//                scatterstandardEntity.setDisabled(0);
//                scatterstandardEntity.setPackageNo(creditorpackage.getCode());
//                scatterstandardEntity.setStatus(ScatterStandardStatusEnums.LENDING_SUCCESS.getCode());
//                ExtendQueryCondition extendQueryCondition2 = new ExtendQueryCondition();
//                Map<ExtendQueryCondition.CompareType, Object> map2 = new HashMap<>();
//                map2.put(ExtendQueryCondition.CompareType.GTE_TIME, DateUtils.stringToDate(DateUtils.DateToString2(new Date())));
//                extendQueryCondition2.addCompareQueryMap(Scatterstandard.refundIngTime_field, map2);
//                scatterstandardEntity.setExtendQueryCondition(extendQueryCondition2);
//                List<Scatterstandard> scatterstandardList = scatterstandardService.findList(scatterstandardEntity);
//
//                if(!CollectionUtils.isEmpty(scatterstandardList)){
//                    log.info("债权包需要债转");
//                    //需要债转
//                    //未到期的金额
//                    BigDecimal amount = new BigDecimal(0);
//                    for(Scatterstandard scatterstandard:scatterstandardList){
//                        amount = amount.add(scatterstandard.getAmountApply());
//                    }
//
//                    UserBo superAccount = this.getSuperAccount();
//                    UseraccountRo useraccountRo = new UseraccountRo();
//                    useraccountRo.setUserId(superAccount.getId());
//                    BaseResponse<UserAccountBo> useraccount = userAccountService.selectUserAccount(useraccountRo);
//
//                    //获取超级投资人账户信息
//                    BigDecimal  currentBalance= useraccount.getData().getCurrentBalance();
//
//                    //超级投资人的钱够
//                    if(currentBalance.compareTo(amount) != -1){
//                        //超级投资人生成订单
//                        String orderNo = this.addOrder(amount,useraccount.getData().getUserUuid(),creditorpackage.getCode(),new BigDecimal("0.00"),ProductTypeEnums.FINANCING,2);
//                        //超级投资人债转订单和债权包关联
//                        Orderpackagerel orderpackagerel = new Orderpackagerel();
//                        orderpackagerel.setCode(creditorpackage.getCode());
//                        orderpackagerel.setOrderNo(orderNo);
//                        orderpackagerel.setBuyUser(useraccount.getData().getUserUuid());
//                        orderpackagerel.setAmount(amount);
//                        orderpackagerel.setUserType(SupperUserTypeEnum.SUPPER_INVESTORS.getType());
//                        orderpackagerel.setStatus(2);
//                        orderPackageRelService.addOne(orderpackagerel);
//                        //超级投资人活期余额减少
//                        useraccountRo.setAmount(amount);
//                        userAccountService.huoqi2dingqi(useraccountRo);
//                        //债权包状态改为债转
//                        creditorpackage.setStatus(CreditorPackageStatusEnum.ZHAIZHUAN.getType());
//                        creditorpackageService.updateOne(creditorpackage);
//
//                    }
//
//
//                }else {
//                    //不需要债转给超级投资人
//                    creditorpackage.setStatus(CreditorPackageStatusEnum.ZHAIZHUAN.getType());
//                    creditorpackageService.updateOne(creditorpackage);
//                }
//
//            }
//        } catch (BusinessException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 计算债权包利息和用户回款金额
     * */
//    public void calculationPackageAmount(){
//
//        try {
//
//            //1查找已经债转的债权包
//            Creditorpackage creditorpackageEntity = new Creditorpackage();
//            creditorpackageEntity.setDisabled(0);
//            creditorpackageEntity.setStatus(CreditorPackageStatusEnum.ZHAIZHUAN.getType());
//            List<Creditorpackage> creditorpackageList = creditorpackageService.findList(creditorpackageEntity);
//
//            for (Creditorpackage creditorpackage:creditorpackageList) {
//
//                BigDecimal incomePackage = null;//单个债权包利息
//
//                //2根据债权包编号查找和订单的关系
//                Orderpackagerel entity = new Orderpackagerel();
//                entity.setCode(creditorpackage.getCode());
//                entity.setStatus(0);
//                entity.setDisabled(0);
//                List<Orderpackagerel> orderpackagerelList = orderpackagerelService.findList(entity);
//                //3计算债权包利息和每个用户应还款的金额
//                for (Orderpackagerel orderpackagerel:orderpackagerelList){
//
//                    BigDecimal repaymentMoney = new BigDecimal("0");//用户回款金额
//                    BigDecimal income = new BigDecimal("0");//用户利息
//
//                    if(!orderpackagerel.getOrderNo().equals("0")){
//                        OrderOrder orderOrderQuery = new OrderOrder();
//                        orderOrderQuery.setDisabled(0);
//                        orderOrderQuery.setId(orderpackagerel.getOrderNo());
//                        orderOrderQuery.setProductType(ProductTypeEnums.FINANCING.getCode());
//                        orderOrderQuery.setType(0);
//                        OrderOrder orderOrder = orderOrderService.findOne(orderOrderQuery);
//
//                        //4 计算用户在此债权包的占比(用户购买/债权包金额)
//                        double proportion = Double.parseDouble(orderpackagerel.getAmount() + "") / Double.parseDouble( creditorpackage.getAmount() + "");
//                        //计算超级投资人在此债权包的占比
//                        Double superMinBuyX = Double.parseDouble(superMinBuyAmount(orderpackagerel.getCode())+"") / Double.parseDouble( creditorpackage.getAmount() + "");
//                        //查询此债权包的逾期未还的标的
//                        Scatterstandard scatterstandardEntity = new Scatterstandard();
//                        scatterstandardEntity.setDisabled(0);
//                        scatterstandardEntity.setPackageNo(orderpackagerel.getCode());
//                        scatterstandardEntity.setStatus(ScatterStandardStatusEnums.LENDING_SUCCESS.getCode());
//                        ExtendQueryCondition extendQueryCondition3 = new ExtendQueryCondition();
//                        Map<ExtendQueryCondition.CompareType, Object> map3 = new HashMap<>();
//                        map3.put(ExtendQueryCondition.CompareType.LTE_TIME, DateUtils.stringToDate(DateUtils.DateToString2(new Date())));
//                        extendQueryCondition3.addCompareQueryMap(Scatterstandard.refundIngTime_field, map3);
//                        scatterstandardEntity.setExtendQueryCondition(extendQueryCondition3);
//                        List<Scatterstandard> scatterstandardList = scatterstandardService.findList(scatterstandardEntity);
//                        //算出逾期标的的总金额
//                        BigDecimal overdueAmount = new BigDecimal(0);//包的非超级投资人的购买金额
//                        for(Scatterstandard scatterstandard:scatterstandardList){
//                            overdueAmount = overdueAmount.add(scatterstandard.getAmountApply());
//                        }
//
//
//                        //5 计算债权包的盈亏(逾期标的金额/债权包总金额)
//                        double overdueProportion = Double.parseDouble(overdueAmount + "") / Double.parseDouble(userBuyProportion(orderpackagerel.getCode()) + "");
//
//                        if(overdueProportion>superMinBuyX){
//                            //坏账超过了超级投资人承受最大比例
//                            if(orderpackagerel.getUserType() == 1){
//                                //是超级投资人
//                                repaymentMoney = new BigDecimal("0");
//                                income= new BigDecimal("0");
//
//                            }else {
//                                //是普通用户
//                                //坏账超过了超级投资人承受最大比例
//                                //计算还款占比
//                                double repaymentRate = 1 - overdueProportion;
//                                //普通投资人投资的总金额
//                                double notSuperAmount = Double.parseDouble(creditorpackage.getAmount().subtract(superMinBuyAmount(orderpackagerel.getCode()))+"");
//                                //计算用户回款金额 = 用户购买金额/普通投资人投资的总金额*回款比例*债权包总金额
//                                repaymentMoney = new BigDecimal(Double.parseDouble(orderpackagerel.getAmount()+"")/notSuperAmount*repaymentRate*Double.parseDouble(creditorpackage.getAmount()+"")).setScale( 0, BigDecimal.ROUND_DOWN );
//                                //计算用户回款利息
//                                income = repaymentMoney.multiply(orderOrder.getYearRateFin()).multiply(new BigDecimal(orderOrder.getTerm())).divide(new BigDecimal(360), 4);
//                                income= new BigDecimal(income.setScale(4,BigDecimal.ROUND_DOWN).setScale(-2, BigDecimal.ROUND_DOWN).intValue());
//
//                            }
//
//                        }else {
//                            //正常计算
//                            if(orderpackagerel.getUserType() == 1){
//                                //是超级投资人
//
//                                //计算用户回款金额  用户占比*债权包金额
//                                repaymentMoney = new BigDecimal((proportion-overdueProportion)*Double.parseDouble(creditorpackage.getAmount()+""));
//                                //计算用户回款利息
//                                income = repaymentMoney.multiply(orderOrder.getYearRateFin()).multiply(new BigDecimal(orderOrder.getTerm())).divide(new BigDecimal(360), 4);
//                                income= new BigDecimal(income.setScale(4,BigDecimal.ROUND_DOWN).setScale(-2, BigDecimal.ROUND_DOWN).intValue());
//
//
//                            }else {
//                                //是普通用户
//                                //正常计算
//                                //计算用户回款金额
//                                repaymentMoney = orderpackagerel.getAmount();
//                                //计算用户回款利息
//                                income = repaymentMoney.multiply(orderOrder.getYearRateFin()).multiply(new BigDecimal(orderOrder.getTerm())).divide(new BigDecimal(360), 4);
//                                income= new BigDecimal(income.setScale(4,BigDecimal.ROUND_DOWN).setScale(-2, BigDecimal.ROUND_DOWN).intValue());
//
//                            }
//
//                        }
//
//                    }
//
//                    //计算债权包的总利息
//                    incomePackage = incomePackage.add(income);
//
//                }
//
//
//
//
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//
//    }


    /**
     * 计算用户利息和回款金额
     * 申请用户利息打回存管账户
     * */
    @Override
    @Transactional
    public void calculationBackAmount() {

        // 计算理财用户回款金额
        //1 查询投资成功，并且到期的订单
        try {
            log.info("查询到期该回款的订单");
            List<OrderOrder> orderList = orderOrderService.selectDueTimeOrderList();
            if(!CollectionUtils.isEmpty(orderList)){

                //超级投资人占比
//                SysDicRo ro = new SysDicRo();
//                ro.setDicCode("SUPER_INVEST_PERCENTAGE");
//                BaseResponse<List<SysDicItemBo>> sysDictItems = sysDictService.dicItemBoListByDicCode(ro);
//                Double superMinBuyX = (double)Integer.parseInt(sysDictItems.getData().get(0).getDicItemValue())/100;//系统配置超级投资人投资占比

                for (OrderOrder orderOrder:orderList){
                    BigDecimal repaymentMoney = new BigDecimal("0");//用户回款金额
                    BigDecimal income = new BigDecimal("0");//用户利息

                    log.info("根据订单去查询债权包编号");
                    //2 根据订单去关系表查找对应的债权包编号
                    Orderpackagerel entity = new Orderpackagerel();
                    entity.setOrderNo(orderOrder.getId());
                    entity.setDisabled(0);
                    List<Orderpackagerel> orderpackagerelList =  orderpackagerelService.findList(entity);


                    boolean flag = false;

                    for (Orderpackagerel orderpackagerel:orderpackagerelList){

                        BigDecimal repaymentMoneyPackage = null;//单个债权包还款金额
                        BigDecimal incomePackage = null;//单个债权包利息

                        log.info("根据债权包编号查询出对应的债权包");
                        //3 根据债权编号去查找债权包
                        ExtendQueryCondition extendQueryCondition = new ExtendQueryCondition();
                        Creditorpackage creditorpackageEntity = new Creditorpackage();
                        creditorpackageEntity.setDisabled(0);
                        creditorpackageEntity.setCode(orderpackagerel.getCode());
                        Map<ExtendQueryCondition.CompareType, Object> map = new HashMap<>();
                        map.put(ExtendQueryCondition.CompareType.LTE_TIME, DateUtils.stringToDate(DateUtils.DateToString2(new Date())));
                        extendQueryCondition.addCompareQueryMap(Creditorpackage.endTime_field, map);
                        creditorpackageEntity.setExtendQueryCondition(extendQueryCondition);
                        Creditorpackage creditorpackage = creditorpackageService.findOne(creditorpackageEntity);

                        if(creditorpackage != null){
                            //债权包到期了
                            log.info("债权包到期");
                            if(creditorpackage.getStatus() != CreditorPackageStatusEnum.ZHAIZHUAN.getType()){
                                log.info("债权包未债转，查询是否需要债转");
                                //未债转，判断是否需要债转
                                //查询此债权包未到期的债权
                                Scatterstandard scatterstandardEntity = new Scatterstandard();
                                scatterstandardEntity.setDisabled(0);
                                scatterstandardEntity.setPackageNo(orderpackagerel.getCode());
                                scatterstandardEntity.setStatus(ScatterStandardStatusEnums.LENDING_SUCCESS.getCode());
                                ExtendQueryCondition extendQueryCondition2 = new ExtendQueryCondition();
                                Map<ExtendQueryCondition.CompareType, Object> map2 = new HashMap<>();
                                map2.put(ExtendQueryCondition.CompareType.GTE_TIME, DateUtils.stringToDate(DateUtils.DateToString2(new Date())));
                                extendQueryCondition2.addCompareQueryMap(Scatterstandard.refundIngTime_field, map2);
                                scatterstandardEntity.setExtendQueryCondition(extendQueryCondition2);
                                List<Scatterstandard> scatterstandardList = scatterstandardService.findList(scatterstandardEntity);

                                if(!CollectionUtils.isEmpty(scatterstandardList)){
                                    log.info("债权包需要债转");
                                    //需要债转
                                    //未到期的金额
                                    BigDecimal amount = new BigDecimal(0);//包的非超级投资人的购买金额
                                    for(Scatterstandard scatterstandard:scatterstandardList){
                                        amount = amount.add(scatterstandard.getAmountApply());
                                    }

                                    UserBo superAccount = this.getSuperAccount();
                                    UseraccountRo useraccountRo = new UseraccountRo();
                                    useraccountRo.setUserId(superAccount.getId());
                                    BaseResponse<UserAccountBo> useraccount = userAccountService.selectUserAccount(useraccountRo);

                                    //获取超级投资人账户信息
                                    BigDecimal  currentBalance= useraccount.getData().getCurrentBalance();

                                    //超级投资人的钱够
                                    if(currentBalance.compareTo(amount) != -1){
                                        //超级投资人生成订单
                                        String orderNo = this.addZhaiZhuanOrder(amount,useraccount.getData().getUserUuid(),creditorpackage.getCode(),ProductTypeEnums.FINANCING);
                                        //超级投资人债转订单和债权包关联
                                        Orderpackagerel orderpackagerelINSERT = new Orderpackagerel();
                                        orderpackagerelINSERT.setCode(creditorpackage.getCode());
                                        orderpackagerelINSERT.setOrderNo(orderNo);
                                        orderpackagerelINSERT.setBuyUser(useraccount.getData().getUserUuid());
                                        orderpackagerelINSERT.setAmount(amount);
                                        orderpackagerelINSERT.setUserType(SupperUserTypeEnum.SUPPER_INVESTORS.getType());
                                        orderpackagerelINSERT.setStatus(2);
                                        orderPackageRelService.addOne(orderpackagerelINSERT);
                                        //超级投资人活期余额减少
                                        useraccountRo.setAmount(amount);
                                        userAccountService.huoqi2dingqi(useraccountRo);

                                    }
                                    //债权包状态改为债转
                                    creditorpackage.setStatus(CreditorPackageStatusEnum.ZHAIZHUAN.getType());
                                    creditorpackageService.updateOne(creditorpackage);

                                }
                            }




                            //4 计算用户在此债权包的占比(用户购买/债权包金额)
                            double proportion = Double.parseDouble(orderpackagerel.getAmount() + "") / Double.parseDouble( creditorpackage.getAmount() + "");
                            //计算超级投资人在此债权包的占比
                            Double superMinBuyX = Double.parseDouble(superMinBuyAmount(orderpackagerel.getCode())+"") / Double.parseDouble( creditorpackage.getAmount() + "");
                            //查询此债权包的逾期未还的标的
                            Scatterstandard scatterstandardEntity = new Scatterstandard();
                            scatterstandardEntity.setDisabled(0);
                            scatterstandardEntity.setPackageNo(orderpackagerel.getCode());
                            scatterstandardEntity.setStatus(ScatterStandardStatusEnums.LENDING_SUCCESS.getCode());
                            ExtendQueryCondition extendQueryCondition3 = new ExtendQueryCondition();
                            Map<ExtendQueryCondition.CompareType, Object> map3 = new HashMap<>();
                            map3.put(ExtendQueryCondition.CompareType.LTE_TIME, DateUtils.stringToDate(DateUtils.DateToString2(new Date())));
                            extendQueryCondition3.addCompareQueryMap(Scatterstandard.refundIngTime_field, map3);
                            scatterstandardEntity.setExtendQueryCondition(extendQueryCondition3);
                            List<Scatterstandard> scatterstandardList = scatterstandardService.findList(scatterstandardEntity);
                            //算出逾期标的的总金额
                            BigDecimal overdueAmount = new BigDecimal(0);//包的非超级投资人的购买金额
                            for(Scatterstandard scatterstandard:scatterstandardList){
                                overdueAmount = overdueAmount.add(scatterstandard.getAmountApply());
                            }


                            //5 计算债权包的盈亏(逾期标的金额/债权包总金额)
                            double overdueProportion = Double.parseDouble(overdueAmount + "") / Double.parseDouble(userBuyProportion(orderpackagerel.getCode()) + "");

                            if(overdueProportion>superMinBuyX){
                                //坏账超过了超级投资人承受最大比例
                                if(orderpackagerel.getUserType() == 1){
                                    //是超级投资人
                                    repaymentMoneyPackage = new BigDecimal("0");
                                    incomePackage= new BigDecimal("0");

                                }else {
                                    //是普通用户
                                    //坏账超过了超级投资人承受最大比例
                                    //计算还款占比
                                    double repaymentRate = 1 - overdueProportion;
                                    //普通投资人投资的总金额
                                    double notSuperAmount = Double.parseDouble(creditorpackage.getAmount().subtract(superMinBuyAmount(orderpackagerel.getCode()))+"");
                                    //计算用户回款金额 = 用户购买金额/普通投资人投资的总金额*回款比例*债权包总金额
                                    repaymentMoneyPackage = new BigDecimal(Double.parseDouble(orderpackagerel.getAmount()+"")/notSuperAmount*repaymentRate*Double.parseDouble(creditorpackage.getAmount()+"")).setScale( 0, BigDecimal.ROUND_DOWN );
                                    //计算用户回款利息
                                    incomePackage = repaymentMoneyPackage.multiply(orderOrder.getYearRateFin()).multiply(new BigDecimal(orderOrder.getTerm())).divide(new BigDecimal(360), 4);
                                    incomePackage= new BigDecimal(incomePackage.setScale(4,BigDecimal.ROUND_DOWN).setScale(-2, BigDecimal.ROUND_DOWN).intValue());

                                }

                            }else {
                                //正常计算
                                if(orderpackagerel.getUserType() == 1){
                                    //是超级投资人

                                    //计算用户回款金额  用户占比*债权包金额
                                    repaymentMoneyPackage = new BigDecimal((proportion-overdueProportion)*Double.parseDouble(creditorpackage.getAmount()+""));
                                    //计算用户回款利息
                                    incomePackage = repaymentMoneyPackage.multiply(orderOrder.getYearRateFin()).multiply(new BigDecimal(orderOrder.getTerm())).divide(new BigDecimal(360), 4);
                                    incomePackage= new BigDecimal(incomePackage.setScale(4,BigDecimal.ROUND_DOWN).setScale(-2, BigDecimal.ROUND_DOWN).intValue());


                                }else {
                                    //是普通用户
                                    //正常计算
                                    //计算用户回款金额
                                    repaymentMoneyPackage = orderpackagerel.getAmount();
                                    //计算用户回款利息
                                    incomePackage = repaymentMoneyPackage.multiply(orderOrder.getYearRateFin()).multiply(new BigDecimal(orderOrder.getTerm())).divide(new BigDecimal(360), 4);
                                    incomePackage= new BigDecimal(incomePackage.setScale(4,BigDecimal.ROUND_DOWN).setScale(-2, BigDecimal.ROUND_DOWN).intValue());

                                }

                            }


                            repaymentMoney = repaymentMoney.add(repaymentMoneyPackage);
                            income = income.add(incomePackage);


                        }else {
                            //如果该订单对应的债权包没到期的就跳过该订单
                            flag = true;
                            break;
                        }

                    }

                    if(flag){
                        //如果该订单有未到期的债权包，跳过该订单
                        continue;
                    }

                    if(repaymentMoney.compareTo(new BigDecimal("0")) > 0){

                        LoanRo loanRoFee = new LoanRo();
                        loanRoFee.setAmount(income);//金额=利息
                        loanRoFee.setBankCode(getEscrowAccount().getBankCode());//收入账户银行编码
                        loanRoFee.setBankNumberNo(getEscrowAccount().getBankCardNo());//收入银行卡号
                        loanRoFee.setCardholder(getEscrowAccount().getBankCardName());//收入持卡人
                        loanRoFee.setCreditorNo(orderOrder.getId());// 债权编号
                        loanRoFee.setFromUserId(getInterestAccount().getId());// 收入账户id
                        loanRoFee.setToUserId(getEscrowAccount().getId());// 托管账户
                        loanRoFee.setTransType(TransTypeEnum.DINGQIFEE.getType());// 交易类型
                        loanRoFee.setDescription(TransTypeEnum.DINGQIFEE.getName());
                        this.loan(loanRoFee);

                        orderOrder.setStatus(OrderStatusEnums.REPAYING.getCode());
                        orderOrder.setActRepayAmount(repaymentMoney.add(income));

                        orderOrderService.updateOne(orderOrder);

                    }else {

                        orderOrder.setStatus(OrderStatusEnums.REPAY_SUCCESS.getCode());
                        orderOrder.setActRepayAmount(repaymentMoney.add(income));

                        orderOrderService.updateOne(orderOrder);

                        //回款成功
                        UseraccountRo useraccountRo = new UseraccountRo();
                        useraccountRo.setUserId(orderOrder.getUserUuid());
                        useraccountRo.setAmount(orderOrder.getAmountBuy());
                        BaseResponse baseResponse = userAccountService.returnMoneyUserDepositLockBlance(useraccountRo);

                        if(baseResponse.getCode() == 0){
                            orderOrder.setStatus(OrderStatusEnums.REPAY_SUCCESS.getCode());
                            orderOrderService.updateOne(orderOrder);

                        }
                    }





                }

            }


        } catch (BusinessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //查询债权包超级投资人投资金额
    public BigDecimal superMinBuyAmount(String packageCode) throws BusinessException {
        Orderpackagerel entity = new Orderpackagerel();
        entity.setCode(packageCode);
        entity.setUserType(1);
        entity.setDisabled(0);
        List<Orderpackagerel> orderpackagerelList =  orderpackagerelService.findList(entity);

        BigDecimal bigDecimal = new BigDecimal("0");

        for (Orderpackagerel orderpackagerel:orderpackagerelList){
            bigDecimal = bigDecimal.add(orderpackagerel.getAmount());
        }

        return bigDecimal;
    }

    /**
     * 查询用户利息处理中和回款处理中的
     * */
    @Override
    @Transactional
    public void backInterestAmountWating(){
        //定时任务 理财用户利息wating

        PayAccountHistory payAccountHistory = new PayAccountHistory();

        ExtendQueryCondition extendQueryCondition = new ExtendQueryCondition();
        List list = new ArrayList();
        list.add(TransTypeEnum.DINGQIFEE.getType());
        list.add(TransTypeEnum.DINGQI_PAYBACK.getType());
        list.add(TransTypeEnum.DINGQI_ZHAIZHUAN.getType());
        extendQueryCondition.addInQueryMap(PayAccountHistory.tradeType_field, list);//in查询
        payAccountHistory.setStatus(PayAccountStatusEnum.WATING.getType());

        payAccountHistory.setExtendQueryCondition(extendQueryCondition);

        try {
           List<PayAccountHistory> payAccountHistoryList =  payAccountHistoryService.findList(payAccountHistory);

            for (PayAccountHistory result:payAccountHistoryList){

                JSONObject jsonObject = executeLoanQuery(result);
                if (!StringUtils.isEmpty(jsonObject.getString("code")) && jsonObject.getString("code").equals("0")) {

                    //支付状态:'PENDING'=处理中,COMPLETED=成功,'FAILED'=失败
                    if (jsonObject.getString("disburseStatus").equals("COMPLETED")) {

                        result.setStatus(PayAccountStatusEnum.SUCCESS.getType());

                        payAccountHistoryService.updateOne(result);

                    } else if (jsonObject.getString("disburseStatus").equals("FAILED")) {
                        //支付失败,则更新资金表状态为打款失败

                        result.setStatus(PayAccountStatusEnum.ERROR.getType());
                        payAccountHistoryService.updateOne(result);
                    }

                }else if(!StringUtils.isEmpty(jsonObject.getString("code")) && jsonObject.getString("code").equals("1")){
                    if (jsonObject.getString("disburseStatus").equals("FAILED")) {
                        //支付失败,则更新资金表状态为打款失败

                        result.setStatus(PayAccountStatusEnum.ERROR.getType());
                        payAccountHistoryService.updateOne(result);
                    }
                }

            }


        } catch (BusinessException e) {
            e.printStackTrace();
        }

    }


    /**
     * 查询利息是否打到存管账户，如果打到了就给用户回款
     * */
    @Override
    @Transactional
    public void backAmount() {

        OrderOrder orderOrderQuery = new OrderOrder();
        orderOrderQuery.setDisabled(0);
        orderOrderQuery.setStatus(OrderStatusEnums.REPAYING.getCode());
        orderOrderQuery.setProductType(ProductTypeEnums.FINANCING.getCode());
        try {
            List<OrderOrder> orderList = orderOrderService.findList(orderOrderQuery);

            for (OrderOrder orderOrder : orderList){

                PayAccountHistory payAccountHistory = new PayAccountHistory();
                payAccountHistory.setTradeType(TransTypeEnum.DINGQIFEE.getType());
                payAccountHistory.setOrderNo(orderOrder.getId());

                PayAccountHistory payAccountHistoryResult = payAccountHistoryService.findOne(payAccountHistory);
                if(payAccountHistoryResult.getStatus() == PayAccountStatusEnum.SUCCESS.getType()){
                    //如果利息打款成功给用户回款
                    //4.根据投资人userid查询投资人银行卡信息
                    UserBankRo userBankRo = new UserBankRo();
                    userBankRo.setUserUuid(orderOrder.getUserUuid());
                    BaseResponse<UserBankBo> userBankInfo = this.userBankService.getUserBankInfo(userBankRo);
                    if (!userBankInfo.isSuccess() || userBankInfo.getCode() != 0) {
                        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
                    }
                    UserBankBo userBankBo = userBankInfo.getData();
                    log.info("投资人银行卡信息{}", JsonUtils.serialize(userBankBo));

                    UseraccountRo useraccountRo = new UseraccountRo();
                    useraccountRo.setUserId(orderOrder.getUserUuid());
                    BaseResponse<UserAccountBo> useraccount = userAccountService.selectUserAccount(useraccountRo);
                    BigDecimal depositLockBalance = useraccount.getData().getDepositLockBalance();

                    if(depositLockBalance.compareTo(orderOrder.getAmountBuy()) != -1){
                        //清分到投资人
                        LoanRo loanRo = new LoanRo();
                        loanRo.setAmount(orderOrder.getActRepayAmount());//金额
                        loanRo.setBankCode(userBankBo.getBankCode());//银行编码
                        loanRo.setBankNumberNo(userBankBo.getBankNumberNo());//银行卡号
                        loanRo.setCardholder(userBankBo.getBankCardName());//持卡人
                        loanRo.setCreditorNo(orderOrder.getId());// 订单编号
                        loanRo.setFromUserId(getEscrowAccount().getId());//  托管账户
                        loanRo.setToUserId(orderOrder.getUserUuid());// 投资人userId
                        loanRo.setTransType(TransTypeEnum.DINGQI_PAYBACK.getType());// 交易类型
                        loanRo.setDescription(TransTypeEnum.DINGQI_PAYBACK.getName());//描述
                        log.info("清分到投资人数据封装:{}", JsonUtils.serialize(loanRo));
                        this.loan(loanRo);

                        orderOrder.setStatus(OrderStatusEnums.REPAY.getCode());
                        orderOrderService.updateOne(orderOrder);
                    }
                }

            }

        } catch (BusinessException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    /**
     * 查询用户回款处理中的订单
     * */
    @Override
    @Transactional
    public void backAmountWating() {
        OrderOrder orderOrderQuery = new OrderOrder();
        orderOrderQuery.setDisabled(0);
        orderOrderQuery.setProductType(ProductTypeEnums.FINANCING.getCode());
        orderOrderQuery.setStatus(OrderStatusEnums.REPAY.getCode());
        try {
            List<OrderOrder> orderList = orderOrderService.findList(orderOrderQuery);

            for (OrderOrder orderOrder : orderList){

                PayAccountHistory payAccountHistory = new PayAccountHistory();
                payAccountHistory.setTradeType(TransTypeEnum.DINGQI_PAYBACK.getType());
                payAccountHistory.setOrderNo(orderOrder.getId());

                PayAccountHistory payAccountHistoryResult = payAccountHistoryService.findOne(payAccountHistory);
                if(payAccountHistoryResult.getStatus() == PayAccountStatusEnum.SUCCESS.getType()){
                    //回款成功

                    UseraccountRo useraccountRo = new UseraccountRo();
                    useraccountRo.setUserId(orderOrder.getUserUuid());
                    useraccountRo.setAmount(orderOrder.getAmountBuy());
                    BaseResponse baseResponse = userAccountService.returnMoneyUserDepositLockBlance(useraccountRo);

                    if(baseResponse.getCode() == 0){
                        orderOrder.setStatus(OrderStatusEnums.REPAY_SUCCESS.getCode());
                        orderOrderService.updateOne(orderOrder);

                    }

                }else if(payAccountHistoryResult.getStatus() == PayAccountStatusEnum.ERROR.getType()){
                    //回款失败
                }

            }

        } catch (BusinessException e) {
            e.printStackTrace();
        }

    }


    //此债权包的总金额
    public BigDecimal userBuyProportion(String code) throws Exception {
        List<Orderpackagerel> orderPackagerelList = orderpackagerelService.findByPackageNo(code);
        BigDecimal packageAmount = new BigDecimal(0);
        for(Orderpackagerel orderPagerel:orderPackagerelList){
            packageAmount = packageAmount.add(orderPagerel.getAmount());
        }
        return packageAmount;
    }


    /**
     * 处理借款人打款结果
     *
     * @param scatterstandard
     * @throws BusinessException
     */
    @Transactional
    public void queryLoanResult(Scatterstandard scatterstandard, int transType) throws BusinessException {
        Integer scatterStandardStatus = 0;
        if (transType == TransTypeEnum.LOAN.getType()) {
            scatterStandardStatus = ScatterStandardStatusEnums.LENDING_SUCCESS.getCode();
        } else if (transType == TransTypeEnum.SERVICE_FEE.getType()) {
            scatterStandardStatus = ScatterStandardStatusEnums.LENDING_ERROR.getCode();
        } else if (transType == TransTypeEnum.INVESTMENT_PAYBACK.getType()) {
            scatterStandardStatus = ScatterStandardStatusEnums.REFUND_DISTRIBUTION_SUCCESS.getCode();
        } else if (transType == TransTypeEnum.PAYBACK_INCOME.getType()) {
            scatterStandardStatus = ScatterStandardStatusEnums.REFUND_DISTRIBUTION_ERROR.getCode();
        }

        PayAccountHistory payAccountHistoryEntity = new PayAccountHistory();
        payAccountHistoryEntity.setStatus(PayAccountStatusEnum.WATING.getType());
        payAccountHistoryEntity.setTradeType(transType);
        payAccountHistoryEntity.setOrderNo(scatterstandard.getCreditorNo());
        List<PayAccountHistory> payAccountHistoryList = this.payAccountHistoryService.findList(payAccountHistoryEntity);
        if (!CollectionUtils.isEmpty(payAccountHistoryList)) {
            for (PayAccountHistory payAccountHistory : payAccountHistoryList) {
                log.info("************请求查询打款处理中请求接口开始,交易流水*************:{}", payAccountHistory.getTradeNo());
                //请求支付系统接口
                JSONObject jsonObject = this.executeLoanQuery(payAccountHistory);
                //支付状态:'PENDING'=处理中,COMPLETED=成功,'FAILED'=失败
                if (jsonObject.getString("disburseStatus").equals("COMPLETED")) {
                    //支付完成,则更新资金表状态为成功,
                    this.updateSuccessAccountHistory(payAccountHistory);
                    //回款支付成功
                    if (transType == TransTypeEnum.INVESTMENT_PAYBACK.getType()) {
                        OrderOrder order = orderOrderService.findById(payAccountHistory.getOrderNo());
                        if (order.getStatus().equals(OrderStatusEnums.REPAY.getCode()))
                            order.setStatus(OrderStatusEnums.REPAY_SUCCESS.getCode());
                        orderOrderService.updateOne(order);
                        // TODO: 2019/4/8 投资人散标账户 --

                    }
                    // 并更新散标标状态为放款完成,
                    this.updateScatterstandardStatus(payAccountHistory.getOrderNo(), scatterStandardStatus);
                    // 通知do-it
                    log.info("通知do-it开始。。。");
                    try {
                        this.orderOrderService.pushOrderStatusToDoit(scatterstandard);
                    } catch (Exception e) {
                        log.error("通知do-it异常,{}", e.getMessage());
                    }
                    log.info("通知do-it结束。。。");
                } else if (jsonObject.getString("disburseStatus").equals("FAILED")) {
                    //支付失败,则更新资金表状态为打款失败
                    this.updateErrorAccountHistory(payAccountHistory);
                    //回款支付成功
                    if (transType == TransTypeEnum.INVESTMENT_PAYBACK.getType()) {
                        OrderOrder order = orderOrderService.findById(payAccountHistory.getOrderNo());
                        if (order.getStatus().equals(OrderStatusEnums.REPAY.getCode()))
                            order.setStatus(OrderStatusEnums.REPAY_FAIL.getCode());
                        orderOrderService.updateOne(order);

                    }
                    //更新散标标为放款失败
                    this.updateScatterstandardStatus(payAccountHistory.getOrderNo(), scatterStandardStatus);
                }
            }
        }
    }


    /**
     * 处理资金结果
     *
     * @param scatterstandard
     * @param transType
     * @throws BusinessException
     */
    @Transactional
    public void processAccountHistoryResult(Scatterstandard scatterstandard, Integer transType) throws BusinessException {
        PayAccountHistory payAccountHistoryEntity = new PayAccountHistory();
        payAccountHistoryEntity.setStatus(PayAccountStatusEnum.WATING.getType());
        payAccountHistoryEntity.setTradeType(transType);
        payAccountHistoryEntity.setOrderNo(scatterstandard.getCreditorNo());
        List<PayAccountHistory> accountHistoryList = this.payAccountHistoryService.findList(payAccountHistoryEntity);
        if (!CollectionUtils.isEmpty(accountHistoryList)) {
            for (PayAccountHistory payAccountHistory : accountHistoryList) {
                JSONObject jsonObject = this.executeLoanQuery(payAccountHistory);
                //支付状态:'PENDING'=处理中,COMPLETED=成功,'FAILED'=失败
                if (jsonObject.getString("disburseStatus").equals("COMPLETED")) {
                    //支付完成,则更新资金表状态为成功,
                    this.updateSuccessAccountHistory(payAccountHistory);

                } else if (jsonObject.getString("disburseStatus").equals("FAILED")) {
                    //支付失败,则更新资金表状态为失败
                    this.updateErrorAccountHistory(payAccountHistory);
                }
            }
        }
    }


    /**
     * 更新散标状态
     *
     * @param creditorNo
     * @param status
     */
    public void updateScatterstandardStatus(String creditorNo, Integer status) {
        try {
            Scatterstandard scatterstandardEntity = new Scatterstandard();
            scatterstandardEntity.setCreditorNo(creditorNo);
            Scatterstandard scatterstandard = this.scatterstandardService.findOne(scatterstandardEntity);
            if (scatterstandard != null) {
                scatterstandard.setStatus(status);
                scatterstandard.setUpdateTime(new Date());
                this.scatterstandardService.updateOne(scatterstandard);
            }
        } catch (BusinessException e) {
            log.error("更新散标表失败{}:", e.getMessage());
        }
    }

    /**
     * 更新资金表,支付结果为成功
     *
     * @param accountHistory
     */
    public void updateSuccessAccountHistory(PayAccountHistory accountHistory) {
        try {
            accountHistory = this.payAccountHistoryService.findById(accountHistory.getId());
            accountHistory.setStatus(PayAccountStatusEnum.SUCCESS.getType());
            accountHistory.setUpdateTime(new Date());
            this.payAccountHistoryService.updateOne(accountHistory);
        } catch (BusinessException e) {
            log.error("更新支付结果失败{}:", e.getMessage());
        }
    }

    /**
     * 更新资金表,支付结果为成功
     *
     * @param accountHistory
     */
    public void updateErrorAccountHistory(PayAccountHistory accountHistory) {
        try {
            accountHistory = this.payAccountHistoryService.findById(accountHistory.getTradeNo());
            accountHistory.setStatus(PayAccountStatusEnum.ERROR.getType());
            accountHistory.setUpdateTime(new Date());
            this.payAccountHistoryService.updateOne(accountHistory);
        } catch (BusinessException e) {
            log.error("更新资金表失败{}:", e.getMessage());
        }
    }


    @Override
    @Transactional
    public void refundClarify() throws IllegalAccessException {
        log.info("************批量还款清分开始*************");
        try {
            //获取托管账户账号
            UserBo escrowAccount = this.getEscrowAccount();
            //获取收入账户账号
            UserBo incomeAccount = this.getIncomeAccount();
            //获取超级投资人账户账号
            UserBo superAccount = this.getIncomeAccount();
            //查询散标状态为还款清分的
            Scatterstandard scatterstandardEntity = new Scatterstandard();
            scatterstandardEntity.setStatus(ScatterStandardStatusEnums.RESOLVED.getCode());
            List<Scatterstandard> scatterstandardList = this.scatterstandardService.findList(scatterstandardEntity);
            if (!CollectionUtils.isEmpty(scatterstandardList)) {
                for (Scatterstandard scatterstandard : scatterstandardList) {
                    if (redisUtil.tryLock(payParamContants.LOCK_LOAN_CREDITORNO + scatterstandard.getCreditorNo(), 3600 * 24 * 7)) {
//                        try {
                            //清分 托管账户->投资人
                            //清分 托管账户->do-it
                            // 1.根据散标的债权编号查询债权信息
                            Creditorinfo creditorinfoPo = new Creditorinfo();
                            creditorinfoPo.setCreditorNo(scatterstandard.getCreditorNo());
                            Creditorinfo creditorinfo = this.creditorinfoService.findOne(creditorinfoPo);
                            if (null == creditorinfo) {
                                log.error("该债权[{}]对应的债权人信息不存在", scatterstandard.getCreditorNo());
                                continue;
                            }
                            log.info("更新标[{}]状态为还款清分处理中",scatterstandard.getCreditorNo());
                            scatterstandard.setStatus(ScatterStandardStatusEnums.REFUND_DISTRIBUTION_WATING.getCode());
                            scatterstandard.setUpdateTime(new Date());
                            this.scatterstandardService.updateOne(scatterstandard);

                            //   区分是 理财还是 散标
                            if (scatterstandard.getPackageNo().equals("0")) {
                                //2.根据债权编号找到投资订单记录
                                OrderOrder orderOrderEntity = new OrderOrder();
                                orderOrderEntity.setCreditorNo(scatterstandard.getCreditorNo());
                                OrderOrder order = this.orderOrderService.findOne(orderOrderEntity);
                                if (order == null) {
                                    log.error("该散标[{}]对应的投资订单信息不存在", scatterstandard.getCreditorNo());
                                    continue;
                                }

                                order.setStatus(OrderStatusEnums.REPAYING.getCode());//回款清分中
                                log.info("订单[{}]状态改为回款清分中",order.getId());
                                orderOrderService.updateOne(order);


                                BigDecimal superAmount = scatterstandard.getAmountApply().add(scatterstandard.getAmountApply().multiply(scatterstandard.getYearRateFin()).multiply(new BigDecimal(scatterstandard.getTerm()).divide(new BigDecimal(360),4)));
                                superAmount = new BigDecimal(superAmount.setScale(-2, BigDecimal.ROUND_DOWN).intValue());
                                BigDecimal incomeAmount = scatterstandard.getAmountActual().subtract(superAmount);
                                log.info("按散标计算--标[{}]还款清分到收入账户",scatterstandard.getCreditorNo());
                                toIncomeAccount(escrowAccount, incomeAccount, scatterstandard, incomeAmount);

                                    //4.根据投资人userid查询投资人银行卡信息
                                    UserBankRo userBankRo = new UserBankRo();
                                    userBankRo.setUserUuid(order.getUserUuid());
                                    BaseResponse<UserBankBo> userBankInfo = this.userBankService.getUserBankInfo(userBankRo);
                                    if (!userBankInfo.isSuccess() || userBankInfo.getCode() != 0) {
                                        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
                                    }
                                    UserBankBo userBankBo = userBankInfo.getData();
                                    log.info("清分到投资人[{}]银行卡信息[{}]",order.getUserUuid(),JsonUtils.serialize(userBankBo));
                                    LoanRo loanRo = new LoanRo();
                                    loanRo.setAmount(superAmount);
                                    loanRo.setBankCode(userBankBo.getBankCode());//银行编码
                                    loanRo.setBankNumberNo(userBankBo.getBankNumberNo());//银行卡号
                                    loanRo.setCardholder(userBankBo.getBankCardName());//持卡人
                                    loanRo.setCreditorNo(order.getId());// 订单编号
                                    loanRo.setFromUserId(escrowAccount.getId());//  托管账户
                                    loanRo.setToUserId(order.getUserUuid());// 投资人userId
                                    loanRo.setTransType(TransTypeEnum.INVESTMENT_PAYBACK.getType());// 交易类型
                                    loanRo.setDescription(TransTypeEnum.INVESTMENT_PAYBACK.getName());//描述
                                    log.info("清分到投资人数据封装:{}", JsonUtils.serialize(loanRo));
                                    this.loan(loanRo);

//                                }

                            } else {




                                Creditorpackage creditorpackage = new Creditorpackage();
                                creditorpackage.setCode(scatterstandard.getPackageNo());
                                creditorpackage =creditorpackageService.findOne(creditorpackage);
                                if (creditorpackage.getStatus()==CreditorPackageStatusEnum.ZHAIZHUAN.getType()){
                                    log.info("债转的清分---直接给超级投资人回款");
                                    Orderpackagerel orderpackagerel = new Orderpackagerel();
                                    orderpackagerel.setCode(scatterstandard.getPackageNo());
                                    orderpackagerel.setStatus(2);//债转
                                    orderpackagerel = orderPackageRelService.findOne(orderpackagerel);
                                    if(null!=orderpackagerel){
                                        BigDecimal superAmount = scatterstandard.getAmountApply().add(scatterstandard.getAmountApply().multiply(scatterstandard.getYearRateFin()).multiply(new BigDecimal(scatterstandard.getTerm()).divide(new BigDecimal(360),4)));
                                        superAmount = new BigDecimal(superAmount.setScale(-2, BigDecimal.ROUND_DOWN).intValue());
                                        BigDecimal incomeAmount = scatterstandard.getAmountActual().subtract(superAmount);
                                        log.info("按散标计算--标[{}]还款清分到收入账户",scatterstandard.getCreditorNo());
                                        toIncomeAccount(escrowAccount, incomeAccount, scatterstandard, incomeAmount);
                                        log.info("给超级投资人回款");
                                        log.info("债转给超级投资人的包[{}]内标[{}]还款给超级投资人回款",scatterstandard.getPackageNo(),scatterstandard.getCreditorNo());
                                        if (superAccount.getId().equals(orderpackagerel.getBuyUser())) {
                                            LoanRo loanSuper = new LoanRo();
                                            loanSuper.setAmount(scatterstandard.getAmountActual().subtract(scatterstandard.getInterest()));//金额 实际还款金额-利息
                                            loanSuper.setBankCode(superAccount.getBankCode());//收入账户银行编码
                                            loanSuper.setBankNumberNo(superAccount.getBankCardNo());//收入账户银行卡号
                                            loanSuper.setCardholder(superAccount.getBankName());//收入账户持卡人
                                            loanSuper.setCreditorNo(scatterstandard.getCreditorNo());// 债权编号
                                            loanSuper.setFromUserId(escrowAccount.getId());// 托管账户
                                            loanSuper.setToUserId(superAccount.getId());// 收入账户
                                            loanSuper.setTransType(TransTypeEnum.INVESTMENT_PAYBACK.getType());// 交易类型
                                           loanSuper.setDescription("标["+creditorinfo.getCreditorNo()+"]还款,债转包["+creditorpackage.getCode()+"]回款");//描述
                                            log.info("给超级投资人回款数据封装:{}", JsonUtils.serialize(loanSuper));
                                            this.loan(loanSuper);
                                        }



                                    }else{
                                        log.info("未找到债权包.." + scatterstandard.getPackageNo());
                                    }

                                }else {
                                    BigDecimal incomeAmount = scatterstandard.getAmountActual().subtract(scatterstandard.getAmountApply());
                                    log.info("按理财计算（不算息差 所有收入清分）---标[{}]还款清分到收入账户",scatterstandard.getCreditorNo());
                                    toIncomeAccount(escrowAccount, incomeAccount, scatterstandard, incomeAmount);
                                    if(DateUtils.compare_date(creditorpackage.getEndTime(),new Date())>0){
                                        log.info("复投   ");
                                        creditorpackage.setStatus(3);
                                        creditorpackageService.updateOne(creditorpackage);
                                        String orderNo = addOrder(scatterstandard.getAmountApply(), superAccount.getId(), creditorpackage.getProductUuid(), new BigDecimal("0.00"), ProductTypeEnums.FINANCING,1);
                                        addPayAccountHistory(creditorpackage.getCode(), scatterstandard.getAmountApply(), superAccount.getId(), escrowAccount.getId(), orderNo, TransTypeEnum.BUY_FINACE);
                                    }else {
                                        log.info("不复投  等回款任务");
                                    }
                                }




                                log.info("标状态改成已清分");
                                scatterstandard.setStatus(ScatterStandardStatusEnums.REFUND_DISTRIBUTION_SUCCESS.getCode());
                                scatterstandard.setUpdateTime(new Date());
                                this.scatterstandardService.updateOne(scatterstandard);
                            }
                            redisUtil.releaseLock(payParamContants.LOCK_LOAN_CREDITORNO + scatterstandard.getCreditorNo());
                    }
                }
            }
        } catch (BusinessException e) {
            log.error("批量还款清分异常!{}", e.getMessage());
        }
        log.info("************批量还款清分结束*************");
    }

    private void toIncomeAccount(UserBo escrowAccount, UserBo incomeAccount, Scatterstandard scatterstandard, BigDecimal incomeAmount) throws BusinessException, IllegalAccessException {
        if (incomeAmount.compareTo(new BigDecimal("0"))>0){
            //清分收益到收入账户
            LoanRo loanRoFee = new LoanRo();
            loanRoFee.setAmount(incomeAmount);//金额=理财收益
            loanRoFee.setBankCode(incomeAccount.getBankCode());//收入账户银行编码
            loanRoFee.setBankNumberNo(incomeAccount.getBankCardNo());//收入账户银行卡号
            loanRoFee.setCardholder(incomeAccount.getBankName());//收入账户持卡人
            loanRoFee.setCreditorNo(scatterstandard.getCreditorNo());// 债权编号
            loanRoFee.setFromUserId(escrowAccount.getId());// 托管账户
            loanRoFee.setToUserId(incomeAccount.getId());// 收入账户
            loanRoFee.setTransType(TransTypeEnum.PAYBACK_INCOME.getType());// 交易类型
            loanRoFee.setDescription(TransTypeEnum.PAYBACK_INCOME.getName());//描述
            log.info("清分收益到收入账户数据封装:{}", JsonUtils.serialize(loanRoFee));
            this.loan(loanRoFee);
        }
    }

    @Override
    @Transactional
    public void refundWatingTask() throws BusinessException {
        log.info("************批量还款处理中清分开始*************");
//        try {
            //查询所有状态为还款清分处理中的
            Scatterstandard scatterstandardEntity = new Scatterstandard();
            scatterstandardEntity.setStatus(ScatterStandardStatusEnums.REFUND_DISTRIBUTION_WATING.getCode());
            List<Scatterstandard> scatterstandardList = this.scatterstandardService.findList(scatterstandardEntity);
            if (!CollectionUtils.isEmpty(scatterstandardList)) {
                for (Scatterstandard scatterstandard : scatterstandardList) {
                    //投资回款结果处理
                    this.queryLoanResult(scatterstandard, TransTypeEnum.INVESTMENT_PAYBACK.getType());
                    //回款收益结果处理
                    this.processAccountHistoryResult(scatterstandard, TransTypeEnum.PAYBACK_INCOME.getType());
                }
            }
//        } catch (Exception e) {
//            log.error("批量还款处理中清分异常!{}", e.getMessage());
//        }
        log.info("************批量还款处理中清分结束*************");
    }


    public String getEnumsNameById(Integer id) {
        String enumsName = "";
        if (TransTypeEnum.BUY_CREDITOR.getType().equals(id)) {
            enumsName = TransTypeEnum.BUY_CREDITOR.getDisburseType();
        } else if (TransTypeEnum.LOAN.getType().equals(id)) {
            enumsName = TransTypeEnum.LOAN.getDisburseType();
        } else if (TransTypeEnum.SERVICE_FEE.getType().equals(id)) {
            enumsName = TransTypeEnum.SERVICE_FEE.getDisburseType();
        } else if (TransTypeEnum.INCOME.getType().equals(id)) {
            enumsName = TransTypeEnum.INCOME.getDisburseType();
        } else if (TransTypeEnum.INVESTMENT_PAYBACK.getType().equals(id)) {
            enumsName = TransTypeEnum.INVESTMENT_PAYBACK.getDisburseType();
        } else if (TransTypeEnum.PAYBACK_INCOME.getType().equals(id)) {
            enumsName = TransTypeEnum.PAYBACK_INCOME.getDisburseType();
        }
        return enumsName;
    }

    public static void main(String[] args) {
//        double rate = Double.parseDouble(10 + "") / Double.parseDouble(3 + "");
//        BigDecimal avgAmount = new BigDecimal(rate).setScale(2, BigDecimal.ROUND_HALF_DOWN);
        BigDecimal avgAmount = new BigDecimal("12.56");
        BigDecimal a = avgAmount.setScale( 0, BigDecimal.ROUND_DOWN );

        Double superMinBuyX = (double)Integer.parseInt("9")/100;

        System.out.println(new BigDecimal("-9").compareTo(new BigDecimal("0")));

    }

}
