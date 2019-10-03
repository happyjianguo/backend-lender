package com.yqg.order.service.scatterstandard.impl;

import com.yqg.api.order.scatterstandard.bo.ScatterstandardBo;
import com.yqg.api.order.scatterstandard.ro.ScatterstandardPageRo;
import com.yqg.api.order.scatterstandard.ro.ScatterstandardRo;
import com.yqg.api.pay.income.ro.InvestmentRo;
import com.yqg.api.user.useraccount.bo.UserAccountBo;
import com.yqg.api.user.useraccount.ro.UseraccountRo;
import com.yqg.api.user.useruser.bo.UserBo;
import com.yqg.common.core.response.BasePageResponse;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.common.utils.DateUtils;
import com.yqg.order.dao.ScatterstandardDao;
import com.yqg.order.entity.OrderOrder;
import com.yqg.order.entity.Orderpackagerel;
import com.yqg.order.entity.Scatterstandard;
import com.yqg.order.enums.ScatterStandardStatusEnums;
import com.yqg.order.service.scatterstandard.ScatterstandardService;
import com.yqg.pay.service.PayCommonServiceImpl;
import com.yqg.pay.service.income.PayIncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 散标表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 11:02:36
 */
@Service("scatterStandardService")
public class ScatterstandardServiceImpl extends PayCommonServiceImpl implements ScatterstandardService {
    @Autowired
    protected ScatterstandardDao scatterstandardDao;
    @Autowired
    private PayIncomeService payService;

    @Transactional
    public void deal30MinLaterOrder() throws Exception {
        //查询订单状态 1,2 ,半小时之外的订单,改成失效,并减去标的锁定金额
        Date date = DateUtils.redMin(Integer.parseInt(35 + ""));
        List<OrderOrder> orderList = orderOrderDao.deal30MinLaterOrder(date);
        for (OrderOrder order : orderList) {
            order.setStatus(9);
            logger.info("失效订单号:" + order.getId());
            orderOrderService.updateOne(order);
            //根据
            Scatterstandard sca = new Scatterstandard();
            sca.setCreditorNo(order.getCreditorNo());
            List<Scatterstandard> scList = scatterstandardService.findList(sca);
            if (CollectionUtils.isEmpty(scList)) {
                logger.info("债权为空" + order.getCreditorNo());
                continue;
            }
            Scatterstandard scatterstandard = scList.get(0);
            scatterstandard.setAmountLock(scatterstandard.getAmountLock().subtract(order.getAmountBuy()));
            scatterstandardService.updateOne(scatterstandard);

        }

    }

    /**
     * 查询债权详情
     * */
    @Override
    public List<Scatterstandard> selectCreditorinfo(ScatterstandardRo ro) throws BusinessException {

        if(ro.getProductType()==1){

            Scatterstandard scatterstandard = new Scatterstandard();
            scatterstandard.setCreditorNo(ro.getCreditorNo());
            scatterstandard.setDisabled(0);
            List<Scatterstandard> result =  scatterstandardService.findList(scatterstandard);

            return result;

        }else {

            Orderpackagerel orderpackagerelQuery = new Orderpackagerel();
            orderpackagerelQuery.setOrderNo(ro.getCreditorNo());
            List<Orderpackagerel> list = orderPackageRelService.findList(orderpackagerelQuery);

            List<Scatterstandard> ScatterstandardList = new ArrayList<>();

            for (Orderpackagerel orderpackagerel:list){

                Scatterstandard scatterstandardQuery = new Scatterstandard();
                scatterstandardQuery.setPackageNo(orderpackagerel.getCode());
                List<Scatterstandard> list1 = scatterstandardService.findList(scatterstandardQuery);
                ScatterstandardList.addAll(list1);

            }

            return ScatterstandardList;

        }



    }

    @Transactional
    public void deal6HoursLaterOrder() throws BusinessException, ParseException, IllegalAccessException {

        Date date = DateUtils.redMin(Integer.parseInt(180 + ""));
        List<Scatterstandard> scatterList = scatterstandardDao.query6Hours(date);
        if (CollectionUtils.isEmpty(scatterList)) {
            logger.info("没有6小时要流标的数据");
            return;
        }
//        if(!CollectionUtils.isEmpty(scatterList)){
        for (Scatterstandard scatterstandard : scatterList){
//            Scatterstandard scatterstandard = scatterList.get(0);
            scatterstandard.setStatus(ScatterStandardStatusEnums.FLOW_STANDARD.getCode());
            scatterstandard.setUpdateTime(new Date());
            this.updateOne(scatterstandard);
            logger.info("6小时流标[{}]", scatterstandard);

        }
        /*
//        }







        //1.满标  购买金额=申请金额
        //2.流标  购买金额=0 && (订单表==null||(订单表!=null 订单表状态都是过期))
        //3.合买  0<申请金额<购买金额 &&  资金表没有处理中   &&   订单表没有失败和处理中
        // 合买金额=申请金额-订单成功金额

        Date date = DateUtils.redMin(Integer.parseInt(180 + ""));

        List<Scatterstandard> scatterList = scatterstandardDao.query6Hours(date);
        if (!CollectionUtils.isEmpty(scatterList)) {
            for (Scatterstandard sc : scatterList) {
                logger.info("散表不为空:"+sc.getCreditorNo());
                OrderOrder order1 = new OrderOrder();
                order1.setCreditorNo(sc.getCreditorNo());
                order1.setStatus(OrderStatusEnums.INVESTMENTING.getCode());
                List<OrderOrder> orderList0 = orderOrderService.findList(order1);
                if (!CollectionUtils.isEmpty(orderList0)) {
                    continue;
                }

                logger.info("1.满标:"+sc.getCreditorNo());
                //6小时后处理标(查标状态为=1和2 1.满标 2.流标 3.标不满超级投资人合买 )
                //1.满标  购买金额=申请金额
                if (sc.getAmountApply().compareTo(sc.getAmountBuy()) == 0) {
                    sc.setStatus(ScatterStandardStatusEnums.FULL_SCALE.getCode());
                    this.updateOne(sc);
                }
                logger.info("流标:"+sc.getCreditorNo());
                //2.流标  购买金额=0 && (订单表==null||(订单表!=null 订单表状态都是过期))
                if (sc.getAmountBuy().compareTo(new BigDecimal(0)) == 0) {
                    sc.setStatus(ScatterStandardStatusEnums.FLOW_STANDARD.getCode());
                    this.updateOne(sc);
                }
                //3.合买  0<申请金额<购买金额 &&&  订单表没有处理中
                // 合买金额=申请金额-订单成功金额
                logger.info("合买:"+sc.getCreditorNo());
                OrderOrder order = new OrderOrder();
                order.setCreditorNo(sc.getCreditorNo());
                order.setStatus(OrderStatusEnums.INVESTMEN_SUCCESS.getCode());
                List<OrderOrder> orderList = orderOrderService.findList(order);
                if (sc.getAmountBuy().compareTo(new BigDecimal(0)) > 0 && sc.getAmountBuy().compareTo(sc.getAmountApply()) < 0) {
                    if (CollectionUtils.isEmpty(orderList)) {
                        logger.info("订单不存在,债权编号:" + sc.getCreditorNo());
                        throw new BusinessException(OrderExceptionEnums.ORDER_NOT_EXIST);
                    }
                    //已购买金额==订单成功金额校验
                    boolean flag = false;
                    for (OrderOrder ord : orderList) {
                        if (ord.getStatus() == OrderStatusEnums.INVESTMENTING.getCode()) {
                            logger.info("债权存在处理中的订单,债权编号:" + sc.getCreditorNo());
                            flag = true;
                        }
                    }
                    if (flag) {
                        continue;
                    }
                    if (sc.getAmountLock().compareTo(new BigDecimal(0)) != 0) {
                        logger.info("锁定金额异常,债权编号:" + sc.getCreditorNo());
                        throw new BusinessException(OrderExceptionEnums.LOCKAMOUT_ERR);
                    }
                    //查询超级投资人账户
                    UserBo superAccount = getSuperAccount();

                    //开始合买
                    BigDecimal toAmount = sc.getAmountApply().subtract(sc.getAmountBuy());
                    InvestmentRo investmentRo = new InvestmentRo();
                    investmentRo.setAmount(toAmount);
                    investmentRo.setUserUuid(superAccount.getId());
                    investmentRo.setUserName(superAccount.getUserName());
                    investmentRo.setCreditorNo(sc.getCreditorNo());
                    investmentRo.setType(1);
                    payService.immediateInvestment(investmentRo);

                    sc.setStatus(ScatterStandardStatusEnums.FULL_SCALE.getCode());
                    sc.setUpdateTime(new Date());
                    this.updateOne(sc);

                }
            }

        }*/

    }

    public void loanFlowStandard() throws BusinessException{

        if (!redisUtil.tryLock("1433223")){
            return;
        }

        //获取流标列表
        Scatterstandard scatterstandard = new Scatterstandard();
        scatterstandard.setDisabled(0);
        scatterstandard.setStatus(ScatterStandardStatusEnums.FLOW_STANDARD.getCode());
        List<Scatterstandard> list = this.findList(scatterstandard);
        if (CollectionUtils.isEmpty(list)) {
            logger.info("没有已流标的数据");
            return;
        }
        //超级投资人放款
        // 暂不考虑多个超级投资人问题（超级投资人账户余额不足则跳过）
        for (Scatterstandard scatters: list){
            //获取超级投资人账户
            logger.info("获取超级投资人账户--购买要流标待散标");
            UserBo superAccount = this.getSuperAccount();
            UseraccountRo useraccountRo = new UseraccountRo();
            useraccountRo.setUserId(superAccount.getId());
            BaseResponse<UserAccountBo> useraccount = userAccountService.selectUserAccount(useraccountRo);
            BigDecimal currentBalance = useraccount.getData().getCurrentBalance();
            // TODO: 2019/4/8 检查超级投资人余额<N印尼盾  预警短信提醒
            if (currentBalance.compareTo(scatters.getAmountApply()) < 0){
                //超级投资人账户余额不足 发送预警短信
                logger.info("超级投资人账户余额[{}]不足流标金额 跳过", currentBalance);
                continue;
            }


            try {
                // TODO: 2019/4/8 超级投资人资金购买散标并改为满标 自动放款 待测试
                BigDecimal amount = scatters.getAmountApply();
                String creditorNo = scatters.getCreditorNo();
                InvestmentRo investmentRo = new InvestmentRo();
                investmentRo.setUserUuid(superAccount.getId());
                investmentRo.setAmount(amount);
                investmentRo.setCreditorNo(creditorNo);
                payService.immediateInvestment(investmentRo);
            } catch (IllegalAccessException e) {
                logger.info("超级投资人购买失败 [{}]", scatters);
            }
        }

        redisUtil.releaseLock("1433223");

    }

    @Override
    public void userRepay(ScatterstandardRo ro) {

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
//    @Transactional
    public void updateOne(Scatterstandard entity) throws BusinessException {
        this.scatterstandardDao.updateOne(entity);
        if (entity.getStatus() == ScatterStandardStatusEnums.LENDING_ERROR.getCode() || entity.getStatus() == ScatterStandardStatusEnums.LENDING_SUCCESS.getCode()
                || entity.getStatus() == ScatterStandardStatusEnums.RESOLVED.getCode() || entity.getStatus() == ScatterStandardStatusEnums.FLOW_STANDARD.getCode()) {
            orderOrderService.pushOrderStatusToDoit(entity);
        }
    }


    @Override
    public BasePageResponse<ScatterstandardBo> queryForPage(ScatterstandardPageRo scatterstandardPageRo) throws BusinessException {
        Scatterstandard entity = new Scatterstandard();
        if (null != scatterstandardPageRo.getAmountApply()) {
            entity.setAmountApply(scatterstandardPageRo.getAmountApply());
        }
        if (null != scatterstandardPageRo.getTerm()) {
            entity.setTerm(scatterstandardPageRo.getTerm());
        }
        if (null != scatterstandardPageRo.getYearRateFin()) {
            entity.setYearRateFin(scatterstandardPageRo.getYearRateFin());
        }

        entity.setDisabled(0);
        entity.setStatus(1);


        Page<Scatterstandard> ScatterstandardPage = scatterstandardDao.findForPage(entity, scatterstandardPageRo.convertPageRequest());

        List<ScatterstandardBo> ScatterstandardBoList = new ArrayList<>();
        for (Scatterstandard scatterstandard : ScatterstandardPage) {

            ScatterstandardBo scatterstandardBo = new ScatterstandardBo();
            scatterstandardBo.setCreditorNo(scatterstandard.getCreditorNo());
            scatterstandardBo.setBorrowingPurposes(scatterstandard.getBorrowingPurposes());
            scatterstandardBo.setAmountApply(scatterstandard.getAmountApply().subtract(scatterstandard.getAmountBuy()).subtract(scatterstandard.getAmountLock()));
            scatterstandardBo.setYearRateFin(scatterstandard.getYearRateFin());
            scatterstandardBo.setTerm(scatterstandard.getTerm());
            scatterstandardBo.setStatus(scatterstandard.getStatus());
            scatterstandardBo.setCreateTime(scatterstandard.getCreateTime());
            ScatterstandardBoList.add(scatterstandardBo);
        }
        BasePageResponse<ScatterstandardBo> scatterstandardPageVOs = new BasePageResponse<>(ScatterstandardPage);
        scatterstandardPageVOs.setContent(ScatterstandardBoList);
        return scatterstandardPageVOs;
    }

    @Override
    public Page<Scatterstandard> queryList(ScatterstandardPageRo scatterstandardPageRo) throws BusinessException {
        Scatterstandard entity = new Scatterstandard();
        entity.setDisabled(0);
        entity.setStatus(scatterstandardPageRo.getStatus());
        entity.setPackageNo("0");
        if (null!=scatterstandardPageRo.getAmountApply()) {
            entity.setAmountApply(scatterstandardPageRo.getAmountApply());
        }
        Page<Scatterstandard> scatterstandardPage = scatterstandardDao.findForPage(entity, scatterstandardPageRo.convertPageRequest());

        return scatterstandardPage;
    }


}