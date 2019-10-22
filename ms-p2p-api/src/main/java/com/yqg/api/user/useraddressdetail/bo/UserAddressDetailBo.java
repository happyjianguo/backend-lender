package com.yqg.api.user.useraddressdetail.bo;

import lombok.Data;
import java.util.List;
/**
 * 用户地址信息表 业务对象
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:20:30
 */
@Data
public class UserAddressDetailBo {
    private String userUuid;
    private Integer addressType;
    private String province;
    private String city;
    private String bigDirect;
    private String smallDirect;
    private String detailed;
    private Integer status;
    private Integer sort;
}

