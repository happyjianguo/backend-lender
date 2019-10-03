package com.yqg.api.user.useraddressdetail.ro;

import lombok.Data;

@Data
public class IdentityCheckResultRo {
    private String name;
    private String idNumber;
    private String province;
    private String city;
    private String district;
    private String village;
}
