package com.yqg.user.service.student.studentloanstepinfo.impl;

import com.yqg.api.system.sysoperatehistory.ro.SysOperateHistoryAddRo;
import com.yqg.api.user.student.studentloanstepinfo.bo.StudentLoanAddInfBo;
import com.yqg.api.user.student.studentloanstepinfo.bo.StudentLoanRepayBo;
import com.yqg.api.user.student.studentloanstepinfo.bo.StudentLoanStepInfoBo;
import com.yqg.api.user.student.studentloanstepinfo.ro.StudentLoanCheckRo;
import com.yqg.api.user.student.studentloanstepinfo.ro.StudentLoanInitRo;
import com.yqg.api.user.student.studentloanstepinfo.ro.StudentLoanStepInfoRo;
import com.yqg.common.core.request.BasePageRo;
import com.yqg.common.core.request.BaseSessionIdRo;
import com.yqg.common.core.response.BasePageResponse;
import com.yqg.common.dao.ExtendQueryCondition;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.common.utils.DateUtils;
import com.yqg.user.dao.student.StudentLoanStepInfoDao;
import com.yqg.user.entity.student.StudentLoanStepInfo;
import com.yqg.api.user.enums.StudentLoanStatusEnum;
import com.yqg.api.user.enums.UserExceptionEnums;
import com.yqg.user.service.UserCommonServiceImpl;
import com.yqg.user.service.student.studentloanstepinfo.StudentLoanStepInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.ParseException;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 学生借款信息步骤表
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-10-08 10:31:24
 */
@Service("studentLoanStepInfoService")
public class StudentLoanStepInfoServiceImpl extends UserCommonServiceImpl implements StudentLoanStepInfoService {
    @Autowired
    protected StudentLoanStepInfoDao studentloanstepinfoDao;


    @Override
    public StudentLoanStepInfo findById(String id) throws BusinessException {
        return this.studentloanstepinfoDao.findOneById(id, new StudentLoanStepInfo());
    }

    @Override
    public <E> E findById(String id, Class<E> boClass) throws BusinessException {
        StudentLoanStepInfo studentloanstepinfo = findById(id);
        E bo = BeanCoypUtil.copyToNewObject(studentloanstepinfo, boClass);
        return bo;
    }

    @Override
    public StudentLoanStepInfo findOne(StudentLoanStepInfo entity) throws BusinessException {
        return this.studentloanstepinfoDao.findOne(entity);
    }

    @Override
    public <E> E findOne(StudentLoanStepInfo entity, Class<E> boClass) throws BusinessException {
        StudentLoanStepInfo studentloanstepinfo = findOne(entity);
        E bo = BeanCoypUtil.copyToNewObject(studentloanstepinfo, boClass);
        return bo;
    }


    @Override
    public List<StudentLoanStepInfo> findList(StudentLoanStepInfo entity) throws BusinessException {
        return this.studentloanstepinfoDao.findForList(entity);
    }

    @Override
    public <E> List<E> findList(StudentLoanStepInfo entity, Class<E> boClass) throws BusinessException {
        List<StudentLoanStepInfo> studentloanstepinfoList = findList(entity);
        List<E> boList = new ArrayList<>();
        for (StudentLoanStepInfo studentloanstepinfo : studentloanstepinfoList) {
            E bo = BeanCoypUtil.copyToNewObject(studentloanstepinfo, boClass);
            boList.add(bo);
        }
        return boList;
    }

    @Override
    @Transactional
    public String addOne(StudentLoanStepInfo entity) throws BusinessException {
        return studentloanstepinfoDao.addOne(entity);
    }

    @Override
    @Transactional
    public void deleteById(String id) throws BusinessException {
        studentloanstepinfoDao.deleteOne(findById(id));
    }

    @Override
    @Transactional
    public void deleteOne(StudentLoanStepInfo entity) throws BusinessException {
        this.studentloanstepinfoDao.deleteOne(entity);
    }

    @Override
    @Transactional
    public void updateOne(StudentLoanStepInfo entity) throws BusinessException {
        this.studentloanstepinfoDao.updateOne(entity);
    }

    @Override
    public String addInfo(StudentLoanAddInfBo bo) throws BusinessException {
        String orderNo = "";
        StudentLoanStepInfo search = new StudentLoanStepInfo();
        search.setUserUuid(bo.getUserUuid());
        search.setStatus(StudentLoanStatusEnum.NOT_SUBMIT.getType());   //待提交的订单
        Integer step = bo.getStep();

        StudentLoanStepInfo result = this.findOne(search);

        if(result != null){ //  之前有填写数据修改
            if(step == null || step < result.getStep()){    //步骤不能回退
                throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
            }
            if(step == 1){  //第一步修改用户姓名
                result.setRealName(bo.getRealname());
                if(!StringUtils.isEmpty(bo.getInviteCode())){       //添加邀请
                    result.setInviteCode(bo.getInviteCode());
                }
            }
            if(step == 4){
                result.setStatus(StudentLoanStatusEnum.NOT_CHECK.getType());    //如果提交第四步信息则修改订单状态为待审核
            }
            result.setStep(step);
            this.updateOne(result);
            orderNo = result.getId();
        }else {     //  之前没有数据报错，去重新初始化
            throw new BusinessException(UserExceptionEnums.STUDENT_LOAN_NOT_INIT);
        }

        return orderNo;
    }

    @Override
    public StudentLoanStepInfoBo getInfo(BaseSessionIdRo ro) throws BusinessException {
        StudentLoanStepInfo search = new StudentLoanStepInfo();
        String userUuid = ro.getUserId();
        search.setUserUuid(userUuid);

        StudentLoanStepInfoBo response = new StudentLoanStepInfoBo();

        BasePageRo request = new BasePageRo();
        request.setPageNo(1);
        request.setPageSize(1);
        request.setSortProperty(StudentLoanStepInfo.sort_Field);
        request.setSortDirection(Direction.DESC);
        Page<StudentLoanStepInfo> result = this.studentloanstepinfoDao.findForPage(search, request.convertPageRequest());       //查询最新一条
        List<StudentLoanStepInfo> orderList = result.getContent();

        if(!CollectionUtils.isEmpty(orderList)){
            StudentLoanStepInfo lastOrder = orderList.get(0);
            response.setStep(lastOrder.getStep());
            response.setUserUuid(userUuid);
            if(lastOrder.getStatus() != StudentLoanStatusEnum.NOT_CHECK.getType()){
                response.setInitAble(0);
            }else {
                response.setInitAble(1);
            }
        }

        return response;
    }

    public StudentLoanStepInfo loanInfoByUserUuid(String userUuid) throws BusinessException {
        StudentLoanStepInfo search = new StudentLoanStepInfo();
        search.setUserUuid(userUuid);
        search.setStatus(StudentLoanStatusEnum.NOT_SUBMIT.getType());
        List<StudentLoanStepInfo> result = this.findList(search);

        if(CollectionUtils.isEmpty(result)){
            throw new BusinessException(UserExceptionEnums.STUDENT_LOAN_NOT_INIT);
        }
        if(result.size() > 1){
            throw new BusinessException(UserExceptionEnums.STUDENT_LOAN_ERROR);
        }
        return result.get(0);

    }

    @Override
    @Transactional
    public StudentLoanStepInfoBo StudentLoanInit(StudentLoanInitRo ro) throws BusinessException {
        String userUuid = ro.getUserId();
        BigDecimal applyAmount = ro.getAmountApply();
        Integer term = ro.getTerm();

        StudentLoanStepInfoBo response = new StudentLoanStepInfoBo();
        if(redisUtil.tryLock(ro, 1000)){    //防重复提交1s
            if(term < 1 || applyAmount.compareTo(new BigDecimal(0.00)) < 0 ){
                throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
            }


            StudentLoanStepInfo search = new StudentLoanStepInfo();
            search.setUserUuid(userUuid);
            search.setStatus(StudentLoanStatusEnum.NOT_CHECK.getType());   //待审核

            List<StudentLoanStepInfo> notCheckList = this.findList(search);
            if(!CollectionUtils.isEmpty(notCheckList)){
                throw new BusinessException(UserExceptionEnums.STUDENT_LOAN_CHECKING);
            }

            search.setStatus(StudentLoanStatusEnum.NOT_SUBMIT.getType());   //待提交
            List<StudentLoanStepInfo> notSubmitList = this.findList(search);

            if(CollectionUtils.isEmpty(notSubmitList)){     //没有则添加
                search.setStatus(StudentLoanStatusEnum.PASS.getType());     //计算审核通过次数
                List<StudentLoanStepInfo> passList = this.findList(search);

                Integer passTime = 0;   //审核通过次数
                if(!CollectionUtils.isEmpty(passList)){
                    passTime = passList.size();
                }
                this.addLoanStepInfo(userUuid,passTime,applyAmount,term);       //添加订单数据
                response.setStep(0);    //默认进入第一步
            }else {    //有则继续完善
                if(notSubmitList.size() > 1){
                    throw new BusinessException(UserExceptionEnums.STUDENT_LOAN_ERROR);
                }
                StudentLoanStepInfo editInfo = notSubmitList.get(0);
                editInfo.setAmountApply(applyAmount);
                editInfo.setTerm(term);
                editInfo.setUpdateTime(new Date());
                this.updateOne(editInfo);
                response.setStep(editInfo.getStep());
            }
        }else {
            throw new BusinessException(UserExceptionEnums.REPEAT_SUBMIT);
        }



        response.setUserUuid(userUuid);

        return response;
    }

    public void addLoanStepInfo(String userUuid,Integer passTime,BigDecimal amountAooly,Integer term) throws BusinessException {
        StudentLoanStepInfo add = new StudentLoanStepInfo();

        add.setStatus(StudentLoanStatusEnum.NOT_SUBMIT.getType());
        if(passTime > 0){
            add.setIsAgain(1);  //是复借
        }else {
            add.setIsAgain(0);
        }
        add.setPassTime(passTime);
        add.setAmountApply(amountAooly);
        add.setTerm(term);
        add.setUserUuid(userUuid);
        add.setStep(0); //默认为第一步
        this.addOne(add);

    }

    public List<StudentLoanRepayBo> StudentLoanRepayCount(StudentLoanInitRo ro) throws BusinessException {
        List<StudentLoanRepayBo> response = new ArrayList<>();
        BigDecimal applyAmount = ro.getAmountApply();
        Integer term = ro.getTerm();

        if(term < 1 || applyAmount.compareTo(new BigDecimal(0.00)) < 0 ){
            throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
        }

        for(Integer count = 0;count < term;count++){
            StudentLoanRepayBo cell = new StudentLoanRepayBo();
            cell.setTime(getNextMonth(count));
            BigDecimal repayMoney = new BigDecimal(0.00);
            if(count == 0){
                repayMoney = applyAmount.multiply(new BigDecimal(0.1)).add(applyAmount.multiply(new BigDecimal(0.03)));
            }else {
                repayMoney = applyAmount.multiply(new BigDecimal(0.9)).divide(new BigDecimal(term - 1))
                        .add(applyAmount.multiply(new BigDecimal(0.0175)));
            }
            cell.setAmount(repayMoney.setScale(4,BigDecimal.ROUND_DOWN).setScale(-2, BigDecimal.ROUND_UP).intValue());
            response.add(cell);
        }

        return response;
    }

    /*public List<StudentLoanRepayBo> countLoanRepay(Integer term,BigDecimal applyAmount ) throws BusinessException {
        List<StudentLoanRepayBo> response = new ArrayList<>();

        for(Integer count = 0;count < term;count++){
            StudentLoanRepayBo cell = new StudentLoanRepayBo();
            cell.setTime(getNextMonth(count));
            BigDecimal repayMoney = new BigDecimal(0.00);
            if(count == 0){
                repayMoney = applyAmount.multiply(new BigDecimal(0.1)).add(applyAmount.multiply(new BigDecimal(0.03)));
            }else {
                repayMoney = applyAmount.multiply(new BigDecimal(0.9)).divide(new BigDecimal(term - 1))
                        .add(applyAmount.multiply(new BigDecimal(0.0175)));
            }
            cell.setAmount(repayMoney.setScale(4,BigDecimal.ROUND_DOWN).setScale(-2, BigDecimal.ROUND_UP).intValue());
            response.add(cell);
        }

        return response;

    }*/

    public static String getNextMonth(Integer count) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        //Date date = new Date();

        cal.add(Calendar.MONTH, count);
        String dateString = sdf.format(cal.getTime());

        return dateString;
    }

    @Override
    public BasePageResponse<StudentLoanStepInfo> queryForPage(StudentLoanStepInfoRo ro) throws BusinessException,ParseException {
        String createTimeMin = ro.getCreateTimeMin();
        String createTimeMax = ro.getCreateTimeMax();

        ExtendQueryCondition extendQueryCondition = new ExtendQueryCondition();

        StudentLoanStepInfo entity = new StudentLoanStepInfo();

        Map<ExtendQueryCondition.CompareType, Object> map = new HashMap<>();

        if(!StringUtils.isEmpty(createTimeMin)){
            map.put(ExtendQueryCondition.CompareType.GTE_TIME, DateUtils.stringToDate(createTimeMin + " 00:00:00"));
        }
        if(!StringUtils.isEmpty(createTimeMax)){
            map.put(ExtendQueryCondition.CompareType.LTE_TIME, DateUtils.stringToDate(createTimeMax + " 23:59:59"));
        }
        if(!StringUtils.isEmpty(createTimeMin) || !StringUtils.isEmpty(createTimeMax)){
            extendQueryCondition.addCompareQueryMap(StudentLoanStepInfo.createTime_Field, map);
            entity.setExtendQueryCondition(extendQueryCondition);
        }

        //比较查询
        if(!StringUtils.isEmpty(ro.getId())){
            entity.setId(ro.getId());
        }
        if(!StringUtils.isEmpty(ro.getRealName())){
            entity.setRealName(ro.getRealName());
        }
        if(!StringUtils.isEmpty(ro.getAmountApply())){
            entity.setAmountApply(new BigDecimal(ro.getAmountApply()));
        }
        if(ro.getTerm() != null){
            entity.setTerm(ro.getTerm());
        }
        if(ro.getStatus() != null){
            entity.setStatus(ro.getStatus());
        }

        ro.setSortProperty("sort");
        ro.setSortDirection(Sort.Direction.DESC);
        Page<StudentLoanStepInfo> studentLoanPage = studentloanstepinfoDao.findForPage(entity, ro.convertPageRequest());

        BasePageResponse<StudentLoanStepInfo> response = new BasePageResponse<>(studentLoanPage);
        response.setContent(studentLoanPage.getContent());

        return response;
    }

    @Transactional
    public void checkStudentLoan(StudentLoanCheckRo ro) throws BusinessException {
        Integer pass = ro.getPass();
        StudentLoanStepInfo search = new StudentLoanStepInfo();
        search.setId(ro.getId());
        StudentLoanStepInfo loanStepInfo = this.findOne(search);

        if(loanStepInfo == null || loanStepInfo.getStatus() != StudentLoanStatusEnum.NOT_CHECK.getType()){
            throw new BusinessException(UserExceptionEnums.STUDENT_LOAN_STATUS_ERROR);
        }
        if(pass == 0){  //审核通过
            loanStepInfo.setStatus(StudentLoanStatusEnum.PASS.getType());
        }else {
            loanStepInfo.setStatus(StudentLoanStatusEnum.REFUSE.getType());
        }
        loanStepInfo.setUpdateTime(new Date());
        loanStepInfo.setUpdateUser(ro.getUserId());
        this.updateOne(loanStepInfo);

        SysOperateHistoryAddRo historyInfo = new SysOperateHistoryAddRo();
        //historyInfo.setCreateUser(ro.getUserId());
        if(pass == 0){
            historyInfo.setOperateString(ro.getUserId() + "审核通过了" + ro.getId() + "该笔订单");
        }else {
            historyInfo.setOperateString(ro.getUserId() + "拒绝了" + ro.getId() + "该笔订单");
        }
        historyInfo.setIpAddress("");

        historyInfo.setType(2);
        historyInfo.setUserName(ro.getUsername());
        historyInfo.setSessionId(ro.getSessionId());
        this.sysOperateHistoryThirdService.addOperateHistory(historyInfo);
    }

}