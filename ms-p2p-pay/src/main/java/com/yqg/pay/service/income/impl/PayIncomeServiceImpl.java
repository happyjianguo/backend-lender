package com.yqg.pay.service.income.impl;

import com.alibaba.fastjson.JSONObject;
import com.yqg.api.order.orderorder.ro.OrderSuccessRo;
import com.yqg.api.pay.income.ro.IncomeRo;
import com.yqg.api.user.useraccounthistory.ro.UserAccountChangeRo;
import com.yqg.common.dao.ExtendQueryCondition;
import com.yqg.common.enums.*;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.DateUtils;
import com.yqg.pay.entity.PayAccountHistory;
import com.yqg.pay.service.PayCommonServiceImpl;
import com.yqg.pay.service.income.PayIncomeService;
import com.yqg.pay.service.third.OrderService;
import com.yqg.pay.service.third.ScatterStandardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.yqg.common.enums.TransTypeEnum.BUY_CREDITOR;

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
    protected OrderService orderService;
    @Autowired
    protected ScatterStandardService scatterStandardService;


    @Override
    public JSONObject incomeRequest(IncomeRo incomeRo) throws BusinessException, IllegalAccessException {

        incomeRo.setDepositChannel(incomeRo.getDepositChannel());
        incomeRo.setCurrency(inComeConfig.currency);
        incomeRo.setPaymentCode(incomeRo.getPaymentCode());
//        incomeRo.setExpireTime("30");//30分钟过期失效
        log.info("调用收款接口[{}]", incomeRo);
        this.addPayAccountHistory(incomeRo);
        try {
            redisUtil.set(RedisKeyEnums.PAY_EXPRIRESECONDS.appendToDefaultKey(incomeRo.getOrderNo()), DateUtils.addMin(30).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    @Override
    public JSONObject incomeRequestQuery(String tradeNo) throws BusinessException {
        //调用收款查询接口
        JSONObject jsonObject = executeHttpGetRequest(inComeConfig.depositConfirmUrl + tradeNo);
        if (StringUtils.isEmpty(jsonObject.getString("code")) || !jsonObject.getString("code").equals("0")) {
//            throw new BusinessException(BaseExceptionEnums.SYSTERM_ERROR);
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
        tradeTypes.add(TransTypeEnum.LOAN.getDisburseType());
        tradeTypes.add(TransTypeEnum.LOAN_STAGING.getDisburseType());
//        tradeTypes.add(TransTypeEnum.BUY_CREDITOR.getDisburseType());
        extendQueryCondition.addInQueryMap(PayAccountHistory.tradeType_field, tradeTypes);//in查询
        payAccountHistory.setExtendQueryCondition(extendQueryCondition);
        List<PayAccountHistory> list = payAccountHistoryService.findList(payAccountHistory);
        for (PayAccountHistory his : list) {
            JSONObject json = incomeRequestQuery(his.getTradeNo());
//            PayAccountHistory his = payAccountHistoryService.findById(p.getId());
            if(!StringUtils.isEmpty(json.getString("depositStatus"))) {
                String depositStatus = json.getString("depositStatus");
                String tradeType = his.getTradeType().toString();
                String interest = "0.0";
                String insurance = "0.0";
                if(!StringUtils.isEmpty(json.getString("insurance"))&&!StringUtils.isEmpty(json.getString("interest"))){
                    insurance = json.getString("insurance");
                    interest = json.getString("interest");
                }
                BigDecimal decInterest = new BigDecimal(interest);
                BigDecimal decInsurance = new BigDecimal(insurance);

                //---------------------------------------------------Investors deposit to purchase recharge------------------------------------------------------------------------------------
//                if (tradeType.equals(BUY_CREDITOR.getDisburseType())) {
//                    if (depositStatus.equals(DepositStatusEnum.COMPLETED.toString())) {
//
//
//                        his.setStatus(PayAccountStatusEnum.SUCCESS.getType());
//                        his.setPayTime(DateUtils.DateToString(new Date()));
//
//                        if (DateUtils.redMin(30).after(his.getCreateTime())){
//                            log.info("Expired Order");
//                            log.info("Modify Order, Order Status, Failure, Unfreeze Order, Unmark Frozen Amount");
//                            orderService.failOrder(his.getOrderNo());
//                            //  1增加活期冻结金额
//                            UserAccountChangeRo userAccountChangeRo = new UserAccountChangeRo();
//                            userAccountChangeRo.setUserUuid(his.getFromUserId());
//                            userAccountChangeRo.setAmount(his.getAmount());
////                            userAccountChangeRo.setBusinessType("充值");
//                            userAccountChangeRo.setBusinessType(UserAccountBusinessTypeEnum.CHARGE.getEnname());
//                            userAccountChangeRo.setTradeInfo("Pembayaran berhasil,namun order sudah kadaluarsa(dana di deposit ke akun user)");
//                            log.info("Investor deposit the current amount（流水1条 金额直接到账户余额）");
//                            userAccountService.addUserCurrentBlance(userAccountChangeRo);
//                        }else {
//                            //  1增加活期冻结金额
//                            UserAccountChangeRo userAccountChangeRo = new UserAccountChangeRo();
//                            userAccountChangeRo.setUserUuid(his.getFromUserId());
//                            userAccountChangeRo.setAmount(his.getAmount());
////                            userAccountChangeRo.setBusinessType("购买债权");
//                            userAccountChangeRo.setBusinessType(UserAccountBusinessTypeEnum.BUY_CREDITOR.getEnname());
//                            userAccountChangeRo.setTradeInfo("投资人存入冻结");
//                            log.info("投资人存入冻结 1增加活期冻结金额（流水2条 金额直接到冻结）");
//                            userAccountService.userCharge(userAccountChangeRo);
//                            log.info("Change Order, Order Status, 判断修改满标");
//                            orderService.successOrder(his.getOrderNo());
//                        }
//
//
//                    } else if (depositStatus.equals(DepositStatusEnum.EXPIRED.toString()) ||DateUtils.redMin(30).after(his.getCreateTime())) {
//                        log.info("EXPIRED失效订单");
//                        his.setStatus(PayAccountStatusEnum.EXPIRED.getType());//过期
//                        log.info("Order Expired,change status to fail");
//                        orderService.failOrder(his.getOrderNo());
//
//
//                    }else if (depositStatus.equals(DepositStatusEnum.PENDING.toString()) ) {
//                        if (DateUtils.redMin(30).after(his.getCreateTime())) {
//                            log.info("PENDING失效订单");
//                            his.setStatus(PayAccountStatusEnum.EXPIRED.getType());//过期
//                            log.info("Order pending and expired(more than 30 min)");
//                            orderService.failOrder(his.getOrderNo());
//                        }
//
//                    } else {
//                        logger.info("Pembayaran gagal------------" + json);
//                        his.setStatus(PayAccountStatusEnum.ERROR.getType());//失败
//                        log.info("Modify order Order status Fail Order unfreeze account amount unfreeze Bulk bid free amount unfreeze");
//                        orderService.failOrder(his.getOrderNo());
//                    }
//
//
//                }

                //---------------------------------------------------Repayment------------------------------------------------------------------------------------
                if (tradeType.equals(TransTypeEnum.LOAN.getDisburseType())) {
                    if (depositStatus.equals(DepositStatusEnum.COMPLETED.toString())) {
                        log.info("Successful repayment loan " + his.getOrderNo());
                        his.setStatus(PayAccountStatusEnum.SUCCESS.getType());
                        his.setPayTime(DateUtils.DateToString(new Date()));
                        OrderSuccessRo ro = new OrderSuccessRo();
                        ro.setInsurance(decInsurance);
                        ro.setServiceFee(decInterest);
                        scatterStandardService.repaySuccess(his.getOrderNo(),ro);

                    } else if (depositStatus.equals(DepositStatusEnum.EXPIRED.toString())) {
                        log.info("还款失效");
                        his.setStatus(PayAccountStatusEnum.EXPIRED.getType());//过期
                        scatterStandardService.repayFail(his.getOrderNo());
                    } else if (depositStatus.equals(DepositStatusEnum.PENDING.toString())) {

                    }
                }
                if (tradeType.equals(TransTypeEnum.LOAN_STAGING.getDisburseType())) {
                    if (depositStatus.equals(DepositStatusEnum.COMPLETED.toString())) {
                        log.info("Successful repayment staging " + his.getOrderNo());
                        his.setStatus(PayAccountStatusEnum.SUCCESS.getType());
                        his.setPayTime(DateUtils.DateToString(new Date()));
                        OrderSuccessRo ro = new OrderSuccessRo();
                        ro.setInsurance(decInsurance);
                        ro.setServiceFee(decInterest);
                        scatterStandardService.repaySuccess(his.getOrderNo(),ro);
                    } else if (depositStatus.equals(DepositStatusEnum.EXPIRED.toString())) {
                        log.info("还款失效");
                        his.setStatus(PayAccountStatusEnum.EXPIRED.getType());//过期
                        scatterStandardService.repayFail(his.getOrderNo());
                    } else if (depositStatus.equals(DepositStatusEnum.PENDING.toString())) {

                    }
                }


                his.setUpdateTime(new Date());
                if (!his.getStatus().equals(PayAccountStatusEnum.WATING.getType())) {
                    payAccountHistoryService.updateOne(his);
                }

            }
            else{
                continue;
            }
        }
    }


}