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
public class InvestmentFinancingRo {
    @JsonProperty
    @ApiModelProperty(value = "amount", required = true)
    private BigDecimal amount;
    @JsonProperty
    @ApiModelProperty(value = "userUuid", required = true)
    private String userUuid;
    @JsonProperty
    @ApiModelProperty(value = "userName", required = true)
    private String userName;
    @JsonProperty
    @ApiModelProperty(value = "productId", required = true)
    private String productId;
    //默认0 超级投资人合买1
    @JsonProperty
    @ApiModelProperty(value = "type", required = true)
    private Integer type=0;
    @JsonProperty
    @ApiModelProperty(value = "status", required = true)
    private Integer status;//通知到账状态

//    // 还款
//    //逾期滞纳金
//    @JsonProperty
//    @ApiModelProperty(value = "overdueRate", required = true)
//    private BigDecimal overdueRate;
//    //逾期服务费
//    @JsonProperty
//    @ApiModelProperty(value = "overdueFee", required = true)
//    private BigDecimal overdueFee;
//    //利息
//    @JsonProperty
//    @ApiModelProperty(value = "interest", required = true)
//    private BigDecimal interest;
}