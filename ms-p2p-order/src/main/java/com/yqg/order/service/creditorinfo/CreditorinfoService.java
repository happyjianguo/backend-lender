package com.yqg.order.service.creditorinfo;

import com.yqg.api.order.creditorinfo.bo.LoanHistiryBo;
import com.yqg.api.order.creditorinfo.bo.ScatterstandardListBo;
import com.yqg.api.order.creditorinfo.ro.CreditorinfoRo;
import com.yqg.api.order.scatterstandard.ro.LoanHistoryRo;
import com.yqg.api.order.scatterstandard.ro.ScatterstandardPageRo;
import com.yqg.common.core.BaseService;
import com.yqg.common.dynamicdata.TargetDataSource;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.order.entity.Creditorinfo;

import java.text.ParseException;
import java.util.List;

/**
 * 债权表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 11:02:36
 */
public interface CreditorinfoService extends BaseService<Creditorinfo> {


    Creditorinfo findByCreditorNo(String creditorNo) throws BusinessException;

    /**
     * 标的是否推送过
     * @param creditorNo
     * @return
     */
    @TargetDataSource(dataSource = "readDruidDataSource")
    boolean isSendCreditorinfo(String creditorNo) throws BusinessException;

    /**
     * 接收推标
     * */
    void sendCreditorinfo(CreditorinfoRo ro) throws BusinessException, ParseException;

    /**
     * 分页组合条件查询散标列表
     * @param scatterstandardPageRo
     * @return
     * @throws BusinessException
     */
    ScatterstandardListBo queryForPage(ScatterstandardPageRo scatterstandardPageRo) throws BusinessException;

    /**
     * 查询借款人借款历史
     * @param ro
     * @return
     * @throws BusinessException
     */
    List<LoanHistiryBo> selectLoanHistoryByNumber(LoanHistoryRo ro) throws BusinessException;

    Integer getLoanHistoryCountByMobileNumber(String mobileNumber) throws BusinessException;
}