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
 * @Author: hyy
 * @Date: 2019/5/28 9:08
 * @Version 1.0
 * @EMAIL: hanyangyang@yishufu.com
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "userWithdraw" )
public class UserWithdraw  extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 5980383337754046560L;
    //用户id
    private String userUuid;
    public static final String userUuid_field="userUuid";

    //快照可用账户余额
    private BigDecimal firstBalance;
    public static final String firstBalance_field="firstBalance";

    //当天成功投资金额
    private BigDecimal successInvestBanlance;
    public static final String successInvestBanlance_field="successInvestBanlance";

    //当天主动提现累计金额
    private BigDecimal withdrawBalance;
    public static final String withdrawBalance_field="withdrawBalance";

    //当天提现总金额 主动+自动
    private BigDecimal allAmount;
    public static final String allAmount_field="allAmount";

    //提现状态 0未提交 1已提交
    private Integer status;
    public static final String status_field="status";

    //交易号
    private String tradeNo;
    public static final String tradeNo_field="tradeNo";
}