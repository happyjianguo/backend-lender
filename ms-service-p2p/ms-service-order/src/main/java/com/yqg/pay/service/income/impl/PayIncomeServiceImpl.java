package com.yqg.pay.service.income.impl;

import com.alibaba.fastjson.JSONObject;
import com.yqg.api.order.scatterstandard.ro.ScatterstandardPageRo;
import com.yqg.api.pay.exception.PayExceptionEnums;
import com.yqg.api.pay.income.ro.IncomeRo;
import com.yqg.api.pay.income.ro.InvestmentFinancingRo;
import com.yqg.api.pay.income.ro.InvestmentRo;
import com.yqg.api.system.sysdic.ro.SysDicRo;
import com.yqg.api.system.sysdicitem.bo.SysDicItemBo;
import com.yqg.api.system.sysparam.ro.SysParamRo;
import com.yqg.api.user.useraccount.bo.UserAccountBo;
import com.yqg.api.user.useraccount.ro.UserAccountNotSessionRo;
import com.yqg.api.user.useraccount.ro.UseraccountRo;
import com.yqg.api.user.useruser.bo.LenderUsrBo;
import com.yqg.api.user.useruser.bo.UserBankAuthStatus;
import com.yqg.api.user.useruser.bo.UserBo;
import com.yqg.api.user.useruser.ro.UserAuthBankStatusRo;
import com.yqg.api.user.useruser.ro.UserReq;
import com.yqg.api.user.useruser.ro.UserRo;
import com.yqg.api.user.useruser.ro.UserTypeSearchRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.dao.ExtendQueryCondition;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.DateUtils;
import com.yqg.order.entity.*;
import com.yqg.order.enums.OrderStatusEnums;
import com.yqg.order.enums.ProductTypeEnums;
import com.yqg.order.enums.ScatterStandardStatusEnums;
import com.yqg.pay.entity.PayAccountHistory;
import com.yqg.pay.enums.*;
import com.yqg.pay.service.PayCommonServiceImpl;
import com.yqg.pay.service.income.PayIncomeService;
import com.yqg.pay.service.loan.PayLoanService;
import com.yqg.pay.util.OrderNoCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static com.yqg.pay.enums.TransTypeEnum.CHARGE;

/**
 * pay
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 11:15:41
 */
@Service("payService")
@Slf4j
public class PayIncomeServiceImpl extends PayCommonServiceImpl implements PayIncomeService {

    @Autowired
    private PayLoanService payLoanService;

    @Override
    public JSONObject charge(String userUuid, BigDecimal amount) throws BusinessException, IllegalAccessException {
        //交易流水号
        String tradeNo = TransPreFixTypeEnum.INCOME.getType() + OrderNoCreator.createOrderNo();

        //获取托管账户账号
        UserBo escrowAccount = this.getEscrowAccount();
        //获取超级投资人账户
        UserBo superAccount = null;
        UserReq userReq = new UserReq();
        userReq.setUserUuid(userUuid);
        BaseResponse<UserBo> user = userService.findUserById(userReq);
        if (!user.getData().getUserType().equals(SupperUserTypeEnum.SUPPER_INVESTORS.getType())) {
            return null;
        } else {
            superAccount = user.getData();
        }


        //调用支付接口 付款类型 充值
        JSONObject jsonObject = null;
        addPayAccountHistory(tradeNo, amount, userUuid, escrowAccount.getId(), "", CHARGE);
        jsonObject = this.incomeRequest(tradeNo, amount, superAccount.getUserName(), CHARGE.getDisburseType());
        return jsonObject;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JSONObject immediateInvestment(InvestmentRo investmentRo) throws BusinessException, IllegalAccessException {
        BigDecimal amount = investmentRo.getAmount();
        String userUuid = investmentRo.getUserUuid();
        String userName = investmentRo.getUserName();
        String creditorNo = investmentRo.getCreditorNo();

        //是否实名、绑卡
        userAuthBankInfo(userUuid);
        //调用接口11查询用户有无doit借款
        isOnBorrow(userUuid);
        //判断有无超过限额
        isOverMaxBuy(amount);
        //锁定散标金额
        Scatterstandard scatterstandard = lockAmount(amount, creditorNo);
        //理财年化利率
        BigDecimal yearRateFin = scatterstandard.getYearRateFin();
        //添加订单记录----状态为投资处理中
        String orderNo = addOrder(amount, userUuid, creditorNo, yearRateFin, ProductTypeEnums.SCATTER_STANDARD,0);
        //---付款流程----
        //记录购买债权流水
        //交易流水号
        String tradeNo = TransPreFixTypeEnum.INCOME.getType() + OrderNoCreator.createOrderNo();


        //获取托管账户账号
        UserBo escrowAccount = this.getEscrowAccount();

        //调用支付接口 付款类型 购买债权
        JSONObject jsonObject = null;

        UserReq userReq = new UserReq();
        userReq.setUserUuid(userUuid);
        BaseResponse<UserBo> user = userService.findUserById(userReq);
        if (user != null && null != user.getData()) {
            if (user.getData().getUserType().equals(SupperUserTypeEnum.SUPPER_INVESTORS.getType())) {
                // 超级投资人账户
                UseraccountRo useraccountRo = new UseraccountRo();
                useraccountRo.setUserId(userUuid);
                BaseResponse<UserAccountBo> useraccount = userAccountService.selectUserAccount(useraccountRo);
                BigDecimal currentBalance = useraccount.getData().getCurrentBalance();
                if (currentBalance.compareTo(amount) != -1) {
//                超级投资人 合买  用户账户活期余额转散标在投金额
//                    useraccountRo.setCurrentBalance(amount);
                    useraccountRo.setAmount(amount);
                    userAccountService.huoqi2sanbiao(useraccountRo);
                    //这笔 散标锁定金额 转为 购买金额
                    if (redisUtil.tryLock(payParamContants.LOCK_SCATTERSTANDARD_CREDITORNO + creditorNo, 10)) {
                        Scatterstandard sca = new Scatterstandard();
                        sca.setCreditorNo(creditorNo);
                        Creditorinfo cre = new Creditorinfo();
                        cre.setCreditorNo(creditorNo);
                        cre = creditorinfoService.findOne(cre);
                        sca = scatterstandardService.findOne(sca);
                        sca.setAmountBuy(sca.getAmountBuy().add(amount));
                        sca.setAmountLock(sca.getAmountBuy().subtract(amount));
                        if (sca.getAmountBuy().compareTo(cre.getAmountApply()) == 0) {
                            sca.setStatus(ScatterStandardStatusEnums.FULL_SCALE.getCode());//满标
                        }
                        scatterstandardService.updateOne(sca);
                        redisUtil.releaseLock(payParamContants.LOCK_SCATTERSTANDARD_CREDITORNO + creditorNo);
                    }
                    addPayAccountHistory(tradeNo, amount, userUuid, escrowAccount.getId(), orderNo, TransTypeEnum.BUY_CREDITOR, PayAccountStatusEnum.SUCCESS.getType());
//            jsonObject = this.incomeRequest(tradeNo, amount, userName, DepositTypeEnum.P2P_CHARGE.toString());
                } else {
                    throw new BusinessException(PayExceptionEnums.BUY_OVER_CURRENT);
                }
            }else {
                addPayAccountHistory(tradeNo, amount, userUuid, escrowAccount.getId(), orderNo, TransTypeEnum.BUY_CREDITOR);
                jsonObject = this.incomeRequest(tradeNo, amount, userName, TransTypeEnum.BUY_CREDITOR.getDisburseType());
            }
        }
        return jsonObject;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JSONObject immediateInvestmentFinancing(InvestmentFinancingRo investmentFinancingRo) throws BusinessException, IllegalAccessException {
        BigDecimal amount = investmentFinancingRo.getAmount();
        String userUuid = investmentFinancingRo.getUserUuid();
//        UserReq userReq = new UserReq();
//        userReq.setUserUuid(userUuid);

        String userName = investmentFinancingRo.getUserName();
//        if (StringUtils.isEmpty(userName)){
//            BaseResponse<UserBo> userBoBaseResponse = userService.findUserById(userReq);
//            userName=userBoBaseResponse.getData().getUserName();
//        }

        String productId = investmentFinancingRo.getProductId();
        //是否实名、绑卡
        userAuthBankInfo(userUuid);
        //调用接口11查询用户有无doit借款
        isOnBorrow(userUuid);


        // 根据productId 获取产品信息 如 理财年化利率
        Productconf productconf = productconfService.findById(investmentFinancingRo.getProductId());
        //理财年化利率
        BigDecimal yearRateFin = productconf.getInterestRate();
        //判断有无超过限额
        isOverMaxBuy(amount);
        String orderNo ="";
        //添加订单记录----状态为投资处理中--超级投资人自动为匹配成功
        orderNo = addOrder(amount, userUuid, productId, yearRateFin, ProductTypeEnums.FINANCING,0);
        //记录购买理财流水
        //交易流水号
        String tradeNo = TransPreFixTypeEnum.INCOME.getType() + OrderNoCreator.createOrderNo();
        //获取托管账户账号
        UserBo escrowAccount = this.getEscrowAccount();


        //调用支付接口 付款类型 购买债权
        JSONObject jsonObject = new JSONObject();
        UserReq userReq = new UserReq();
        userReq.setUserUuid(userUuid);
        BaseResponse<UserBo> user = userService.findUserById(userReq);
//        if (!user.getData().getUserType().equals(SupperUserTypeEnum.SUPPER_INVESTORS.getType())) {

            addPayAccountHistory(tradeNo, amount, userUuid, escrowAccount.getId(), orderNo, TransTypeEnum.BUY_FINACE);
            //调用支付接口 付款类型 购买理财
            log.info("调用支付接口 付款类型 购买理财");
            jsonObject = this.incomeRequest(tradeNo, amount, user.getData().getUserName(), TransTypeEnum.BUY_FINACE.getDisburseType());
//        } else {
//
//
//
//        }
        jsonObject.put("orderNo", orderNo);
        log.info("jsonObject=[{}]",jsonObject);
        return jsonObject;
    }

    private String superBuy(BigDecimal amount,String packageNo) throws BusinessException {
        String orderNo ="";
                //获取托管账户账号
        UserBo escrowAccount = this.getEscrowAccount();
        //获取超级投资人账户
        log.info("获取超级投资人账户--购买理财");
        UserBo superAccount = this.getSuperAccount();
        UseraccountRo useraccountRo = new UseraccountRo();
        useraccountRo.setUserId(superAccount.getId());
        BaseResponse<UserAccountBo> useraccount = userAccountService.selectUserAccount(useraccountRo);
        BigDecimal currentBalance = useraccount.getData().getCurrentBalance();
        BigDecimal dingqiBalance = useraccount.getData().getDepositBalance();
        BigDecimal buyAmount = amount;
        //活期余额+定期余额<要购买的金额 返回错误
        if (currentBalance.add(dingqiBalance).compareTo(buyAmount) == -1) {
            log.info("活期余额+定期余额<要购买的金额 返回错误");
            throw new BusinessException(PayExceptionEnums.BUY_OVER_CURRENT);
        } else {
            //超级投资人购买最低30%债券包  定期账户余额and活期账户余额按优先级----->>>>超级投资人定期账户冻结金额
            log.info("超级投资人购买最低30%债券包  定期账户余额and活期账户余额按优先级----->>>>超级投资人定期账户冻结金额");
            //优先使用定期余额 即债权包回款的钱
            log.info("优先使用定期余额 即债权包回款的钱");
            if (dingqiBalance.compareTo(new BigDecimal("0")) == 1) {
                //定期余额 有钱
                log.info("定期余额 有钱");
                //定期余额 小于 准备购买的金额

                if (dingqiBalance.compareTo(buyAmount) == -1) {
                    log.info("定期余额 小于 准备购买的金额");
                    //超级投资人 合买  用户账户定期余额转定期冻结 直接购买成功
                    log.info("超级投资人 合买  用户账户定期余额转定期冻结 直接购买成功");
                    useraccountRo.setAmount(dingqiBalance);
//                        userAccountService.dingqi2dongjie(useraccountRo);
                    BaseResponse baseResponse = userAccountService.dingqi2dongjie(useraccountRo);
                    log.info("账户操作：{}",baseResponse);
                    if (baseResponse.getCode()!=0){
                        log.error("超级投资人 首投、首投、首投 定期转冻结 失败---账户操作错误，请重试");
                        throw new BusinessException(PayExceptionEnums.ACCOUNT_ERROR);
                    }

                    buyAmount = buyAmount.subtract(dingqiBalance);
//                    log.info("增加流水");
//                    addPayAccountHistory(tradeNo, dingqiBalance, superAccount.getId(), escrowAccount.getId(), orderNo, TransTypeEnum.BUY_FINACE, PayAccountStatusEnum.SUCCESS.getType());
                     orderNo = addOrder(buyAmount,superAccount.getId(),packageNo,new BigDecimal("0"),ProductTypeEnums.FINANCING,0);
                } else {
                    log.info("定期余额 大于等于 准备购买的金额");
                    //超级投资人 合买  用户账户定期余额转定期冻结 直接购买成功
                    log.info("超级投资人 合买  用户账户定期余额转定期冻结 直接购买成功");
                    useraccountRo.setAmount(buyAmount);
//                        userAccountService.dingqi2dongjie(useraccountRo);
                    orderNo = addOrder(buyAmount,superAccount.getId(),packageNo,new BigDecimal("0"),ProductTypeEnums.FINANCING,0);
                    BaseResponse baseResponse = userAccountService.dingqi2dongjie(useraccountRo);
                    log.info("账户操作：{}",baseResponse);
                    if (baseResponse.getCode()!=0){
                        log.error("超级投资人 首投、首投、首投 定期转冻结 失败---账户操作错误，请重试");
                        throw new BusinessException(PayExceptionEnums.ACCOUNT_ERROR);
                    }

//                    log.info("增加流水");
//                    addPayAccountHistory(tradeNo, buyAmount,     superAccount.getId(), escrowAccount.getId(), orderNo, TransTypeEnum.BUY_FINACE, PayAccountStatusEnum.SUCCESS.getType());
                    buyAmount=new BigDecimal("0");
                }
            }
            //定期余额 用完没买完 还需要买 buyAmount 用活期余额购买
            if (buyAmount.compareTo(new BigDecimal("0")) == 1) {
                log.info("定期余额 用完没买完 还需要买 buyAmount 用活期余额购买");
                if (currentBalance.compareTo(buyAmount) != -1) {
                    // 超级投资人合买理财--记账 活期余额到定期冻结 直接购买成功
                    log.info("超级投资人合买理财--记账 活期余额到定期冻结 直接购买成功");
                    //超级投资人 合买  用户账户活期余额转定期冻结 直接购买成功
                    log.info("超级投资人 合买  用户账户活期余额转定期冻结 直接购买成功");
                    useraccountRo.setAmount(buyAmount);
                    userAccountService.huoqi2dingqi(useraccountRo);
//                    addPayAccountHistory(tradeNo, buyAmount, superAccount.getId(), escrowAccount.getId(), orderNo, TransTypeEnum.BUY_FINACE, PayAccountStatusEnum.SUCCESS.getType());
                    orderNo =  addOrder(buyAmount,superAccount.getId(),packageNo,new BigDecimal("0"),ProductTypeEnums.FINANCING,0);

                } else {
                    throw new BusinessException(PayExceptionEnums.BUY_OVER_CURRENT);
                }
            }


        }
        return orderNo;
    }


    /**
     * 判断有无超过限额
     *
     * @param amount 购买金额
     * @throws BusinessException
     */
    private void isOverMaxBuy(BigDecimal amount) throws BusinessException {
        SysParamRo sysParamRo = new SysParamRo();
        sysParamRo.setSysKey(payParamContants.PAY_BUY_MAX);
        BigDecimal maxBuy = new BigDecimal(sysParamService.sysValueByKey(sysParamRo).getData().getSysValue());
        // //购买金额大于系统限制最大可购买金额
        if (amount.compareTo(maxBuy) == 1) {
            //提示无法投资或联系客服
            throw new BusinessException(PayExceptionEnums.BUY_OVERTOP);
        }
    }

    /**
     * 查询用户有无doit借款
     *
     * @param userUuid 投资用户id
     * @throws BusinessException
     */
    private void isOnBorrow(String userUuid) throws BusinessException {
        UserRo userRo = new UserRo();
        userRo.setUserId(userUuid);
        LenderUsrBo lenderUsrRo = orderOrderService.isLoaning(userRo);
        boolean onBorrow = lenderUsrRo.getIsExist().equals("1") ? true : false;
        if (onBorrow) {
            throw new BusinessException(PayExceptionEnums.BUY_ONBORROW);
        }
    }

    /**
     * 是否实名、绑卡
     *
     * @param userUuid 投资用户ID
     * @throws BusinessException
     */
    private void userAuthBankInfo(String userUuid) throws BusinessException {
        UserAuthBankStatusRo ro = new UserAuthBankStatusRo();
        ro.setUserId(userUuid);
        BaseResponse<UserBankAuthStatus> userBankAuthStatus = userService.userAuthBankInfo(ro);
        log.info("投资人信息"+userBankAuthStatus.getData());
        if (!userBankAuthStatus.isSuccess() || userBankAuthStatus.getCode() != 0) {
            throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
        } else {
            if (userBankAuthStatus.getData().getAuthStatus() != 1) {
                throw new BusinessException(PayExceptionEnums.USER_NOT_REALNAME);
            }
            if (userBankAuthStatus.getData().getBankStatus() != 1) {
                throw new BusinessException(PayExceptionEnums.USER_NOT_BINDCARD);
            }
            if (userBankAuthStatus.getData().getIdentity() != 0) {
                throw new BusinessException(PayExceptionEnums.STUDENT_CANNOT_BUY);
            }
        }
    }

    @Override
    @Transactional
    public JSONObject repayment(InvestmentRo investmentRo) throws BusinessException, IllegalAccessException {
        // 交易流水号
        String tradeNo = TransPreFixTypeEnum.INCOME.getType() + OrderNoCreator.createOrderNo();

        //更新散标表 应还款金额
        Scatterstandard scatterstandard = new Scatterstandard();
        scatterstandard.setCreditorNo(investmentRo.getCreditorNo());
        scatterstandard = scatterstandardService.findOne(scatterstandard);
//        scatterstandard.setAmountActual(investmentRo.getAmount().add(investmentRo.getInterest()).add(investmentRo.getOverdueFee()).add(investmentRo.getOverdueRate()));

        scatterstandard.setAmountActual(investmentRo.getAmount());
        scatterstandard.setStatus(ScatterStandardStatusEnums.REFUND_WATING.getCode());
        scatterstandard.setRefundActualTime(new Date());
        scatterstandard.setUpdateTime(new Date());
        scatterstandardService.updateOne(scatterstandard);

        //新增流水
        //托管账户
        UserTypeSearchRo ro = new UserTypeSearchRo();
        ro.setUserType(2);
        BaseResponse<List<UserBo>> userListByType = userService.userListByType(ro);
        this.addPayAccountHistory(tradeNo, investmentRo.getAmount(), investmentRo.getUserUuid(), userListByType.getData().get(0).getId(), investmentRo.getCreditorNo(), TransTypeEnum.INCOME);
        //调用支付接口 付款类型 doit还款
        JSONObject jsonObject = this.incomeRequest(tradeNo, investmentRo.getAmount(), investmentRo.getUserName(), TransTypeEnum.INCOME.getDisburseType());
        return jsonObject;
    }

    @Override
    public JSONObject incomeRequest(String tradeNo, BigDecimal amount, String userName, String depositType) throws BusinessException, IllegalAccessException {
        IncomeRo incomeRo = new IncomeRo();
        incomeRo.setExternalId(tradeNo);
        incomeRo.setDepositAmount(amount);
        incomeRo.setCustomerName(userName);
        incomeRo.setDepositType(depositType);//付款类型 购买债权
        incomeRo.setDepositChannel(inComeConfig.depositChannel);
        incomeRo.setDepositMethod(inComeConfig.depositMethod);
        incomeRo.setCurrency(inComeConfig.currency);
        incomeRo.setPaymentCode("");
        //调用收款接口
//        JSONObject jsonObject = executeHttpPostRequest(inComeConfig.depositUrl, incomeRo);
        JSONObject jsonObject = executeHttpPost(inComeConfig.depositUrl, incomeRo);
        if (StringUtils.isEmpty(jsonObject.getString("code")) || !jsonObject.getString("code").equals("0")) {
            throw new BusinessException(BaseExceptionEnums.SYSTERM_ERROR);
        } else {
            jsonObject.remove("code");
            jsonObject.remove("errorMessage");
        }
        return jsonObject;
    }

    @Override
    public JSONObject incomeRequestQuery(String tradeNo) throws BusinessException {
        //调用收款查询接口
        JSONObject jsonObject = executeHttpGetRequest(inComeConfig.depositConfirmUrl + tradeNo);
        if (StringUtils.isEmpty(jsonObject.getString("code")) || !jsonObject.getString("code").equals("0")) {
            throw new BusinessException(BaseExceptionEnums.SYSTERM_ERROR);
        } else {
            jsonObject.remove("code");
            jsonObject.remove("errorMessage");
        }
        return jsonObject;

    }

    @Override
    @Transactional
    public void payResult() throws Exception {
        PayAccountHistory payAccountHistory = new PayAccountHistory();
        payAccountHistory.setStatus(PayAccountStatusEnum.WATING.getType());
        ExtendQueryCondition extendQueryCondition = new ExtendQueryCondition();
        List<Object> tradeTypes = new ArrayList<>();
        tradeTypes.add(TransTypeEnum.INCOME.getType());
        tradeTypes.add(TransTypeEnum.BUY_CREDITOR.getType());
        tradeTypes.add(TransTypeEnum.BUY_FINACE.getType());
        extendQueryCondition.addInQueryMap(PayAccountHistory.tradeType_field, tradeTypes);//in查询
        payAccountHistory.setExtendQueryCondition(extendQueryCondition);
        List<PayAccountHistory> list = payAccountHistoryService.findList(payAccountHistory);
//        list.forEach(p -> {
            for (PayAccountHistory p:list){
//            try {
                JSONObject json = incomeRequestQuery(p.getTradeNo());
                PayAccountHistory his = payAccountHistoryService.findById(p.getId());
                OrderOrder order = orderOrderService.findById(his.getOrderNo());
                String depositStatus=json.getString("depositStatus");
                String tradeType=his.getTradeType().toString();
                if (!depositStatus.equals(DepositStatusEnum.PENDING.toString())) {
                    //---------------------------------------------------超级投资人存入活期------------------------------------------------------------------------------------
                    if (his.getTradeType().equals(CHARGE.getType())) {
                        //   超级投资人存入活期 1增加活期账户余额 2匹配成功再  转入  定期冻结金额 债券包回款 记录增加利息 转入 定期余额
                        if (depositStatus.equals(DepositStatusEnum.COMPLETED.toString())) {
                            //  增加活期账户余额
                            UseraccountRo userAcountRo = new UseraccountRo();
                            userAcountRo.setUserId(his.getFromUserId());
                            userAcountRo.setCurrentBalance(his.getAmount());
                            userAccountService.addUserCurrentBlance(userAcountRo);

                        } else if (depositStatus.equals(DepositStatusEnum.EXPIRED.toString())) {
                            his.setStatus(PayAccountStatusEnum.EXPIRED.getType());//过期
                            order.setStatus(OrderStatusEnums.FAILORDER.getCode());//失效订单
                            orderOrderService.updateOne(order);
                        } else {
                            logger.info("支付过期------------" + json);
                        }
                    }
                    //---------------------------------------------------购买理财------------------------------------------------------------------------------------
                    if (his.getTradeType().equals(TransTypeEnum.BUY_FINACE.getType())) {
                        //   购买理财 1增加定期账户余额 2匹配成功再  转入  定期冻结金额
                        if (depositStatus.equals(DepositStatusEnum.COMPLETED.toString())) {
                            //  增加定期账户余额
                            UseraccountRo userAcountRo = new UseraccountRo();
                            userAcountRo.setUserId(his.getFromUserId());
                            userAcountRo.setAmount(his.getAmount());
                            userAccountService.addUserDepositBlance(userAcountRo);
                            his.setStatus(PayAccountStatusEnum.SUCCESS.getType());
                            order.setStatus(OrderStatusEnums.INVESTMEN_SUCCESS.getCode());//投资成功
                            order.setIncomeTime(new Date());
                            order.setDueTime(DateUtils.addDate(new Date(),order.getTerm()));
                            order.setCanpay(order.getAmountBuy());
                            orderOrderService.updateOne(order);
                            log.info("购买理财 付款成功 增加定期账户余额");
                            this.initPackage(order.getCreditorNo(), order.getId());//// 查询初始化债权包
                        } else if (depositStatus.equals(DepositStatusEnum.EXPIRED.toString())) {
                            his.setStatus(PayAccountStatusEnum.EXPIRED.getType());//过期
                            order.setStatus(OrderStatusEnums.FAILORDER.getCode());//失效订单
                            orderOrderService.updateOne(order);
                        } else {
                            logger.info("支付过期------------" + json);
                        }


                    }
                    //---------------------------------------------------购买债权------------------------------------------------------------------------------------
                    if (his.getTradeType().equals(TransTypeEnum.BUY_CREDITOR.getType())) {
                        if (depositStatus.equals(DepositStatusEnum.COMPLETED.toString())) {
                            // 用户账户表+散标金额 用户账户明细表
                            UserAccountNotSessionRo useraccountRo = new UserAccountNotSessionRo();
                            useraccountRo.setUserId(his.getFromUserId());
                            useraccountRo.setAmount(his.getAmount());
//                            useraccountRo.setSessionId("");
                            userAccountService.addScatterstandardBlance(useraccountRo);
                            his.setStatus(PayAccountStatusEnum.SUCCESS.getType());
                            order.setStatus(OrderStatusEnums.INVESTMEN_SUCCESS.getCode());//投资成功
                            order.setIncomeTime(new Date());
                            orderOrderService.updateOne(order);
                            if (redisUtil.tryLock(payParamContants.LOCK_SCATTERSTANDARD_CREDITORNO + his.getOrderNo(), 10)) {
                                Scatterstandard sca = new Scatterstandard();
                                sca.setCreditorNo(order.getCreditorNo());
                                sca = scatterstandardService.findOne(sca);
                                Creditorinfo cre = new Creditorinfo();
                                cre.setCreditorNo(order.getCreditorNo());
                                cre = creditorinfoService.findOne(cre);
                                sca.setAmountBuy(sca.getAmountBuy().add(his.getAmount()));
                                sca.setAmountLock(sca.getAmountLock().subtract(his.getAmount()));
                                if (sca.getAmountBuy().compareTo(cre.getAmountApply()) == 0) {
                                    sca.setStatus(ScatterStandardStatusEnums.FULL_SCALE.getCode());//满标
                                }
                                scatterstandardService.updateOne(sca);
                                redisUtil.releaseLock(payParamContants.LOCK_SCATTERSTANDARD_CREDITORNO + his.getOrderNo());
                            }
                        } else if (depositStatus.equals(DepositStatusEnum.EXPIRED.toString())) {
                            his.setStatus(PayAccountStatusEnum.EXPIRED.getType());//过期
                            order.setStatus(OrderStatusEnums.FAILORDER.getCode());//失效订单
                            orderOrderService.updateOne(order);
                            if (redisUtil.tryLock(payParamContants.LOCK_SCATTERSTANDARD_CREDITORNO + his.getOrderNo(), 10)) {
                                Scatterstandard sca = new Scatterstandard();
                                sca.setCreditorNo(order.getCreditorNo());
                                sca = scatterstandardService.findOne(sca);
//                                Creditorinfo cre = new Creditorinfo();
//                                cre.setCreditorNo(order.getCreditorNo());
//                                cre = creditorinfoService.findOne(cre);
                                sca.setAmountLock(sca.getAmountBuy().subtract(his.getAmount()));
                                scatterstandardService.updateOne(sca);
                                redisUtil.releaseLock(payParamContants.LOCK_SCATTERSTANDARD_CREDITORNO + his.getOrderNo());
                            }

                        } else {
                            logger.info("支付过期------------" + json);
                        }
                    }
                    //---------------------------------------------------还款------------------------------------------------------------------------------------
                    if (his.getTradeType().equals(TransTypeEnum.INCOME.getType())) {
                        if (depositStatus.equals(DepositStatusEnum.COMPLETED.toString())) {
//                            // 还款成功  收入账户 转回 托管账户 理财 暂不转回
//                            //交易流水号
//                            String tradeNo = TransPreFixTypeEnum.INCOME.getType() + OrderNoCreator.createOrderNo();
//                            this.addPayAccountHistory(tradeNo,order.getIncome(),getInterestAccount().getId(),getEscrowAccount().getId(),payAccountHistory.getOrderNo(),TransTypeEnum.Fee);
//                            LoanRo loanRoFee = new LoanRo();
//                            loanRoFee.setAmount(order.getIncome());//金额=利息
//                            loanRoFee.setBankCode(getEscrowAccount().getBankCode());//收入账户银行编码
//                            loanRoFee.setBankNumberNo(getEscrowAccount().getBankCardNo());//收入银行卡号
//                            loanRoFee.setCardholder(getEscrowAccount().getBankCardName());//收入持卡人
//                            loanRoFee.setCreditorNo(payAccountHistory.getOrderNo());// 债权编号
//                            loanRoFee.setFromUserId(getInterestAccount().getId());// 托管账户
//                            loanRoFee.setToUserId(getEscrowAccount().getId());// 收入账户id
//                            loanRoFee.setTransType(TransTypeEnum.Fee.getType());// 交易类型
//                            loanRoFee.setDescription(TransTypeEnum.Fee.getName());

//                            log.info("还款成功 收入账户 转回 托管账户  债权编号:{}", payAccountHistory.getOrderNo());
//                            log.info("还款成功 收入账户 转回 托管账户 数据封装:{}", JsonUtils.serialize(loanRoFee));
//                            payLoanService.loan(loanRoFee);

                            his.setStatus(PayAccountStatusEnum.SUCCESS.getType());
                            if (redisUtil.tryLock(payParamContants.LOCK_SCATTERSTANDARD_CREDITORNO + his.getOrderNo(), 10)) {
                                Scatterstandard sca = new Scatterstandard();
                                sca.setCreditorNo(his.getOrderNo());
                                sca = scatterstandardService.findOne(sca);
//                                sca.setAmountActual(his.getAmount());//实际还款金额
                                sca.setStatus(ScatterStandardStatusEnums.RESOLVED.getCode());//已还款
                                sca.setRefundActualTime(new Date());
                                sca.setPaymentcode(json.getString("paymentCode"));
                                sca.setDepositStatus(depositStatus);
                                sca.setExternalId(json.getString("externalId"));
                                sca.setTransactionId(json.getString("transactionId"));
                                sca.setDepositMethod(json.getString("depositMethod"));
                                sca.setDepositChannel(json.getString("depositChannel"));
                                scatterstandardService.updateOne(sca);
                                redisUtil.releaseLock(payParamContants.LOCK_SCATTERSTANDARD_CREDITORNO + his.getOrderNo());



                                //向doit推送订单状态
                                //   orderOrderService.pushOrderStatusToDoit(sca.getCreditorNo(),ScatterStandardStatusEnums.RESOLVED.getCode());
                            }
                        }
                    }

                    if (his.getTradeType().equals(TransTypeEnum.Fee.getType())) {
                        if (depositStatus.equals(DepositStatusEnum.COMPLETED.toString())) {
                            //利息打回 成功
                            his.setStatus(PayAccountStatusEnum.SUCCESS.getType());

                        }
                    }
                    his.setUpdateTime(new Date());

                    payAccountHistoryService.updateOne(his);


                }

//            } catch (BusinessException e) {
//                e.printStackTrace();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
    }

    // 查询初始化债权包
    public Creditorpackage initPackage(String productId, String orderNo) throws Exception {
        //step1 有购买中的包使用购买中的包 没有则创建包
        Creditorpackage creditorPackage = new Creditorpackage();
        creditorPackage.setProductUuid(productId);
        creditorPackage.setStatus(0);//初始化中
        creditorPackage = creditorpackageService.findOne(creditorPackage);

        if (null == creditorPackage) {
            creditorPackage= new Creditorpackage();
            creditorPackage.setStatus(1);//购买中
            creditorPackage = creditorpackageService.findOne(creditorPackage);
            if (null == creditorPackage) {
                creditorPackage= new Creditorpackage();
                creditorPackage.setProductUuid(productId);
                creditorPackage.setStatus(0);//初始化中
                //  创建债权包
                creditorPackage = creditorpackageService.createCreditorpackage(creditorPackage);
                log.info("创建债权包");
            }
        }

        return creditorPackage;
    }

    //债权打包--找标
    @Transactional
    @Override
    public void buildPackage() throws BusinessException {
        SysDicRo ro = new SysDicRo();
        ro.setDicCode("REGULAR_PACK_NUMBER");
        BaseResponse<List<SysDicItemBo>> sysDictItems = sysDictService.dicItemBoListByDicCode(ro);
        Integer maxPackage = Integer.parseInt(sysDictItems.getData().get(0).getDicItemValue());//系统配置个数
//        Integer maxPackage = 5;//系统配置个数
        log.info("1包内标系统配置个数[{}]",maxPackage);
        Creditorpackage creditorPackage = new Creditorpackage();
        creditorPackage.setStatus(0);//初始化中
        List<Creditorpackage> packageList = creditorpackageService.findList(creditorPackage);
        if (CollectionUtils.isEmpty(packageList)){
            log.info("1.1没有初始化中的包");
        }
        for (Creditorpackage creditorpackage : packageList) {
            // 包中有效标(投标中)的个数 为系统配置个数 购买， 低于系统配置个数 找标
            Scatterstandard scatterstandard = new Scatterstandard();
            scatterstandard.setPackageNo(creditorpackage.getCode());
//            depositStatus 不是还款成功的
            scatterstandard.setDepositStatus("0");
            List<Scatterstandard> curList = scatterstandardService.findList(scatterstandard);
            if (CollectionUtils.isEmpty(curList)){
                log.info("1.2打包 没有找到标");
            }

            //包中有效标的个数不够
            if (curList.size() < maxPackage) {
                //标不够 找标
                log.info("2.1标不够 找标");
                Iterator<Scatterstandard> iterator = curList.iterator();
                while (iterator.hasNext()) {
                    Scatterstandard item = iterator.next();
                    //  等待时出现流标的标的---流标 从债券包剔除标的
                    if (item.getStatus().equals(ScatterStandardStatusEnums.THE_TENDER) && DateUtils.getDateTimeDifference(new Date(), item.getCreateTime()) > 6 * 60 * 60 * 1000) {
                        item.setPackageNo("0");
                        scatterstandardService.updateOne(item);
                        log.info("2.1.1等待时出现流标的标的---流标 从债券包剔除标的",item);
                        iterator.remove();

                        //债权包金额--
                        log.info("债权包金额增--");
                        creditorpackage.setAmount(creditorpackage.getAmount().subtract(item.getAmountApply()));
                        creditorpackageService.updateOne(creditorpackage);
                    }
                }
                ScatterstandardPageRo pageRo = new ScatterstandardPageRo();
                pageRo.setStatus(ScatterStandardStatusEnums.THE_TENDER.getCode());
                pageRo.setPageNo(1);
                pageRo.setPageSize(maxPackage - curList.size());
                pageRo.setSortProperty("createTime desc");
                Page<Scatterstandard> scatterstandardList = scatterstandardService.queryList(pageRo);

                if (CollectionUtils.isEmpty(scatterstandardList.getContent())){
                    log.info("2.2分页查询 没有找到标");
                }

                for (Scatterstandard sca : scatterstandardList.getContent()) {

                    if (curList.size() == maxPackage) {
                        log.info("已经够了");
                        return;
                    }
                    String creditorNo = sca.getCreditorNo();
                    if (redisUtil.tryLock(payParamContants.LOCK_SCATTERSTANDARD_CREDITORNO + creditorNo, 10)) {
                        if (sca.getStatus() == ScatterStandardStatusEnums.THE_TENDER.getCode() && sca.getPackageNo().equals("0")) {
                            //债权包ID set到 散标表
                            log.info("债权包ID set到 散标表");
                            sca.setPackageNo(creditorpackage.getCode());
                            scatterstandardService.updateOne(sca);


                            curList.add(sca);
                            if (curList.size() == maxPackage) {
                                creditorpackage.setStatus(1);//债券包满 改为购买中
                                log.info("债券包满 改为购买中");
                            }
                            //债权包金额增加
                            log.info("债权包金额增加");
                            creditorpackage.setAmount(creditorpackage.getAmount().add(sca.getAmountApply()));
                            creditorpackageService.updateOne(creditorpackage);
                        }
                        redisUtil.releaseLock(payParamContants.LOCK_SCATTERSTANDARD_CREDITORNO + creditorNo);
                    } else {
                        log.info("打债权包---获取锁失败{}", payParamContants.LOCK_SCATTERSTANDARD_CREDITORNO + creditorNo);
                        throw new BusinessException(BaseExceptionEnums.SYSTERM_ERROR);
                    }
                }
            }
        }




        // 找标 考虑 已有还款后的情况
        // 还款的标金额多大就找多大的

        creditorPackage = new Creditorpackage();
        creditorPackage.setStatus(3);//重新匹配中
        packageList = creditorpackageService.findList(creditorPackage);
        for (Creditorpackage creditorpackage : packageList) {
            log.info("已有还款后的情况--重新匹配中---找标---还款的标金额多大就找多大的");
            Scatterstandard scatterstandard = new Scatterstandard();
            scatterstandard.setPackageNo(creditorpackage.getCode());
            scatterstandard.setDepositStatus("COMPLETED");
            scatterstandard.setStatus(ScatterStandardStatusEnums.REFUND_DISTRIBUTION_SUCCESS.getCode());
            List<Scatterstandard> refundList = scatterstandardService.findList(scatterstandard);
            for (Scatterstandard scatterstandard1:refundList){


                ScatterstandardPageRo pageRo = new ScatterstandardPageRo();
                pageRo.setStatus(ScatterStandardStatusEnums.THE_TENDER.getCode());
                pageRo.setPageNo(1);
                pageRo.setPageSize(1);
                pageRo.setAmountApply(scatterstandard1.getAmountApply());
                pageRo.setSortProperty("createTime desc");
                Page<Scatterstandard> scatterstandardList = scatterstandardService.queryList(pageRo);

                if (CollectionUtils.isEmpty(scatterstandardList.getContent())){
                    log.info("3分页查询 没有找到标");
                }

                for (Scatterstandard sca : scatterstandardList.getContent()) {

                    String creditorNo = sca.getCreditorNo();
                    if (redisUtil.tryLock(payParamContants.LOCK_SCATTERSTANDARD_CREDITORNO + creditorNo, 10)) {
                        if (sca.getStatus() == ScatterStandardStatusEnums.THE_TENDER.getCode() && sca.getPackageNo().equals("0")) {
                            //债权包ID set到 散标表
                            log.info("3-债权包ID {} set到 散标表 {}",creditorpackage.getCode(),creditorNo);
                            sca.setPackageNo(creditorpackage.getCode());
                            sca.setUpdateTime(new Date());
                            scatterstandardService.updateOne(sca);

                            scatterstandard1.setStatus(ScatterStandardStatusEnums.RE_MATCH.getCode());
                            scatterstandard1.setUpdateTime(new Date());
                            scatterstandardService.updateOne(scatterstandard1);


                            //债权包金额增加
                            log.info("3-债权包{} 金额{}增加后{}",creditorpackage.getCode(),creditorpackage.getAmount(),creditorpackage.getAmount().add(sca.getAmountApply()));
                            creditorpackage.setAmount(creditorpackage.getAmount().add(sca.getAmountApply()));
                            creditorpackage.setUpdateTime(new Date());
                            creditorpackageService.updateOne(creditorpackage);
                        }
                        redisUtil.releaseLock(payParamContants.LOCK_SCATTERSTANDARD_CREDITORNO + creditorNo);
                    } else {
                        log.info("3-打债权包---获取锁失败{}", payParamContants.LOCK_SCATTERSTANDARD_CREDITORNO + creditorNo);
                        throw new BusinessException(BaseExceptionEnums.SYSTERM_ERROR);
                    }
                }


            }
            if (creditorpackage.getBuyAmount().compareTo(creditorpackage.getAmount())==-1){
                creditorpackage.setStatus(1);//债券包 改为购买中
                creditorpackageService.updateOne(creditorpackage);
            }

        }


    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void buyPackage() throws Exception {
        SysDicRo ro = new SysDicRo();
        ro.setDicCode("SUPER_INVEST_PERCENTAGE");
        //  系统配置超级投资人投资占比
        BaseResponse<List<SysDicItemBo>> sysDictItems = sysDictService.dicItemBoListByDicCode(ro);
        Integer superMinBuyX = Integer.parseInt(sysDictItems.getData().get(0).getDicItemValue());//系统配置超级投资人投资占比

//        Integer superMinBuyX = 30;//系统配置超级投资人投资占比
        // 查询已经付款的订单 资金 准备购买
        OrderOrder orderOrder = new OrderOrder();
        orderOrder.setStatus(OrderStatusEnums.INVESTMEN_SUCCESS.getCode());
        orderOrder.setType(0);
        orderOrder.setDisabled(0);
        List<OrderOrder> orderOrders = orderOrderService.findList(orderOrder);


//        List<UserAccountBo> userAccountBos = userAccountService.selectAllUserAccount();
//        userAccountBos.forEach(userAccountBo -> {
//        orderOrders.forEach(order -> {
        for (OrderOrder order:orderOrders){
            UserReq userReq = new UserReq();
            userReq.setUserUuid(order.getUserUuid());
            BaseResponse<UserBo> user = userService.findUserById(userReq);
            log.info("普通投资人购买 user:[{}]",user);
            if (user.getData().getUserType().equals(SupperUserTypeEnum.SUPPER_INVESTORS.getType())) {
                log.info("普通投资人购买 超级投资人用户 continue order:[{}]",order);
                continue;
            }
            log.info("查询到普通投资人已经付款的订单 资金 准备购买order[{}]",order);
            Creditorpackage creditorPackage = new Creditorpackage();
            creditorPackage.setStatus(1);//购买中
            creditorPackage.setProductUuid(order.getCreditorNo());//订单对应的产品
//            try {

                creditorPackage = creditorpackageService.findOne(creditorPackage);
                if (creditorPackage == null) {
                    //没有可购买的债权包  创建债权包
                    log.info("没有可购买的债权包  创建债权包");
                    creditorPackage= this.initPackage(order.getCreditorNo(), order.getId());
                    continue;
                }else {
                    if (creditorPackage.getStatus() == 1 && creditorPackage.getAmount().compareTo(creditorPackage.getBuyAmount()) == 1) {
                        log.info("包还没买满{}", creditorPackage.getCode());
                        //超级投资人最低购买金额
                        BigDecimal superMinBuy = creditorPackage.getAmount().multiply(new BigDecimal(superMinBuyX).divide(new BigDecimal(100)));
                        //普通投资人最多可购买金额
                        BigDecimal normalCanBuy = creditorPackage.getAmount().subtract(superMinBuy).subtract(creditorPackage.getBuyAmount());
//                    InvestmentFinancingRo inv = new InvestmentFinancingRo();
//                    inv.setProductId(creditorPackage.getProductUuid());


                        if (normalCanBuy.compareTo(new BigDecimal(0)) == 1) {
                            //   记录订单和包的关系
                            Orderpackagerel orderpackagerel = new Orderpackagerel();
                            orderpackagerel.setCode(creditorPackage.getCode());
                            orderpackagerel.setOrderNo(order.getId());
                            orderpackagerel.setBuyUser(order.getUserUuid());


                            BigDecimal preBuy = new BigDecimal("0.00");
                            if (order.getCanpay().compareTo(normalCanBuy) <= 0) {
                                log.info("订单可购买金额<= 普通投资人可购买金额 order:{}", order);
                                preBuy = order.getCanpay();
                            } else {
                                log.info("订单可购买金额 > 普通投资人可购买金额 order:{}", order);
                                preBuy = normalCanBuy;
                            }

                            //普通投资人 首投、首投、首投 定期转冻结
                            if (order.getType() == 0) {
                                UseraccountRo useraccountRo = new UseraccountRo();
                                log.info("普通投资人 首投、首投、首投 定期转冻结 userId=[{}]", order.getUserUuid());
                                useraccountRo.setUserId(order.getUserUuid());
                                useraccountRo.setAmount(preBuy);

                                log.info("普通投资人 首投、首投、首投 定期转冻结 useraccountRo:{}", useraccountRo);
                                BaseResponse baseResponse = userAccountService.dingqi2dongjie(useraccountRo);
                                log.info("普通投资人 首投、首投、首投 定期转冻结 账户操作：{}", baseResponse);
                                if (baseResponse.getCode() != 0) {
                                    log.error("普通投资人 首投、首投、首投 定期转冻结 失败");
                                    throw new Exception("普通投资人 首投、首投、首投 定期转冻结");
                                }
                            }
                            //增加债权包已购买金额
                            creditorPackage.setBuyAmount(creditorPackage.getBuyAmount().add(preBuy));
                            if (creditorPackage.getBuyAmount().compareTo(creditorPackage.getAmount()) == 0) {
                                creditorPackage.setStatus(2);//2.已买满

                                makeFullScatterStandard(creditorPackage);

                            }
                            creditorpackageService.updateOne(creditorPackage);
                            log.info("增加债权包已购买金额 order:{},creditorPackage:{}", order, creditorPackage);

                            //增加订单——包关系
                            orderpackagerel.setAmount(preBuy);
                            orderPackageRelService.addOne(orderpackagerel);
                            log.info("增加订单——包关系");
                            //订单可购买金额--
                            order.setCanpay(order.getCanpay().subtract(preBuy));
                            //订单可购买金额为0 订单匹配成功
                            if (order.getCanpay().compareTo(new BigDecimal("0")) == 0) {
                                order.setStatus(OrderStatusEnums.MATCH_SUCCESS.getCode());
                            }

                            orderOrderService.updateOne(order);
                            log.info("订单匹配部分 订单可购买金额--  order:{}", order);


                            //普通投资人 首投、首投、首投 定期转冻结
//                        if (order.getType()==0) {
//                            UseraccountRo useraccountRo = new UseraccountRo();
//                            useraccountRo.setUserId(order.getUserUuid());
//                            useraccountRo.setAmount(order.getCanpay());
//                            log.info("普通投资人 首投、首投、首投 定期转冻结 useraccountRo:{}",useraccountRo);
//                            BaseResponse baseResponse = userAccountService.dingqi2dongjie(useraccountRo);
//                            log.info("账户操作：{}",baseResponse);
//                            if (baseResponse.getCode()!=0){
//                                log.error("普通投资人 首投、首投、首投 定期转冻结 失败");
//                                throw new Exception("普通投资人 首投、首投、首投 定期转冻结");
//                            }
//                        }
//
//                        //增加债权包已购买金额
//                        creditorPackage.setBuyAmount(creditorPackage.getBuyAmount().add(order.getCanpay()));
//                        if (creditorPackage.getBuyAmount().compareTo(creditorPackage.getAmount())==0) {
//                            creditorPackage.setStatus(2);//2.已买满
//
//                            makeFullScatterStandard(creditorPackage);
//                        }
//                        creditorpackageService.updateOne(creditorPackage);
//                        log.info("增加债权包已购买金额 order:{},package:{}",order,creditorPackage);
//
//
//
//                        //增加订单——包关系
//                        orderpackagerel.setAmount(order.getCanpay());
//
//                        orderPackageRelService.addOne(orderpackagerel);
//                        log.info("增加订单——包关系");
//                        //订单匹配成功 订单可购买金额为0
//                        order.setStatus(OrderStatusEnums.MATCH_SUCCESS.getCode());
//                        order.setCanpay(new BigDecimal("0"));
//                        orderOrderService.updateOne(order);
//                        log.info("订单匹配成功 订单可购买金额为0  order:{}",order);

                        }
                    }
                }
        }

        //  复投的购买 之前的债权包
        // 查询复投的订单 资金 准备购买
        orderOrder = new OrderOrder();
        orderOrder.setStatus(OrderStatusEnums.INVESTMEN_SUCCESS.getCode());
        orderOrder.setType(1);
        orderOrders = orderOrderService.findList(orderOrder);

//        orderOrders.forEach(order -> {
        for(OrderOrder order:orderOrders){
            String packageNo = order.getCreditorNo();

            Creditorpackage creditorPackage = new Creditorpackage();
            creditorPackage.setStatus(1);//购买中
            creditorPackage.setCode(packageNo);

                creditorPackage = creditorpackageService.findOne(creditorPackage);
                if (creditorPackage == null) {
                    log.info("复投 没有可购买的债权包");
                    return;
                }else{
                    if (order.getAmountBuy().compareTo(creditorPackage.getAmount().subtract(creditorPackage.getBuyAmount()))<1) {
                        //增加债权包已购买金额
                        creditorPackage.setBuyAmount(creditorPackage.getBuyAmount().add(order.getCanpay()));
                        if (creditorPackage.getBuyAmount().compareTo(creditorPackage.getAmount()) == 0) {
                            creditorPackage.setStatus(2);//2.已买满

                            makeFullScatterStandard(creditorPackage);
                        }
                        creditorpackageService.updateOne(creditorPackage);


                        //增加订单——包关系
                        //   记录订单和包的关系
                        Orderpackagerel orderpackagerel = new Orderpackagerel();
                        orderpackagerel.setCode(creditorPackage.getCode());
                        orderpackagerel.setOrderNo(order.getId());
                        orderpackagerel.setAmount(order.getCanpay());
                        order.setCanpay(new BigDecimal("0"));
                        orderpackagerel.setBuyUser(order.getUserUuid());
                        orderPackageRelService.addOne(orderpackagerel);
                        //订单匹配成功 订单可购买金额为0
                        order.setStatus(OrderStatusEnums.MATCH_SUCCESS.getCode());
                        orderOrderService.updateOne(order);
                    }
                }



        };


    }

    /**
     * 超级投资人购买 定时任务
     *
     * @throws BusinessException
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void superBuyPackage() throws BusinessException, IllegalAccessException {
        log.info("超级投资人合买");
        SysDicRo ro = new SysDicRo();
        ro.setDicCode("SUPER_INVEST_PERCENTAGE");
        //  系统配置超级投资人投资占比
        BaseResponse<List<SysDicItemBo>> sysDictItems = sysDictService.dicItemBoListByDicCode(ro);
        Integer superMinBuyX = Integer.parseInt(sysDictItems.getData().get(0).getDicItemValue());//系统配置超级投资人投资占比

        Creditorpackage creditorPackageTemp = new Creditorpackage();
        creditorPackageTemp.setStatus(1);//购买中
        List<Creditorpackage> creditorPackageList = creditorpackageService.findList(creditorPackageTemp);
        if (CollectionUtils.isEmpty(creditorPackageList)){
            log.info("超级投资人合买 没有购买中的包");
        }
//        creditorPackageList.forEach(creditorPackage -> {
        for (Creditorpackage creditorPackage:creditorPackageList){
            if (creditorPackage.getAmount().compareTo(creditorPackage.getBuyAmount()) != 0) {
                //超级投资人最低购买金额
                BigDecimal superMinBuy = creditorPackage.getAmount().multiply(new BigDecimal(superMinBuyX).divide(new BigDecimal(100)));
                log.info("超级投资人最低购买金额【{}】",superMinBuy);
                //普通投资人最多可购买金额
                BigDecimal normalCanBuy = creditorPackage.getAmount().subtract(superMinBuy).subtract(creditorPackage.getBuyAmount());
                log.info("普通投资人最多可购买金额【{}】",normalCanBuy);
                InvestmentFinancingRo inv = new InvestmentFinancingRo();
                inv.setProductId(creditorPackage.getProductUuid());

                    Boolean[] canBuy = {false};
                    Scatterstandard scatt = new Scatterstandard();
                    scatt.setPackageNo(creditorPackage.getCode());
                    List<Scatterstandard> scatterstandardList = scatterstandardService.findList(scatt);
                    scatterstandardList.forEach(scat -> {
                        //6*59 差6分钟则间隔6小时  即将到期 有将要流标的情况需要超级投资人直接全部合买掉
                        if (DateUtils.getDateTimeDifferenceMin(new Date(), scat.getCreateTime()) >= 6 * 59) {
                            canBuy[0] = true;
                            log.info("6*59 差6分钟则间隔6小时  即将到期 有将要流标的情况需要超级投资人直接全部合买掉[{}]",scat);
                        }
                    });
                    //将要流标的时候 超级投资人 买满
                    if (canBuy[0]){
                        superMinBuy= superMinBuy.add(normalCanBuy);
                        log.info("将要流标的时候 超级投资人 买满");
                    }
                    if (normalCanBuy.compareTo(new BigDecimal(0)) == 0 || canBuy[0]) {
                        // TODO: 2018/11/27  考虑加锁
                        // 超级投资人购买
                        log.info("超级投资人购买");
                        inv.setAmount(superMinBuy);
                        UserBo user = this.getSuperAccount();
                        inv.setUserUuid(user.getId());
                        inv.setUserName(user.getUserName());

                        // 超级投资人购买30%债券包  定期账户余额and活期账户余额按优先级----->>>>超级投资人定期账户冻结金额
                        log.info(" 超级投资人购买30%债券包  定期账户余额and活期账户余额按优先级----->>>>超级投资人定期账户冻结金额");

//                        JSONObject jsonObject = null;
                        String orderNo ="";
                        try{
//                            jsonObject = this.immediateInvestmentFinancing(inv);
                            orderNo = superBuy(superMinBuy,creditorPackage.getCode());
                        }catch (Exception e){
                            e.printStackTrace();
log.info("----------");
                            log.error("超级投资人购买失败0",e.getStackTrace());
                            log.error("超级投资人购买失败0",e);
                            log.error("超级投资人购买失败4[{}]",e);

                            log.info("超级投资人购买失败00",e);
                            log.info("超级投资人购买失败0",e);
                            log.info("超级投资人购买失败11[{}]",e);
                            continue;
                        }
                        log.info("超级投资人购买[{}]【{}】完成",creditorPackage.getCode(), superMinBuy);
                        creditorPackage.setBuyAmount(creditorPackage.getBuyAmount().add(superMinBuy));
                        if (creditorPackage.getBuyAmount().compareTo(creditorPackage.getAmount())==0) {
                            creditorPackage.setStatus(2);//2.已买满
                            log.info("------已买满");
                        }
                        creditorPackage.setUpdateTime(new Date());
                        creditorpackageService.updateOne(creditorPackage);


                        //记录订单和包的关系
                        Orderpackagerel orderpackagerel = new Orderpackagerel();
                        orderpackagerel.setCode(creditorPackage.getCode());
                        orderpackagerel.setOrderNo(orderNo);
                        orderpackagerel.setAmount(superMinBuy);
                        orderpackagerel.setBuyUser(user.getId());
                        orderpackagerel.setUserType(1);//超级投资人订单
                        orderpackagerel.setRemark("超级投资人 购买");
                        if (canBuy[0]){
                            orderpackagerel.setRemark("将要流标 超级投资人 买满");
                        }
                        log.info("记录订单和包的关系");
                        orderPackageRelService.addOne(orderpackagerel);
                        makeFullScatterStandard(creditorPackage);

                    }
            }
        }
    }

    private void makeFullScatterStandard(Creditorpackage creditorPackage) throws BusinessException {
        log.info("债权包已买满---债权包内全部改为满标 {}",creditorPackage.getCode());
        Scatterstandard sca = new Scatterstandard();
        sca.setPackageNo(creditorPackage.getCode());
        sca.setStatus(ScatterStandardStatusEnums.THE_TENDER.getCode());
        // 根据债权包编号查询 散标
        List<Scatterstandard> scaList = scatterstandardService.findList(sca);
//        scaList.forEach(scatterstandard -> {
        for (Scatterstandard scatterstandard: scaList){
            scatterstandard.setAmountBuy(scatterstandard.getAmountApply());
            scatterstandard.setStatus(ScatterStandardStatusEnums.FULL_SCALE.getCode());
            scatterstandard.setUpdateTime(new Date());
            //债权包内全部改为满标
            scatterstandardService.updateOne(scatterstandard);

        }
    }
}