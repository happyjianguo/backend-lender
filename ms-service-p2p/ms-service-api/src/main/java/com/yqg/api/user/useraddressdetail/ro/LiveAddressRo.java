package com.yqg.api.user.useraddressdetail.ro;


import lombok.Data;

@Data
public class LiveAddressRo {

    private String liveProvince;

    private String liveCity;

    private String liveBigDirect;

    private String liveSmallDirect;

    private String liveDetailed;
}
