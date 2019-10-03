package com.yqg.pay.service.payaccounthistory.impl;

import com.yqg.api.pay.payaccounthistory.ro.PayAccountPageRo;
import com.yqg.api.user.useruser.bo.UserBo;
import com.yqg.api.user.useruser.ro.UserReq;
import com.yqg.common.core.response.BasePageResponse;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.dao.ExtendQueryCondition;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.common.utils.DateUtils;
import com.yqg.pay.dao.PayAccountHistoryDao;
import com.yqg.pay.entity.PayAccountHistory;
import com.yqg.pay.service.PayCommonServiceImpl;
import com.yqg.pay.service.payaccounthistory.PayAccountHistoryService;
import com.yqg.pay.service.third.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资金流水表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 17:28:20
 */
@Service("payAccountHistoryService")
public class PayAccountHistoryServiceImpl extends PayCommonServiceImpl implements PayAccountHistoryService {
    @Autowired
    protected PayAccountHistoryDao payAccountHistoryDao;
    /*@Autowired
    protected UserService userService;*/


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

}