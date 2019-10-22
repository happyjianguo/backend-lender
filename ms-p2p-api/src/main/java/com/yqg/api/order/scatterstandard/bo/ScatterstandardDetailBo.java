package com.yqg.api.order.scatterstandard.bo;

import com.yqg.api.order.orderorder.bo.RepaymentPlanBo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Remark:
 * Created by huwei on 19.5.13.
 */
@Data
public class ScatterstandardDetailBo {


    private String creditorNo;
    private Integer orderStatus;

    //标的相关
    private int creditorType;
    private List<RepaymentPlanBo> refundPlanList;
    private String borrowingPurposes;
    private BigDecimal amountApply;
    private BigDecimal amountBuy;
    private BigDecimal yearRateFin;
    private String refundIngTime;
    private String term;
    private Integer status;
    //借款人相关
    private String mobileNumber;
    private String realName;
    private String idCardNo;
    private String birthday;
    private String sex;
    private String address;
    private String score;
    private String loanCount;
    //投资标识
    private Integer isBuy;
}
