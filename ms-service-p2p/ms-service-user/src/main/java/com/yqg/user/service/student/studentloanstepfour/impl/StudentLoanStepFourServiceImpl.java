package com.yqg.user.service.student.studentloanstepfour.impl;

import com.yqg.api.user.student.studentloanstepfour.bo.StudentLoanStepFourBo;
import com.yqg.api.user.student.studentloanstepfour.ro.StudentLoanStepFourRo;
import com.yqg.api.user.student.studentloanstepinfo.bo.StudentLoanAddInfBo;
import com.yqg.api.user.student.studentloanstepinfo.ro.StudentLoanStepSearchRo;
import com.yqg.common.core.request.BaseSessionIdRo;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.user.dao.student.StudentLoanStepFourDao;
import com.yqg.user.entity.student.StudentLoanStepFour;

import com.yqg.user.entity.student.StudentLoanStepInfo;
import com.yqg.user.enums.UserExceptionEnums;
import com.yqg.user.service.UserCommonServiceImpl;
import com.yqg.user.service.student.studentloanstepfour.StudentLoanStepFourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * 学生借款申请步骤4(联系人信息)
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-10-08 10:31:24
 */
@Service("studentLoanStepFourService")
public class StudentLoanStepFourServiceImpl extends UserCommonServiceImpl implements StudentLoanStepFourService {
    @Autowired
    protected StudentLoanStepFourDao studentloanstepfourDao;


    @Override
    public StudentLoanStepFour findById(String id) throws BusinessException {
        return this.studentloanstepfourDao.findOneById(id, new StudentLoanStepFour());
    }

    @Override
    public <E> E findById(String id, Class<E> boClass) throws BusinessException {
        StudentLoanStepFour studentloanstepfour = findById(id);
        E bo = BeanCoypUtil.copyToNewObject(studentloanstepfour, boClass);
        return bo;
    }

    @Override
    public StudentLoanStepFour findOne(StudentLoanStepFour entity) throws BusinessException {
        return this.studentloanstepfourDao.findOne(entity);
    }

    @Override
    public <E> E findOne(StudentLoanStepFour entity, Class<E> boClass) throws BusinessException {
        StudentLoanStepFour studentloanstepfour = findOne(entity);
        E bo = BeanCoypUtil.copyToNewObject(studentloanstepfour, boClass);
        return bo;
    }


    @Override
    public List<StudentLoanStepFour> findList(StudentLoanStepFour entity) throws BusinessException {
        return this.studentloanstepfourDao.findForList(entity);
    }

    @Override
    public <E> List<E> findList(StudentLoanStepFour entity, Class<E> boClass) throws BusinessException {
        List<StudentLoanStepFour> studentloanstepfourList = findList(entity);
        List<E> boList = new ArrayList<>();
        for (StudentLoanStepFour studentloanstepfour : studentloanstepfourList) {
            E bo = BeanCoypUtil.copyToNewObject(studentloanstepfour, boClass);
            boList.add(bo);
        }
        return boList;
    }

    @Override
    @Transactional
    public String addOne(StudentLoanStepFour entity) throws BusinessException {
        return studentloanstepfourDao.addOne(entity);
    }

    @Override
    @Transactional
    public void deleteById(String id) throws BusinessException {
        studentloanstepfourDao.deleteOne(findById(id));
    }

    @Override
    @Transactional
    public void deleteOne(StudentLoanStepFour entity) throws BusinessException {
        this.studentloanstepfourDao.deleteOne(entity);
    }

    @Override
    @Transactional
    public void updateOne(StudentLoanStepFour entity) throws BusinessException {
        this.studentloanstepfourDao.updateOne(entity);
    }

    @Override
    @Transactional
    public void stepFourInfoAdd(StudentLoanStepFourRo ro) throws BusinessException {
        if(redisUtil.tryLock(ro, 1000)){    //防重复提交1s
            StudentLoanAddInfBo infoBo = new StudentLoanAddInfBo(4,ro.getUserId(), "");
            String orderNo = this.studentLoanStepInfoService.addInfo(infoBo);

            StudentLoanStepFour search = new StudentLoanStepFour();
            search.setOrderNo(orderNo);

            StudentLoanStepFour result = this.findOne(search);

            if(result != null){
                //StudentLoanStepFour needDisabled = new StudentLoanStepFour();
                result.setDisabled(1);
                //needDisabled.setId(result.getId());
                this.updateOne(result);
            }

            StudentLoanStepFour addInfo = new StudentLoanStepFour();
            addInfo.setUserUuid(ro.getUserId());
            addInfo.setUsername(ro.getUsername());
            addInfo.setMobileNumber(ro.getMobileNumber());
            addInfo.setRelationship(ro.getRelationship());
            addInfo.setOrderNo(orderNo);
            this.addOne(addInfo);
        }else {
            throw new BusinessException(UserExceptionEnums.REPEAT_SUBMIT);
        }

    }

    @Override
    public StudentLoanStepFourBo getInfo(BaseSessionIdRo ro,Integer flag,String id) throws BusinessException{
        String userUuid = ro.getUserId();

        StudentLoanStepInfo info = new StudentLoanStepInfo();
        if(flag == 0){
            info = this.studentLoanStepInfoService.loanInfoByUserUuid(userUuid);
        }else {
            info.setId(id);
        }

        StudentLoanStepFour search = new StudentLoanStepFour();
        StudentLoanStepFourBo response = new StudentLoanStepFourBo();

        search.setOrderNo(info.getId());
        StudentLoanStepFour result = this.findOne(search);

        if(result != null){
            response.setMobileNumber(result.getMobileNumber());
            response.setRelationship(result.getRelationship());
            response.setUsername(result.getUsername());
            response.setUserUuid(result.getUserUuid());
        }

        return response;
    }

    @Override
    public StudentLoanStepFourBo getInfoSys(StudentLoanStepSearchRo ro) throws BusinessException {
        BaseSessionIdRo search = new BaseSessionIdRo();
        search.setUserId(ro.getUuid());

        return this.getInfo(search,1,ro.getUuid());
    }

}