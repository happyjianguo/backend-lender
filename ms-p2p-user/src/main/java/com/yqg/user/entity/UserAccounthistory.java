package com.yqg.user.entity;

import com.yqg.common.dao.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户账户明细表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-11 10:43:36
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "userAccountHistory" )
public class UserAccounthistory extends BaseEntity implements Serializable{
    private static final long serialVersionUID=1L;

    //用户id
    private String userUuid;
    public static final String userUuid_field="userUuid";

    //账户变动类型
    private String type;
    public static final String type_field="type";

    //业务场景类型
    private String businessType;
    public static final String businessType_field="businessType";

    //动账时间
    private String dealTime;
    public static final String dealTime_field="dealTime";

    //动账前金额
    private BigDecimal lastBanlance;
    public static final String lastBanlance_field="lastBanlance";

    //交易金额
    private BigDecimal amount;
    public static final String amount_field="amount";

    //当前可用账户余额
    private BigDecimal currentBanlance;
    public static final String currentBanlance_field="currentBanlance";

    //锁定账户余额
    private BigDecimal lockedBanlance;
    public static final String lockedBanlance_field="lockedBanlance";

    //在投账户余额
    private BigDecimal investBanlance;
    public static final String investBanlance_field="investBanlance";

    //交易账单号
    private String tradeNo;
    public static final String tradeNo_field="tradeNo";

    //交易信息
    private String tradeInfo;
    public static final String tradeInfo_field="tradeInfo";

    //支付渠道
    private String payType;
    public static final String payType_field="payType";
}
