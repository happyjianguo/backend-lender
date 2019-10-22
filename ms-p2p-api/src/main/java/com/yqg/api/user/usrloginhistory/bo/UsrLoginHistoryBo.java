package com.yqg.api.user.usrloginhistory.bo;

import lombok.Data;
/**
 * 用户登录历史表 业务对象
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:20:30
 */
@Data
public class UsrLoginHistoryBo {
    private String userUuid;
    private String deviceNomber;
    private String deviceType;
    private String macAddress;
    private String ipAddress;
    private String networkType;
    private String mobileSysVersionNo;
    private String marketChannelNo;
    private String applicationVersionNo;
    private String lbsX;
    private String lbsY;
    private Integer sort;
}

