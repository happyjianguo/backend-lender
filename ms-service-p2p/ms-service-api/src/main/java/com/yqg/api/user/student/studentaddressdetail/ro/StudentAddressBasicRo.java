package com.yqg.api.user.student.studentaddressdetail.ro;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 学生借款地址信息表 请求对象
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-10-08 16:22:40
 */
@Data
public class StudentAddressBasicRo {
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
}
