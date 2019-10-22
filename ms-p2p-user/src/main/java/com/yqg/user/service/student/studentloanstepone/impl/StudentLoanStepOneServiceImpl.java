package com.yqg.user.service.student.studentloanstepone.impl;

import com.yqg.api.user.student.studentaddressdetail.ro.StudentAddressBasicRo;
import com.yqg.api.user.student.studentloanstepinfo.bo.StudentLoanAddInfBo;
import com.yqg.api.user.student.studentloanstepinfo.ro.StudentLoanStepSearchRo;
import com.yqg.api.user.student.studentloanstepone.bo.StudentLoanStepOneBo;
import com.yqg.api.user.student.studentloanstepone.ro.StudentLoanStepOneRo;
import com.yqg.common.core.request.BaseSessionIdRo;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.user.dao.student.StudentLoanStepOneDao;
import com.yqg.user.entity.student.StudentLoanStepInfo;
import com.yqg.user.entity.student.StudentLoanStepOne;
import com.yqg.api.user.enums.StudentAddressTypeEnum;
import com.yqg.api.user.enums.UserExceptionEnums;
import com.yqg.user.service.UserCommonServiceImpl;
import com.yqg.user.service.student.studentloanstepone.StudentLoanStepOneService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * 学生借款申请步骤1
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-10-08 10:31:24
 */
@Service("studentLoanStepOneService")
public class StudentLoanStepOneServiceImpl extends UserCommonServiceImpl implements StudentLoanStepOneService {
    @Autowired
    protected StudentLoanStepOneDao studentloansteponeDao;


    @Override
    public StudentLoanStepOne findById(String id) throws BusinessException {
        return this.studentloansteponeDao.findOneById(id, new StudentLoanStepOne());
    }

    @Override
    public <E> E findById(String id, Class<E> boClass) throws BusinessException {
        StudentLoanStepOne studentloanstepone = findById(id);
        E bo = BeanCoypUtil.copyToNewObject(studentloanstepone, boClass);
        return bo;
    }

    @Override
    public StudentLoanStepOne findOne(StudentLoanStepOne entity) throws BusinessException {
        return this.studentloansteponeDao.findOne(entity);
    }

    @Override
    public <E> E findOne(StudentLoanStepOne entity, Class<E> boClass) throws BusinessException {
        StudentLoanStepOne studentloanstepone = findOne(entity);
        E bo = BeanCoypUtil.copyToNewObject(studentloanstepone, boClass);
        return bo;
    }


    @Override
    public List<StudentLoanStepOne> findList(StudentLoanStepOne entity) throws BusinessException {
        return this.studentloansteponeDao.findForList(entity);
    }

    @Override
    public <E> List<E> findList(StudentLoanStepOne entity, Class<E> boClass) throws BusinessException {
        List<StudentLoanStepOne> studentloansteponeList = findList(entity);
        List<E> boList = new ArrayList<>();
        for (StudentLoanStepOne studentloanstepone : studentloansteponeList) {
            E bo = BeanCoypUtil.copyToNewObject(studentloanstepone, boClass);
            boList.add(bo);
        }
        return boList;
    }

    @Override
    @Transactional
    public String addOne(StudentLoanStepOne entity) throws BusinessException {
        return studentloansteponeDao.addOne(entity);
    }

    @Override
    @Transactional
    public void deleteById(String id) throws BusinessException {
        studentloansteponeDao.deleteOne(findById(id));
    }

    @Override
    @Transactional
    public void deleteOne(StudentLoanStepOne entity) throws BusinessException {
        this.studentloansteponeDao.deleteOne(entity);
    }

    @Override
    @Transactional
    public void updateOne(StudentLoanStepOne entity) throws BusinessException {
        this.studentloansteponeDao.updateOne(entity);
    }

    @Override
    @Transactional
    public void stepOneInfoAdd(StudentLoanStepOneRo ro) throws BusinessException {
        if(redisUtil.tryLock(ro, 1000)){    //防重复提交1s
            StudentLoanAddInfBo infoBo = new StudentLoanAddInfBo(1,ro.getUserId(), ro.getUserName());
            if(!StringUtils.isEmpty(ro.getInviteCode())){
                infoBo.setInviteCode(ro.getInviteCode());
            }

            //修改学生申请步骤
            String orderNo = this.studentLoanStepInfoService.addInfo(infoBo);

            ro.setOrderNo(orderNo);

            StudentLoanStepOne search = new StudentLoanStepOne();
            search.setOrderNo(orderNo);

            StudentLoanStepOne result = this.findOne(search);

            if(result != null){     //已存在数据,删除之前数据
                //StudentLoanStepOne needDisabled = new StudentLoanStepOne();
                result.setDisabled(1);
                //needDisabled.setId(result.getId());
                this.updateOne(result);
            }

            StudentAddressBasicRo idAddress = ro.getIdAddressRo();  //学生身份证上地址
            this.studentAddressDetailService.addAddressInfo(idAddress.getProvince(), idAddress.getCity(), idAddress.getBigDirect(),
                    idAddress.getSmallDirect(), idAddress.getDetailed(), ro.getUserId(), StudentAddressTypeEnum.STUDENT_ID.getType(),
                    orderNo);

            StudentAddressBasicRo liveAddress = ro.getLiveAddressRo();  //学生现居住地址
            this.studentAddressDetailService.addAddressInfo(liveAddress.getProvince(), liveAddress.getCity(), liveAddress.getBigDirect(),
                    liveAddress.getSmallDirect(), liveAddress.getDetailed(), ro.getUserId(), StudentAddressTypeEnum.STUDENT_LIVE.getType(),
                    orderNo);
            this.addInfo(ro);
        }else {
            throw new BusinessException(UserExceptionEnums.REPEAT_SUBMIT);
        }

    }

    public void addInfo(StudentLoanStepOneRo ro) throws BusinessException {
        StudentLoanStepOne addInfo = new StudentLoanStepOne();
        addInfo.setUserUuid(ro.getUserId());
        addInfo.setOrderNo(ro.getOrderNo());
        addInfo.setUserName(ro.getUserName());
        addInfo.setIdCard(ro.getIdCard());
        addInfo.setSex(ro.getSex());
        addInfo.setAge(ro.getAge());
        addInfo.setReligion(ro.getReligion());

        addInfo.setEmail(ro.getEmail());
        addInfo.setMobileNumber(ro.getMobileNumber());
        addInfo.setFamilyYearSalary(ro.getFamilyYearSalary());
        addInfo.setFamilyMember(ro.getFamilyMember());
        addInfo.setBankName(ro.getBankName());
        addInfo.setBankNo(ro.getBankNo());
        addInfo.setLoanAble(ro.getLoanAble());      //是否有分期贷款
        if(ro.getLoanAble() == 1){      //有分期付款时保存　loanAmount
            addInfo.setLoanAmount(ro.getLoanAmount());  //每月分期付款金额
        }


        addInfo.setIdCardUrl(ro.getIdCardUrl());
        addInfo.setFamilyCardUrl(ro.getFamilyCardUrl());
        addInfo.setStudentCardUrl(ro.getStudentCardUrl());
        addInfo.setIdCardInHandUrl(ro.getIdCardInHandUrl());
        addInfo.setCollageStudentUrl(ro.getCollageStudentUrl());    //大学学生证明照片
        this.addOne(addInfo);
    }

    @Override
    public StudentLoanStepOneBo getInfo(BaseSessionIdRo ro,Integer flag,String id) throws BusinessException {
        String userUuid = ro.getUserId();
        StudentLoanStepOneBo response = new StudentLoanStepOneBo();
        StudentLoanStepInfo info = new StudentLoanStepInfo();
        if(flag == 0){
            info = this.studentLoanStepInfoService.loanInfoByUserUuid(userUuid);
        }else {
            info.setId(id);
        }

        StudentLoanStepOne search = new StudentLoanStepOne();
        search.setOrderNo(info.getId());
        StudentLoanStepOne result = this.findOne(search);

        StudentAddressBasicRo idAddress = this.studentAddressDetailService.getStudentAddressDetail(info.getId(), StudentAddressTypeEnum.STUDENT_ID.getType());
        StudentAddressBasicRo liveAddress = this.studentAddressDetailService.getStudentAddressDetail(info.getId(), StudentAddressTypeEnum.STUDENT_LIVE.getType());


        if(result != null){
            response.setUserUuid(result.getUserUuid());
            response.setUserName(result.getUserName());
            response.setIdCard(result.getIdCard());
            response.setSex(result.getSex());
            response.setAge(result.getAge());
            response.setReligion(result.getReligion());

            response.setEmail(result.getEmail());
            response.setMobileNumber(result.getMobileNumber());
            response.setFamilyYearSalary(result.getFamilyYearSalary());
            response.setFamilyMember(result.getFamilyMember());
            response.setBankName(result.getBankName());
            response.setBankNo(result.getBankNo());
            response.setLoanAble(result.getLoanAble());      //是否有分期贷款
            response.setLoanAmount(result.getLoanAmount());  //每月分期付款金额

            response.setIdCardUrl(result.getIdCardUrl());
            response.setFamilyCardUrl(result.getFamilyCardUrl());
            response.setStudentCardUrl(result.getStudentCardUrl());
            response.setIdCardInHandUrl(result.getIdCardInHandUrl());
            response.setCollageStudentUrl(result.getCollageStudentUrl());    //大学学生证明照片
        }

        if(idAddress != null){  //身份证地址
            response.setIdAddressRo(idAddress);
        }
        if(liveAddress != null){    //居住地址
            response.setLiveAddressRo(liveAddress);
        }

        return response;

    }

    public StudentLoanStepOneBo getInfoSys(StudentLoanStepSearchRo ro) throws BusinessException{

        BaseSessionIdRo search = new BaseSessionIdRo();
        search.setUserId(ro.getUuid());

        StudentLoanStepOneBo response = this.getInfo(search, 1, ro.getUuid());
        return response;
    }

}