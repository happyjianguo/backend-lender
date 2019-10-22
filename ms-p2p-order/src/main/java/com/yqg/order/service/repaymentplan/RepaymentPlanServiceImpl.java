package com.yqg.order.service.repaymentplan;

import com.alibaba.fastjson.JSONObject;
import com.yqg.api.order.orderorder.bo.RepaymentPlanBo;
import com.yqg.api.order.orderorder.ro.RepaymentPlanRo;
import com.yqg.common.enums.OrderExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.common.utils.JsonUtils;
import com.yqg.common.utils.MD5Util;
import com.yqg.order.dao.RepaymentPlanDao;
import com.yqg.order.entity.RepaymentPlan;
import com.yqg.order.service.OrderCommonServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lixiangjun on 2019/7/1.
 */
@Slf4j
@Service("repaymentPlanService")
public class RepaymentPlanServiceImpl extends OrderCommonServiceImpl implements RepaymentPlanService {

    /**
     * 日志组件
     */
    protected final Logger logger = LoggerFactory.getLogger(getClass());


    @Value("${third.doitLoan:url:repaymentPlanUrl}")
    private String repaymentPlanUrl;
    @Autowired
    @Qualifier(value = "remoteRestTemplate")
    protected RestTemplate remoteRestTemplate;

    @Autowired
    RepaymentPlanDao repaymentPlanDao;


    @Override
    @Transactional
    public void addRepaymentPlan(RepaymentPlanRo ro) throws BusinessException {

        logger.info(JsonUtils.serialize(ro));

        //签名校验
        String sign= MD5Util.md5UpCase("Do-It"+ro.getCreditorNo()+"Do-It");
        if(!sign.equals(ro.getSign())){
            logger.info("签名失败"+sign);
            throw new BusinessException(OrderExceptionEnums.SIGE_ERROR);
        }

        List<RepaymentPlanRo> list = ro.getList();
        for(RepaymentPlanRo temp:list){
            RepaymentPlan entity = new RepaymentPlan();
            BeanCoypUtil.copy(temp,entity);
            entity.setCreditorNo(ro.getCreditorNo());
            this.addOne(entity);
            logger.info("存入还款计划成功：{}",entity);
        }
    }

    @Override
    @Transactional
    public List<RepaymentPlanBo> findRepaymentPlanList(RepaymentPlanRo ro) throws Exception {
        List<RepaymentPlanBo> list = new ArrayList<>();
        RepaymentPlan entity = new RepaymentPlan();
        entity.setCreditorNo(ro.getCreditorNo());
        List<RepaymentPlan> repaymentPlanList = this.findList(entity);
        if(StringUtils.isEmpty(repaymentPlanList)){
            //主动查doit存入还款计划表
            logger.info("没有存入还款计划，主动查还款计划开始");
            JSONObject jsonObject = repaymentPlanHttpPost(repaymentPlanUrl, ro);

            RepaymentPlan repaymentPlan = new RepaymentPlan();
            BeanCoypUtil.copy(ro,repaymentPlan);
            this.addOne(repaymentPlan);
            logger.info("没有存入还款计划，主动查还款计划结束");
        }else {
            for(RepaymentPlan temp : repaymentPlanList){
                RepaymentPlanBo bo = new RepaymentPlanBo();
                BeanCoypUtil.copy(temp,bo);
                list.add(bo);
            }
        }
        return list;
    }

    @Override
    @Transactional
    public void updateStatus(RepaymentPlanRo ro) throws BusinessException {
        RepaymentPlan entity = new RepaymentPlan();
        entity.setCreditorNo(ro.getCreditorNo());
        entity.setPeriodNo(ro.getPeriodNo());
        entity.setStatus(1);
        RepaymentPlan repaymentPlan = this.findOne(entity);
        if(repaymentPlan!=null){
            repaymentPlan.setStatus(ro.getStatus());
            this.updateOne(repaymentPlan);
            logger.info("还款计划状态更新：{}",repaymentPlan);
        }
    }


    protected JSONObject repaymentPlanHttpPost(String url, RepaymentPlanRo ro) throws IllegalAccessException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "*/*");
        headers.set("X-AUTH-TOKEN", "doit");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        logger.info("请求参数---------" + ro.getCreditorNo().toString());
        String urlStr = url + "?" + ro.getCreditorNo().toString();
        logger.info("请求URL------" + urlStr);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(new LinkedMultiValueMap<String, String>(), headers);
        ResponseEntity<String> response = this.remoteRestTemplate.exchange(urlStr, HttpMethod.POST, entity, String.class);
        JSONObject jsonObject = JSONObject.parseObject(response.getBody());
        logger.info("请求结果返回----------" + jsonObject.toJSONString());
        return jsonObject;
    }

    @Override
    @Transactional
    public String addOne(RepaymentPlan repaymentPlan) throws BusinessException {
        return repaymentPlanDao.addOne(repaymentPlan);
    }

    @Override
    public RepaymentPlan findById(String s) throws BusinessException {
        return repaymentPlanDao.findOneById(s,new RepaymentPlan());
    }

    @Override
    public <E> E findById(String s, Class<E> aClass) throws BusinessException {
        RepaymentPlan repaymentPlan = findById(s);
        E bo = BeanCoypUtil.copyToNewObject(repaymentPlan, aClass);
        return bo;
    }

    @Override
    public RepaymentPlan findOne(RepaymentPlan repaymentPlan) throws BusinessException {
        return repaymentPlanDao.findOne(repaymentPlan);
    }

    @Override
    public <E> E findOne(RepaymentPlan repaymentPlan, Class<E> aClass) throws BusinessException {
        RepaymentPlan one = findOne(repaymentPlan);
        E bo = BeanCoypUtil.copyToNewObject(one, aClass);
        return bo;
    }

    @Override
    public List<RepaymentPlan> findList(RepaymentPlan repaymentPlan) throws BusinessException {
        return repaymentPlanDao.findForList(repaymentPlan);
    }

    @Override
    public <E> List<E> findList(RepaymentPlan entity, Class<E> aClass) throws BusinessException {
        List<RepaymentPlan> list = findList(entity);
        List<E> boList = new ArrayList<>();
        for (RepaymentPlan repaymentPlan : list){
            E bo = BeanCoypUtil.copyToNewObject(repaymentPlan, aClass);
            boList.add(bo);
        }
        return boList;
    }

    @Override
    @Transactional
    public void updateOne(RepaymentPlan repaymentPlan) throws BusinessException {
        this.repaymentPlanDao.updateOne(repaymentPlan);
    }

    @Override
    @Transactional
    public void deleteById(String s) throws BusinessException {
        this.repaymentPlanDao.deleteOne(findById(s));
    }

    @Override
    @Transactional
    public void deleteOne(RepaymentPlan repaymentPlan) throws BusinessException {
        this.repaymentPlanDao.deleteOne(repaymentPlan);
    }
}
