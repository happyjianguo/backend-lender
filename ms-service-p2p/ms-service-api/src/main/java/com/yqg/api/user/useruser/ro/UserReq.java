package com.yqg.api.user.useruser.ro;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Administrator on 2018/9/5.
 */
@Data
public class UserReq {
    @ApiModelProperty(value = "手机号", required = true)
    private String mobileNumber;
    @ApiModelProperty(value = "身份证号", required = true)
    private String idCardNo;
//    private String userId;
    private String userUuid;

    @ApiModelProperty(value = "姓名", required = true)
    private String realName;
}
