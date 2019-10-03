package com.yqg.api.pay.income.ro;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


@ApiModel
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class IncomeRo {
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
    private String depositType;
    @JsonProperty
    @ApiModelProperty(value = "customerName", required = true)
    private String customerName;
    @JsonProperty
    @ApiModelProperty(value = "paymentCode", required = true)
    private String paymentCode;
    @JsonProperty
    @ApiModelProperty(value = "currency", required = true)
    private String currency;
}