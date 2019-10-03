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
    //散标账户在投金额
    private BigDecimal sanbiaoBalance;
    public static final String sanbiaoBalance_field = "sanbiaoBalance";
    //散标账户冻结金额
    private BigDecimal sanbiaoLockBalance;
    public static final String sanbiaoLockBalance_field = "sanbiaoLockBalance";
    //活期账户余额
    private BigDecimal currentBalance;
    public static final String currentBalance_field = "currentBalance";
    //活期账户冻结金额
    private BigDecimal currentLockBalance;
    public static final String currentLockBalance_field = "currentLockBalance";
    //定期账户余额
    private BigDecimal depositBalance;
    public static final String depositBalance_field = "depositBalance";
    //定期账户冻结金额
    private BigDecimal depositLockBalance;
    public static final String depositLockBalance_field = "depositLockBalance";
}
