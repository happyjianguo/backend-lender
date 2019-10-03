package com.yqg.user.service.student.studentloansteptwo.impl;

import com.yqg.api.user.student.studentaddressdetail.ro.StudentAddressBasicRo;
import com.yqg.api.user.student.studentloanstepinfo.bo.StudentLoanAddInfBo;
import com.yqg.api.user.student.studentloanstepinfo.ro.StudentLoanStepSearchRo;
import com.yqg.api.user.student.studentloansteptwo.bo.StudentLoanStepTwoBo;
import com.yqg.api.user.student.studentloansteptwo.ro.StudentLoanStepTwoRo;
import com.yqg.common.core.request.BaseSessionIdRo;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.user.dao.student.StudentLoanStepTwoDao;
import com.yqg.user.entity.student.StudentLoanStepInfo;
import com.yqg.user.entity.student.StudentLoanStepTwo;
import com.yqg.user.enums.StudentAddressTypeEnum;
import com.yqg.user.enums.UserExceptionEnums;
import com.yqg.user.service.UserCommonServiceImpl;
import com.yqg.user.service.student.studentloansteptwo.StudentLoanStepTwoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 学生借款申请步骤2
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-10-08 10:31:24
 */
@Service("studentLoanStepTwoService")
public class StudentLoanStepTwoServiceImpl extends UserCommonServiceImpl implements StudentLoanStepTwoService {
    @Autowired
    protected StudentLoanStepTwoDao studentloansteptwoDao;


    @Override
    public StudentLoanStepTwo findById(String id) throws BusinessException {
        return this.studentloansteptwoDao.findOneById(id, new StudentLoanStepTwo());
    }

    @Override
    public <E> E findById(String id, Class<E> boClass) throws BusinessException {
        StudentLoanStepTwo studentloansteptwo = findById(id);
        E bo = BeanCoypUtil.copyToNewObject(studentloansteptwo, boClass);
        return bo;
    }

    @Override
    public StudentLoanStepTwo findOne(StudentLoanStepTwo entity) throws BusinessException {
        return this.studentloansteptwoDao.findOne(entity);
    }

    @Override
    public <E> E findOne(StudentLoanStepTwo entity, Class<E> boClass) throws BusinessException {
        StudentLoanStepTwo studentloansteptwo = findOne(entity);
        E bo = BeanCoypUtil.copyToNewObject(studentloansteptwo, boClass);
        return bo;
    }


    @Override
    public List<StudentLoanStepTwo> findList(StudentLoanStepTwo entity) throws BusinessException {
        return this.studentloansteptwoDao.findForList(entity);
    }

    @Override
    public <E> List<E> findList(StudentLoanStepTwo entity, Class<E> boClass) throws BusinessException {
        List<StudentLoanStepTwo> studentloansteptwoList = findList(entity);
        List<E> boList = new ArrayList<>();
        for (StudentLoanStepTwo studentloansteptwo : studentloansteptwoList) {
            E bo = BeanCoypUtil.copyToNewObject(studentloansteptwo, boClass);
            boList.add(bo);
        }
        return boList;
    }

    @Override
    @Transactional
    public String addOne(StudentLoanStepTwo entity) throws BusinessException {
        return studentloansteptwoDao.addOne(entity);
    }

    @Override
    @Transactional
    public void deleteById(String id) throws BusinessException {
        studentloansteptwoDao.deleteOne(findById(id));
    }

    @Override
    @Transactional
    public void deleteOne(StudentLoanStepTwo entity) throws BusinessException {
        this.studentloansteptwoDao.deleteOne(entity);
    }

    @Override
    @Transactional
    public void updateOne(StudentLoanStepTwo entity) throws BusinessException {
        this.studentloansteptwoDao.updateOne(entity);
    }

    @Override
    @Transactional
    public void stepTwoInfoAdd(StudentLoanStepTwoRo ro) throws BusinessException {
        if(redisUtil.tryLock(ro, 1000)){    //防重复提交1s
            StudentLoanAddInfBo infoBo = new StudentLoanAddInfBo(2,ro.getUserId(), "");
            //修改学生申请步骤
            String orderNo = this.studentLoanStepInfoService.addInfo(infoBo);
            StudentLoanStepTwo search = new StudentLoanStepTwo();
            search.setOrderNo(orderNo);
            ro.setOrderNo(orderNo);

            StudentLoanStepTwo result = this.findOne(search);

            if(result != null){     //已存在数据,删除之前数据
                //StudentLoanStepTwo needDisabled = new StudentLoanStepTwo();
                result.setDisabled(1);
                //needDisabled.setId(result.getId());
                this.updateOne(result);
            }

            StudentAddressBasicRo idAddress = ro.getSchoolAddressRo();  //学生学校地址
            this.studentAddressDetailService.addAddressInfo(idAddress.getProvince(), idAddress.getCity(), idAddress.getBigDirect(),
                    idAddress.getSmallDirect(), idAddress.getDetailed(), ro.getUserId(), StudentAddressTypeEnum.STUDENT_SCHOOL.getType(),orderNo);

            this.addInfo(ro);
        }else {
            throw new BusinessException(UserExceptionEnums.REPEAT_SUBMIT);
        }
    }

    public void addInfo(StudentLoanStepTwoRo ro) throws BusinessException {
        StudentLoanStepTwo addInfo = new StudentLoanStepTwo();
        addInfo.setUserUuid(ro.getUserId());
        addInfo.setOrderNo(ro.getOrderNo());
        addInfo.setSchoolName(ro.getSchoolName());
        addInfo.setSchoolMobile(ro.getSchoolMobile());
        addInfo.setLevel(ro.getLevel());
        addInfo.setEnterYear(ro.getEnterYear());
        addInfo.setSubject(ro.getSubject());
        addInfo.setStudentId(ro.getStudentId());
        addInfo.setAverageBasePoint(ro.getAverageBasePoint());
        addInfo.setSchoolFee(ro.getSchoolFee());
        addInfo.setSchoolFeeTerm(ro.getSchoolFeeTerm());
        addInfo.setSchoolFeePayWay(ro.getSchoolFeePayWay());
        if(!StringUtils.isEmpty(ro.getSchoolFeePayBank())){
            addInfo.setSchoolFeePayBank(ro.getSchoolFeePayBank());
        }
        if(!StringUtils.isEmpty(ro.getSchoolFeePayBankNo())){
            addInfo.setSchoolFeePayBankNo(ro.getSchoolFeePayBankNo());
        }
        if(!StringUtils.isEmpty(ro.getSchoolFeePayDetail())){
            addInfo.setSchoolFeePayDetail(ro.getSchoolFeePayDetail());
        }

        this.addOne(addInfo);

    }

    @Override
    public StudentLoanStepTwoBo getInfo(BaseSessionIdRo ro,Integer flag,String id) throws BusinessException {
        String userUuid = ro.getUserId();

        StudentLoanStepTwoBo response = new StudentLoanStepTwoBo();
        StudentLoanStepInfo info = new StudentLoanStepInfo();

        if(flag == 0){
            info = this.studentLoanStepInfoService.loanInfoByUserUuid(userUuid);
        }else {
            info.setId(id);
        }


        StudentLoanStepTwo search = new StudentLoanStepTwo();
        search.setOrderNo(info.getId());
        StudentLoanStepTwo result = this.findOne(search);

        StudentAddressBasicRo schoolAddress = this.studentAddressDetailService.getStudentAddressDetail(info.getId(),
                StudentAddressTypeEnum.STUDENT_SCHOOL.getType());

        if(result != null){
            response.setSchoolName(result.getSchoolName());
            response.setSchoolMobile(result.getSchoolMobile());
            response.setLevel(result.getLevel());
            response.setEnterYear(result.getEnterYear());
            response.setSubject(result.getSubject());
            response.setStudentId(result.getStudentId());
            response.setAverageBasePoint(result.getAverageBasePoint());
            response.setSchoolFee(result.getSchoolFee());
            response.setSchoolFeeTerm(result.getSchoolFeeTerm());
            response.setSchoolFeePayBank(result.getSchoolFeePayBank());
            response.setSchoolFeePayWay(result.getSchoolFeePayWay());
            response.setSchoolFeePayBank(result.getSchoolFeePayBank());
            response.setSchoolFeePayBankNo(result.getSchoolFeePayBankNo());
            response.setSchoolFeePayDetail(result.getSchoolFeePayDetail());
        }

        if(schoolAddress != null){
            response.setSchoolAddressRo(schoolAddress);
        }

        return response;

    }

    public StudentLoanStepTwoBo getInfoSys(StudentLoanStepSearchRo ro) throws BusinessException {
        BaseSessionIdRo search = new BaseSessionIdRo();
        search.setUserId(ro.getUuid());

        return this.getInfo(search,1, ro.getUuid());
    }

}