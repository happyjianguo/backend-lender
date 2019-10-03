package com.yqg.api.user.useruser.ro;

import com.yqg.common.core.request.BasePageRo;
import com.yqg.common.core.request.BaseSessionIdRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

/**
 * 用户基本信息表 请求对象
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:20:30
 */
@Data
public class UserRo extends BaseSessionIdRo {
//用户名
@ApiModelProperty(value = "用户名", required = true)
private String userName;
//手机号
@ApiModelProperty(value = "手机号", required = true)
private String mobileNumber;
//真实姓名
@ApiModelProperty(value = "真实姓名", required = true)
private String realName;
//身份证号
@ApiModelProperty(value = "身份证号", required = true)
private String idCardNo;
//出生日期
@ApiModelProperty(value = "出生日期", required = true)
private String birthDate;
//年龄
@ApiModelProperty(value = "年龄", required = true)
private Integer age;
//性别，0：未知，1：男，2：女
@ApiModelProperty(value = "性别，0：未知，1：男，2：女", required = true)
private Integer sex;
//学历
@ApiModelProperty(value = "学历", required = true)
private String education;
//工作信息
@ApiModelProperty(value = "工作信息", required = true)
private String job;
//行业
@ApiModelProperty(value = "行业", required = true)
private String workField;
//宗教
@ApiModelProperty(value = "宗教", required = true)
private String religion;
//公司营业执照号
@ApiModelProperty(value = "公司营业执照号", required = true)
private String companyBusinessCode;
//公司类型
@ApiModelProperty(value = "公司类型", required = true)
private Integer companyType;
//注册法人名称
@ApiModelProperty(value = "注册法人名称", required = true)
private String registeredLegalName;
//组织编号
@ApiModelProperty(value = "组织编号", required = true)
private String organizerCode;
//税号
@ApiModelProperty(value = "税号", required = true)
private String npwpNo;
//年收入
@ApiModelProperty(value = "年收入", required = true)
private String yearSalary;
//印尼税务居籍
@ApiModelProperty(value = "印尼税务居籍", required = true)
private Integer salaryHomeValue;
//工作年限
@ApiModelProperty(value = "工作年限", required = true)
private String workTime;
//其他资产来源
@ApiModelProperty(value = "其他资产来源", required = true)
private String otherSalaryFrom;
//排序自增字段
@ApiModelProperty(value = "排序自增字段", required = true)
private Integer sort;
}

