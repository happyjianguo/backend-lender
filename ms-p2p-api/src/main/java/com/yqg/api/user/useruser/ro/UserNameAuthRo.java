package com.yqg.api.user.useruser.ro;

import com.yqg.api.user.useraddressdetail.ro.BirthAddressRo;
import com.yqg.api.user.useraddressdetail.ro.LiveAddressRo;
import com.yqg.common.core.request.BaseSessionIdRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserNameAuthRo extends BaseSessionIdRo {
    @ApiModelProperty(value = "手机号", required = true)
    private String mobileNumber;

    @ApiModelProperty(value = "用户名", required = true)
    private String userName;
    @ApiModelProperty(value = "真实姓名", required = true)
    private String realName;
    @ApiModelProperty(value = "身份证号", required = true)
    private String idCardNo;


    @ApiModelProperty(value = "出生地址", required = true)
    private BirthAddressRo birthAddressRo;

    @ApiModelProperty(value = "出生日期", required = true)
    private String birthDate;
    @ApiModelProperty(value = "年龄", required = true)
    private Integer age;
    @ApiModelProperty(value = "性别", required = true)
    private Integer sex;
    @ApiModelProperty(value = "教育", required = true)
    private String education;
    @ApiModelProperty(value = "工作", required = true)
    private String job;
    @ApiModelProperty(value = "行业", required = true)
    private String workField;


    @ApiModelProperty(value = "住址", required = true)
    private LiveAddressRo liveAddressRo;


    @ApiModelProperty(value = "宗教", required = true)
    private String religion;
    /*@ApiModelProperty(value = "", required = true)
    private String companyBusinessCode;
    @ApiModelProperty(value = "用户名", required = true)
    private Integer companyType;
    @ApiModelProperty(value = "用户名", required = true)
    private String registeredLegalName;
    @ApiModelProperty(value = "用户名", required = true)
    private String organizerCode;*/
    @ApiModelProperty(value = "npwpNo", required = true)
    private String npwpNo;

    @ApiModelProperty(value = "年收入", required = true)
    private String yearSalary;
    @ApiModelProperty(value = "印尼税务居籍", required = true)
    private String salaryHomeValue;
    @ApiModelProperty(value = "工作年限", required = true)
    private String workTime;
    @ApiModelProperty(value = "其他资产来源", required = true)
    private String otherSalaryFrom;

    @ApiModelProperty(value = "身份证图片", required = true)
    private String idCardImage;
    @ApiModelProperty(value = "头像图片", required = true)
    private String headImage;

}
