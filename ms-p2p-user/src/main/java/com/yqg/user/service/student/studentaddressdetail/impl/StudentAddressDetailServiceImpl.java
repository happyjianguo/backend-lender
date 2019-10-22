package com.yqg.user.service.student.studentaddressdetail.impl;

import com.yqg.api.user.student.studentaddressdetail.ro.StudentAddressBasicRo;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.user.entity.student.StudentAddressDetail;
import com.yqg.user.dao.student.StudentAddressDetailDao;
import com.yqg.user.service.UserCommonServiceImpl;
import com.yqg.user.service.student.studentaddressdetail.StudentAddressDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * 学生借款地址信息表
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-10-08 16:22:40
 */
@Service("studentAddressDetailService")
public class StudentAddressDetailServiceImpl extends UserCommonServiceImpl implements StudentAddressDetailService {
    @Autowired
    protected StudentAddressDetailDao studentaddressdetailDao;


    @Override
    public StudentAddressDetail findById(String id) throws BusinessException {
        return this.studentaddressdetailDao.findOneById(id, new StudentAddressDetail());
    }

    @Override
    public <E> E findById(String id, Class<E> boClass) throws BusinessException {
        StudentAddressDetail studentaddressdetail = findById(id);
        E bo = BeanCoypUtil.copyToNewObject(studentaddressdetail, boClass);
        return bo;
    }

    @Override
    public StudentAddressDetail findOne(StudentAddressDetail entity) throws BusinessException {
        return this.studentaddressdetailDao.findOne(entity);
    }

    @Override
    public <E> E findOne(StudentAddressDetail entity, Class<E> boClass) throws BusinessException {
        StudentAddressDetail studentaddressdetail = findOne(entity);
        E bo = BeanCoypUtil.copyToNewObject(studentaddressdetail, boClass);
        return bo;
    }


    @Override
    public List<StudentAddressDetail> findList(StudentAddressDetail entity) throws BusinessException {
        return this.studentaddressdetailDao.findForList(entity);
    }

    @Override
    public <E> List<E> findList(StudentAddressDetail entity, Class<E> boClass) throws BusinessException {
        List<StudentAddressDetail> studentaddressdetailList = findList(entity);
        List<E> boList = new ArrayList<>();
        for (StudentAddressDetail studentaddressdetail : studentaddressdetailList) {
            E bo = BeanCoypUtil.copyToNewObject(studentaddressdetail, boClass);
            boList.add(bo);
        }
        return boList;
    }

    @Override
    @Transactional
    public String addOne(StudentAddressDetail entity) throws BusinessException {
        return studentaddressdetailDao.addOne(entity);
    }

    @Override
    @Transactional
    public void deleteById(String id) throws BusinessException {
        studentaddressdetailDao.deleteOne(findById(id));
    }

    @Override
    @Transactional
    public void deleteOne(StudentAddressDetail entity) throws BusinessException {
        this.studentaddressdetailDao.deleteOne(entity);
    }

    @Override
    @Transactional
    public void updateOne(StudentAddressDetail entity) throws BusinessException {
        this.studentaddressdetailDao.updateOne(entity);
    }


    @Override
    @Transactional
    public void addAddressInfo(String province, String city,String bigDirect,String smallDirect,String detailed,
                               String userUuid,Integer addressType,String orderNo) throws
            BusinessException{
        StudentAddressDetail search = new StudentAddressDetail();   //查询是否存在
        search.setOrderNo(orderNo);
        search.setAddressType(addressType);

        StudentAddressDetail result = this.findOne(search);
        if(result != null){         //存在数据就先删除
            //StudentAddressDetail needDisabled = new StudentAddressDetail();
            //needDisabled.setId(result.getId());
            result.setDisabled(1);
            this.updateOne(result);
        }

        this.addAddressOne(province, city, bigDirect, smallDirect, detailed, userUuid, addressType, orderNo);
    }


    public void addAddressOne(String province, String city,String bigDirect,String smallDirect,String detailed,
                              String userUuid,Integer addressType,String orderNo) throws BusinessException{
        StudentAddressDetail addInfo = new StudentAddressDetail();
        addInfo.setAddressType(addressType);
        addInfo.setProvince(province);
        addInfo.setCity(city);
        addInfo.setBigDirect(bigDirect);
        addInfo.setSmallDirect(smallDirect);
        addInfo.setDetailed(detailed);
        addInfo.setUserUuid(userUuid);
        addInfo.setOrderNo(orderNo);
        this.addOne(addInfo);
    }

    public StudentAddressDetail addressDetailByUserUuidType(String orderNo, Integer addressType) throws BusinessException {
        StudentAddressDetail search = new StudentAddressDetail();
        search.setOrderNo(orderNo);
        search.setAddressType(addressType);
        return this.findOne(search);
    }

    @Override
    public StudentAddressBasicRo getStudentAddressDetail(String orderNo, Integer addressType) throws BusinessException{
        StudentAddressDetail result = this.addressDetailByUserUuidType(orderNo, addressType);
        StudentAddressBasicRo response = new StudentAddressBasicRo();
        if(result != null){
            response.setProvince(result.getProvince());
            response.setCity(result.getCity());
            response.setBigDirect(result.getBigDirect());
            response.setSmallDirect(result.getSmallDirect());
            response.setDetailed(result.getDetailed());
        }

        return response;
    }

}