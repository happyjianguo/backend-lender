package com.yqg.user.service.student.studentloanstepthree.impl;

import com.yqg.api.user.student.studentaddressdetail.ro.StudentAddressBasicRo;
import com.yqg.api.user.student.studentloanstepinfo.bo.StudentLoanAddInfBo;
import com.yqg.api.user.student.studentloanstepinfo.ro.StudentLoanStepSearchRo;
import com.yqg.api.user.student.studentloanstepthree.bo.StudentLoanStepThreeBo;
import com.yqg.api.user.student.studentloanstepthree.ro.StudentLoanStepThreeRo;
import com.yqg.common.core.request.BaseSessionIdRo;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.user.dao.student.StudentLoanStepThreeDao;
import com.yqg.user.entity.student.StudentLoanStepInfo;
import com.yqg.user.entity.student.StudentLoanStepThree;
import com.yqg.api.user.enums.StudentAddressTypeEnum;
import com.yqg.api.user.enums.UserExceptionEnums;
import com.yqg.user.service.UserCommonServiceImpl;
import com.yqg.user.service.student.studentloanstepthree.StudentLoanStepThreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 学生借款申请步骤3(担保人信息)
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-10-08 10:31:24
 */
@Service("studentLoanStepThreeService")
public class StudentLoanStepThreeServiceImpl extends UserCommonServiceImpl implements StudentLoanStepThreeService {
    @Autowired
    protected StudentLoanStepThreeDao studentloanstepthreeDao;


    @Override
    public StudentLoanStepThree findById(String id) throws BusinessException {
        return this.studentloanstepthreeDao.findOneById(id, new StudentLoanStepThree());
    }

    @Override
    public <E> E findById(String id, Class<E> boClass) throws BusinessException {
        StudentLoanStepThree studentloanstepthree = findById(id);
        E bo = BeanCoypUtil.copyToNewObject(studentloanstepthree, boClass);
        return bo;
    }

    @Override
    public StudentLoanStepThree findOne(StudentLoanStepThree entity) throws BusinessException {
        return this.studentloanstepthreeDao.findOne(entity);
    }

    @Override
    public <E> E findOne(StudentLoanStepThree entity, Class<E> boClass) throws BusinessException {
        StudentLoanStepThree studentloanstepthree = findOne(entity);
        E bo = BeanCoypUtil.copyToNewObject(studentloanstepthree, boClass);
        return bo;
    }


    @Override
    public List<StudentLoanStepThree> findList(StudentLoanStepThree entity) throws BusinessException {
        return this.studentloanstepthreeDao.findForList(entity);
    }

    @Override
    public <E> List<E> findList(StudentLoanStepThree entity, Class<E> boClass) throws BusinessException {
        List<StudentLoanStepThree> studentloanstepthreeList = findList(entity);
        List<E> boList = new ArrayList<>();
        for (StudentLoanStepThree studentloanstepthree : studentloanstepthreeList) {
            E bo = BeanCoypUtil.copyToNewObject(studentloanstepthree, boClass);
            boList.add(bo);
        }
        return boList;
    }

    @Override
    @Transactional
    public String addOne(StudentLoanStepThree entity) throws BusinessException {
        return studentloanstepthreeDao.addOne(entity);
    }

    @Override
    @Transactional
    public void deleteById(String id) throws BusinessException {
        studentloanstepthreeDao.deleteOne(findById(id));
    }

    @Override
    @Transactional
    public void deleteOne(StudentLoanStepThree entity) throws BusinessException {
        this.studentloanstepthreeDao.deleteOne(entity);
    }

    @Override
    @Transactional
    public void updateOne(StudentLoanStepThree entity) throws BusinessException {
        this.studentloanstepthreeDao.updateOne(entity);
    }

    @Override
    @Transactional
    public void stepThreeInfoAdd(StudentLoanStepThreeRo ro) throws BusinessException {
        if(redisUtil.tryLock(ro, 1000)){    //防重复提交1s
            StudentLoanAddInfBo infoBo = new StudentLoanAddInfBo(3,ro.getUserId(), "");
            String orderNo = this.studentLoanStepInfoService.addInfo(infoBo);
            StudentLoanStepThree search = new StudentLoanStepThree();
            search.setOrderNo(orderNo);
            ro.setOrderNo(orderNo);

            StudentLoanStepThree result = this.findOne(search);

            if(result != null){
                //StudentLoanStepThree needDisabled = new StudentLoanStepThree();
                result.setDisabled(1);
                //needDisabled.setId(result.getId());
                this.updateOne(result);
            }

            StudentAddressBasicRo idAddressRo = ro.getIdAddressRo();    //担保人身份证地址
            this.studentAddressDetailService.addAddressInfo(idAddressRo.getProvince(), idAddressRo.getCity(), idAddressRo.getBigDirect(),
                    idAddressRo.getSmallDirect(), idAddressRo.getDetailed(), ro.getUserId(), StudentAddressTypeEnum.GUARANTEE_ID.getType(),orderNo);

            StudentAddressBasicRo liveAddressRo = ro.getLiveAddressRo();    //担保人现居地地址
            this.studentAddressDetailService.addAddressInfo(liveAddressRo.getProvince(), liveAddressRo.getCity(), liveAddressRo.getBigDirect(),
                    liveAddressRo.getSmallDirect(), liveAddressRo.getDetailed(), ro.getUserId(), StudentAddressTypeEnum.GUARANTEE_LIVE.getType(),orderNo);

            StudentAddressBasicRo liveCompanyAddressRo = ro.getLiveCompanyAddressRo();  //担保人公司地址
            this.studentAddressDetailService.addAddressInfo(liveCompanyAddressRo.getProvince(), liveCompanyAddressRo.getCity(), liveCompanyAddressRo.getBigDirect(),
                    liveCompanyAddressRo.getSmallDirect(), liveCompanyAddressRo.getDetailed(), ro.getUserId(), StudentAddressTypeEnum.GUARANTEE_COMPANY.getType(),orderNo);

            this.addInfo(ro);
        }else {
            throw new BusinessException(UserExceptionEnums.REPEAT_SUBMIT);
        }

    }

    public void addInfo(StudentLoanStepThreeRo ro) throws BusinessException{
        StudentLoanStepThree addInfo = new StudentLoanStepThree();
        addInfo.setUserUuid(ro.getUserId());
        addInfo.setOrderNo(ro.getOrderNo());
        addInfo.setUserName(ro.getUserName());
        addInfo.setIdCardNo(ro.getIdCardNo());
        addInfo.setRelationship(ro.getRelationship());
        addInfo.setTaxCardNo(ro.getTaxCardNo());    //税卡号
        addInfo.setEmail(ro.getEmail());
        addInfo.setMobileNumber(ro.getMobileNumber());
        addInfo.setCompanyName(ro.getCompanyName());
        addInfo.setCompanyMobile(ro.getCompanyMobile());
        addInfo.setPosition(ro.getPosition());
        addInfo.setIncome(ro.getIncome());
        addInfo.setDoitLoan(ro.getDoitLoan());      //是否在doit借款
        addInfo.setIdCardUrl(ro.getIdCardUrl());
        if(!StringUtils.isEmpty(ro.getPayDetailUrl())){
            addInfo.setPayDetailUrl(ro.getPayDetailUrl());      //工资单照片(选填)
        }

        addInfo.setAggrementUrl(ro.getAggrementUrl());      //担保人声明照片url
        addInfo.setAggrementInHandUrl(ro.getAggrementInHandUrl());      //担保人持有声明照片

        this.addOne(addInfo);

    }

    public StudentLoanStepThreeBo getInfo(BaseSessionIdRo ro,Integer flag,String id) throws BusinessException {
        String userUuid = ro.getUserId();

        StudentLoanStepThreeBo response = new StudentLoanStepThreeBo();
        StudentLoanStepInfo info = new StudentLoanStepInfo();
        if(flag == 0){      //前端反显
            info = this.studentLoanStepInfoService.loanInfoByUserUuid(userUuid);
        }else {             //管理后台反显
            info.setId(id);
        }


        StudentLoanStepThree search = new StudentLoanStepThree();
        search.setOrderNo(info.getId());
        StudentLoanStepThree result = this.findOne(search);

        StudentAddressBasicRo idAddressRo = this.studentAddressDetailService.getStudentAddressDetail(info.getId(),
                StudentAddressTypeEnum.GUARANTEE_ID.getType());

        StudentAddressBasicRo liveAddressRo = this.studentAddressDetailService.getStudentAddressDetail(info.getId(),
                StudentAddressTypeEnum.GUARANTEE_LIVE.getType());

        StudentAddressBasicRo liveCompanyAddressRo = this.studentAddressDetailService.getStudentAddressDetail(info.getId(),
                StudentAddressTypeEnum.GUARANTEE_COMPANY.getType());

        if(idAddressRo != null){
            response.setIdAddressRo(idAddressRo);
        }
        if(liveAddressRo != null){
            response.setLiveAddressRo(liveAddressRo);
        }
        if(liveCompanyAddressRo != null){
            response.setLiveCompanyAddressRo(liveCompanyAddressRo);
        }

        if(result != null){
            response.setUserName(result.getUserName());
            response.setIdCardNo(result.getIdCardNo());
            response.setRelationship(result.getRelationship());
            response.setTaxCardNo(result.getTaxCardNo());
            response.setEmail(result.getEmail());
            response.setMobileNumber(result.getMobileNumber());
            response.setCompanyName(result.getCompanyName());
            response.setCompanyMobile(result.getCompanyMobile());
            response.setPosition(result.getPosition());
            response.setIncome(result.getIncome());
            response.setDoitLoan(result.getDoitLoan());
            response.setIdCardUrl(result.getIdCardUrl());
            response.setPayDetailUrl(result.getPayDetailUrl());     //工资单
            response.setAggrementUrl(result.getAggrementUrl());      //担保人声明照片url
            response.setAggrementInHandUrl(result.getAggrementInHandUrl());//担保人持有声明照片
        }

        return response;
    }

    public StudentLoanStepThreeBo getInfoSys(StudentLoanStepSearchRo ro) throws BusinessException{
        BaseSessionIdRo search = new BaseSessionIdRo();
        search.setUserId(ro.getUuid());

        return this.getInfo(search, 1, ro.getUuid());
    }

}