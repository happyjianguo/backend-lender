package com.yqg.order.service.scatterstandard.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yqg.api.order.creditorinfo.bo.LoanHistiryBo;
import com.yqg.api.order.orderorder.bo.RepaymentPlanBo;
import com.yqg.api.order.orderorder.ro.OrderPayRo;
import com.yqg.api.order.orderorder.ro.OrderSuccessRo;
import com.yqg.api.order.orderorder.ro.RepaymentPlanRo;
import com.yqg.api.order.scatterstandard.bo.ScatterstandardDetailBo;
import com.yqg.api.order.scatterstandard.bo.SignBo;
import com.yqg.api.order.scatterstandard.bo.SignStatusBo;
import com.yqg.api.order.scatterstandard.ro.LoanHistoryRo;
import com.yqg.api.order.scatterstandard.ro.ScatterstandardRo;
import com.yqg.api.order.scatterstandard.ro.SignRo;
import com.yqg.api.order.shoppingcart.bo.ShoppingCartBo;
import com.yqg.api.pay.exception.PayExceptionEnums;
import com.yqg.api.pay.income.bo.IncomeBo;
import com.yqg.api.pay.income.ro.DigisignRo;
import com.yqg.api.pay.income.ro.IncomeRo;
import com.yqg.api.pay.income.ro.InvestmentRo;
import com.yqg.api.pay.loan.ro.LoanRo;
import com.yqg.api.pay.loan.vo.LoanResponse;
import com.yqg.api.pay.payaccounthistory.ro.PayAccountHistoryRo;
import com.yqg.api.user.enums.MessageTypeEnum;
import com.yqg.api.user.enums.UserTypeEnum;
import com.yqg.api.user.useraccount.bo.UserAccountBo;
import com.yqg.api.user.useraccount.ro.UserAccountNotSessionRo;
import com.yqg.api.user.useraccounthistory.bo.UserAccountHistoryTotalBo;
import com.yqg.api.user.useraccounthistory.ro.UserAccountChangeRo;
import com.yqg.api.user.useraccounthistory.ro.UserAccountHistoryTotalRo;
import com.yqg.api.user.userbank.bo.UserBankBo;
import com.yqg.api.user.userbank.ro.UserBankRo;
import com.yqg.api.user.useruser.bo.LenderUsrBo;
import com.yqg.api.user.useruser.bo.UserBo;
import com.yqg.api.user.useruser.ro.MessageRo;
import com.yqg.api.user.useruser.ro.UserReq;
import com.yqg.api.user.useruser.ro.UserRo;
import com.yqg.api.user.useruser.ro.UserTypeSearchRo;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.dao.ExtendQueryCondition;
import com.yqg.common.enums.*;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.*;
import com.yqg.order.dao.ScatterstandardDao;
import com.yqg.order.entity.*;
import com.yqg.order.service.OrderCommonServiceImpl;
import com.yqg.order.service.creditorinfo.CreditorinfoService;
import com.yqg.order.service.orderScatterStandardRel.OrderScatterStandardRelService;
import com.yqg.order.service.orderorder.OrderOrderService;
import com.yqg.order.service.repaymentplan.RepaymentPlanService;
import com.yqg.order.service.scatterstandard.ScatterstandardService;
import com.yqg.order.service.shoppingCart.ShoppingCartService;
import com.yqg.order.service.third.PayAccountHistoryService;
import com.yqg.order.service.third.UserAccountHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

import static com.yqg.api.user.enums.MessageTypeEnum.ORDER_DEFEATED;
import static com.yqg.api.user.enums.MessageTypeEnum.ORDER_SUCCESS;

//import com.yqg.common.enums.*;

//import com.yqg.common.utils.*;

/**
 * 散标表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 11:02:36
 */
@Slf4j
@Service("scatterStandardService")
public class ScatterstandardServiceImpl extends OrderCommonServiceImpl implements ScatterstandardService {
    @Autowired
    protected ScatterstandardDao scatterstandardDao;
    @Autowired
    protected CreditorinfoService creditorinfoService;

    @Autowired
    public ShoppingCartService shoppingCartService;
    @Autowired
    protected PayAccountHistoryService payAccountHistoryService;

    public String LOCK_SCATTERSTANDARD_CREDITORNO = "lock:scatterstandard:creditorno";
    public String LOCK_ORDER_ORDERNO = "lock:order:orderno";

    @Autowired
    protected OrderOrderService orderOrderService;
    @Autowired
    protected OrderScatterStandardRelService orderScatterStandardRelService;
    @Autowired
    private UserAccountHistoryService userAccountHistoryService;
    @Autowired
    private RepaymentPlanService repaymentPlanService;


    @Autowired
    @Qualifier(value = "remoteRestTemplate")
    protected RestTemplate remoteRestTemplate;

    @Override
    public ScatterstandardDetailBo finDetaileById(ScatterstandardRo ro) throws Exception {
        ScatterstandardDetailBo bo = new ScatterstandardDetailBo();
        Scatterstandard scatterstandard = new Scatterstandard();
        scatterstandard.setDisabled(0);
        scatterstandard.setCreditorNo(ro.getCreditorNo());
        Scatterstandard one = findOne(scatterstandard);
        Creditorinfo creditorinfo = creditorinfoService.findByCreditorNo(ro.getCreditorNo());

        bo.setStatus(one.getStatus());
        bo.setYearRateFin(one.getYearRateFin());//年化收益
        bo.setAmountBuy(one.getAmountBuy().add(one.getAmountLock()));//已购买金额
        bo.setAmountApply(one.getAmountApply());//总金额
        bo.setFullAmount(creditorinfo.getAmountApply());
        bo.setCreditorType(one.getCreditorType());
        if (one.getCreditorType() == CreditorTypeEnum.STAGING.getType()){
            if (one.getStatus()>ScatterStandardStatusEnums.LOAN_SUCCESS.getCode()){
                RepaymentPlanRo repaymentPlanRo = new RepaymentPlanRo();
                repaymentPlanRo.setCreditorNo(ro.getCreditorNo());
                List<RepaymentPlanBo> repaymentPlanList = repaymentPlanService.findRepaymentPlanList(repaymentPlanRo);
                bo.setRefundPlanList(repaymentPlanList);
            }else{
                List<RepaymentPlanBo> objects = new ArrayList<>();
                int limitCount = Integer.parseInt(creditorinfo.getDetail());
                BigDecimal amount = creditorinfo.getAmountApply().divide(new BigDecimal(limitCount), 4);
                amount = new BigDecimal(amount.setScale(-2, BigDecimal.ROUND_DOWN).intValue());
                for (int i = 0; i < limitCount; i++) {
                    RepaymentPlanBo planBo = new RepaymentPlanBo();
                    planBo.setPeriodNo(i+1);
                    planBo.setStatus(StageStatusEnum.REFUND_WATING.getCode());
                    planBo.setRefundIngAmount(amount);
                    if (one.getTerm().endsWith("m")) {
                        String month = one.getTerm().replaceAll("m", "");
                        int oneterm = Integer.parseInt(month)/limitCount;
                        Calendar c = Calendar.getInstance();
                        c.setTime(new Date());
                        c.add(Calendar.MONTH, (i+1) * oneterm);
                        planBo.setRefundIngTime(c.getTime());
                    } else if (one.getTerm().endsWith("w")) {
                        String week = one.getTerm().replaceAll("w", "");
                        int oneterm = Integer.parseInt(week)/limitCount;
                        Calendar c = Calendar.getInstance();
                        c.setTime(new Date());
                        c.add(Calendar.WEEK_OF_YEAR, (i+1) * oneterm);
                        planBo.setRefundIngTime(c.getTime());
                    }
                    objects.add(planBo);
                }
                bo.setRefundPlanList(objects);
            }
        }else{
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            if (one.getStatus()>ScatterStandardStatusEnums.LOAN_SUCCESS.getCode()){
                bo.setRefundIngTime(df.format(one.getRefundIngTime()));
            } else if (one.getTerm().endsWith("d")) {
                String day = one.getTerm().replaceAll("d", "");
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
                bo.setRefundIngTime(df.format(c.getTime()));
            } else if (one.getTerm().endsWith("m")) {
                String month = one.getTerm().replaceAll("m", "");
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.MONTH, Integer.parseInt(month));
                bo.setRefundIngTime(df.format(c.getTime()));
            } else if (one.getTerm().endsWith("w")) {
                String month = one.getTerm().replaceAll("w", "");
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.WEEK_OF_YEAR, Integer.parseInt(month));
                bo.setRefundIngTime(df.format(c.getTime()));
            }
        }
        bo.setTerm(one.getTerm());//借款期限
        bo.setBorrowingPurposes(creditorinfo.getBorrowingPurposes());//借款用途

        OrderScatterStandardRel rel = new OrderScatterStandardRel();
        rel.setCreditorNo(ro.getCreditorNo());
        rel.setBuyUser(ro.getUserId());
        List<OrderScatterStandardRel> list = orderScatterStandardRelService.findList(rel);
        //掩码标识 是否已投资
        boolean flag = false;
        if (!CollectionUtils.isEmpty(list)) {
            for (OrderScatterStandardRel rel1:list){
                OrderOrder order = new OrderOrder();
                order.setId(rel1.getOrderNo());
                OrderOrder order1 = orderOrderService.findOne(order);
                if (order1.getStatus()== OrderStatusEnums.INVESTMEN_SUCCESS.getCode()){
                    flag = true;
                    break;
                }
            }
        }
        bo.setIsBuy(flag ? 1 : 0);
        String address = creditorinfo.getAddress().replaceAll("#", " ");
        bo.setAddress(flag ? address+" "+creditorinfo.getInhabit() : address);
        bo.setBirthday(flag ? creditorinfo.getBirty() : MaskUtils.maskString(creditorinfo.getBirty(), MaskUtils.Type.BIRTHDAY));
        bo.setIdCardNo(flag ? creditorinfo.getIdCardNo() : MaskUtils.maskString(creditorinfo.getIdCardNo(), MaskUtils.Type.IDCARDNO));
        bo.setMobileNumber(creditorinfo.getMobile());
        bo.setSex(creditorinfo.getSex());
        bo.setRealName(flag ? creditorinfo.getName() : MaskUtils.maskString(creditorinfo.getName(), MaskUtils.Type.NAME));
        LoanHistoryRo loanHistoryRo = new LoanHistoryRo();
        loanHistoryRo.setMobileNumber(creditorinfo.getMobile().toLowerCase());
        List<LoanHistiryBo> boList = creditorinfoService.selectLoanHistoryByNumber(loanHistoryRo);
        if(!boList.isEmpty())
            bo.setLoanCount(String.valueOf(boList.size()));
        else
            bo.setLoanCount("0");
        bo.setScore(creditorinfo.getCreditScore());

        UserReq userReq = new UserReq();
        userReq.setUserUuid(ro.getUserId());
        BaseResponse<UserBo> user = userService.findUserById(userReq);
        int insurance = 0;
        if(user.getData().getIsinsurance()!=null){
            insurance = user.getData().getIsinsurance();
        }
        if(insurance==1){
            bo.setInsurance(one.getAmountApply().multiply(new BigDecimal(11)).divide(new BigDecimal(100), RoundingMode.HALF_UP));
        }
        else {
            bo.setInsurance(new BigDecimal(0.0));
        }

        return bo;
    }

    @Override
    public List<Scatterstandard> findListByCreditor(List<Creditorinfo> content) throws BusinessException {
        List<Scatterstandard> scatterstandards = new ArrayList<>();
        if (CollectionUtils.isEmpty(content)) {
            return scatterstandards;
        }
        Scatterstandard entity = new Scatterstandard();
//        ExtendQueryCondition condition = new ExtendQueryCondition();
//        List<Object> list = new ArrayList<>();
//        for (Creditorinfo info : content) {
//            list.add(info.getCreditorNo());
//        }
//        condition.addInQueryMap(Scatterstandard.creditorNo_field, list);
//        entity.setExtendQueryCondition(condition);
//        scatterstandards = scatterstandardDao.findForList(entity);
        for (Creditorinfo info : content) {
            scatterstandards.add(findOneByCreditorNo(info.getCreditorNo()));
        }
        return scatterstandards;
    }

    @Override
    public Scatterstandard findOneByCreditorNo(String creditorNo) throws BusinessException {
        Scatterstandard entity = new Scatterstandard();
        entity.setDisabled(0);
        entity.setCreditorNo(creditorNo);
        return this.scatterstandardDao.findOne(entity);
    }

    @Override
    @Transactional
    public void batchLoan() throws Exception {
        //查询所有满标待放款订单
        Scatterstandard entity = new Scatterstandard();
        entity.setStatus(ScatterStandardStatusEnums.FULL_SCALE.getCode());
        List<Scatterstandard> list = findList(entity);
        for (Scatterstandard scatterstandard : list) {
            //请求放款
            loan(scatterstandard);
        }
    }

    @Override
    @Transactional
    public void repaySuccess(String creditorNo, OrderSuccessRo ro) throws Exception {
        log.info("还款成功");

        Scatterstandard scatterstandard = new Scatterstandard();
        scatterstandard.setCreditorNo(creditorNo);
        scatterstandard = this.findOne(scatterstandard);

        if (scatterstandard.getCreditorType().equals(CreditorTypeEnum.EXTENSION.getType())){

        }
//        rizky : dont need to check overduefee,already handled by payrest
//        BigDecimal overFee = scatterstandard.getInterest().add(scatterstandard.getOverdueRate()).add(scatterstandard.getOverdueFee());
//        if (overFee.compareTo(new BigDecimal("0.0")) > 1) {
//
//            log.info("公司收入账户Glotech逾期罚息&逾期服务费");
//            //获取托管账户账号
//            Creditorinfo creditorinfo = creditorinfoService.findByCreditorNo(creditorNo);
//            String bankCode = creditorinfo.getBankCode();
////            UserAccountBo escrowAccount = this.getEscrowAccount(bankCode);
//
//            //（改）CIMB——>所有非BCA，BNI银行的
//            if (!creditorinfo.getBankCode().equals("BCA") && !creditorinfo.getBankCode().equals("BNI")){
//                bankCode="CIMB";
//            }
//
//            UserAccountBo incomeAccount = this.getIncomeAccount(bankCode);
////            UserReq userReq = new UserReq();
////            userReq.setUserUuid(incomeAccount.getUserUuid());
////            BaseResponse<UserBo> incomeUser = userService.findUserById(userReq);
//            UserBankRo userBankRo = new UserBankRo();
//            userBankRo.setUserUuid(incomeAccount.getUserUuid());
//            BaseResponse<UserBankBo> incomeUser = userBankService.getUserBankInfo(userBankRo);
//
//            LoanRo ro = new LoanRo();
//            ro.setOrderNo(creditorNo);
//            ro.setAmount(overFee);
//            ro.setCardholder(incomeUser.getData().getBankCardName());
////            ro.setBankCode(creditorinfo.getBankCode());
//            ro.setBankCode(bankCode);
//            ro.setBankNumberNo(incomeUser.getData().getBankNumberNo());
////            ro.setDescription("逾期罚息&逾期服务费");
//            ro.setDescription(TransTypeEnum.PAYBACK_INCOME.getDisburseType());
//            ro.setDisburseChannel(bankCode);
//            ro.setTransType(TransTypeEnum.PAYBACK_INCOME.getDisburseType());
//            if (payService.loan(ro).isSuccess()) {
//                log.info("更改散标状态为逾期已还款");
//                scatterstandard.setStatus(ScatterStandardStatusEnums.OVERDUE_RESOLVED.getCode());
//
//            }
////            payService.loan(ro);
//
//        } else {
        if (scatterstandard.getCreditorType().equals(CreditorTypeEnum.STAGING.getType())){
            RepaymentPlan repaymentPlan = new RepaymentPlan();
            repaymentPlan.setCreditorNo(creditorNo);
            repaymentPlan.setStatus(StageStatusEnum.REFUNDING.getCode());
            repaymentPlan = this.repaymentPlanService.findOne(repaymentPlan);
            logger.info("change staging repayment status to RESOLVED");
            repaymentPlan.setStatus(StageStatusEnum.RESOLVED.getCode());
            this.repaymentPlanService.updateOne(repaymentPlan);
            if(scatterstandard.getLimitCount().equals(repaymentPlan.getPeriodNo().toString())){
                logger.info("this is last staging order, update scattered to COMPLETED");
                scatterstandard.setStatus(ScatterStandardStatusEnums.RESOLVED.getCode());
                scatterstandard.setDepositStatus("COMPLETED");
                updateOne(scatterstandard);
                OrderScatterStandardRel orderScatterStandardRel = new OrderScatterStandardRel();
                orderScatterStandardRel.setCreditorNo(creditorNo);
                orderScatterStandardRel.setDisabled(0);
                List<OrderScatterStandardRel> list = orderScatterStandardRelService.findList(orderScatterStandardRel);
                for (OrderScatterStandardRel rel : list) {
                    String buyUserId = rel.getBuyUser();
//                    if(ro.getInsurance().compareTo(new BigDecimal(0)) > 0){
//                        UserAccountChangeRo userAccountChangeRo = new UserAccountChangeRo();
//                        userAccountChangeRo.setUserUuid(buyUserId);
//                        userAccountChangeRo.setAmount(ro.getInsurance());
//                        userAccountChangeRo.setBusinessType(UserAccountBusinessTypeEnum.PAYBACK_INCOME.getEnname());
//                        userAccountChangeRo.setTradeInfo("Transfer the available balance to the amount invested");
//                        userAccountService.used2current(userAccountChangeRo);
//
//                    }
                    if(ro.getServiceFee().compareTo(new BigDecimal(0)) > 0){
                        UserAccountChangeRo userAccountChangeRo = new UserAccountChangeRo();
                        userAccountChangeRo.setUserUuid(buyUserId);
                        userAccountChangeRo.setAmount(ro.getServiceFee());
                        userAccountChangeRo.setBusinessType(UserAccountBusinessTypeEnum.INCOME.getEnname());
                        userAccountChangeRo.setTradeInfo("Income for lender");
                        userAccountService.addCurrentBlance(userAccountChangeRo);

                    }
                    BigDecimal buyAmount = rel.getAmount();
                    UserAccountChangeRo userAccountChangeRo = new UserAccountChangeRo();
                    userAccountChangeRo.setUserUuid(buyUserId);
                    userAccountChangeRo.setAmount(buyAmount);

                    userAccountChangeRo.setBusinessType(UserAccountBusinessTypeEnum.PAYBACK_INCOME.getEnname());
                    userAccountChangeRo.setTradeInfo("Order Completed,return balance to lender");
                    userAccountService.used2current(userAccountChangeRo);
                    log.info("The user [{}] order [{}] purchased the bulk bid [{}] amount [{}], and transferred the available balance to the amount invested", buyUserId, rel.getOrderNo(), creditorNo, buyAmount);

                }
            }
        }
        else {
            log.info("change order repayment status to RESOLVED");
            scatterstandard.setStatus(ScatterStandardStatusEnums.RESOLVED.getCode());
//        }
            scatterstandard.setDepositStatus("COMPLETED");
            updateOne(scatterstandard);
            OrderScatterStandardRel orderScatterStandardRel = new OrderScatterStandardRel();
            orderScatterStandardRel.setCreditorNo(creditorNo);
            orderScatterStandardRel.setDisabled(0);
            List<OrderScatterStandardRel> list = orderScatterStandardRelService.findList(orderScatterStandardRel);
            for (OrderScatterStandardRel rel : list) {
                String buyUserId = rel.getBuyUser();
//                if(ro.getInsurance().compareTo(new BigDecimal(0)) > 0){
//                    UserAccountChangeRo userAccountChangeRo = new UserAccountChangeRo();
//                    userAccountChangeRo.setUserUuid(buyUserId);
//                    userAccountChangeRo.setAmount(ro.getInsurance());
//                    userAccountChangeRo.setBusinessType(UserAccountBusinessTypeEnum.PAYBACK_INCOME.getEnname());
//                    userAccountChangeRo.setTradeInfo("Transfer the available balance to the amount invested");
//                    userAccountService.used2current(userAccountChangeRo);
//
//                }
                if(ro.getServiceFee().compareTo(new BigDecimal(0)) > 0){
                    UserAccountChangeRo userAccountChangeRo = new UserAccountChangeRo();
                    userAccountChangeRo.setUserUuid(buyUserId);
                    userAccountChangeRo.setAmount(ro.getServiceFee());
                    userAccountChangeRo.setBusinessType(UserAccountBusinessTypeEnum.INCOME.getEnname());
                    userAccountChangeRo.setTradeInfo("Transfer the available balance to the amount invested");
                    userAccountService.addCurrentBlance(userAccountChangeRo);

                }

                BigDecimal buyAmount = rel.getAmount();
                UserAccountChangeRo userAccountChangeRo = new UserAccountChangeRo();
                userAccountChangeRo.setUserUuid(buyUserId);
                userAccountChangeRo.setAmount(buyAmount);

//            userAccountChangeRo.setBusinessType("还款清分");
                userAccountChangeRo.setBusinessType(UserAccountBusinessTypeEnum.PAYBACK_INCOME.getEnname());
                userAccountChangeRo.setTradeInfo("Transfer the available balance to the amount invested");
                userAccountService.used2current(userAccountChangeRo);
                log.info("The user [{}] order [{}] purchased the bulk bid [{}] amount [{}], and transferred the available balance to the amount invested", buyUserId, rel.getOrderNo(), creditorNo, buyAmount);

            }
        }



    }

    @Override
    @Transactional
    public void repayFail(String creditorNo) throws Exception {
        log.info("还款失败");

        Scatterstandard scatterstandard = new Scatterstandard();
        scatterstandard.setCreditorNo(creditorNo);
        scatterstandard = this.findOne(scatterstandard);

        scatterstandard.setStatus(ScatterStandardStatusEnums.LOAN_DISTRIBUTION_SUCCESS.getCode());
        scatterstandard.setUpdateTime(new Date());
        this.updateOne(scatterstandard);
    }


    @Override
    @Transactional
    public void serviceFeeSuccess(String creditorNo) throws BusinessException {
        log.info("服务费打款成功---继续清分");
        Creditorinfo cre = creditorinfoService.findByCreditorNo(creditorNo);
        Scatterstandard scatterstandard = this.findOneByCreditorNo(creditorNo);
        scatterstandard.setStatus(ScatterStandardStatusEnums.LOAN_DISTRIBUTION_SUCCESS.getCode());
        this.updateOne(scatterstandard);

        BigDecimal incresfee= BigDecimal.ZERO;
        if (cre.getCreditorType().equals(CreditorTypeEnum.STAGING.getType())){
            // TODO: 2019/7/3 分期债权利息计算 待测试
            for (int i=1;i<=Integer.parseInt(scatterstandard.getLimitCount());i++){
                //  每期还款金额
                BigDecimal singleAmount = scatterstandard.getAmountApply().divide(new BigDecimal(scatterstandard.getLimitCount()));
                LocalDate localDate = LocalDate.now();
                log.info("localDate[{}]",localDate);
                LocalDate nextDate = LocalDate.of(localDate.getYear(),localDate.getMonthValue()+i,localDate.getDayOfMonth());
                log.info("nextDate[{}]",nextDate);

                Period period = Period.between(localDate,nextDate);
                BigDecimal day = new BigDecimal(period.getDays());
                log.info("相隔[{}]天",nextDate);
                incresfee = singleAmount.multiply(day).multiply(scatterstandard.getYearRateFin()).divide(new BigDecimal(365),4);

            }
        }else{
            incresfee = cre.getAmountApply().multiply(scatterstandard.getYearRateFin()).multiply(new BigDecimal(termToDays(scatterstandard.getTerm()))).divide(new BigDecimal(365),4);
            log.info("债权[{}]利息应为[{}]",creditorNo,incresfee);
        }

        incresfee= new BigDecimal(incresfee.setScale(-2, BigDecimal.ROUND_DOWN).intValue());
        log.info("债权[{}]利息 取百位 应为[{}]",creditorNo,incresfee);
        //公司收入账户Glotech  25%服务费 (砍头息-利息）*25%（25%可以配置)
        BigDecimal income4Company = (cre.getServiceFee().subtract(incresfee)).multiply(new BigDecimal("0.25"));
        log.info("债权[{}]公司收入应为[{}]",creditorNo,income4Company);
        income4Company= new BigDecimal(income4Company.setScale(-2, BigDecimal.ROUND_DOWN).intValue());
        log.info("债权[{}]公司收入 取百位 应为[{}]",creditorNo,income4Company);


        OrderScatterStandardRel rel = new OrderScatterStandardRel();
        rel.setCreditorNo(creditorNo);
        rel.setDisabled(0);
        List<OrderScatterStandardRel> orderScatterStandardRels = orderScatterStandardRelService.findList(rel);
        for (OrderScatterStandardRel orderScatterStandardRel : orderScatterStandardRels) {
            log.info("放款清分成功-冻结转在投");
            UserAccountChangeRo userAccountChangeRo = new UserAccountChangeRo();
            userAccountChangeRo.setAmount(orderScatterStandardRel.getAmount());
            userAccountChangeRo.setUserUuid(orderScatterStandardRel.getBuyUser());
//                            userAccountChangeRo.setBusinessType("购买债权成功");
            userAccountChangeRo.setBusinessType(UserAccountBusinessTypeEnum.BUY_CREDITOR_SUCCESS.getEnname());
            userAccountChangeRo.setTradeInfo("放款清分成功-冻结转在投");
            userAccountService.lock2used(userAccountChangeRo);


                log.info("投资人的可用资金利息  投资本金*年化收益/365*借款期限   *  购买标的百分比");
              BigDecimal  amountRo= incresfee.multiply(orderScatterStandardRel.getAmount()).divide(cre.getAmountApply(),4);

            log.info("截取前 amountRo[{}]",amountRo);
            amountRo= new BigDecimal(amountRo.setScale(-2, BigDecimal.ROUND_DOWN).intValue());
            log.info("截取后 amountRo[{}]",amountRo);
            userAccountChangeRo.setAmount(amountRo);
//                            userAccountChangeRo.setBusinessType("利息收入");
            userAccountChangeRo.setBusinessType(UserAccountBusinessTypeEnum.INCOME.getEnname());
            userAccountChangeRo.setTradeInfo("放款清分成功-投资人的可用资金利息 增加余额");
            userAccountService.addCurrentBlance(userAccountChangeRo);

            if (orderScatterStandardRel.getUserType().equals(UserTypeEnum.SUPPER_INVESTORS.getType())) {

                BigDecimal amount = (cre.getServiceFee().subtract(incresfee).subtract(income4Company)).multiply(orderScatterStandardRel.getAmount()).divide(cre.getAmountApply(),4);
                amount= new BigDecimal(amount.setScale(-2, BigDecimal.ROUND_DOWN).intValue());
                userAccountChangeRo.setAmount(amount);
//                                userAccountChangeRo.setBusinessType("75%服务费");
                userAccountChangeRo.setBusinessType(UserAccountBusinessTypeEnum.SERVICE_FEE.getEnname());
                userAccountChangeRo.setTradeInfo("放款清分成功-75%服务费 超级投资人特有 增加余额");
                userAccountService.addCurrentBlance(userAccountChangeRo);

            } else if (orderScatterStandardRel.getUserType().equals(UserTypeEnum.BRANCH_INVESTORS.getType())) {

                //机构投资人所有放款清分收入先存入redis 在一天后分给超级投资人（按照超级投资人在当天的投资金额在所有超级投资人的投资总金额的占比来进行清分）
                //此处只做累加当天机构投资人放款清分总金额
                log.info("获取当天机构投资人放款清分总金额");
                Calendar calendar = Calendar.getInstance();
                BigDecimal decimal = getBreachDisburseAccount(calendar);
                BigDecimal income = cre.getServiceFee().subtract(incresfee).subtract(income4Company).multiply(orderScatterStandardRel.getAmount().divide(cre.getAmountApply()));
                int i = calendar.get(Calendar.DAY_OF_MONTH);
                BigDecimal after = decimal.add(income);
                log.info("机构投资人清分收入：{} 清分前总金额:{}  清分后总金额:{}",income, decimal, after);
                redisUtil.set(RedisKeyEnums.BRANCH_DISBURSE_SUM_KEY.appendToDefaultKey(i + ""),after);
            }
        }

    }


    //    @Transactional
    private void loan(Scatterstandard scatterstandard) throws Exception {
        Creditorinfo creditorinfo = creditorinfoService.findByCreditorNo(scatterstandard.getCreditorNo());


         if(creditorinfo.getCreditorType().equals(CreditorTypeEnum.EXTENSION.getType())){

            String oldCreditorNo = creditorinfo.getDetail();
            log.info("展期关联订单[{}]",oldCreditorNo);

            Scatterstandard oldScatterstandard = new Scatterstandard();
            oldScatterstandard.setCreditorNo(oldCreditorNo);
                Scatterstandard finaloldScatterstandard = this.findOne(oldScatterstandard);

            OrderScatterStandardRel rel = new OrderScatterStandardRel();
            rel.setCreditorNo(oldCreditorNo);
            List<OrderScatterStandardRel> rels = orderScatterStandardRelService.findList(rel);

            BigDecimal newAmount = scatterstandard.getAmountApply();
            log.info("展期订单[{}]本金[{}]",scatterstandard.getCreditorNo(),newAmount);

            final boolean[] isok = {true};


            rels.forEach(r->{

                String buyUserId = r.getBuyUser();
                BigDecimal buyAmount = r.getAmount();

                BigDecimal actAmount= newAmount.multiply(buyAmount).divide(finaloldScatterstandard.getAmountApply());

                UserAccountChangeRo userAccountChangeRo = new UserAccountChangeRo();
                userAccountChangeRo.setUserUuid(buyUserId);
                userAccountChangeRo.setAmount(actAmount);

                userAccountChangeRo.setBusinessType(UserAccountBusinessTypeEnum.PAYBACK_INCOME.getEnname());
                userAccountChangeRo.setTradeInfo("展期后被购买 按投资比例清分剩余本金 在投金额转可用余额");
                try {
                    if (!userAccountService.used2current(userAccountChangeRo).isSuccess()){
                        log.info("展期后被购买 按投资比例清分剩余本金 在投金额转可用余额  处理失败");
                        isok[0] =false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                log.info("用户[{}]订单[{}]购买了散标[{}] 展期后被购买清分剩余本金 金额[{}],在投金额转可用余额", buyUserId, rel.getOrderNo(), oldCreditorNo, actAmount);

            });

            if (isok[0]) {

                log.info("更改展期后散标[{}]状态为处理中", scatterstandard.getCreditorNo());

                scatterstandard.setStatus(ScatterStandardStatusEnums.LOAN_WATING.getCode());
                updateOne(scatterstandard);

                log.info("更改展期前散标[{}]状态为 还款清分成功", oldCreditorNo);
                finaloldScatterstandard.setStatus(ScatterStandardStatusEnums.REFUND_DISTRIBUTION_SUCCESS.getCode());
                finaloldScatterstandard.setUpdateTime(new Date());
                this.updateOne(finaloldScatterstandard);


                log.info("清分服务费给公司收入账户");
                BigDecimal incresfee = creditorinfo.getAmountApply().multiply(scatterstandard.getYearRateFin()).multiply(new BigDecimal(termToDays(scatterstandard.getTerm()))).divide(new BigDecimal(365), 4);
                incresfee = new BigDecimal(incresfee.setScale(-2, BigDecimal.ROUND_DOWN).intValue());

                //公司收入账户Glotech  25%服务费 (砍头息-利息）*25%（25%可以配置)
                BigDecimal income4Company = (creditorinfo.getServiceFee().subtract(incresfee)).multiply(new BigDecimal("0.25"));
                income4Company = new BigDecimal(income4Company.setScale(-2, BigDecimal.ROUND_DOWN).intValue());

                String bankCode = creditorinfo.getBankCode();

                //（改）CIMB——>所有非BCA，BNI银行的
                if (!creditorinfo.getBankCode().equals("BCA") && !creditorinfo.getBankCode().equals("BNI")) {
                    bankCode = "CIMB";
                }

                UserAccountBo incomeAccount = this.getIncomeAccount(bankCode);

                UserBankRo userBankRo = new UserBankRo();
                userBankRo.setUserUuid(incomeAccount.getUserUuid());
                BaseResponse<UserBankBo> incomeUser = userBankService.getUserBankInfo(userBankRo);
                LoanRo ro = new LoanRo();
                ro.setOrderNo(creditorinfo.getCreditorNo());
                ro.setAmount(income4Company);
                ro.setCardholder(incomeUser.getData().getBankCardName());
                ro.setBankCode(bankCode);
                ro.setBankNumberNo(incomeUser.getData().getBankNumberNo());
                ro.setDescription(TransTypeEnum.SERVICE_FEE.getDisburseType());
                ro.setDisburseChannel(bankCode);
                ro.setTransType(TransTypeEnum.SERVICE_FEE.getDisburseType());
                if (payService.loan(ro).isSuccess()) {
                    log.info("公司收入账户Glotech  25%服务费 (砍头息-利息）*25%（25%可以配置)");
                    log.info("服务费打款成功后 新债权投资人 资金冻结转在投");
                }
            }

        }else{
             log.info("Installment(staging) and normal order");
             LoanRo ro = new LoanRo();
             ro.setOrderNo(scatterstandard.getCreditorNo());
             ro.setCreditorNo(scatterstandard.getCreditorNo());
             ro.setAmount(scatterstandard.getAmountApply());
             ro.setCardholder(creditorinfo.getBankCardholder());
             ro.setBankCode(creditorinfo.getBankCode());
             ro.setBankNumberNo(creditorinfo.getBankNumber());
//        ro.setDescription("放款");
             ro.setDescription("Disbursed from Doit");

             //（改）CIMB——>所有非BCA，BNI银行的
             if (!creditorinfo.getBankCode().equals("BCA") && !creditorinfo.getBankCode().equals("BNI")) {
                 ro.setDisburseChannel("CIMB");
             } else {
                 ro.setDisburseChannel(creditorinfo.getBankCode());
             }
             ro.setTransType(TransTypeEnum.LOAN.getDisburseType());
             log.info("放款参数：[{}]", ro);
             BaseResponse<JSONObject> payResponse = payService.loan(ro);
             log.info("放款返回参数【{}】", payResponse);
             if (payResponse.isSuccess()) {
                 log.info("更改散标状态为处理中");
                 scatterstandard.setStatus(ScatterStandardStatusEnums.LOAN_WATING.getCode());
                 updateOne(scatterstandard);
             }
         }

    }


    @Override
    public Scatterstandard findById(String id) throws BusinessException {
        return this.scatterstandardDao.findOneById(id, new Scatterstandard());
    }

    @Override
    public <E> E findById(String id, Class<E> boClass) throws BusinessException {
        Scatterstandard scatterstandard = findById(id);
        E bo = BeanCoypUtil.copyToNewObject(scatterstandard, boClass);
        return bo;
    }

    @Override
    public Scatterstandard findOne(Scatterstandard entity) throws BusinessException {
        return this.scatterstandardDao.findOne(entity);
    }

    @Override
    public <E> E findOne(Scatterstandard entity, Class<E> boClass) throws BusinessException {
        Scatterstandard scatterstandard = findOne(entity);
        E bo = BeanCoypUtil.copyToNewObject(scatterstandard, boClass);
        return bo;
    }


    @Override
    public List<Scatterstandard> findList(Scatterstandard entity) throws BusinessException {
        return this.scatterstandardDao.findForList(entity);
    }

    @Override
    public <E> List<E> findList(Scatterstandard entity, Class<E> boClass) throws BusinessException {
        List<Scatterstandard> scatterstandardList = findList(entity);
        List<E> boList = new ArrayList<>();
        for (Scatterstandard scatterstandard : scatterstandardList) {
            E bo = BeanCoypUtil.copyToNewObject(scatterstandard, boClass);
            boList.add(bo);
        }
        return boList;
    }

    @Override
    @Transactional
    public String addOne(Scatterstandard entity) throws BusinessException {
        return scatterstandardDao.addOne(entity);
    }

    @Override
    @Transactional
    public void deleteById(String id) throws BusinessException {
        scatterstandardDao.deleteOne(findById(id));
    }

    @Override
    @Transactional
    public void deleteOne(Scatterstandard entity) throws BusinessException {
        this.scatterstandardDao.deleteOne(entity);
    }

    @Override
    @Transactional
    public void updateOne(Scatterstandard entity) throws BusinessException {
        this.scatterstandardDao.updateOne(entity);
        if (entity.getStatus() == ScatterStandardStatusEnums.LOAN_ERROR.getCode()
                || entity.getStatus() == ScatterStandardStatusEnums.LOAN_SUCCESS.getCode()
                || entity.getStatus() == ScatterStandardStatusEnums.RESOLVED.getCode()) {
            orderOrderService.pushOrderStatusToDoit(entity);
        }
    }

    /**
     * 锁定散标金额
     *
     * @param amount
     * @param creditorNo
     * @return
     * @throws BusinessException
     */
    private Scatterstandard lockAmount(BigDecimal amount, String creditorNo) throws BusinessException {
        Scatterstandard scatterstandard = new Scatterstandard();
        if (redisUtil.tryLock(LOCK_SCATTERSTANDARD_CREDITORNO + creditorNo, 10)) {
            scatterstandard.setCreditorNo(creditorNo);
            scatterstandard = this.findOne(scatterstandard);
            //Bulk bid table locks shares tentatively 30 minutes
            scatterstandard.setAmountLock(scatterstandard.getAmountLock().add(amount));
            scatterstandard.setStatus(ScatterStandardStatusEnums.READY_TO_SEND_DOCUMENT.getCode());
            //The purchase amount is greater than the current purchase amount
            if (scatterstandard.getAmountBuy().add(scatterstandard.getAmountLock()).compareTo(scatterstandard.getAmountApply()) == 1) {
                redisUtil.releaseLock(LOCK_SCATTERSTANDARD_CREDITORNO + creditorNo);
                throw new BusinessException(PayExceptionEnums.BUY_OVER_CURRENT);
            }
            if (scatterstandard.getAmountApply().compareTo(scatterstandard.getAmountLock().add(scatterstandard.getAmountBuy()))==0){
                log.info("购买加锁定金额满 债权改为不可购买 则不再查出");
                Creditorinfo creditorinfo = creditorinfoService.findByCreditorNo(creditorNo);
                creditorinfo.setStatus(1);// 不可购买
                creditorinfo.setUpdateTime(new Date());
                creditorinfo.setRemark("购买加锁定金额满 债权改为不可购买 则不再查出");
                creditorinfoService.updateOne(creditorinfo);
            }
            this.updateOne(scatterstandard);
            redisUtil.releaseLock(LOCK_SCATTERSTANDARD_CREDITORNO + creditorNo);
        } else {
            log.info("锁定散标金额---获取锁失败{}", LOCK_SCATTERSTANDARD_CREDITORNO + creditorNo);
            throw new BusinessException(BaseExceptionEnums.SYSTERM_ERROR);
        }
        return scatterstandard;
    }

//    /**
//     * 锁定散标金额(ALLIN)
//     *
//     * @param amount
//     * @param creditorNo
//     * @return
//     * @throws BusinessException
//     */
//    public Scatterstandard lockAmount(BigDecimal amount, String creditorNo, boolean isAllIn) throws BusinessException {
//        Scatterstandard scatterstandard = new Scatterstandard();
//        if (redisUtil.tryLock(LOCK_SCATTERSTANDARD_CREDITORNO + creditorNo, 10)) {
//            scatterstandard.setCreditorNo(creditorNo);
//            scatterstandard = scatterstandardDao.findOne(scatterstandard);
//            if (isAllIn) {
//                amount = scatterstandard.getAmountApply().subtract(scatterstandard.getAmountLock()).subtract(scatterstandard.getAmountBuy());
//            }
//            //散标表锁定份额 暂定30分钟
//            scatterstandard.setAmountLock(scatterstandard.getAmountLock().add(amount));
//            //购买金额大于当前可购买金额
//            if (scatterstandard.getAmountBuy().add(scatterstandard.getAmountLock()).compareTo(scatterstandard.getAmountApply()) == 1) {
//                redisUtil.releaseLock(LOCK_SCATTERSTANDARD_CREDITORNO + creditorNo);
//                throw new BusinessException(PayExceptionEnums.BUY_OVER_CURRENT);
//            }
//            scatterstandardDao.updateOne(scatterstandard);
//            redisUtil.releaseLock(LOCK_SCATTERSTANDARD_CREDITORNO + creditorNo);
//        } else {
//            log.info("锁定散标金额---获取锁失败{}", LOCK_SCATTERSTANDARD_CREDITORNO + creditorNo);
//            throw new BusinessException(BaseExceptionEnums.SYSTERM_ERROR);
//        }
//        return scatterstandard;
//    }


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

    @Transactional
    @Override
    public boolean checkDigisign(SignRo ro) throws BusinessException {
        boolean check = false;
        if(ro.getOrders().size()>0){
            SignStatusBo statusBo = new SignStatusBo();
            statusBo.setOrderNo(ro.getOrders().get(0));
            JSONObject result = executeHttpPostRequest(doitConfig.doItLoanUrl + "/digisign/check-document-status",statusBo);
            if(result!=null) {
                try {
                    JSONObject json = result.getJSONObject("JSONFile");
                    if(json.getJSONArray("signed")!=null){
                        // one of the party already sign,assume its the lender
                        logger.info("signed exist!");
                        updateOrderToSignSuccess(ro.getOrders());
                        check = true;
                    }
                    else {
                        throw new BusinessException(PayExceptionEnums.DIGISIGN_PENDING);
                    }
                }
                catch (Exception e){
                    logger.info("error " + e.getMessage());
                    throw new BusinessException(PayExceptionEnums.FAILED_TO_SEND_DIGISIGN);
                }
            }
            else {
                throw new BusinessException(PayExceptionEnums.FAILED_TO_SEND_DIGISIGN);
            }
        }
        else {
            throw new BusinessException(PayExceptionEnums.NO_ORDER_TO_CHECK);
        }
        return check;
    }

    @Transactional
    public void updateOrderToSignSuccess(List<String> ro) throws BusinessException {
        Scatterstandard scatterstandard = new Scatterstandard();
        ExtendQueryCondition extendQueryCondition = new ExtendQueryCondition();
        List<Object> orderNo = new ArrayList<>(ro);
        extendQueryCondition.addInQueryMap(Scatterstandard.creditorNo_field, orderNo);
        scatterstandard.setExtendQueryCondition(extendQueryCondition);
        List<Scatterstandard> scatterstandards = scatterstandardDao.findForList(scatterstandard);
        for (Scatterstandard std : scatterstandards) {
            std.setStatus(ScatterStandardStatusEnums.SIGN_SUCCESS.getCode());
            scatterstandardDao.updateOne(std);
        }
    }

    @Override
    public String signOrder(SignRo ro) throws BusinessException {
        String url;
        SignBo signBo = new SignBo();
        signBo.setOrderNo(ro.getOrders());
        JSONObject result = executeHttpPostRequest(doitConfig.doItLoanUrl + "/digisign/bulk-sign",signBo);
        if(result!=null) {
            try {
                JSONObject json = result.getJSONObject("JSONFile");
                url = json.getString("link");
                logger.info("link digisign = " + url);
                if(StringUtils.isEmpty(url)){
                    SignRo signRo = new SignRo();
                    signBo.setOrderNo(ro.getOrders());
                    if(checkDigisign(signRo)){
                        throw new BusinessException(PayExceptionEnums.ORDER_ALREADY_SIGN);
                    }
                    else {
                        throw new BusinessException(PayExceptionEnums.UNKNOWN_ORDER);
                    }
                }
            }
            catch (Exception e){
                logger.info("error " + e.getMessage());
                throw new BusinessException(PayExceptionEnums.FAILED_TO_SEND_DIGISIGN);
            }
        }
        else {
            throw new BusinessException(PayExceptionEnums.FAILED_TO_SEND_DIGISIGN);
        }
        return url;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sendDigisign(DigisignRo ro) throws BusinessException {
        SignStatusBo statusBo = new SignStatusBo();
        statusBo.setOrderNo(ro.getOrderNo());
        String result = executeHttpPostRequestString(doitConfig.doItLoanUrl + "/digisign/send-document",statusBo);
        if(result!=null) {
            if(result.equals("true")) {
                Scatterstandard entity = new Scatterstandard();
                entity.setCreditorNo(ro.getOrderNo());
                Scatterstandard scatterstandard = scatterstandardDao.findOne(entity);
                if (scatterstandard != null) {
                    scatterstandard.setStatus(ScatterStandardStatusEnums.READY_TO_SIGN.getCode());
                    scatterstandardDao.updateOne(scatterstandard);
                } else {
                    throw new BusinessException(PayExceptionEnums.ORDER_NOT_FOUND);
                }
            }else
                throw new BusinessException(PayExceptionEnums.FAILED_TO_SEND_DIGISIGN);

        }
        else {
            throw new BusinessException(PayExceptionEnums.FAILED_TO_SEND_DIGISIGN);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public IncomeBo immediateInvestment(InvestmentRo investmentRo) throws BusinessException {
        IncomeBo incomeBo = new IncomeBo();
        String userUuid = investmentRo.getUserUuid();
        UserReq userReq = new UserReq();
        userReq.setUserUuid(userUuid);
        BaseResponse<UserBo> user = userService.findUserById(userReq);
        if (user == null || null == user.getData()) {
            log.info("No such user");
            throw new BusinessException(PayExceptionEnums.ACCOUNT_ERROR);
        } else {
            log.info("Current operating user: [{}]", user.getData());
        }
//        String userName = user.getData().getUserName();
//        String mobileNumber = user.getData().getMobileNumber();
        Integer userType = user.getData().getUserType();
        int isInsurance = 0;
        if(user.getData().getIsinsurance()!=null)
            isInsurance = user.getData().getIsinsurance();
        boolean allIn = investmentRo.isAllIn();


        log.info("Check user card");
        userAuthBankInfo(userUuid);
        log.info("Check if the user has a doit loan");
        isOnBorrow(userUuid);

        log.info("Add order record-status is investment processing");
        BigDecimal orderAmount = new BigDecimal("0.0");
//        if (!StringUtils.isEmpty(investmentRo.getCreditorNo())) {
//            String creditorNo = investmentRo.getCreditorNo();
//            BigDecimal buyAmount = investmentRo.getAmount();
//            orderAmount = orderAmount.add(preBuy(userUuid, allIn, orderNo, creditorNo, buyAmount,userType));
//
//        } else {

        Map<String, String> cartMap = shoppingCartService.getCartByUserId(userUuid);
        Set<String> set = cartMap.keySet();
        //get last not complete order
        OrderOrder lastOrder  = new OrderOrder();
        lastOrder.setUserUuid(userUuid);
        lastOrder.setDisabled(0);
        lastOrder.setStatus(OrderStatusEnums.INVESTMENTING.getCode());
        OrderOrder last = orderOrderService.findOne(lastOrder);
        String orderNo;
        if(last!=null) {
            OrderScatterStandardRel rel = new OrderScatterStandardRel();
            rel.setOrderNo(last.getId());
            List<OrderScatterStandardRel> orderScatterStandardRels = orderScatterStandardRelService.findList(rel);

            //check if theres new order
            Set<String> setScatter = new HashSet<>();
            for (OrderScatterStandardRel rel1 : orderScatterStandardRels) {
                setScatter.add(rel1.getCreditorNo());
            }
            if (setScatter.size() == set.size()) {
                incomeBo.setNeedPwd(true);
                incomeBo.setNeedPay(last.getAmountBuy());
                incomeBo.setOrderNo(last.getId());

//                sendNotice(userUuid, last.getId(), last.getAmountBuy());
                return incomeBo;
            }
            set.removeAll(setScatter);
            orderNo = last.getId();
            orderAmount = last.getAmountBuy();
        }
        else
            orderNo = orderOrderService.initOrder(userUuid);
        for (String creditorNo : set) {
            Scatterstandard x = new Scatterstandard();
            x.setCreditorNo(creditorNo);
            Scatterstandard scatterstandard = scatterstandardDao.findOne(x);
            BigDecimal buyAmount = new BigDecimal(cartMap.get(creditorNo)).multiply(scatterstandard.getAmountApply()).divide(new BigDecimal(100),RoundingMode.HALF_UP);
            orderAmount = orderAmount.add(preBuy(userUuid, allIn, orderNo, creditorNo, buyAmount,userType));
        }
        if(isInsurance==1){
            orderAmount = orderAmount.multiply(new BigDecimal(111)).divide(new BigDecimal(100), RoundingMode.HALF_UP);
        }

        log.info("Update orderOrder payable amount [{}]", orderAmount);
        OrderOrder orderOrder = orderOrderService.findById(orderNo);
        orderOrder.setApplyBuy(orderAmount);
        orderOrderService.updateOne(orderOrder);

        log.info("付款流程 begin");


        UserAccountNotSessionRo useraccountRo = new UserAccountNotSessionRo();
        useraccountRo.setUserId(userUuid);
        log.info("UserAccountNotSessionRo[{}]", useraccountRo);
        BaseResponse<UserAccountBo> useraccount = userAccountService.selectUserAccountNotSession(useraccountRo);
        BigDecimal currentBalance = useraccount.getData().getCurrentBalance();
        if (currentBalance.compareTo(orderAmount) != -1 && currentBalance.compareTo(new BigDecimal("0.0")) > 0) {

            log.info("Full purchase of balance The current balance of the user account will be frozen and the password will be transferred after the password is entered.");
            UserAccountChangeRo userAccountChangeRo = new UserAccountChangeRo();
            userAccountChangeRo.setUserUuid(userUuid);
            userAccountChangeRo.setAmount(orderAmount);


//            userAccountChangeRo.setBusinessType("购买债权");
            userAccountChangeRo.setBusinessType(UserAccountBusinessTypeEnum.BUY_CREDITOR.getEnname());
            userAccountChangeRo.setTradeInfo("Full purchase of balance-current transfer to freeze");
            log.info("Full purchase of balance-current transfer to freeze [{}]", userAccountChangeRo);
            userAccountService.current2lock(userAccountChangeRo);

            log.info("更新 orderOrder 余额 付款金额(全额)");
            orderOrder.setAmountBuy(orderAmount);
            orderOrderService.updateOne(orderOrder);

            incomeBo.setNeedPwd(true);
//            incomeBo.put("amountPay", orderAmount);
            incomeBo.setNeedPay(orderAmount);
            incomeBo.setOrderNo(orderNo);
//                    successOrder(orderNo);

            sendNotice(userUuid, orderNo, orderAmount);
        } else {
            if (currentBalance.compareTo(new BigDecimal("0.0")) > 0) {
                UserAccountChangeRo userAccountChangeRo = new UserAccountChangeRo();
                userAccountChangeRo.setUserUuid(userUuid);
                userAccountChangeRo.setAmount(currentBalance);
//                userAccountChangeRo.setBusinessType("购买债权");
                userAccountChangeRo.setBusinessType(UserAccountBusinessTypeEnum.BUY_CREDITOR.getEnname());
                userAccountChangeRo.setTradeInfo("部分购买--活期转冻结");
                log.info("部分购买--活期转冻结[{}]", userAccountChangeRo);
                userAccountService.current2lock(userAccountChangeRo);
            }

            BigDecimal needPay = orderAmount.subtract(currentBalance);

        String tradeNo = TransPreFixTypeEnum.INCOME.getType() + OrderNoCreator.createOrderNo();
            log.info("交易流水号[{}],应付款金额[{}]", tradeNo, needPay);
            log.info("更新 orderOrder 购买金额");
            orderOrder.setAmountBuy(currentBalance);
            orderOrder.setChargeBuy(needPay);
            orderOrderService.updateOne(orderOrder);
            //获取托管账户账号
//            UserBankRo userBankRo = new UserBankRo();
//            userBankRo.setUserUuid(userUuid);
//            BaseResponse<UserBankBo> userBankBoBaseResponse = userBankService.getUserBankInfo(userBankRo);
//            String bankCode = userBankBoBaseResponse.getData().getBankCode();
//            UserAccountBo escrowAccount = this.getEscrowAccount(bankCode);

            if (user.getData().getUserType().equals(UserTypeEnum.SUPPER_INVESTORS.getType())) {

                //Rizky : Disable top up when user balance is not enough
                failOrder(orderNo);
                throw new BusinessException(PayExceptionEnums.BUY_CREDITORIN_IS_NULL);
//                log.info("超级投资人账户");
//                log.info("调用支付接口 付款类型 充值");
//                IncomeRo incomeRo = new IncomeRo();
//                incomeRo.setOrderNo(orderNo);
//                incomeRo.setExternalId(tradeNo);
//                incomeRo.setDepositAmount(needPay);
//                incomeRo.setCustomerUserId(userUuid);
//                incomeRo.setCustomerName(userName);
//                incomeRo.setDepositType(TransTypeEnum.BUY_CREDITOR);//付款类型 购买债权充值
//                incomeRo.setDepositMethod(bankCode);//指定是那个银行的托管账户
//                incomeRo.setToUserId(escrowAccount.getUserUuid());
//                incomeBo = payService.incomeRequest(incomeRo).getData();
//                incomeBo.setNeedPwd(false);
//                incomeBo.setAmountPay(currentBalance);
//                incomeBo.setOrderAmount(orderAmount);
//                incomeBo.setNeedPay(needPay);
//                incomeBo.setOrderNo(orderNo);
//                incomeBo.setBankCode(bankCode);
//                incomeBo.setTimestamp(redisUtil.get(RedisKeyEnums.PAY_EXPRIRESECONDS.appendToDefaultKey(orderNo)));
//
//                sendNotice(userUuid, orderNo, orderAmount);

            } else if (user.getData().getUserType().equals(UserTypeEnum.BRANCH_INVESTORS.getType())) {
                log.info("机构投资人账户");
                if(user.getData().getWithholding()==0){

                    //Rizky : Disable top up when user balance is not enough
                    throw new BusinessException(PayExceptionEnums.BUY_CREDITORIN_IS_NULL);
//                    log.info("调用支付接口 付款类型 充值");
//                    IncomeRo incomeRo = new IncomeRo();
//                    incomeRo.setOrderNo(orderNo);
//                    incomeRo.setExternalId(tradeNo);
//                    incomeRo.setDepositAmount(needPay);
//                    incomeRo.setCustomerUserId(userUuid);
//
//                    incomeRo.setCustomerName(user.getData().getCompanyName());
//                    incomeRo.setDepositType(TransTypeEnum.BUY_CREDITOR);//付款类型 购买债权充值
//                    incomeRo.setDepositMethod(bankCode);//指定是那个银行的托管账户
//                    incomeRo.setToUserId(escrowAccount.getUserUuid());
//                    incomeBo = payService.incomeRequest(incomeRo).getData();
//                    incomeBo.setNeedPwd(false);
//                    incomeBo.setAmountPay(currentBalance);
//                    incomeBo.setOrderAmount(orderAmount);
//                    incomeBo.setNeedPay(needPay);
//                    incomeBo.setOrderNo(orderNo);
//                    incomeBo.setBankCode(bankCode);
//                    incomeBo.setTimestamp(redisUtil.get(RedisKeyEnums.PAY_EXPRIRESECONDS.appendToDefaultKey(orderNo)));

//                    sendNotice(userUuid, orderNo, orderAmount);
                }else if (user.getData().getWithholding()==1){
                    incomeBo.setNeedPwd(true);
                    incomeBo.setAmountPay(currentBalance);
                    incomeBo.setOrderAmount(orderAmount);
                    incomeBo.setNeedPay( needPay);
                    incomeBo.setOrderNo(orderNo);
                    sendNotice(userUuid, orderNo, orderAmount);
                }
            } else {
                log.info("普通投资人 暂无法购买");
                throw new BusinessException(PayExceptionEnums.CAN_NOT_BUY);
            }
        }
//        if (investmentRo.getAmount().compareTo(orderAmount) != 0) {
//            incomeBo.setTip(true);
//        } else {
//            incomeBo.setTip(false);
//        }



        return incomeBo;
    }

    private void sendNotice(String userUuid, String orderNo, BigDecimal orderAmount) throws BusinessException {
        //发送消息通知
        MessageRo messageRo = new MessageRo();
        messageRo.setUserId(userUuid);
        messageRo.setMessageTypeEnum(ORDER_SUCCESS);
        String content=  MessageTypeEnum.ORDER_SUCCESS.getContent();
        content = content.replace("${orderNo}",orderNo);
        content = content.replace("${amount}",orderAmount.toString());
        messageRo.setContent(content);
        userService.addUserMessage(messageRo);
    }
    @Transactional
    @Override
    public JSONObject checkPayPWD(OrderPayRo orderPayRo) throws Exception {
        String userUuid = orderPayRo.getUserUuid();
        String pwd = orderPayRo.getPayPWD();
        String orderNo = orderPayRo.getOrderNo();

        JSONObject result = new JSONObject();
        UserReq userReq = new UserReq();
        userReq.setUserUuid(userUuid);
        BaseResponse<UserBo> user = userService.findUserById(userReq);
        if (!user.getData().getPayPwd().equals(SignUtils.generateMd5(pwd))) {
            throw new BusinessException(PayExceptionEnums.WRONG_PWD);
        }
        log.info("支付密码验证成功");
        UserBankRo userBankRo = new UserBankRo();
        userBankRo.setUserUuid(userUuid);
        BaseResponse<UserBankBo> userBank = userBankService.getUserBankInfo(userBankRo);




        log.info("userBank[{}]",userBank);
//        String bankCode = userBank.getData().getBankCode();
        String bankCode = userBank.getData().getBankCode();

        if (!bankCode.equals("BCA") && !bankCode.equals("BNI")){
            bankCode="CIMB";
        }

        UserAccountBo escrowAccount = this.getEscrowAccount(bankCode);
        userBankRo.setUserUuid(escrowAccount.getUserUuid());
        UserBankBo escrowAccountBank = userBankService.getUserBankInfo(userBankRo).getData();

        OrderOrder order = orderOrderService.findById(orderNo);
        BigDecimal orderAmount = order.getAmountBuy();
        if (user.getData().getUserType().equals(UserTypeEnum.SUPPER_INVESTORS.getType())) {
            if(user.getData().getIsinsurance()==1){
                BigDecimal temp = orderAmount;
                orderAmount = orderAmount
                        .multiply(new BigDecimal(100))
                        .divide(new BigDecimal(111),RoundingMode.HALF_UP);

                BigDecimal insurance = temp.subtract(orderAmount);

                UserAccountChangeRo userAccountChangeRo = new UserAccountChangeRo();
                userAccountChangeRo.setUserUuid(userUuid);
                userAccountChangeRo.setAmount(insurance);
                userAccountChangeRo.setBusinessType(UserAccountBusinessTypeEnum.INSURANCE.getEnname());
                userAccountChangeRo.setTradeInfo("Biaya asuransi");
                userAccountService.subtractLockedBlance(userAccountChangeRo);
            }
            UserAccountChangeRo userAccountChangeRo = new UserAccountChangeRo();
            userAccountChangeRo.setUserUuid(userUuid);
            userAccountChangeRo.setAmount(orderAmount);
//            userAccountChangeRo.setBusinessType("购买债权成功");
            userAccountChangeRo.setBusinessType(UserAccountBusinessTypeEnum.BUY_CREDITOR_SUCCESS.getEnname());
            userAccountChangeRo.setTradeInfo("pembelian sukses");
            userAccountService.lock2used(userAccountChangeRo);
            log.info("冻结转在投");
//            order.setStatus(OrderStatusEnums.INVESTMEN_SUCCESS.getCode());
//            orderOrderService.updateOne(order);

            log.info("Clear shopping cart [{}]", user.getData().getMobileNumber());
            redisUtil.delete(RedisKeyEnums.USER_CART_KEY.appendToDefaultKey(userUuid));
            this.successOrder(orderNo);

        } else if (user.getData().getUserType().equals(UserTypeEnum.BRANCH_INVESTORS.getType())) {

            if (user.getData().getWithholding()==0){
                UserAccountChangeRo userAccountChangeRo = new UserAccountChangeRo();
                userAccountChangeRo.setUserUuid(userUuid);
                userAccountChangeRo.setAmount(orderAmount);
                userAccountChangeRo.setBusinessType(UserAccountBusinessTypeEnum.BUY_CREDITOR_SUCCESS.getEnname());
                userAccountChangeRo.setTradeInfo("冻结转在投");
                userAccountService.lock2used(userAccountChangeRo);
                log.info("冻结转在投");

                this.successOrder(orderNo);
            }else if (user.getData().getWithholding()==1){
                log.info("代扣机构投资人");
                LoanRo ro = new LoanRo();
                ro.setOrderNo(orderNo);
                ro.setFromUserId(userUuid);
                ro.setToUserId(escrowAccount.getUserUuid());
//            ro.setCreditorNo(orderNo);
                ro.setAmount(order.getChargeBuy());
                ro.setCardholder(escrowAccountBank.getBankCardName());
                ro.setBankCode(bankCode);
                ro.setBankNumberNo(escrowAccountBank.getBankNumberNo());
//            ro.setDescription("逾期罚息&逾期服务费");
                ro.setDescription(TransTypeEnum.BUY_CREDITOR_BRANCH.getDisburseType());
                ro.setDisburseChannel(bankCode);
                ro.setTransType(TransTypeEnum.BUY_CREDITOR_BRANCH.getDisburseType());
                if (payService.loan(ro).isSuccess()){
                    log.info("代扣机构投资人后订单改为支付中状态");
                    order.setStatus(OrderStatusEnums.PAYING.getCode());
                    order.setUpdateTime(new Date());
                    orderOrderService.updateOne(order);
                }
            }


        }
        result.put("success", true);
        result.put("msg", "Berhasil");
        return result;
    }

    private BigDecimal preBuy(String userUuid, boolean allIn, String orderNo, String creditorNo, BigDecimal buyAmount,Integer userType) throws BusinessException {
        boolean thisAllIn = allIn;
        if (buyAmount.compareTo(new BigDecimal("-1")) == 0) {
            thisAllIn = true;
        }

        Scatterstandard scatterstandard = new Scatterstandard();
        scatterstandard.setCreditorNo(creditorNo);
        scatterstandard = this.findOne(scatterstandard);
        try {
            if (null != scatterstandard) {
                if (thisAllIn) {
                    buyAmount = scatterstandard.getAmountApply().subtract(scatterstandard.getAmountLock()).subtract(scatterstandard.getAmountBuy());
                }
                log.info("锁定散标[{}]金额[{}]", creditorNo, buyAmount);
                lockAmount(buyAmount, creditorNo);
                log.info("增加用户[{}]订单[{}]散标[{}]金额[{}]关系", userUuid, orderNo, creditorNo, buyAmount);
                OrderScatterStandardRel orderScatterStandardRel = new OrderScatterStandardRel();
                orderScatterStandardRel.setCreditorNo(creditorNo);
                orderScatterStandardRel.setBuyUser(userUuid);
                orderScatterStandardRel.setOrderNo(orderNo);
                orderScatterStandardRel.setAmount(buyAmount);
                orderScatterStandardRel.setUserType(userType);
                orderScatterStandardRelService.addOne(orderScatterStandardRel);
                return buyAmount;
            } else {
                return new BigDecimal("0.0");
            }

        } catch (BusinessException e) {
            e.printStackTrace();
            return new BigDecimal("0.0");
        }
    }

    @Override
    @Transactional
    public void successOrder(String orderNo) throws BusinessException {
        OrderOrder order = orderOrderService.findById(orderNo);
        if (order.getStatus().equals(OrderStatusEnums.INVESTMENTING.getCode()) || order.getStatus().equals(OrderStatusEnums.PAYING.getCode())) {
            if (redisUtil.tryLock(LOCK_ORDER_ORDERNO + orderNo, 10)) {
                order.setStatus(OrderStatusEnums.INVESTMEN_SUCCESS.getCode());//投资成功
//        try {
//            if (DateUtils.redMin(30).after(order.getCreateTime())){
//                order.setStatus(OrderStatusEnums.INVESTMEN_FAIL.getCode());//投资失败 支付成功
//            }
//        } catch (ParseException e) {
//            e.printStackTrace()/        }
                order.setIncomeTime(new Date());
                order.setUpdateTime(new Date());
                orderOrderService.updateOne(order);


                //发送消息通知
                MessageRo messageRo = new MessageRo();
                messageRo.setUserId(order.getUserUuid());
                messageRo.setMessageTypeEnum(MessageTypeEnum.PAY_SUCCESS);
                String content = MessageTypeEnum.PAY_SUCCESS.getContent();
                content = content.replace("${amount1}", order.getChargeBuy().toString());
                UserAccountNotSessionRo useraccountRo = new UserAccountNotSessionRo();
                useraccountRo.setUserId(order.getUserUuid());
                log.info("UserAccountNotSessionRo[{}]", useraccountRo);
                BaseResponse<UserAccountBo> useraccount = userAccountService.selectUserAccountNotSession(useraccountRo);
                BigDecimal amount = new BigDecimal("0.0");
                if (useraccount.isSuccess()) {
                    amount = useraccount.getData().getCurrentBalance().add(useraccount.getData().getLockedBalance()).add(useraccount.getData().getInvestingBanlance());
                }

                content = content.replace("${amount2}", amount.toString());
                messageRo.setContent(content);
                userService.addUserMessage(messageRo);

                log.info("订单所有散标，该用户购买金额 、 该用户购买金额、该用户购买金额 转为购买金额");
                OrderScatterStandardRel rel = new OrderScatterStandardRel();
                rel.setOrderNo(orderNo);
                List<OrderScatterStandardRel> orderScatterStandardRels = orderScatterStandardRelService.findList(rel);
                for (OrderScatterStandardRel orderScatterStandardRel : orderScatterStandardRels) {
                    String creditorNo = orderScatterStandardRel.getCreditorNo();

                    if (redisUtil.tryLock(LOCK_SCATTERSTANDARD_CREDITORNO + creditorNo, 10)) {
                        Scatterstandard sca = new Scatterstandard();
                        sca.setCreditorNo(creditorNo);
                        sca = this.findOne(sca);
                        sca.setAmountBuy(sca.getAmountBuy().add(orderScatterStandardRel.getAmount()));
                        sca.setAmountLock(sca.getAmountLock().subtract(orderScatterStandardRel.getAmount()));
                        if (sca.getAmountBuy().compareTo(sca.getAmountApply()) == 0 && (sca.getStatus() == ScatterStandardStatusEnums.SIGN_SUCCESS.getCode())) {
                            log.info("债权[{}]满标", creditorNo);
                            sca.setStatus(ScatterStandardStatusEnums.FULL_SCALE.getCode());//满标


                        }
                        this.updateOne(sca);
                        log.info("散标[{}]锁定金额[{}] 转为 购买金额", creditorNo, orderScatterStandardRel.getAmount());
                        redisUtil.releaseLock(LOCK_SCATTERSTANDARD_CREDITORNO + creditorNo);
                    }
                }

                redisUtil.releaseLock(LOCK_ORDER_ORDERNO + orderNo);
            }

        }
    }

    @Override
    @Transactional
    public void failOrder(String orderNo) throws BusinessException {
        OrderOrder order = orderOrderService.findById(orderNo);
        if (order.getStatus().equals(OrderStatusEnums.INVESTMENTING.getCode()) || order.getStatus().equals(OrderStatusEnums.PAYING.getCode())) {
            if (redisUtil.tryLock(LOCK_ORDER_ORDERNO + orderNo, 10)) {
//                order.setStatus(OrderStatusEnums.INVESTMEN_FAIL.getCode());//投资失败
//                try {
//                    if (DateUtils.redMin(1440).after(order.getCreateTime())) {
//                        order.setStatus(OrderStatusEnums.FAILORDER.getCode());//投资失效
//                    }
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }

//                order.setUpdateTime(new Date());
//                orderOrderService.updateOne(order);

                //发送消息通知
//                MessageRo messageRo = new MessageRo();
//                messageRo.setUserId(order.getUserUuid());
//                messageRo.setMessageTypeEnum(MessageTypeEnum.ORDER_DEFEATED);
//                String content = MessageTypeEnum.ORDER_DEFEATED.getContent();
//                content = content.replace("${orderNo}", orderNo);
//                messageRo.setContent(content);
//                userService.addUserMessage(messageRo);

                log.info("订单[{}]所有散标，该用户购买金额 、 该用户购买金额、该用户购买金额 散标解冻 ----账户冻结金额也解冻",orderNo);
                OrderScatterStandardRel rel = new OrderScatterStandardRel();
                rel.setDisabled(0);
                rel.setOrderNo(orderNo);
                List<OrderScatterStandardRel> orderScatterStandardRels = orderScatterStandardRelService.findList(rel);
                int count = 0;
                BigDecimal changeAmount = BigDecimal.ZERO;
                Map<String, String> map = getCartByUserId(order.getUserUuid());
                for (OrderScatterStandardRel orderScatterStandardRel : orderScatterStandardRels) {
                    String creditorNo = orderScatterStandardRel.getCreditorNo();

                    if (redisUtil.tryLock(LOCK_SCATTERSTANDARD_CREDITORNO + creditorNo, 10)) {
                        Scatterstandard sca = new Scatterstandard();
                        sca.setCreditorNo(creditorNo);
                        sca = this.findOne(sca);

                        if(sca.getStatus()==ScatterStandardStatusEnums.READY_TO_SEND_DOCUMENT.getCode()
                                || sca.getStatus() == ScatterStandardStatusEnums.READY_TO_SIGN.getCode()) {
                            count++;
                            map.remove(creditorNo);
                            updateCart(order.getUserUuid(),map);
                            changeAmount = changeAmount.add(orderScatterStandardRel.getAmount());
                            log.info("散标[{}]锁定金额--[{}] ", creditorNo, orderScatterStandardRel.getAmount());
                            sca.setAmountLock(sca.getAmountLock().subtract(orderScatterStandardRel.getAmount()));
                            sca.setStatus(ScatterStandardStatusEnums.THE_TENDER.getCode());
                            this.updateOne(sca);

                            Creditorinfo creditorinfo = creditorinfoService.findByCreditorNo(creditorNo);
                            creditorinfo.setStatus(0);// 可购买
                            creditorinfo.setUpdateTime(new Date());
                            creditorinfo.setRemark("付款失败，解锁债权改为可购买");
                            creditorinfoService.updateOne(creditorinfo);
                            redisUtil.releaseLock(LOCK_SCATTERSTANDARD_CREDITORNO + creditorNo);

                            orderScatterStandardRel.setDisabled(1);
                            orderScatterStandardRelService.updateOne(orderScatterStandardRel);
                        }
                    }
                }
                if(count == orderScatterStandardRels.size()){
                    order.setStatus(OrderStatusEnums.INVESTMEN_FAIL.getCode());//投资失败
                    order.setUpdateTime(new Date());
                    orderOrderService.updateOne(order);
                }
                else{
                    order.setAmountBuy(order.getAmountBuy().subtract(changeAmount));
                    order.setApplyBuy(order.getApplyBuy().subtract(changeAmount));
                    order.setUpdateTime(new Date());
                    orderOrderService.updateOne(order);
                }
                UserAccountChangeRo userAccountChangeRo = new UserAccountChangeRo();
                userAccountChangeRo.setUserUuid(order.getUserUuid());
                userAccountChangeRo.setAmount(changeAmount);
                userAccountChangeRo.setBusinessType(UserAccountBusinessTypeEnum.BUY_CREDITOR_FAIL.getEnname());
                userAccountChangeRo.setTradeInfo("购买失败--冻结转活期");
                log.info("购买失败--冻结转活期[{}]", userAccountChangeRo);
                userAccountService.lock2current(userAccountChangeRo);
            }
            redisUtil.releaseLock(LOCK_ORDER_ORDERNO + orderNo);
        }
    }

    public Map<String, String> getCartByUserId(String userUuid){
        Map map = this.redisUtil.get(RedisKeyEnums.USER_CART_KEY.appendToDefaultKey(userUuid), Map.class);
        if (null == map){
            map = new LinkedHashMap<>();
            redisUtil.set(RedisKeyEnums.USER_CART_KEY.appendToDefaultKey(userUuid),map);
            return getCartByUserId(userUuid);
        }
        return (Map<String, String>)map;
    }

    public void updateCart(String userUuid, Map map){
        redisUtil.set(RedisKeyEnums.USER_CART_KEY.appendToDefaultKey(userUuid), map);
    }

    @Override
    @Transactional
    public IncomeBo repayment(ScatterstandardRo ro) throws BusinessException, IllegalAccessException {
        // TODO: 2019/7/3 分期还款
        if (ro.getRepaymentType().equals(CreditorTypeEnum.STAGING.getType())) {//分期还款
            return this.repayMentForStage(ro);
        } else {//普通 he 展期还款
            return this.repayMentForCommon(ro);
        }
    }


    //普通 he 展期还款
    @Transactional
    public IncomeBo repayMentForCommon(ScatterstandardRo ro) throws BusinessException, IllegalAccessException{
        // 交易流水号
//        String tradeNo = TransPreFixTypeEnum.INCOME.getType() + createOrderNo();

        //更新散标表 应还款金额
        Scatterstandard scatterstandard = new Scatterstandard();
        scatterstandard.setCreditorNo(ro.getCreditorNo());
        scatterstandard = this.findOne(scatterstandard);
        log.info("还款scatterstandard[{}]",scatterstandard);
        scatterstandard.setAmountActual(ro.getAmountApply().add(ro.getInterest().add(ro.getOverdueFee()).add(ro.getOverdueRate())));
        scatterstandard.setStatus(ScatterStandardStatusEnums.REFUND_WATING.getCode());
        scatterstandard.setRefundActualTime(new Date());
        scatterstandard.setUpdateTime(new Date());
        if (ro.getRepaymentType().equals(CreditorTypeEnum.EXTENSION.getType())){
            scatterstandard.setExtensionStatus(1);//0未展期 1被展期
        }
        scatterstandard.setAmountRepay(ro.getAmountApply());//还款本金
        scatterstandard.setPaymentcode(ro.getPaymentcode());
        scatterstandard.setDepositStatus(ro.getDepositStatus());
        scatterstandard.setExternalId(ro.getExternalId());
//        scatterstandard.setTransactionId("");
        scatterstandard.setDepositChannel(ro.getDepositChannel());
        scatterstandard.setDepositMethod(ro.getDepositMethod());
        scatterstandard.setPaymentcode(ro.getPaymentcode());

        //获取托管账户账号
        Creditorinfo creditorinfo = creditorinfoService.findByCreditorNo(scatterstandard.getCreditorNo());

        UserAccountBo escrowAccount = this.getEscrowAccount(ro.getBankCode());

        //调用支付接口 付款类型 doit还款
        IncomeBo jsonObject = new IncomeBo();

        log.info("调用支付接口 付款类型 充值");
        IncomeRo incomeRo = new IncomeRo();

        incomeRo.setOrderNo(ro.getCreditorNo());
        incomeRo.setDepositAmount(ro.getAmountApply());
        incomeRo.setExternalId(ro.getExternalId());
        incomeRo.setCustomerUserId(creditorinfo.getLenderId());
        incomeRo.setCustomerName(creditorinfo.getName());
        incomeRo.setDepositType(TransTypeEnum.INCOME);//付款类型 还款
        incomeRo.setDepositMethod(ro.getBankCode());//指定是那个银行的托管账户
        incomeRo.setToUserId(escrowAccount.getUserUuid());
        jsonObject = payService.incomeRequest(incomeRo).getData();


        this.updateOne(scatterstandard);

        return jsonObject;
    }

    //分期还款
    @Transactional
    public IncomeBo repayMentForStage(ScatterstandardRo ro) throws BusinessException, IllegalAccessException{
        // 交易流水号
//        String tradeNo = TransPreFixTypeEnum.INCOME.getType() + createOrderNo();

        //更新还款计划表 应还款金额
        RepaymentPlan repaymentPlan = new RepaymentPlan();
        repaymentPlan.setCreditorNo(ro.getCreditorNo());
        repaymentPlan.setPeriodNo(ro.getPeriodNo());
        repaymentPlan = this.repaymentPlanService.findOne(repaymentPlan);
        log.info("还款repaymentPlan[{}]",repaymentPlan);
        repaymentPlan.setStatus(StageStatusEnum.REFUNDING.getCode());
        repaymentPlan.setActualRefundTime(new Date());
        repaymentPlan.setUpdateTime(new Date());
        repaymentPlan.setAmountActual(ro.getAmountApply().add(ro.getInterest().add(ro.getOverdueFee()).add(ro.getOverdueRate())));
        repaymentPlan.setAmountRepay(ro.getAmountApply());//还款本金
        repaymentPlan.setPeriodNo(ro.getPeriodNo());//期数

        //获取托管账户账号
        Creditorinfo creditorinfo = creditorinfoService.findByCreditorNo(repaymentPlan.getCreditorNo());

        UserAccountBo escrowAccount = this.getEscrowAccount(ro.getBankCode());

        //调用支付接口 付款类型 doit还款
        IncomeBo jsonObject = new IncomeBo();

        log.info("调用支付接口 付款类型 充值");
        IncomeRo incomeRo = new IncomeRo();

        incomeRo.setOrderNo(ro.getCreditorNo());
        incomeRo.setDepositAmount(ro.getAmountApply());
        incomeRo.setExternalId(ro.getExternalId());
        incomeRo.setCustomerUserId(creditorinfo.getLenderId());
        incomeRo.setCustomerName(creditorinfo.getName());
        incomeRo.setDepositType(TransTypeEnum.LOAN_STAGING);//付款类型 还款
        incomeRo.setDepositMethod(ro.getBankCode());//指定是那个银行的托管账户
        incomeRo.setToUserId(escrowAccount.getUserUuid());
        jsonObject = payService.incomeRequest(incomeRo).getData();

        this.repaymentPlanService.updateOne(repaymentPlan);

        return jsonObject;
    }

    /**
     * 给借款人打款处理中
     */
    @Override
    @Transactional
    public void loanWatingTask() {
        log.info("************ Batch processing starts to process the payment to the borrower *************");
        try {
            //查询所有打款处理中的信息
            Scatterstandard scatterstandardEntity = new Scatterstandard();
            scatterstandardEntity.setStatus(ScatterStandardStatusEnums.LOAN_WATING.getCode());
            List<Scatterstandard> scatterstandardList = this.findList(scatterstandardEntity);
            if (!CollectionUtils.isEmpty(scatterstandardList)) {
                for (Scatterstandard scatterstandard : scatterstandardList) {
                    log.info("************Batch processing starts to process the disburse to the borrower*************:{}", scatterstandard.getCreditorNo());
                    //打款到借款人结果处理
                    log.info("Processing claim number{}", scatterstandard.getCreditorNo());
                    LoanRo loanRo = new LoanRo();
                    loanRo.setCreditorNo(scatterstandard.getCreditorNo());
                    loanRo.setTransType(TransTypeEnum.LOAN.getDisburseType());
                    BaseResponse<LoanResponse> loanResult = this.payService.queryLoanResult(loanRo);
                    LoanResponse response = new LoanResponse();
                    if(loanResult.getData()!=null)
                        response = loanResult.getData();
                    logger.info("LoanResponse---------[{}]",response);
                    if(StringUtils.isEmpty(response.getDisburseStatus())){
                        continue;
                    }

                    if (response.getDisburseStatus().equals("COMPLETED")) {
                        scatterstandard.setStatus(ScatterStandardStatusEnums.LOAN_SUCCESS.getCode());
                        scatterstandard.setUpdateTime(new Date());
                        //维护应还款时间
                        scatterstandard.setRefundIngTime(new Date());
                        log.info("Payment is completed, continue to disburse the funds, and the funds are frozen and transferred to investment");
                        this.updateOne(scatterstandard);

                    } else if (response.getDisburseStatus().equals("PENDING")) {
                        log.info("Payment Pending{}", scatterstandard.getCreditorNo());
                    }
                    else if (response.getDisburseStatus().equals("FAILED")) {

                        log.info("放款失败,订单号:{},失败原因：{}", scatterstandard.getCreditorNo(), response.getErrorMessage());
                        if (response.getErrorCode().equals("RECIPIENT_ACCOUNT_NUMBER_ERROR") ||
                                response.getErrorCode().equals("INVALID_DESTINATION") ||
                                response.getErrorCode().equals("BANK_CODE_NOT_SUPPORTED_ERROR")
                                || (response.getErrorCode().equals("12") && response.getErrorMessage().equals("INVALID TRANSACTION"))
                                || (response.getErrorCode().equals("001") && response.getErrorMessage().equals("Account not Found"))
                                || (response.getErrorCode().equals("76") && response.getErrorMessage().equals("INVALID CREDIT ACCOUNT"))
                                || (response.getErrorCode().equals("ESB-82-021") && response.getErrorMessage().equals("Account cannot do transaction"))
                                || (response.getErrorCode().equals("0169") && response.getErrorMessage().equals("Account number is not found"))
                                || (response.getErrorCode().equals("0110") && response.getErrorMessage().equals("Account is closed"))) {
                            log.info("因为银行卡问题 放款失败 回退给doit");

                            scatterstandard.setStatus(ScatterStandardStatusEnums.LOAN_ERROR.getCode());
                            scatterstandard.setUpdateTime(new Date());
                            this.updateOne(scatterstandard);

                            log.info("支付失败 回退账户资金");
                            OrderScatterStandardRel rel = new OrderScatterStandardRel();
                            rel.setCreditorNo(scatterstandard.getCreditorNo());
                            List<OrderScatterStandardRel> orderScatterStandardRels = orderScatterStandardRelService.findList(rel);

                            for (OrderScatterStandardRel orderScatterStandardRel : orderScatterStandardRels) {
                                UserAccountChangeRo userAccountChangeRo = new UserAccountChangeRo();
                                userAccountChangeRo.setAmount(orderScatterStandardRel.getAmount());
                                userAccountChangeRo.setUserUuid(orderScatterStandardRel.getBuyUser());
//                                userAccountChangeRo.setBusinessType("购买债权失败");
                                userAccountChangeRo.setBusinessType(UserAccountBusinessTypeEnum.BUY_CREDITOR_FAIL.getEnname());
                                userAccountChangeRo.setTradeInfo("放款支付失败 回退账户资金 锁定转余额");
                                userAccountService.lock2current(userAccountChangeRo);
                            }


                        } else if (response.getErrorCode().equals("SWITCHING_NETWORK_ERROR") ||
                                response.getErrorCode().equals("REJECTED_BY_BANK") ||
                                (response.getErrorCode().equals("16") && response.getErrorMessage().equals("REACHED AMOUNT LIMIT")) ||
                                (response.getErrorCode().equals("15") && response.getErrorMessage().equals("EXCEEDED NUMBER LIMIT")) ||
                                (response.getErrorCode().equals("24") && response.getErrorMessage().equals("EXCEEDS FUNDS AVAILABLE")) ||
                                (response.getErrorCode().equals("86") && response.getErrorMessage().equals("TRANSACTION COULD NOT BE PROCESSED")) ||
                                (response.getErrorCode().equals("10") && response.getErrorMessage().equals("TRANSACTION COULD NOT BE PROCESSED")) ||
                                (response.getErrorCode().equals("23") && response.getErrorMessage().equals("ACCOUNT ALREADY NSF")) ||
                                (response.getErrorCode().equals("ESB-82-019") && response.getErrorMessage().equals("Insufficient fund")) ||
                                (response.getErrorCode().equals("029") && response.getErrorMessage().equals("Security words's locked")) ||
                                (response.getErrorCode().equals("22") && response.getErrorMessage().equals("DO NOT HONOR ")) ||
                                (response.getErrorCode().equals("0002") && response.getErrorMessage().equals("Insufficient Balance")) ||
                                (response.getErrorCode().equals("ESB-82-006") && response.getErrorMessage().equals("Max amount transaction is exceeded"))
                                ) {
                            //
                            log.info("网络波动导致的放款失败 改为满标 可以重新打款");
                            scatterstandard.setStatus(ScatterStandardStatusEnums.FULL_SCALE.getCode());
                            scatterstandard.setUpdateTime(new Date());
                            this.updateOne(scatterstandard);
                        } else {
                            scatterstandard.setStatus(ScatterStandardStatusEnums.LOAN_ERROR.getCode());
                            scatterstandard.setUpdateTime(new Date());
                            this.updateOne(scatterstandard);

                            log.info("支付失败 回退账户资金");
                            OrderScatterStandardRel rel = new OrderScatterStandardRel();
                            rel.setCreditorNo(scatterstandard.getCreditorNo());
                            List<OrderScatterStandardRel> orderScatterStandardRels = orderScatterStandardRelService.findList(rel);

                            for (OrderScatterStandardRel orderScatterStandardRel : orderScatterStandardRels) {
                                UserAccountChangeRo userAccountChangeRo = new UserAccountChangeRo();
                                userAccountChangeRo.setAmount(orderScatterStandardRel.getAmount());
                                userAccountChangeRo.setUserUuid(orderScatterStandardRel.getBuyUser());

//                                userAccountChangeRo.setBusinessType("购买债权失败");
                                userAccountChangeRo.setBusinessType(UserAccountBusinessTypeEnum.BUY_CREDITOR_FAIL.getEnname());
                                userAccountChangeRo.setTradeInfo("支付失败 回退账户资金  锁定转余额");
                                userAccountService.lock2current(userAccountChangeRo);
                            }
                        }
                    }

                }
            }


        } catch (Exception e) {
            e.printStackTrace();
//            log.error("批量处理给借款人打款处理中异常!{}", e.getLocalizedMessage());
//            log.error("批量处理给借款人打款处理中异常!{}", e.getMessage());
            log.error("批量处理给借款人打款处理中异常!",e);
        }
        log.info("************批量处理给借款人打款处理中结束*************");
    }

    /**
     * 机构投资人清分收入处理
     */
    public void handleBreachDisburse() throws Exception {
        //获取前一天机构投资人清分总收入
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-1);
        BigDecimal account = getBreachDisburseAccount(calendar);//机构投资人清分收入
        log.info("获取昨日机构投资人清分总收入 :{}",account);
        //获取前一天进行过投资的超级投资人的各投资比例
        UserAccountHistoryTotalRo ro = new UserAccountHistoryTotalRo();
        ro.setDate(calendar.getTime());
        List<UserAccountHistoryTotalBo> list = userAccountHistoryService.getUserAccountHistoryTotal(ro).getData();
        BigDecimal decimal = new BigDecimal(0);//所有超级投资人当日总投资金额
        for (UserAccountHistoryTotalBo bo: list){
            decimal = decimal.add(bo.getAmount());
        }
        log.info("昨日超级投资人投资人数：{}，投资总金额：{}", list.size(), decimal);
        //获取机构投资人
        UserTypeSearchRo userTypeSearchRo = new UserTypeSearchRo();
        userTypeSearchRo.setUserType(UserTypeEnum.BRANCH_INVESTORS.getType());
        BaseResponse<UserAccountBo> response = this.userService.userListByType(userTypeSearchRo);
        String userUuid = response.getData().getUserUuid();//机构投资人uuid
        //计算是否有清分结余
        BigDecimal total = new BigDecimal(0);
        //按比例把机构投资人清分收入清分给超级投资人
        for (UserAccountHistoryTotalBo bo: list){
            //机构清分收入*超级投资人当日投资金额/所有超级投资人当日投资总金额
            BigDecimal amount = account.multiply(bo.getAmount()).divide(decimal,4);
            amount= new BigDecimal(amount.setScale(-2, BigDecimal.ROUND_DOWN).intValue());
//            UserAccountChangeRo userAccountChangeRo = new UserAccountChangeRo();
//            userAccountChangeRo.setAmount(amount);
//            userAccountChangeRo.setUserUuid(bo.getUserUuid());
////            userAccountChangeRo.setBusinessType("75%服务费");
//            userAccountChangeRo.setBusinessType(UserAccountBusinessTypeEnum.SERVICE_FEE.getEnname());
//            userAccountChangeRo.setTradeInfo("机构投资人清分收入清分给超级投资人");
//            userAccountService.addCurrentBlance(userAccountChangeRo);
            PayAccountHistoryRo payAccountHistoryRo = new PayAccountHistoryRo();
            payAccountHistoryRo.setAmount(amount);
            payAccountHistoryRo.setStatus(PayAccountStatusEnum.WATING.getType());
            payAccountHistoryRo.setTradeNo(TransPreFixTypeEnum.TRANSFER.getType()+OrderNoCreator.createOrderNo());
            payAccountHistoryRo.setTradeType(TransTypeEnum.BRANCH_CLEAR.getDisburseType());
            payAccountHistoryRo.setToUserId(bo.getUserUuid());
            payAccountHistoryRo.setFromUserId(userUuid);
            this.payAccountHistoryService.addPayAccountHistory(payAccountHistoryRo);
            total = total.add(amount);
        }
        log.info("清分总金额：{}", total);
        if (total.compareTo(account)<0){
            BigDecimal subtract = account.subtract(total);
            log.info("本期机构投资人清分结余金额为：{}", subtract);
            //存入当日机构投资人清分金额
            Calendar calendar1 = Calendar.getInstance();
            BigDecimal today = getBreachDisburseAccount(calendar1);
            int i = calendar1.get(Calendar.DAY_OF_MONTH);
            BigDecimal after = today.add(subtract);
            log.info("当日机构投资人清分金额：{} 存入结余后总金额:{}",today, after);
            redisUtil.set(RedisKeyEnums.BRANCH_DISBURSE_SUM_KEY.appendToDefaultKey(i + ""),after);
        }
    }


    /**
     * 获取机构投资人清分收入总额（仅支持查询当天与前一天）
     * @param calendar
     * @return
     */
    private BigDecimal getBreachDisburseAccount(Calendar calendar){
        int i = calendar.get(Calendar.DAY_OF_MONTH);
        String s = redisUtil.get(RedisKeyEnums.BRANCH_DISBURSE_SUM_KEY.appendToDefaultKey(i + ""));
        if (StringUtils.isEmpty(s)){
            redisUtil.set(RedisKeyEnums.BRANCH_DISBURSE_SUM_KEY.appendToDefaultKey(i + ""),new BigDecimal(0));
            return getBreachDisburseAccount(calendar);
        }
        return new BigDecimal(s);
    }

    /**
     * 根据期限 转化成对应天数
     * @param term
     * @return
     */
    private Integer termToDays(String term){
        if (term.endsWith("d")){
            String d = term.replaceAll("d", "");
            return Integer.parseInt(d);
        }else if (term.endsWith("m")){
            String m = term.replaceAll("m", "");
            int i = Integer.parseInt(m);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH,i);
            long day = (calendar.getTimeInMillis()-Calendar.getInstance().getTimeInMillis())/(1000*60*60*24);
            return (int) day;
        }
        return 0;
    }

    public static void main(String[] args) throws ParseException {
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        Date d = new Date();
//
//        Calendar c = Calendar.getInstance();
//        c.setTime(new Date());
//        c.add(Calendar.DAY_OF_MONTH, Integer.parseInt("30"));
//        String a = df.format(c.getTime());
//        System.out.println(a);
        String s = "60.00";
        System.out.println(Integer.parseInt(s));
    }

    public List<Scatterstandard> findForList(Scatterstandard scatterstandard) throws BusinessException {
        return this.scatterstandardDao.findForList(scatterstandard);
    }

}