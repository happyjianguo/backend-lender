package com.yqg.pay.service.loan.impl;

import com.alibaba.fastjson.JSONObject;
import com.yqg.api.pay.loan.ro.LoanRo;
import com.yqg.api.pay.loan.vo.LoanResponse;
import com.yqg.api.pay.loan.vo.PaymentThirdVo;
import com.yqg.api.system.systhirdlogs.bo.SysThirdLogsBo;
import com.yqg.api.user.useraccounthistory.ro.UserAccountChangeRo;
import com.yqg.common.dao.ExtendQueryCondition;
import com.yqg.common.enums.*;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.DateUtils;
import com.yqg.common.utils.JsonUtils;
import com.yqg.pay.entity.PayAccountHistory;
import com.yqg.pay.service.PayCommonServiceImpl;
import com.yqg.pay.service.loan.PayLoanService;
import com.yqg.pay.service.third.OrderService;
import com.yqg.pay.service.third.ScatterStandardService;
import com.yqg.pay.service.third.UserAccountService;
import com.yqg.pay.util.OrderNoCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 放款
 */
@Slf4j
@Service("payLoanService")
public class PayLoanServiceImpl extends PayCommonServiceImpl implements PayLoanService {

    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    protected OrderService orderService;

    @Autowired
    protected ScatterStandardService scatterStandardService;


    @Override
    public void loanResult() throws Exception {
        PayAccountHistory payAccountHistory = new PayAccountHistory();
        payAccountHistory.setStatus(PayAccountStatusEnum.WATING.getType());
//        payAccountHistory.setTradeType(TransTypeEnum.BUY_CREDITOR_BRANCH.getDisburseType());
        ExtendQueryCondition extendQueryCondition = new ExtendQueryCondition();
        List<Object> tradeTypes = new ArrayList<>();
        tradeTypes.add(TransTypeEnum.BUY_CREDITOR_BRANCH.getDisburseType());
        tradeTypes.add(TransTypeEnum.SERVICE_FEE.getDisburseType());
        extendQueryCondition.addInQueryMap(PayAccountHistory.tradeType_field, tradeTypes);//in查询
        payAccountHistory.setExtendQueryCondition(extendQueryCondition);
        List<PayAccountHistory> list = payAccountHistoryService.findList(payAccountHistory);
        for (PayAccountHistory his : list) {
            LoanResponse response = this.executeLoanQuery(his);

            String depositStatus = response.getDisburseStatus();
            if (his.getTradeType().equals(TransTypeEnum.BUY_CREDITOR_BRANCH.getDisburseType())) {

                if (!depositStatus.equals(DepositStatusEnum.PENDING.toString()) && !depositStatus.equals(DepositStatusEnum.CREATED.toString())) {
                    if (depositStatus.equals(DepositStatusEnum.COMPLETED.toString())) {
                        //  1增加活期冻结金额
                        UserAccountChangeRo userAccountChangeRo = new UserAccountChangeRo();
                        userAccountChangeRo.setUserUuid(his.getFromUserId());
                        userAccountChangeRo.setAmount(his.getAmount());
//                        userAccountChangeRo.setBusinessType("购买债权");
                        userAccountChangeRo.setBusinessType(UserAccountBusinessTypeEnum.BUY_CREDITOR.getEnname());
                        userAccountChangeRo.setTradeInfo("机构投资人存入冻结");
                        log.info("机构投资人存入冻结 1增加活期冻结金额（流水2条 金额直接到冻结）");
                        userAccountService.userCharge(userAccountChangeRo);

                        his.setStatus(PayAccountStatusEnum.SUCCESS.getType());
                        his.setPayTime(DateUtils.DateToString(new Date()));
                        log.info("修改订单  订单状态 判断修改满标");
                        orderService.successOrder(his.getOrderNo());


                    } else if (depositStatus.equals(DepositStatusEnum.EXPIRED.toString())) {
                        log.info("失效订单");
                        his.setStatus(PayAccountStatusEnum.EXPIRED.getType());//过期
                        log.info("修改订单  订单状态 失败 订单冻结账户金额解冻 散标冻结金额解冻");
                        orderService.failOrder(his.getOrderNo());


                    } else {
                        logger.info("购买支付失败------------" + response);
                        his.setStatus(PayAccountStatusEnum.ERROR.getType());//失败
                        log.info("修改订单  订单状态 失败 订单冻结账户金额解冻 散标冻结金额解冻");
                        orderService.failOrder(his.getOrderNo());
                    }
                    his.setUpdateTime(new Date());

                    payAccountHistoryService.updateOne(his);
                }
            }


            if (his.getTradeType().equals(TransTypeEnum.SERVICE_FEE.getDisburseType())) {
                if (!depositStatus.equals(DepositStatusEnum.PENDING.toString()) &&!depositStatus.equals(DepositStatusEnum.CREATED.toString())) {
                    if (depositStatus.equals(DepositStatusEnum.COMPLETED.toString())) {
                        his.setStatus(PayAccountStatusEnum.SUCCESS.getType());
                        his.setPayTime(DateUtils.DateToString(new Date()));
                        log.info("服务费打款成功");

                        scatterStandardService.serviceFeeSuccess(his.getOrderNo());
                    }else if (depositStatus.equals(DepositStatusEnum.EXPIRED.toString())) {
                        log.info("失效订单--服务费打款失败");
                        his.setStatus(PayAccountStatusEnum.EXPIRED.getType());//过期

                    } else {
                        log.info("失效订单--服务费打款失败");
                        his.setStatus(PayAccountStatusEnum.ERROR.getType());//失败
                    }
                    his.setUpdateTime(new Date());

                    payAccountHistoryService.updateOne(his);
                }
            }


        }
    }

    /**
     * 放款
     */
    @Override
    @Transactional
    public LoanResponse loan(LoanRo loanRo) throws BusinessException, IllegalAccessException {
        log.info("放款参数：[{}]", loanRo);
        String transNo = TransPreFixTypeEnum.LOAN.getType() + OrderNoCreator.createOrderNo();//交易流水号
        PaymentThirdVo paymentThirdVo = new PaymentThirdVo();
        if (StringUtils.isEmpty(loanRo.getCreditorNo())) {
            paymentThirdVo.setExternalId(transNo);//交易流水号
        } else {
            paymentThirdVo.setExternalId(loanRo.getCreditorNo());//交易流水号

            log.info("=========================开始打款=====================");

        }
        paymentThirdVo.setBankCode(loanRo.getBankCode());//银行code
        paymentThirdVo.setAccountNumber(loanRo.getBankNumberNo());//银行卡号
        paymentThirdVo.setAccountHolderName(loanRo.getCardholder());//持卡人
        paymentThirdVo.setAmount(loanRo.getAmount());
        paymentThirdVo.setDescription(loanRo.getDescription());//描述
        paymentThirdVo.setDisburseType(loanRo.getTransType());//交易类型
        paymentThirdVo.setDisburseChannel(loanRo.getDisburseChannel());//支付渠道
        log.info("打款前数据处理封装:{}", JsonUtils.serialize(loanRo));
        //初始化资金表为处理中
        this.addAccountHistory(paymentThirdVo, loanRo);
        //调用打款
        return this.executeLoanRequst(paymentThirdVo, loanRo);
    }

    /**
     * 查询放款订单是否存在
     */
    public boolean loanQuery(String creditorNo) {

        boolean flag = false;
        try {
            log.info("查询债权号:{}", creditorNo);
            LoanResponse response = this.queryLoanResult(creditorNo, TransTypeEnum.LOAN.getDisburseType());

            if (response != null) {
                if (response.getCode().equals("0") || response.getCode().equals("1")) {
                    flag = true;
                }
            }
            return flag;
        } catch (Exception e) {
            log.error("查询债权异常,单号: " + creditorNo, e);
        }
        return flag;
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
        payAccountHistory.setOrderNo(loanRo.getOrderNo());//债权/订单编号
        payAccountHistory.setTradeNo(paymentThirdVo.getExternalId());//交易流水号
        payAccountHistory.setAmount(paymentThirdVo.getAmount());//金额
        payAccountHistory.setStatus(PayAccountStatusEnum.WATING.getType());//支付状态
        payAccountHistory.setTradeType(loanRo.getTransType());//交易类型
        payAccountHistory.setFromUserId(loanRo.getFromUserId());//资金出方
        payAccountHistory.setToUserId(loanRo.getToUserId());//资金入方
        payAccountHistory.setRemark(paymentThirdVo.getDescription());//备注
        payAccountHistory.setPaychannel(paymentThirdVo.getDisburseChannel());
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
    public LoanResponse executeLoanRequst(PaymentThirdVo paymentThirdVo, LoanRo loanRo) throws BusinessException, IllegalAccessException {
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
        LoanResponse loanResponse = JsonUtils.deserialize(jsonObject.toJSONString(), LoanResponse.class);
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
        return loanResponse;
    }


    /**
     * 打款查询
     *
     * @param
     * @return
     * @throws BusinessException
     */
    public LoanResponse executeLoanQuery(PayAccountHistory payAccountHistory) throws BusinessException {
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
        LoanResponse loanResponse = JsonUtils.deserialize(jsonObject.toJSONString(), LoanResponse.class);
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
        logger.info("确认结果----------" + jsonObject.toString());
//        if (StringUtils.isEmpty(jsonObject.getString("code")) || !jsonObject.getString("code").equals("0")) {
//            throw new BusinessException(BaseExceptionEnums.SYSTERM_ERROR);
//        }
        log.info("查询打款请求执行结束。。。。");
        return loanResponse;

    }


    /**
     * 处理借款人打款结果
     *
     * @param creditorNo
     * @param transType
     * @throws BusinessException
     */
    @Override
    @Transactional
    public LoanResponse queryLoanResult(String creditorNo, String transType) throws BusinessException {


        PayAccountHistory payAccountHistoryEntity = new PayAccountHistory();
        payAccountHistoryEntity.setStatus(PayAccountStatusEnum.WATING.getType());
        payAccountHistoryEntity.setTradeType(transType);
        payAccountHistoryEntity.setOrderNo(creditorNo);
        List<PayAccountHistory> payAccountHistoryList = this.payAccountHistoryService.findList(payAccountHistoryEntity);
        LoanResponse response = null;
        if (!CollectionUtils.isEmpty(payAccountHistoryList) && payAccountHistoryList.size() == 1) {
            PayAccountHistory payAccountHistory = payAccountHistoryList.get(0);
            response = this.executeLoanQuery(payAccountHistory);
            log.info("LoanResponse[{}]",response);
            //支付状态:'PENDING'=处理中,COMPLETED=成功,'FAILED'=失败
            if (response.getDisburseStatus().equals("COMPLETED")) {
                //支付完成,则更新资金表状态为成功,
                this.updateSuccessAccountHistory(payAccountHistory);

            } else if (response.getDisburseStatus().equals("FAILED")) {
                //支付失败,则更新资金表状态为失败
                this.updateErrorAccountHistory(payAccountHistory);

            }
        }
        return response;
    }


    /**
     * 更新资金表,支付结果为成功
     *
     * @param accountHistory
     */
    public void updateSuccessAccountHistory(PayAccountHistory accountHistory) {
        try {
//            accountHistory = this.payAccountHistoryService.findById(accountHistory.getId());
            accountHistory.setStatus(PayAccountStatusEnum.SUCCESS.getType());
            accountHistory.setPayTime(DateUtils.DateToString(new Date()));
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
//            accountHistory = this.payAccountHistoryService.findById(accountHistory.getTradeNo());
            accountHistory.setStatus(PayAccountStatusEnum.ERROR.getType());
            accountHistory.setUpdateTime(new Date());
            this.payAccountHistoryService.updateOne(accountHistory);
        } catch (BusinessException e) {
            log.error("更新资金表失败{}:", e.getMessage());
        }
    }


//    @Override
//    @Transactional
//    public void refundClarify() throws IllegalAccessException {
//        log.info("************批量还款清分开始*************");
//        try {
//            //获取托管账户账号
////            UserAccountBo escrowAccount = this.getEscrowAccount();
//            //获取收入账户账号
//            UserBo incomeAccount = this.getIncomeAccount();
//            //获取超级投资人账户账号
//            UserBo superAccount = this.getIncomeAccount();
    //查询散标状态为还款清分的
//            Scatterstandard scatterstandardEntity = new Scatterstandard();
//            scatterstandardEntity.setStatus(ScatterStandardStatusEnums.RESOLVED.getCode());
//            List<Scatterstandard> scatterstandardList = this.scatterstandardService.findList(scatterstandardEntity);
//            if (!CollectionUtils.isEmpty(scatterstandardList)) {
//                for (Scatterstandard scatterstandard : scatterstandardList) {
//                    if (redisUtil.tryLock(payParamContants.LOCK_LOAN_CREDITORNO + scatterstandard.getCreditorNo(), 3600 * 24 * 7)) {
//                        try {
    //清分 托管账户->投资人
    //清分 托管账户->do-it
    // 1.根据散标的债权编号查询债权信息
//                            Creditorinfo creditorinfoPo = new Creditorinfo();
//                            creditorinfoPo.setCreditorNo(scatterstandard.getCreditorNo());
//                            Creditorinfo creditorinfo = this.creditorinfoService.findOne(creditorinfoPo);
//                            if (null == creditorinfo) {
//                                log.error("该债权[{}]对应的债权人信息不存在", scatterstandard.getCreditorNo());
//                                continue;
//                            }
//                            log.info("更新标[{}]状态为还款清分处理中",scatterstandard.getCreditorNo());
//                            scatterstandard.setStatus(ScatterStandardStatusEnums.REFUND_DISTRIBUTION_WATING.getCode());
//                            scatterstandard.setUpdateTime(new Date());
//                            this.scatterstandardService.updateOne(scatterstandard);

    //   区分是 理财还是 散标
    //2.根据债权编号找到投资订单记录
//                                OrderOrder orderOrderEntity = new OrderOrder();
//                                orderOrderEntity.setCreditorNo(scatterstandard.getCreditorNo());
//                                OrderOrder order = this.orderOrderService.findOne(orderOrderEntity);
//                                if (order == null) {
//                                    log.error("该散标[{}]对应的投资订单信息不存在", scatterstandard.getCreditorNo());
//                                    continue;
//                                }
//
//                                order.setStatus(OrderStatusEnums.REPAYING.getCode());//回款清分中
//                                log.info("订单[{}]状态改为回款清分中",order.getId());
//                                orderOrderService.updateOne(order);
//
//
//                                BigDecimal superAmount = scatterstandard.getAmountApply().add(scatterstandard.getAmountApply().multiply(scatterstandard.getYearRateFin()).multiply(new BigDecimal(scatterstandard.getTerm()).divide(new BigDecimal(360),4)));
//                                superAmount = new BigDecimal(superAmount.setScale(-2, BigDecimal.ROUND_DOWN).intValue());
//                                BigDecimal incomeAmount = scatterstandard.getAmountActual().subtract(superAmount);
//                                log.info("按散标计算--标[{}]还款清分到收入账户",scatterstandard.getCreditorNo());
//                                toIncomeAccount(escrowAccount, incomeAccount, scatterstandard, incomeAmount);
//
//                                    //4.根据投资人userid查询投资人银行卡信息
//                                    UserBankRo userBankRo = new UserBankRo();
//                                    userBankRo.setUserUuid(order.getUserUuid());
//                                    BaseResponse<UserBankBo> userBankInfo = this.userBankService.getUserBankInfo(userBankRo);
//                                    if (!userBankInfo.isSuccess() || userBankInfo.getCode() != 0) {
//                                        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
//                                    }
//                                    UserBankBo userBankBo = userBankInfo.getData();
//                                    log.info("清分到投资人[{}]银行卡信息[{}]",order.getUserUuid(),JsonUtils.serialize(userBankBo));
//                                    LoanRo loanRo = new LoanRo();
//                                    loanRo.setAmount(superAmount);
//                                    loanRo.setBankCode(userBankBo.getBankCode());//银行编码
//                                    loanRo.setBankNumberNo(userBankBo.getBankNumberNo());//银行卡号
//                                    loanRo.setCardholder(userBankBo.getBankCardName());//持卡人
//                                    loanRo.setCreditorNo(order.getId());// 订单编号
//                                    loanRo.setFromUserId(escrowAccount.getId());//  托管账户
//                                    loanRo.setToUserId(order.getUserUuid());// 投资人userId
//                                    loanRo.setTransType(TransTypeEnum.INVESTMENT_PAYBACK.getType());// 交易类型
//                                    loanRo.setDescription(TransTypeEnum.INVESTMENT_PAYBACK.getName());//描述
//                                    log.info("清分到投资人数据封装:{}", JsonUtils.serialize(loanRo));
//                                    this.loan(loanRo);

//                                }

//                            redisUtil.releaseLock(payParamContants.LOCK_LOAN_CREDITORNO + scatterstandard.getCreditorNo());
//                    }
//                }
//            }
//        } catch (BusinessException e) {
//            log.error("批量还款清分异常!{}", e.getMessage());
//        }
//        log.info("************批量还款清分结束*************");
//    }

//    private void toIncomeAccount(UserBo escrowAccount, UserBo incomeAccount, String creditorNo, BigDecimal incomeAmount) throws BusinessException, IllegalAccessException {
//        if (incomeAmount.compareTo(new BigDecimal("0"))>0){
//            //清分收益到收入账户
//            LoanRo loanRoFee = new LoanRo();
//            loanRoFee.setAmount(incomeAmount);//金额=理财收益
//            loanRoFee.setBankCode(incomeAccount.getBankCode());//收入账户银行编码
//            loanRoFee.setBankNumberNo(incomeAccount.getBankCardNo());//收入账户银行卡号
//            loanRoFee.setCardholder(incomeAccount.getBankName());//收入账户持卡人
//            loanRoFee.setCreditorNo(creditorNo);// 债权编号
//            loanRoFee.setFromUserId(escrowAccount.getId());// 托管账户
//            loanRoFee.setToUserId(incomeAccount.getId());// 收入账户
//            loanRoFee.setTransType(TransTypeEnum.PAYBACK_INCOME.getDisburseType());// 交易类型
//            loanRoFee.setDescription(TransTypeEnum.PAYBACK_INCOME.getName());//描述
//            log.info("清分收益到收入账户数据封装:{}", JsonUtils.serialize(loanRoFee));
//            this.loan(loanRoFee);
//        }
//    }

//    @Override
//    @Transactional
//    public void refundWatingTask() throws BusinessException {
//        log.info("************批量还款处理中清分开始*************");
////        try {
//            //查询所有状态为还款清分处理中的
////            Scatterstandard scatterstandardEntity = new Scatterstandard();
////            scatterstandardEntity.setStatus(ScatterStandardStatusEnums.REFUND_DISTRIBUTION_WATING.getCode());
////            List<Scatterstandard> scatterstandardList = this.scatterstandardService.findList(scatterstandardEntity);
////            if (!CollectionUtils.isEmpty(scatterstandardList)) {
////                for (Scatterstandard scatterstandard : scatterstandardList) {
////                    //投资回款结果处理
////                    this.queryLoanResult(scatterstandard, TransTypeEnum.INVESTMENT_PAYBACK.getType());
////                    //回款收益结果处理
////                    this.processAccountHistoryResult(scatterstandard, TransTypeEnum.PAYBACK_INCOME.getType());
////                }
////            }
////        } catch (Exception e) {
////            log.error("批量还款处理中清分异常!{}", e.getMessage());
////        }
//        log.info("************批量还款处理中清分结束*************");
//    }


//    public String getEnumsNameById(Integer id) {
//        String enumsName = "";
//        if (TransTypeEnum.BUY_CREDITOR.getType().equals(id)) {
//            enumsName = TransTypeEnum.BUY_CREDITOR.getDisburseType();
//        } else if (TransTypeEnum.LOAN.getType().equals(id)) {
//            enumsName = TransTypeEnum.LOAN.getDisburseType();
//        } else if (TransTypeEnum.SERVICE_FEE.getType().equals(id)) {
//            enumsName = TransTypeEnum.SERVICE_FEE.getDisburseType();
//        } else if (TransTypeEnum.INCOME.getType().equals(id)) {
//            enumsName = TransTypeEnum.INCOME.getDisburseType();
////        } else if (TransTypeEnum.INVESTMENT_PAYBACK.getType().equals(id)) {
////            enumsName = TransTypeEnum.INVESTMENT_PAYBACK.getDisburseType();
//        } else if (TransTypeEnum.PAYBACK_INCOME.getType().equals(id)) {
//            enumsName = TransTypeEnum.PAYBACK_INCOME.getDisburseType();
//        }
//        return enumsName;
//    }

    public static void main(String[] args) {
//        double rate = Double.parseDouble(10 + "") / Double.parseDouble(3 + "");
//        BigDecimal avgAmount = new BigDecimal(rate).setScale(2, BigDecimal.ROUND_HALF_DOWN);
        BigDecimal avgAmount = new BigDecimal("12.56");
        BigDecimal a = avgAmount.setScale(0, BigDecimal.ROUND_DOWN);

        Double superMinBuyX = (double) Integer.parseInt("9") / 100;

        System.out.println(new BigDecimal("-9").compareTo(new BigDecimal("0")));

    }

}
