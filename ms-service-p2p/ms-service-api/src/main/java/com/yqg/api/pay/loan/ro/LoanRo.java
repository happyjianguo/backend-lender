package com.yqg.api.pay.loan.ro;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 打款Ro
 */

@ApiModel
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class LoanRo {

    @JsonProperty
    @ApiModelProperty(value = "订单编号", required = true)
    private String orderNo;
    @JsonProperty
    @ApiModelProperty(value = "债权编号", required = true)
    private String creditorNo;
    @JsonProperty
    @ApiModelProperty(value = "打款金额", required = true)
    private BigDecimal amount;
    @JsonProperty
    @ApiModelProperty(value = "银行卡号", required = true)
    private String bankNumberNo;
    @JsonProperty
    @ApiModelProperty(value = "持卡人", required = true)
    private String cardholder;
    @JsonProperty
    @ApiModelProperty(value = "银行编码", required = true)
    private String bankCode;
    @JsonProperty
    @ApiModelProperty(value = "交易类型", required = true)
    private Integer transType;
    @JsonProperty
    @ApiModelProperty(value = "资金出方id", required = true)
    private String fromUserId;
    @JsonProperty
    @ApiModelProperty(value = "资金入方id", required = true)
    private String toUserId;
    @JsonProperty
    @ApiModelProperty(value = "打款备注", required = true)
    private String description;

}