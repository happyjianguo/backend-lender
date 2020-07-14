package com.yqg.api.pay.income.ro;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yqg.common.core.request.BaseSessionIdRo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


@ApiModel
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class DigisignRo extends BaseSessionIdRo {
    @JsonProperty
    @ApiModelProperty(value = "orderNo", required = true)
    private String orderNo;


}