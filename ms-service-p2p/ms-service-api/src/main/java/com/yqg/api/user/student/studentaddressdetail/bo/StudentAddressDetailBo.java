package com.yqg.api.user.student.studentaddressdetail.bo;

import lombok.Data;
import java.util.List;
/**
 * 学生借款地址信息表 业务对象
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-10-08 16:22:40
 */
@Data
public class StudentAddressDetailBo {
    private String userUuid;
    private Integer addressType;
    private String province;
    private String city;
    private String bigDirect;
    private String smallDirect;
    private String detailed;
    private Integer status;
}

