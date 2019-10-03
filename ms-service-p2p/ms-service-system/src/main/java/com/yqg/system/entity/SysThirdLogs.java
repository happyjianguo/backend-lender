package com.yqg.system.entity;

import com.yqg.common.dao.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 第三方日志信息
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "sysThirdLogs" )
public class SysThirdLogs extends BaseEntity implements Serializable{
    private static final long serialVersionUID=1L;

//用户id
    private String userUuid;
    public static final String userUuid_field="userUuid";
//订单号
    private String orderNo;
    public static final String orderNo_field="orderNo";
//第三方类型(1、短信服务，2、advance，3、依图，4、支付服务,5、卡bin)
    private Integer thirdType;
    public static final String thirdType_field="thirdType";
//根据不同的第三方接口类型自定义
    private String logType;
    public static final String logType_field="logType";
//请求参数
    private String request;
    public static final String request_field="request";
//响应参数
    private String response;
    public static final String response_field="response";
//耗时（毫秒）
    private Integer timeUsed;
    public static final String timeUsed_field="timeUsed";
    //交易流水号
    private String transNo;
    public static final String transNo_field="transNo";
}
