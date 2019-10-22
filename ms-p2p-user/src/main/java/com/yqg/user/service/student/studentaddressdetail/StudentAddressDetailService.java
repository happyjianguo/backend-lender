package com.yqg.user.service.student.studentaddressdetail;

import com.yqg.api.user.student.studentaddressdetail.ro.StudentAddressBasicRo;
import com.yqg.common.core.BaseService;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.user.entity.student.StudentAddressDetail;

/**
 * 学生借款地址信息表
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-10-08 16:22:40
 */
public interface StudentAddressDetailService extends BaseService<StudentAddressDetail> {

    /**
     * 添加学生地址数据
     * @param province 省,　
     * @param city 市,
     * @param bigDirect 大区,
     * @param  smallDirect 小区,
     * @param detailed 详细地址,
     * @param userUuid 用户uuid,
     * @param addressType 地址类型
     * */
    void addAddressInfo(String province, String city,String bigDirect,String smallDirect,String detailed,
                               String userUuid,Integer addressType,String orderNo) throws
            BusinessException;

    /**
     * 通过用户uuid和type查询地址
     * @param userUuid 用户uuid,
     * @param addressType 地址类型,
     * */
    //StudentAddressDetail addressDetailByUserUuidType(String userUuid, Integer addressType) throws BusinessException;

    /**
     *
     * 通过用户uuid和type查询地址
     * @param orderNo 订单uuid,
     * @param addressType 地址类型,
     * */
    StudentAddressBasicRo getStudentAddressDetail(String orderNo, Integer addressType) throws BusinessException;
}