package com.yqg.api.user.student.studentaddressdetail.ro;

import com.yqg.common.core.request.BasePageRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

/**
 * 学生借款地址信息表 请求对象
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-10-08 16:22:40
 */
@Data
public class StudentAddressDetailRo {
//userUuid
@ApiModelProperty(value = "userUuid", required = true)
private String userUuid;
//地址类型：0.学生身份证地址,1.学生现居住地址,2.学生学校地址,3.担保人身份证地址,4.担保人居住地址,5.担保人公司地址
@ApiModelProperty(value = "地址类型：0.学生身份证地址,1.学生现居住地址,2.学生学校地址,3.担保人身份证地址,4.担保人居住地址,5.担保人公司地址", required = true)
private Integer addressType;
//省
@ApiModelProperty(value = "省", required = true)
private String province;
//市
@ApiModelProperty(value = "市", required = true)
private String city;
//大区
@ApiModelProperty(value = "大区", required = true)
private String bigDirect;
//小区
@ApiModelProperty(value = "小区", required = true)
private String smallDirect;
//详细地址
@ApiModelProperty(value = "详细地址", required = true)
private String detailed;
//是否有效：0有效；1无效
@ApiModelProperty(value = "是否有效：0有效；1无效", required = true)
private Integer status;
}

