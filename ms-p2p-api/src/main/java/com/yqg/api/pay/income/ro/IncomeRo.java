package com.yqg.api.pay.income.ro;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yqg.common.enums.TransTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


@ApiModel
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class IncomeRo {
    @JsonProperty
    @ApiModelProperty(value = "orderNo", required = true)
    private String orderNo;
    @JsonProperty
    @ApiModelProperty(value = "externalId", required = true)
    private String externalId;
    @JsonProperty
    @ApiModelProperty(value = "transactionId", required = true)
    private String transactionId="";
    @JsonProperty
    @ApiModelProperty(value = "depositAmount", required = true)
    private BigDecimal depositAmount;
    @JsonProperty
    @ApiModelProperty(value = "depositChannel", required = true)
    private String depositChannel;
    @JsonProperty
    @ApiModelProperty(value = "depositMethod", required = true)
    private String depositMethod;
    @JsonProperty
    @ApiModelProperty(value = "depositType", required = true)
    private TransTypeEnum depositType;
    @JsonProperty
    @ApiModelProperty(value = "customerUserId", required = true)
    private String customerUserId;
    @JsonProperty
    @ApiModelProperty(value = "customerName", required = true)
    private String customerName;
    @JsonProperty
    @ApiModelProperty(value = "toUserId", required = true)
    private String toUserId;
    @JsonProperty
    @ApiModelProperty(value = "paymentCode", required = true)
    private String paymentCode;
    @JsonProperty
    @ApiModelProperty(value = "currency", required = true)
    private String currency;

    @JsonProperty
    @ApiModelProperty(value = "expireTime", required = true)
    private String expireTime;
}