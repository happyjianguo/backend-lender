package com.yqg.order.service.scatterstandard;

import com.alibaba.fastjson.JSONObject;
import com.yqg.api.order.orderorder.ro.OrderPayRo;
import com.yqg.api.order.scatterstandard.bo.ScatterstandardDetailBo;
import com.yqg.api.order.scatterstandard.ro.ScatterstandardRo;
import com.yqg.api.pay.income.ro.InvestmentRo;
import com.yqg.common.core.BaseService;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.order.entity.Creditorinfo;
import com.yqg.order.entity.Scatterstandard;

import java.util.List;

/**
 * 散标表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 11:02:36
 */
public interface ScatterstandardService extends BaseService<Scatterstandard> {

//    void deal30MinLaterOrder() throws Exception;

    /**
     * 立即下单--散标
     *
     * @param investmentRo
     * @throws BusinessException
     */
    public JSONObject immediateInvestment(InvestmentRo investmentRo) throws BusinessException;
    public JSONObject checkPayPWD(OrderPayRo orderPayRo) throws Exception;

    public void successOrder(String orderNo) throws BusinessException;

    public void failOrder(String orderNo) throws BusinessException;


    /**
     * 立即还款
     *
     * @param ro
     * @throws BusinessException
     */
    public JSONObject repayment(ScatterstandardRo ro) throws BusinessException, IllegalAccessException;

    void repaySuccess(String creditorNo) throws Exception;
    void repayFail(String creditorNo) throws Exception;

    public void serviceFeeSuccess(String creditorNo) throws BusinessException;

    /**
     * 查询散标详情
     * @param ro
     * @return
     * @throws BusinessException
     */
    ScatterstandardDetailBo finDetaileById(ScatterstandardRo ro) throws Exception;

//    BasePageResponse<ScatterstandardBo> selectLoanHistoryByNumber(LoanHistoryPageRo mobileNumber) throws BusinessException;

    /**
     * 通过债券列表查找对应的散标列表
     * @param content
     * @return
     * @throws BusinessException
     */
    List<Scatterstandard> findListByCreditor(List<Creditorinfo> content) throws BusinessException;

    Scatterstandard findOneByCreditorNo(String creditorNo) throws BusinessException;

    //    BasePageResponse<ScatterstandardBo> selectLoanHistoryByNumber(LoanHistoryPageRo mobileNumber) throws BusinessException;

    //放款
    void batchLoan() throws Exception;

    //定时任务 放款处理中
    void loanWatingTask() ;

    //定时任务 放款清分
//    void loanACC() ;

    //机构投资人清分收入处理
    void handleBreachDisburse() throws Exception;

    List<Scatterstandard> findForList(Scatterstandard scatterstandard) throws BusinessException;
}