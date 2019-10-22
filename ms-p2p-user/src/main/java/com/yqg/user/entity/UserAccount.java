package com.yqg.user.entity;

import com.yqg.common.dao.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户账户表
 *
 * @author wu
 * @email wsc@yishufu.com
 * @date 2018-08-31 14:16:46
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "userAccount")
public class UserAccount extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //用户id
    private String userUuid;
    public static final String userUuid_field = "userUuid";
    //当前可用账户余额
    private BigDecimal currentBalance;
    public static final String currentBalance_field = "currentBalance";
    //锁定账户余额
    private BigDecimal lockedBalance;
    public static final String lockedBalance_field = "lockedBalance";
    //在投金额
    private BigDecimal investingBanlance;
    public static final String investingBanlance_field = "investingBanlance";
    //用户类型
    private Integer type;
    public static final String type_field = "type";
}
