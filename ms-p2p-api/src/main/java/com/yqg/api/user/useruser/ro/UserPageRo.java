package com.yqg.api.user.useruser.ro;

import com.yqg.common.core.request.BasePageRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: hyy
 * @Date: 2019/5/17 16:09
 * @Version 1.0
 * @EMAIL: hanyangyang@yishufu.com
 */
@Data
public class UserPageRo  extends BasePageRo {
    //手机号
    @ApiModelProperty(value = "手机号", required = true)
    private String mobileNumber;
    //真实姓名
    @ApiModelProperty(value = "真实姓名", required = true)
    private String realName;

}