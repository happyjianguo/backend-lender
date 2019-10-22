package com.yqg.pay.service.income.impl;

import com.alibaba.fastjson.JSONObject;
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


        if (incomeRo.getDepositMethod().equals("BNI")) {
            incomeRo.setDepositChannel("BNI");
        } else {
            incomeRo.setDepositChannel(inComeConfig.depositChannel);
        }
        incomeRo.setCurrency(inComeConfig.currency);
        incomeRo.setPaymentCode("");
        incomeRo.setExpireTime("30");//30分钟过期失效
        log.info("调用收款接口[{}]", incomeRo);
        JSONObject jsonObject = executeHttpPost(inComeConfig.depositUrl, incomeRo);
        if (StringUtils.isEmpty(jsonObject.getString("code")) || !jsonObject.getString("code").equals("0")) {
            throw new BusinessException(BaseExceptionEnums.SYSTERM_ERROR);
        } else {
            jsonObject.remove("code");
            jsonObject.remove("errorMessage");

            String paymentCode = jsonObject.getString("paymentCode");
            incomeRo.setPaymentCode(paymentCode);
            this.addPayAccountHistory(incomeRo);
            try {
                redisUtil.set(RedisKeyEnums.PAY_EXPRIRESECONDS.appendToDefaultKey(incomeRo.getOrderNo()), DateUtils.addMin(30).getTime());

            } catch (ParseException e) {
                e.printStackTrace();
            }
//            this.addPayAccountHistory(incomeRo.getExternalId(), incomeRo.getDepositAmount(), incomeRo.getCustomerUserId(), incomeRo.getToUserId(), incomeRo.getOrderNo(), incomeRo.getDepositType());
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
        tradeTypes.add(TransTypeEnum.INCOME.getDisburseType());
        tradeTypes.add(BUY_CREDITOR.getDisburseType());
        extendQueryCondition.addInQueryMap(PayAccountHistory.tradeType_field, tradeTypes);//in查询
        payAccountHistory.setExtendQueryCondition(extendQueryCondition);
        List<PayAccountHistory> list = payAccountHistoryService.findList(payAccountHistory);
        for (PayAccountHistory his : list) {
            JSONObject json = incomeRequestQuery(his.getTradeNo());
//            PayAccountHistory his = payAccountHistoryService.findById(p.getId());
            String depositStatus = json.getString("depositStatus");
            String tradeType = his.getTradeType().toString();

                //---------------------------------------------------投资人存入购买充值------------------------------------------------------------------------------------
                if (tradeType.equals(BUY_CREDITOR.getDisburseType())) {
                    if (depositStatus.equals(DepositStatusEnum.COMPLETED.toString())) {


                        his.setStatus(PayAccountStatusEnum.SUCCESS.getType());
                        his.setPayTime(DateUtils.DateToString(new Date()));

                        if (DateUtils.redMin(30).after(his.getCreateTime())){
                            log.info("过期订单");
                            log.info("修改订单  订单状态 失败 订单冻结账户金额解冻 散标冻结金额解冻");
                            orderService.failOrder(his.getOrderNo());
                            //  1增加活期冻结金额
                            UserAccountChangeRo userAccountChangeRo = new UserAccountChangeRo();
                            userAccountChangeRo.setUserUuid(his.getFromUserId());
                            userAccountChangeRo.setAmount(his.getAmount());
//                            userAccountChangeRo.setBusinessType("充值");
                            userAccountChangeRo.setBusinessType(UserAccountBusinessTypeEnum.CHARGE.getEnname());
                            userAccountChangeRo.setTradeInfo("投资人支付成功订单过期存入活期余额");
                            log.info("投资人存入活期金额（流水1条 金额直接到账户余额）");
                            userAccountService.addUserCurrentBlance(userAccountChangeRo);
                        }else {
                            //  1增加活期冻结金额
                            UserAccountChangeRo userAccountChangeRo = new UserAccountChangeRo();
                            userAccountChangeRo.setUserUuid(his.getFromUserId());
                            userAccountChangeRo.setAmount(his.getAmount());
//                            userAccountChangeRo.setBusinessType("购买债权");
                            userAccountChangeRo.setBusinessType(UserAccountBusinessTypeEnum.BUY_CREDITOR.getEnname());
                            userAccountChangeRo.setTradeInfo("投资人存入冻结");
                            log.info("投资人存入冻结 1增加活期冻结金额（流水2条 金额直接到冻结）");
                            userAccountService.userCharge(userAccountChangeRo);
                            log.info("修改订单  订单状态 判断修改满标");
                            orderService.successOrder(his.getOrderNo());
                        }


                    } else if (depositStatus.equals(DepositStatusEnum.EXPIRED.toString()) ||DateUtils.redMin(30).after(his.getCreateTime())) {
                        log.info("EXPIRED失效订单");
                        his.setStatus(PayAccountStatusEnum.EXPIRED.getType());//过期
                        log.info("修改订单  订单状态 失败 订单冻结账户金额解冻 散标冻结金额解冻");
                        orderService.failOrder(his.getOrderNo());


                    }else if (depositStatus.equals(DepositStatusEnum.PENDING.toString()) ) {
                        if (DateUtils.redMin(30).after(his.getCreateTime())) {
                            log.info("PENDING失效订单");
                            his.setStatus(PayAccountStatusEnum.EXPIRED.getType());//过期
                            log.info("修改订单  订单状态 失败 订单冻结账户金额解冻 散标冻结金额解冻");
                            orderService.failOrder(his.getOrderNo());
                        }

                    } else {
                        logger.info("购买支付失败------------" + json);
                        his.setStatus(PayAccountStatusEnum.ERROR.getType());//失败
                        log.info("修改订单  订单状态 失败 订单冻结账户金额解冻 散标冻结金额解冻");
                        orderService.failOrder(his.getOrderNo());
                    }


                }

                //---------------------------------------------------还款------------------------------------------------------------------------------------
                if (tradeType.equals(TransTypeEnum.INCOME.getDisburseType())) {
                    // TODO: 2019/7/3 分期还款清分  均在 还款成功失败方法内 判断处理
                    // TODO: 2019/7/3 展期清分   展期债权还款还是走正常对还款清分
                    if (depositStatus.equals(DepositStatusEnum.COMPLETED.toString())) {
                        log.info("还款成功");
                        his.setStatus(PayAccountStatusEnum.SUCCESS.getType());
                        his.setPayTime(DateUtils.DateToString(new Date()));
                        scatterStandardService.repaySuccess(his.getOrderNo());
                    } else if (depositStatus.equals(DepositStatusEnum.EXPIRED.toString())|| DateUtils.redMin(30).after(his.getCreateTime())) {
                        log.info("还款失效");
                        his.setStatus(PayAccountStatusEnum.EXPIRED.getType());//过期
                        scatterStandardService.repayFail(his.getOrderNo());
                    }else if (depositStatus.equals(DepositStatusEnum.PENDING.toString()) ) {
                        if ( DateUtils.redMin(30).after(his.getCreateTime())) {
                            log.info("还款失效");
                            his.setStatus(PayAccountStatusEnum.EXPIRED.getType());//过期
                            scatterStandardService.repayFail(his.getOrderNo());
                        }
                    } else {
//                        logger.info("还款支付失败------------" + json);
//                        his.setStatus(PayAccountStatusEnum.ERROR.getType());//失败
//                        scatterStandardService.repayFail(his.getOrderNo());
                    }
                }



                his.setUpdateTime(new Date());
                if (!his.getStatus().equals(PayAccountStatusEnum.WATING.getType())) {
                    payAccountHistoryService.updateOne(his);
                }


        }
    }


}