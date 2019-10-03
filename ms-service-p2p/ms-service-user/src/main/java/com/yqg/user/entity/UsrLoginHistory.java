package com.yqg.user.entity;

import com.yqg.common.dao.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 用户登录历史表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:20:30
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "userLoginHistory" )
public class UsrLoginHistory extends BaseEntity implements Serializable{
    private static final long serialVersionUID=1L;

    //用户uuid
    private String userUuid;
    public static final String userUuid_field="userUuid";
    //设备号
    private String deviceNomber;
    public static final String deviceNomber_field="deviceNomber";
    //设备类型
    private String deviceType;
    public static final String deviceType_field="deviceType";
    //mac地址
    private String macAddress;
    public static final String macAddress_field="macAddress";
    //ip地址
    private String ipAddress;
    public static final String ipAddress_field="ipAddress";
    //网络类型(0=4G,1=WiFi)
    private String networkType;
    public static final String networkType_field="networkType";
    //手机系统版本号
    private String mobileSysVersionNo;
    public static final String mobileSysVersionNo_field="mobileSysVersionNo";
    //市场渠道号
    private String marketChannelNo;
    public static final String marketChannelNo_field="marketChannelNo";
    //应用版本号
    private String applicationVersionNo;
    public static final String applicationVersionNo_field="applicationVersionNo";
    //经度
    private String lbsX;
    public static final String lbsX_field="lbsX";
    //纬度
    private String lbsY;
    public static final String lbsY_field="lbsY";
}
