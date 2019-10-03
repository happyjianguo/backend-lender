package com.yqg.order.service.creditorinfo.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.yqg.api.order.creditorinfo.ro.CreditorinfoRo;
import com.yqg.api.system.sysparam.ro.SysParamRo;
import com.yqg.common.core.response.BasePageResponse;
import com.yqg.common.dao.ExtendQueryCondition;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.common.utils.JsonUtils;
import com.yqg.common.utils.MD5Util;
import com.yqg.order.entity.Creditorinfo;
import com.yqg.order.dao.CreditorinfoDao;
import com.yqg.order.entity.Scatterstandard;
import com.yqg.order.enums.OrderExceptionEnums;
import com.yqg.order.service.creditorinfo.CreditorinfoService;
import com.yqg.pay.service.PayCommonServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 债权表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 11:02:36
 */
@Service("creditorInfoService")
public class CreditorinfoServiceImpl extends PayCommonServiceImpl implements CreditorinfoService {
    @Autowired
    protected CreditorinfoDao creditorinfoDao;


    @Override
    public Creditorinfo findById(String id) throws BusinessException {
        return this.creditorinfoDao.findOneById(id, new Creditorinfo());
    }

    @Override
    public <E> E findById(String id, Class<E> boClass) throws BusinessException {
        Creditorinfo creditorinfo = findById(id);
        E bo = BeanCoypUtil.copyToNewObject(creditorinfo, boClass);
        return bo;
    }

    @Override
    public Creditorinfo findOne(Creditorinfo entity) throws BusinessException {
        return this.creditorinfoDao.findOne(entity);
    }

    @Override
    public <E> E findOne(Creditorinfo entity, Class<E> boClass) throws BusinessException {
        Creditorinfo creditorinfo = findOne(entity);
        E bo = BeanCoypUtil.copyToNewObject(creditorinfo, boClass);
        return bo;
    }


    @Override
    public List<Creditorinfo> findList(Creditorinfo entity) throws BusinessException {
        return this.creditorinfoDao.findForList(entity);
    }

    @Override
    public <E> List<E> findList(Creditorinfo entity, Class<E> boClass) throws BusinessException {
        List<Creditorinfo> creditorinfoList = findList(entity);
        List<E> boList = new ArrayList<>();
        for (Creditorinfo creditorinfo : creditorinfoList) {
            E bo = BeanCoypUtil.copyToNewObject(creditorinfo, boClass);
            boList.add(bo);
        }
        return boList;
    }

    @Override
    @Transactional
    public String addOne(Creditorinfo entity) throws BusinessException {
        return creditorinfoDao.addOne(entity);
    }

    @Override
    @Transactional
    public void deleteById(String id) throws BusinessException {
        creditorinfoDao.deleteOne(findById(id));
    }

    @Override
    @Transactional
    public void deleteOne(Creditorinfo entity) throws BusinessException {
        this.creditorinfoDao.deleteOne(entity);
    }

    @Override
    @Transactional
    public void updateOne(Creditorinfo entity) throws BusinessException {
        this.creditorinfoDao.updateOne(entity);
    }

    /**
     * 标的是否推送过
     * */
    @Override
    public boolean isSendCreditorinfo(String creditorNo) throws BusinessException {
        Creditorinfo creditorinfo = new Creditorinfo();
        creditorinfo.setCreditorNo(creditorNo);
        creditorinfo.setDisabled(0);
        if(null != this.findOne(creditorinfo)){
            return true;
        }
        return false;
    }

    /**
     * 接收推标
     * */
    @Override
    @Transactional
    public void sendCreditorinfo(CreditorinfoRo ro) throws BusinessException, ParseException {

        logger.info(JsonUtils.serialize(ro));

        //签名校验
        String sign= MD5Util.md5UpCase("Do-It"+ro.getCreditorNo()+"Do-It");
        if(!sign.equals(ro.getSign())){
            logger.info("签名失败"+sign);
            throw new BusinessException(OrderExceptionEnums.SIGE_ERROR);
        }


        //判断标的是否存在
        if(this.isSendCreditorinfo(ro.getCreditorNo())){
            throw new BusinessException(OrderExceptionEnums.SEND_CREDITORINFO_ERROR);
        }else {
            //存入债券表
            Creditorinfo creditorinfo = new Creditorinfo();
            creditorinfo.setCreditorNo(ro.getCreditorNo());
            creditorinfo.setAmountApply(ro.getAmountApply());
            creditorinfo.setBankCardholder(ro.getBankCardholder());
            creditorinfo.setBankCode(ro.getBankCode());
            creditorinfo.setBankName(ro.getBankName());
            creditorinfo.setBankNumber(ro.getBankNumber());
            creditorinfo.setBiddingTime(new Date());
            creditorinfo.setBorrowerYearRate(ro.getBorrowerYearRate());
            creditorinfo.setBorrowingPurposes(ro.getBorrowingPurposes());
            creditorinfo.setChannel(ro.getChannel());
            creditorinfo.setLenderId(ro.getLenderId());
            creditorinfo.setRiskLevel(ro.getRiskLevel());
            creditorinfo.setServiceFee(ro.getServiceFee());
            creditorinfo.setTerm(ro.getTerm());
            this.addOne(creditorinfo);

            //插入散标
            Scatterstandard scatterstandard = new Scatterstandard();
            scatterstandard.setCreditorNo(ro.getCreditorNo());
            scatterstandard.setStatus(1);
            scatterstandard.setTerm(ro.getTerm());
            scatterstandard.setAmountApply(ro.getAmountApply());
            scatterstandard.setBorrowingPurposes(ro.getBorrowingPurposes());
            scatterstandard.setLendingTime(new Date());
            scatterstandard.setRefundIngTime(new Date());
            scatterstandard.setRefundActualTime(new Date());
            SysParamRo sysParamRo = new SysParamRo();
            sysParamRo.setSysKey(payParamContants.SCATTERSTANDARD_YEAR_RATE);
            BigDecimal yearRate = new BigDecimal(sysParamService.sysValueByKey(sysParamRo).getData().getSysValue());
            scatterstandard.setYearRateFin(yearRate);
            scatterstandardService.addOne(scatterstandard);

        }

    }


}