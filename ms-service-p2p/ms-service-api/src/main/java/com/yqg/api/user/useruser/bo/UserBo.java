package com.yqg.api.user.useruser.bo;

import com.yqg.common.core.BaseBo;
import lombok.Data;
import java.util.List;
/**
 * 用户基本信息表 业务对象
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:20:30
 */
@Data
public class UserBo extends BaseBo {
    private String bankCardName;
    private String userName;
    private String mobileNumber;
    private String mobileNumberDES;
    private String realName;
    private String idCardNo;
    private String birthDate;
    private Integer age;
    private Integer sex;
    private String education;
    private String job;
    private String workField;
    private String religion;
    private String companyBusinessCode;
    private Integer companyType;
    private String registeredLegalName;
    private String organizerCode;
    private String npwpNo;
    private String yearSalary;
    private String salaryHomeValue;
    private String workTime;
    private String otherSalaryFrom;
    private Integer sort;
    private Integer userType;
    private Integer authStatus;
    private String bankCardNo;
    private String bankName;
    private String bankCode;
    private String isExist;
    private String id;
}

