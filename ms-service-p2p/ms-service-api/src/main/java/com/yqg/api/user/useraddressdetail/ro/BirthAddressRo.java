package com.yqg.api.user.useraddressdetail.ro;

import lombok.Data;

@Data
public class BirthAddressRo {
    private String birthProvince;

    private String birthCity;

    private String birthBigDirect;

    private String birthSmallDirect;

    private String birthDetailed;
}
