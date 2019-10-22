package com.yqg.user.entity;

import com.yqg.common.dao.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 用户地址信息表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:20:30
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "userAddressDetail" )
public class UserAddressDetail extends BaseEntity implements Serializable{
    private static final long serialVersionUID=1L;

    //userUuid
    private String userUuid;
    public static final String userUuid_field="userUuid";
    //地址类型：0下单地址；1居住地址；2公司地址；3学校地址
    private Integer addressType;
    public static final String addressType_field="addressType";
    //省
    private String province;
    public static final String province_field="province";
    //市
    private String city;
    public static final String city_field="city";
    //大区
    private String bigDirect;
    public static final String bigDirect_field="bigDirect";
    //小区
    private String smallDirect;
    public static final String smallDirect_field="smallDirect";
    //详细地址
    private String detailed;
    public static final String detailed_field="detailed";
    //是否有效：0有效；1无效
    private Integer status;
    public static final String status_field="status";
}
