package com.yqg.api.order.lenderuser.bo;

import lombok.Data;

/**
 * 借款人信息表 业务对象
 *
 * @author wu
 * @email wu@yishufu.com
 * @date 2018-09-03 11:15:41
 */
@Data
public class LenderUserBo {
    private String lenderId;
    private String name;
    private String idCardNo;
    private String sex;
    private Integer age;
    private String isMarried;
    private String identidy;
    private String mobile;
    private String email;
    private String acdemic;
    private String birty;
    private String religion;
    private String address;
    private String isIdentidyAuth;
    private String isBankCardAuth;
    private String isLindManAuth;
    private String isInsuranceCardAuth;
    private String isFamilyCardAuth;
}

