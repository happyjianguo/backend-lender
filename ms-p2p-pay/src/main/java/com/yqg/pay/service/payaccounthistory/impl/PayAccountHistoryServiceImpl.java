package com.yqg.pay.service.payaccounthistory.impl;

import com.yqg.api.pay.exception.PayExceptionEnums;
import com.yqg.api.pay.payaccounthistory.bo.BreanchClearPageBo;
import com.yqg.api.pay.payaccounthistory.bo.PayAccountHistoryBo;
import com.yqg.api.pay.payaccounthistory.bo.PayAccountListPageBo;
import com.yqg.api.pay.payaccounthistory.ro.*;
import com.yqg.api.user.useraccounthistory.ro.UserAccountChangeRo;
import com.yqg.api.user.userbank.bo.UserBankBo;
import com.yqg.api.user.userbank.ro.UserBankRo;
import com.yqg.api.user.useruser.bo.UserBo;
import com.yqg.api.user.useruser.ro.UserReq;
import com.yqg.common.core.response.BasePageResponse;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.dao.ExtendQueryCondition;
import com.yqg.common.enums.PayAccountStatusEnum;
import com.yqg.common.enums.TransTypeEnum;
import com.yqg.common.enums.UserAccountBusinessTypeEnum;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.redis.BaseRedisKeyEnums;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.common.utils.DateUtils;
import com.yqg.pay.dao.PayAccountHistoryDao;
import com.yqg.pay.entity.PayAccountHistory;
import com.yqg.pay.service.PayCommonServiceImpl;
import com.yqg.pay.service.payaccounthistory.PayAccountHistoryService;
import com.yqg.pay.service.third.UserAccountService;
import com.yqg.pay.service.third.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.*;

/**
 * 资金流水表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 17:28:20
 */
@Service("payAccountHistoryService")
public class PayAccountHistoryServiceImpl extends PayCommonServiceImpl implements PayAccountHistoryService {
    public Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    protected PayAccountHistoryDao payAccountHistoryDao;
    @Autowired
    protected UserService userService;
    @Autowired
    protected UserAccountService userAccountService;


    @Override
    public PayAccountHistory findById(String id) throws BusinessException {
        return this.payAccountHistoryDao.findOneById(id, new PayAccountHistory());
    }

    @Override
    public <E> E findById(String id, Class<E> boClass) throws BusinessException {
        PayAccountHistory payaccounthistory = findById(id);
        E bo = BeanCoypUtil.copyToNewObject(payaccounthistory, boClass);
        return bo;
    }

    @Override
    public PayAccountHistory findOne(PayAccountHistory entity) throws BusinessException {
        return this.payAccountHistoryDao.findOne(entity);
    }

    @Override
    public <E> E findOne(PayAccountHistory entity, Class<E> boClass) throws BusinessException {
        PayAccountHistory payaccounthistory = findOne(entity);
        E bo = BeanCoypUtil.copyToNewObject(payaccounthistory, boClass);
        return bo;
    }


    @Override
    public List<PayAccountHistory> findList(PayAccountHistory entity) throws BusinessException {
        return this.payAccountHistoryDao.findForList(entity);
    }

    @Override
    public <E> List<E> findList(PayAccountHistory entity, Class<E> boClass) throws BusinessException {
        List<PayAccountHistory> payaccounthistoryList = findList(entity);
        List<E> boList = new ArrayList<>();
        for (PayAccountHistory payaccounthistory : payaccounthistoryList) {
            E bo = BeanCoypUtil.copyToNewObject(payaccounthistory, boClass);
            boList.add(bo);
        }
        return boList;
    }

    @Override
    @Transactional
    public String addOne(PayAccountHistory entity) throws BusinessException {
        return payAccountHistoryDao.addOne(entity);
    }

    @Override
    @Transactional
    public void deleteById(String id) throws BusinessException {
        payAccountHistoryDao.deleteOne(findById(id));
    }

    @Override
    @Transactional
    public void deleteOne(PayAccountHistory entity) throws BusinessException {
        this.payAccountHistoryDao.deleteOne(entity);
    }

    @Override
    @Transactional
    public void updateOne(PayAccountHistory entity) throws BusinessException {
        this.payAccountHistoryDao.updateOne(entity);
    }

    /*理财资金流水查询*/
    @Override
    public BasePageResponse<PayAccountHistory> payAccountListByPage(PayAccountPageRo ro) throws BusinessException,ParseException {
        String timeMax = ro.getTimeMax();
        String timeMin = ro.getTimeMin();

        ExtendQueryCondition extendQueryCondition = new ExtendQueryCondition();
        PayAccountHistory entity = new PayAccountHistory();

        Map<ExtendQueryCondition.CompareType, Object> timeMap = new HashMap<>();     //时间
        if(!StringUtils.isEmpty(timeMax)){
            timeMap.put(ExtendQueryCondition.CompareType.LTE_TIME, DateUtils.stringToDate(timeMax + " 23:59:59"));
        }
        if(!StringUtils.isEmpty(timeMin)){
            timeMap.put(ExtendQueryCondition.CompareType.LTE_TIME, DateUtils.stringToDate(timeMin + " 00:00:00"));
        }
        if(!StringUtils.isEmpty(timeMax) || !StringUtils.isEmpty(timeMin)){
            extendQueryCondition.addCompareQueryMap(PayAccountHistory.createTime_Field, timeMap);
            entity.setExtendQueryCondition(extendQueryCondition);
        }
        if(ro.getTradeType() != null){      //交易类型
            entity.setTradeType(ro.getTradeType());
        }

        if(!StringUtils.isEmpty(ro.getOrderNo())){      //理财订单号
            entity.setOrderNo(ro.getOrderNo());
        }

        if(!StringUtils.isEmpty(ro.getTradeNo())){      //债权编号
            entity.setTradeNo(ro.getTradeNo());
        }

        entity.setStatus(2);    //仅查出成功的
        ro.setSortProperty("sort");
        ro.setSortDirection(Sort.Direction.DESC);

        Page<PayAccountHistory> historyPage = this.payAccountHistoryDao.findForPage(entity, ro.convertPageRequest());
        BasePageResponse<PayAccountHistory> response = new BasePageResponse<>(historyPage);

        List<PayAccountHistory> payAccountHistories = historyPage.getContent();
        for(PayAccountHistory cell:payAccountHistories){
            UserReq search = new UserReq();
            search.setUserUuid(cell.getToUserId());
            BaseResponse<UserBo> result = this.userService.findOneByMobileOrId(search);     //查询用户手机号

            if (!result.isSuccess() || result.getCode() != 0) {
                throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
            }
            UserBo userResult = result.getData();
            if(userResult != null){
                cell.setToUserId(userResult.getMobileNumber());     //资金入方
            }

            search.setUserUuid(cell.getFromUserId());
            BaseResponse<UserBo> fromResult = this.userService.findOneByMobileOrId(search);     //查询用户手机号

            if (!fromResult.isSuccess() || fromResult.getCode() != 0) {
                throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
            }
            UserBo fromUserResult = result.getData();
            if(userResult != null){
                cell.setFromUserId(fromUserResult.getMobileNumber());     //资金出方
            }
        }

        response.setContent(payAccountHistories);


        return response;

    }

    @Override
    public BasePageResponse<PayAccountListPageBo> payListByPage(PayAccountListPageRo ro) throws Exception{
        String timeMax = ro.getTimeMax();
        String timeMin = ro.getTimeMin();

        ExtendQueryCondition extendQueryCondition = new ExtendQueryCondition();
        PayAccountHistory entity = new PayAccountHistory();

        Map<ExtendQueryCondition.CompareType, Object> timeMap = new HashMap<>();     //时间
        if(!StringUtils.isEmpty(timeMax)){
            timeMap.put(ExtendQueryCondition.CompareType.LTE_TIME, DateUtils.stringToDate(timeMax + " 23:59:59"));
        }
        if(!StringUtils.isEmpty(timeMin)){
            timeMap.put(ExtendQueryCondition.CompareType.LTE_TIME, DateUtils.stringToDate(timeMin + " 00:00:00"));
        }
        if(!StringUtils.isEmpty(timeMax) || !StringUtils.isEmpty(timeMin)){
            extendQueryCondition.addCompareQueryMap(PayAccountHistory.createTime_Field, timeMap);
            entity.setExtendQueryCondition(extendQueryCondition);
        }

        if(!StringUtils.isEmpty(ro.getStatus())){      //状态
            entity.setStatus(ro.getStatus());
        }

        //根据 姓名或手机号 查询用户 UUID
        UserReq search = new UserReq();
        if (!StringUtils.isEmpty(ro.getMobile())){
            search.setMobileNumber(ro.getMobile());
        }
        if (!StringUtils.isEmpty(ro.getName())){
            search.setRealName(ro.getName());
        }
        if (!StringUtils.isEmpty(ro.getName()) || !StringUtils.isEmpty(ro.getName())){
            logger.info("开始掉用户服务{}",search);
            BaseResponse<UserBo> result = this.userService.findOneByMobileOrName(search);     //查询用户
            logger.info("yonghu jieshu {}",result);

            if (!result.isSuccess() || result.getCode() != 0) {
                throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
            }
            UserBo userResult = result.getData();
            if(userResult != null){
                entity.setFromUserId(userResult.getId());     //投资人 id
            }
        }

        if (!StringUtils.isEmpty(ro.getChannel())){
            entity.setPaychannel(ro.getChannel());
        }
        entity.setTradeType(ro.getType());//充值 提现

        ro.setSortProperty("sort");
        ro.setSortDirection(Sort.Direction.DESC);
        logger.info("参数组装完毕{}",entity);

        Page<PayAccountHistory> historyPage = this.payAccountHistoryDao.findForPage(entity, ro.convertPageRequest());
        List<PayAccountHistory> payAccountHistories = historyPage.getContent();

        List<PayAccountListPageBo> boList = new ArrayList<>();

        for(PayAccountHistory cell:payAccountHistories) {
            PayAccountListPageBo pageBo = BeanCoypUtil.copyToNewObject(cell, PayAccountListPageBo.class);
            pageBo.setId(cell.getId());
            pageBo.setCreateTime(cell.getCreateTime().toString());
            UserReq userReq = new UserReq();
            if (!StringUtils.isEmpty(ro.getType()) && ("P2P_BOND").equals(ro.getType())){
                userReq.setUserUuid(cell.getFromUserId());
            }else {
                userReq.setUserUuid(cell.getToUserId());
            }

            BaseResponse<UserBo> userBoBaseResponse = this.userService.findOneByMobileOrId(userReq);     //查询用户手机号

            if (!userBoBaseResponse.isSuccess() || userBoBaseResponse.getCode() != 0) {
                throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
            }
            UserBo userBo = userBoBaseResponse.getData();
            if (userBo != null) {
                pageBo.setFromUserId(userBo.getRealName());     //投资人姓名
                pageBo.setToUserId(userBo.getMobileNumber());     //投资人手机号
            }
            boList.add(pageBo);
        }

        BasePageResponse<PayAccountListPageBo> response = new BasePageResponse<>(historyPage);
        response.setContent(boList);

        return response;
    }

    @Override
    @Transactional(readOnly = false)
    public void updatePayAccountHistoryById(PayAccountHistoryUpdateRo ro) throws BusinessException{

        PayAccountHistory payAccountHistory =this.findById(ro.getId());

        payAccountHistory.setRemark(ro.getRemark());
        payAccountHistory.setDealStatus(ro.getDealStatus());

        this.updateOne(payAccountHistory);
    }

    @Override
    public String paymentCodeByOrderNo(PayAccountHistoryRo ro) throws Exception {
        PayAccountHistory payAccountHistory =new PayAccountHistory();
        payAccountHistory.setOrderNo(ro.getOrderNo());
        payAccountHistory = this.findOne(payAccountHistory);
        return payAccountHistory.getPaymentCode();
    }

    @Override
    public List<PayAccountHistoryBo> getPayAccountHistoryByType(PayAccountHistoryRo ro) throws Exception{
        List<PayAccountHistoryBo> list = new ArrayList<>();
        PayAccountHistory history = new PayAccountHistory();
        history.setTradeType(ro.getTradeType());
        history.setStatus(1);//处理中的
        history.setDisabled(0);
        list = this.findList(history,PayAccountHistoryBo.class);
        return list;
    }

    @Override
    @Transactional(readOnly = false)
    public void addPayAccountHistory(PayAccountHistoryRo ro) throws BusinessException {
        PayAccountHistory payAccountHistory = new PayAccountHistory();
        payAccountHistory.setAmount(ro.getAmount());
        payAccountHistory.setTradeNo(ro.getTradeNo());
        payAccountHistory.setStatus(ro.getStatus());
        payAccountHistory.setDealStatus(ro.getDealStatus());
        payAccountHistory.setFromUserId(ro.getFromUserId());
        payAccountHistory.setToUserId(ro.getToUserId());
        payAccountHistory.setTradeType(ro.getTradeType());
        payAccountHistoryDao.addOne(payAccountHistory);

    }

    public BasePageResponse<BreanchClearPageBo> getBranchClearList(BreanchClearPageRo ro) throws ParseException, BusinessException {
        String timeMax = ro.getTimeMax();
        String timeMin = ro.getTimeMin();

        ExtendQueryCondition extendQueryCondition = new ExtendQueryCondition();
        PayAccountHistory entity = new PayAccountHistory();

        Map<ExtendQueryCondition.CompareType, Object> timeMap = new HashMap<>();     //时间
        if(!StringUtils.isEmpty(timeMax)){
            timeMap.put(ExtendQueryCondition.CompareType.LTE_TIME, DateUtils.stringToDate(timeMax + " 23:59:59"));
        }
        if(!StringUtils.isEmpty(timeMin)){
            timeMap.put(ExtendQueryCondition.CompareType.GTE_TIME, DateUtils.stringToDate(timeMin + " 00:00:00"));
        }
        if(!StringUtils.isEmpty(timeMax) || !StringUtils.isEmpty(timeMin)){
            extendQueryCondition.addCompareQueryMap(PayAccountHistory.createTime_Field, timeMap);
            entity.setExtendQueryCondition(extendQueryCondition);
        }

        if(!StringUtils.isEmpty(ro.getStatus())&&ro.getStatus()!=0){      //状态
            entity.setStatus(ro.getStatus());
        }
        entity.setTradeType(TransTypeEnum.BRANCH_CLEAR.getDisburseType());
        Page<PayAccountHistory> page = this.payAccountHistoryDao.findForPage(entity, ro.convertPageRequest());
        List<PayAccountHistory> list = page.getContent();
        List<BreanchClearPageBo> bos = new LinkedList<>();
        BasePageResponse<BreanchClearPageBo> response = new BasePageResponse<>(page);
        for (PayAccountHistory history:list){
            BreanchClearPageBo bo = new BreanchClearPageBo();
            bo.setCreateTime(history.getCreateTime());
            UserReq userReq = new UserReq();
            userReq.setUserUuid(history.getFromUserId());
            BaseResponse<UserBo> fromUser = this.userService.findUserById(userReq);
            bo.setFromUser(fromUser.getData().getUserName());
            userReq.setUserUuid(history.getToUserId());
            BaseResponse<UserBo> toUser = this.userService.findUserById(userReq);
            bo.setToUser(toUser.getData().getUserName());
            UserBankRo userBankRo = new UserBankRo();
            userBankRo.setUserUuid(history.getToUserId());
            BaseResponse<UserBankBo> info = this.userBankService.getUserBankInfo(userBankRo);
            bo.setToAccount(info.getData().getBankName());
//            UserAccountBo escrowAccount = this.getEscrowAccount(toUser.getData().getBankCode());
//            bo.setToAccountNo(escrowAccount.);
            bo.setAmount(history.getAmount().intValue()+"");
            bo.setStatus(history.getStatus());
            bo.setRemark(history.getRemark());
            bo.setId(history.getId());
            bos.add(bo);
        }
        response.setContent(bos);

        return response;
    }

    /**
     * 编辑操作机构清分资金流水
     * 状态若为已打款，添加对应超级投资人的账户流水并增加可用余额
     * @param ro
     */
    @Override
    @Transactional(readOnly = false)
    public void updatePayAccountHistoryByIdForBranchClear(PayAccountHistoryUpdateRo ro) throws BusinessException {
        PayAccountHistory payAccountHistory =this.findById(ro.getId());
        String userId = redisUtil.get(BaseRedisKeyEnums.USER_SESSION_KEY.appendToDefaultKey(ro.getSessionId()));
        if (payAccountHistory.getStatus().equals(PayAccountStatusEnum.SUCCESS)){
            throw new BusinessException(PayExceptionEnums.ACCOUNT_ERROR);
        }
        if (ro.getDealStatus().equals("1")){
            payAccountHistory.setRemark(ro.getRemark());
            payAccountHistory.setUpdateUser(userId);
            payAccountHistory.setUpdateTime(new Date());
            this.updateOne(payAccountHistory);
        }else if (ro.getDealStatus().equals("2")){
            payAccountHistory.setStatus(Integer.parseInt(ro.getDealStatus()));
            payAccountHistory.setPayTime(ro.getPayTime());
            payAccountHistory.setRemark(ro.getRemark());
            payAccountHistory.setUpdateTime(new Date());
            payAccountHistory.setUpdateUser(userId);
            this.updateOne(payAccountHistory);
            UserAccountChangeRo userAccountChangeRo = new UserAccountChangeRo();
            userAccountChangeRo.setAmount(payAccountHistory.getAmount());
            userAccountChangeRo.setUserUuid(payAccountHistory.getToUserId());
//            userAccountChangeRo.setBusinessType("75%服务费");
            userAccountChangeRo.setBusinessType(UserAccountBusinessTypeEnum.SERVICE_FEE.getEnname());
            userAccountChangeRo.setTradeInfo("机构投资人清分收入清分给超级投资人");
            userAccountService.addUserCurrentBlance(userAccountChangeRo);
        }
    }

}