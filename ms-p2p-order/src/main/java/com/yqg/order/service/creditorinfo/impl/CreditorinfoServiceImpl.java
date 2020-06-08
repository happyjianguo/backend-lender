package com.yqg.order.service.creditorinfo.impl;

import com.alibaba.fastjson.JSONObject;
import com.yqg.api.order.creditorinfo.bo.LoanHistiryBo;
import com.yqg.api.order.creditorinfo.bo.ScatterstandardListBo;
import com.yqg.api.order.creditorinfo.ro.CreditorinfoRo;
import com.yqg.api.order.scatterstandard.bo.ScatterstandardBo;
import com.yqg.api.order.scatterstandard.ro.LoanHistoryRo;
import com.yqg.api.order.scatterstandard.ro.ScatterstandardPageRo;
import com.yqg.api.pay.exception.PayExceptionEnums;
import com.yqg.api.system.sysparam.ro.SysParamRo;
import com.yqg.api.user.userbank.bo.UserBankBo;
import com.yqg.api.user.userbank.ro.UserBankRo;
import com.yqg.api.user.useruser.bo.UserBo;
import com.yqg.api.user.useruser.ro.UserReq;
import com.yqg.common.core.response.BasePageResponse;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.dao.ExtendQueryCondition;
import com.yqg.common.enums.CreditorTypeEnum;
import com.yqg.common.enums.OrderExceptionEnums;
import com.yqg.common.enums.RedisKeyEnums;
import com.yqg.common.enums.ScatterStandardStatusEnums;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.httpclient.RestTemplateUtil;
import com.yqg.common.redis.PayParamContants;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.common.utils.JsonUtils;
import com.yqg.common.utils.MD5Util;
import com.yqg.order.dao.CreditorinfoDao;
import com.yqg.order.entity.Creditorinfo;
import com.yqg.order.entity.Scatterstandard;
import com.yqg.order.service.OrderCommonServiceImpl;
import com.yqg.order.service.creditorinfo.CreditorinfoService;
import com.yqg.order.service.scatterstandard.ScatterstandardService;
import org.omg.PortableInterceptor.USER_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.*;

/**
 * 债权表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 11:02:36
 */
@Service("creditorInfoService")
public class CreditorinfoServiceImpl extends OrderCommonServiceImpl implements CreditorinfoService {
    @Autowired
    protected CreditorinfoDao creditorinfoDao;
    @Autowired
    protected ScatterstandardService scatterstandardService;
    @Autowired
    private PayParamContants payParamContants;
    @Autowired
    @Qualifier(value = "remoteRestTemplate")
    protected RestTemplate remoteRestTemplate;
    @Autowired
    protected RestTemplateUtil restTemplateUtil;


    @Override
    public Creditorinfo findById(String id) throws BusinessException {
        return this.creditorinfoDao.findOneById(id, new Creditorinfo());
    }

    @Override
    public Creditorinfo findByCreditorNo(String creditorNo) throws BusinessException {
        Creditorinfo entity = new Creditorinfo();
        entity.setDisabled(0);
        entity.setCreditorNo(creditorNo);
        return this.creditorinfoDao.findOne(entity);
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
        Creditorinfo one = this.findOne(creditorinfo);
        if(null != one){
            Scatterstandard scatterstandard = this.scatterstandardService.findOneByCreditorNo(one.getCreditorNo());
            if (!scatterstandard.getStatus().equals(ScatterStandardStatusEnums.LOAN_ERROR.getCode())){
                return true;
            }
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

        //Signature verification
        String sign= MD5Util.md5UpCase("Do-It"+ro.getCreditorNo()+"Do-It");
        if(!sign.equals(ro.getSign())){
            logger.info("签名失败"+sign);
            throw new BusinessException(OrderExceptionEnums.SIGE_ERROR);
        }


        //Determine whether the target exists
        if(this.isSendCreditorinfo(ro.getCreditorNo())){
            throw new BusinessException(OrderExceptionEnums.SEND_CREDITORINFO_ERROR);
        }else {
            //
            //If it is the repayment of the failed loan form, the original claim and the bulk bid are disabled here
            Scatterstandard one = scatterstandardService.findOneByCreditorNo(ro.getCreditorNo());
            if (null != one){
                logger.info("旧标的放款失败，进行逻辑删除 creditorNo：{}",ro.getCreditorNo());
                scatterstandardService.deleteOne(one);
                Creditorinfo creditorinfo = new Creditorinfo();
                creditorinfo.setCreditorNo(ro.getCreditorNo());
                Creditorinfo one1 = findOne(creditorinfo);
                deleteOne(one1);
            }
            Integer type = ro.getCreditorType();
            if (type == null){
                type = 1;
            }
            //存入债权表

            BigDecimal actualamountApply = BigDecimal.ZERO;
            if(ro.getAmountApply()!=null && ro.getServiceFee()!=null){
                actualamountApply = ro.getAmountApply().subtract(ro.getServiceFee());
            }

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

            creditorinfo.setName(ro.getName());
            creditorinfo.setIdCardNo(ro.getIdCardNo());
            creditorinfo.setMobile(ro.getMobile());
            creditorinfo.setSex(ro.getSex());
            creditorinfo.setAddress(ro.getAddress());
            creditorinfo.setAcdemic(ro.getAcdemic());
            creditorinfo.setAge(ro.getAge());
            creditorinfo.setBirty(ro.getBirty());
            creditorinfo.setEmail(ro.getEmail());
            creditorinfo.setIsInsuranceCardAuth(ro.getIsInsuranceCardAuth());
            creditorinfo.setIsBankCardAuth(ro.getIsBankCardAuth());
            creditorinfo.setIsFamilyCardAuth(ro.getIsFamilyCardAuth());
            creditorinfo.setIsIdentidyAuth(ro.getIsIdentidyAuth());
            creditorinfo.setIsLindManAuth(ro.getIsLindManAuth());
            creditorinfo.setIsMarried(ro.getIsMarried());
            creditorinfo.setInhabit(ro.getInhabit());
            creditorinfo.setReligion(ro.getReligion());
            creditorinfo.setIdentidy(ro.getIdentidy());
            creditorinfo.setCreditScore(ro.getCreditScore());
            creditorinfo.setCreditorType(type);
            creditorinfo.setDetail(ro.getDetail());
            this.addOne(creditorinfo);

            //插入散标
            Scatterstandard scatterstandard = new Scatterstandard();
            scatterstandard.setCreditorNo(ro.getCreditorNo());
            scatterstandard.setStatus(1);
            scatterstandard.setTerm(ro.getTerm());
            scatterstandard.setAmountApply(actualamountApply);
            scatterstandard.setBorrowingPurposes(ro.getBorrowingPurposes());
            scatterstandard.setLendingTime(new Date());
            scatterstandard.setRefundIngTime(new Date());
            scatterstandard.setRefundActualTime(new Date());
            SysParamRo sysParamRo = new SysParamRo();
            sysParamRo.setSysKey(payParamContants.SCATTERSTANDARD_YEAR_RATE);
            BigDecimal yearRate = new BigDecimal(sysParamService.sysValueByKey(sysParamRo).getData().getSysValue()).setScale(4);
            logger.info("yearRate-------{}",yearRate);
            scatterstandard.setYearRateFin(yearRate);
            scatterstandard.setCreditorType(type);
            if (type == CreditorTypeEnum.STAGING.getType()){
                scatterstandard.setLimitCount(ro.getDetail());
            }else if (type == CreditorTypeEnum.EXTENSION.getType()){
                scatterstandard.setPreCreditorNo(ro.getDetail());
            }
            scatterstandardService.addOne(scatterstandard);
        }

    }

    /**
     * 分页复合条件查询散标列表
     * @param ro
     * @return
     * @throws BusinessException
     */
    @Override
    public ScatterstandardListBo queryForPage(ScatterstandardPageRo ro) throws BusinessException {
        UserReq userReq = new UserReq();
        userReq.setUserUuid(ro.getUserId());
        BaseResponse<UserBo> user = this.userService.findUserById(userReq);
        int insurance = 0;
        if(user.getData().getIsinsurance()!=null){
            insurance = user.getData().getIsinsurance();
        }
        if(user.getData().getAuthStatus()!=2){
            if(user.getData().getBankCode()==null){
                throw new BusinessException(PayExceptionEnums.USER_NOT_BINDCARD);
            }
            if(user.getData().getUserName()==null){
                throw new BusinessException(PayExceptionEnums.USER_NOT_REALNAME);
            }
        }
        Creditorinfo entity = new Creditorinfo();
        ExtendQueryCondition extendQueryCondition = new ExtendQueryCondition();
        //期限
        if (!CollectionUtils.isEmpty(ro.getTerms())){
            List<Object> tradeTypes = new ArrayList<>();
            for (String term: ro.getTerms()){
                tradeTypes.add(term);
            }
            extendQueryCondition.addInQueryMap(Creditorinfo.term_field, tradeTypes);
        }
        //年龄
        if (null != ro.getUpperAge()&&null != ro.getLowerAge()){
            Map<ExtendQueryCondition.CompareType, Object> map = new HashMap<>();
            map.put(ExtendQueryCondition.CompareType.GTE, ro.getLowerAge());
            map.put(ExtendQueryCondition.CompareType.LTE, ro.getUpperAge());
            extendQueryCondition.addCompareQueryMap(Creditorinfo.age_field, map);
        }
        //性别
        if (null != ro.getSex()){
            if (ro.getSex() != 0){
                entity.setSex(ro.getSex().toString());
            }
        }
        //地址
        if (!StringUtils.isEmpty(ro.getProvince())){
            StringBuffer sb = new StringBuffer(ro.getProvince());
            if (!StringUtils.isEmpty(ro.getCity())){
                sb.append("#"+ro.getCity());
            }
            extendQueryCondition.addLikeQueyField(Creditorinfo.address_field);
            entity.setAddress(sb.toString());
        }
        entity.setStatus(0);
        if(!StringUtils.isEmpty(ro.getBorrowingPurpose())) {
            entity.setBorrowingPurposes(ro.getBorrowingPurpose());
        }
        //金额
        if (!CollectionUtils.isEmpty(ro.getAmountApplys())){
            List<BigDecimal> amounts = this.creditorinfoDao.getApplyAmounts();
            List<Object> list = new ArrayList<>();
            for (BigDecimal bd: amounts){
                for (String s: ro.getAmountApplys()){
                    if (!s.contains("#")){
                        throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
                    }
                    if (s.startsWith("#")){
                        int i = Integer.parseInt(s.replaceAll("#", ""))*10000;
                        if (bd.intValue()<=i){
                            list.add(bd);
                            continue;
                        }
                    }else if (s.endsWith("#")){
                        int i = Integer.parseInt(s.replaceAll("#", ""))*10000;
                        if (bd.intValue()>i){
                            list.add(bd);
                            continue;
                        }
                    }else{
                        String[] split = s.split("#");
                        int i = Integer.parseInt(split[0])*10000;
                        int j = Integer.parseInt(split[1])*10000;
                        if (bd.intValue()<=j&&bd.intValue()>i){
                            list.add(bd);
                            continue;
                        }
                    }
                }
            }
            //Compatible with JPA inquery list cannot be empty
            if (CollectionUtils.isEmpty(list)){
                list.add(new BigDecimal(0));
            }
            extendQueryCondition.addInQueryMap(Creditorinfo.amountApply_field, list);
        }
        // Query the bank code corresponding to the current user
        // rizky remove bank filter
//        UserBankRo userBankRo = new UserBankRo();
//        userBankRo.setUserUuid(ro.getUserId());
//        BaseResponse<UserBankBo> userBankBoBaseResponse = userBankService.getUserBankInfo(userBankRo);
//        logger.info("userBankBoBaseResponse------------:[{}]",userBankBoBaseResponse);
//        String bankCode = "";
//        if (null!=userBankBoBaseResponse.getData()){
//            bankCode =userBankBoBaseResponse.getData().getBankCode();
//        }
//        if (!StringUtils.isEmpty(bankCode)){
//            //BCA——>BCA；
//            //BNI——>BNI；
//            //（改）CIMB——>所有非BCA，BNI银行的
//            //（改2）CIMB——>所有银行
//            if (!bankCode.equals("CIMB")){
//                entity.setBankCode(bankCode);
//            }
//        }
        entity.setExtendQueryCondition(extendQueryCondition);
        if (ro.getSortDirection()==null||ro.getSortProperty()==null){
            ro.setSortProperty("createTime");
            ro.setSortDirection(Sort.Direction.DESC);
        }
        Page<Creditorinfo> forPage = creditorinfoDao.findForPage(entity, ro.convertPageRequest());
        // Cache the credit id of all query results to redis to provide one-click to add to the shopping cart function
        List<Creditorinfo> forList = creditorinfoDao.findForList(entity);
        List<Object> creditorNos = new ArrayList<>();
        for (Creditorinfo creditorinfo: forList){
            creditorNos.add(creditorinfo.getCreditorNo());
        }
        this.redisUtil.set(RedisKeyEnums.SCATTERSTANDARD_QUERY_CACHE_KEY.appendToDefaultKey(user.getData().getMobileNumber()), creditorNos);

        // Calculate the total purchase amount of the current query results
        Scatterstandard scatterstandard1 = new Scatterstandard();
        ExtendQueryCondition extendQueryCondition1 = new ExtendQueryCondition();
        extendQueryCondition1.addInQueryMap(Scatterstandard.creditorNo_field, creditorNos);
        scatterstandard1.setExtendQueryCondition(extendQueryCondition1);
        List<Scatterstandard> scatterstandards = scatterstandardService.findForList(scatterstandard1);
        BigDecimal decimal = new BigDecimal(0);
        for (Scatterstandard ss:scatterstandards){
            BigDecimal subtract = ss.getAmountApply().subtract(ss.getAmountBuy()).subtract(ss.getAmountLock());
            logger.info("creditorNo:{}  可购买金额:{}", ss.getCreditorNo(), subtract);
            decimal = decimal.add(subtract);
        }
        logger.info("The total remaining purchaseable amount of this query result is：{}", decimal);

        List<Creditorinfo> content = forPage.getContent();
        List<Scatterstandard> list = scatterstandardService.findListByCreditor(content);
        BasePageResponse<ScatterstandardBo> response = new BasePageResponse<>(forPage);
        List<ScatterstandardBo> objects = new ArrayList<>();
        for (Scatterstandard scatterstandard: list){
            ScatterstandardBo bo = new ScatterstandardBo();
            bo.setBorrowingPurposes(scatterstandard.getBorrowingPurposes());
            bo.setTerm(scatterstandard.getTerm());
            bo.setAmountApply(scatterstandard.getAmountApply());
            bo.setAmountBuy(scatterstandard.getAmountBuy().add(scatterstandard.getAmountLock()));
            bo.setYearRateFin(scatterstandard.getYearRateFin().toString());
            bo.setCreditorNo(scatterstandard.getCreditorNo());
            if(insurance==1) {
                bo.setInsurance(scatterstandard.getAmountApply().multiply(new BigDecimal(11)).divide(new BigDecimal(100), RoundingMode.HALF_UP));
            }
            else{
                bo.setInsurance(new BigDecimal(0));
            }
            objects.add(bo);
        }
        response.setContent(objects);
        ScatterstandardListBo bo = new ScatterstandardListBo();
        bo.setBo(response);
        if(insurance==1) {
            decimal = decimal.multiply(new BigDecimal(11)).divide(new BigDecimal(100), RoundingMode.HALF_UP);
        }
        bo.setAmount(decimal);
        return bo;
    }

    @Override
    public List<LoanHistiryBo> selectLoanHistoryByNumber(LoanHistoryRo ro) throws BusinessException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(ro), headers);
        logger.info("查询用户历史成功借款记录" + JSONObject.toJSONString(ro));
        logger.info("------------{}----------" + doitConfig.getDoItLoanUrl());
        JSONObject jsonObject = remoteRestTemplate.postForObject(doitConfig.getDoItLoanUrl() + "/p2p/settled-loan-list", entity, JSONObject.class);
        logger.info("返回查询用户历史成功借款记录" + jsonObject);
        if (StringUtils.isEmpty(jsonObject.getString("code")) || !jsonObject.getString("code").equals("0")) {
            throw new BusinessException(BaseExceptionEnums.SYSTERM_ERROR);
        }
        List<LoanHistiryBo> list = (List<LoanHistiryBo>)jsonObject.get("data");

        return list;
    }

    @Override
    public Integer getLoanHistoryCountByMobileNumber(String mobileNumber) throws BusinessException{
        Creditorinfo creditorinfo = new Creditorinfo();
        creditorinfo.setMobile(mobileNumber);
        creditorinfo.setDisabled(0);
        List<Creditorinfo> list = this.findList(creditorinfo);
        if (CollectionUtils.isEmpty(list)){
            return 0;
        }
        return list.size();
    }

    public static void main(String[] args) {

        BigDecimal bigDecimal = new BigDecimal(0);
        bigDecimal.add(new BigDecimal(100));
        System.out.println(bigDecimal);
//        List<String> list1 = new ArrayList<>();
//        list1.add("#2000000");
//        list1.add("6000000#8000000");
//        list1.add("9000000#");
//        List<BigDecimal> amounts = new ArrayList<>();
//        amounts.add(new BigDecimal(1000000));
//        amounts.add(new BigDecimal(2000000));
//        amounts.add(new BigDecimal(5000000));
//        amounts.add(new BigDecimal(6000000));
//        amounts.add(new BigDecimal(8000000));
//        amounts.add(new BigDecimal(9000000));
//        amounts.add(new BigDecimal(10000000));
//        List<Object> list = new ArrayList<>();
//        for (BigDecimal bd: amounts){
//            for (String s: list1){
//                if (s.startsWith("#")){
//                    int i = Integer.parseInt(s.replaceAll("#", ""));
//                    if (bd.intValue()<=i){
//                        list.add(bd);
//                        continue;
//                    }
//                }else if (s.endsWith("#")){
//                    int i = Integer.parseInt(s.replaceAll("#", ""));
//                    if (bd.intValue()>i){
//                        list.add(bd);
//                        continue;
//                    }
//                }else{
//                    String[] split = s.split("#");
//                    int i = Integer.parseInt(split[0]);
//                    int j = Integer.parseInt(split[1]);
//                    if (bd.intValue()<=j&&bd.intValue()>i){
//                        list.add(bd);
//                        continue;
//                    }
//                }
//            }
//        }
//        System.out.println(list.toString());
    }


}