package com.yqg.api.order.productconf.ro;

import com.yqg.common.core.request.BasePageRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 投资产品表 请求对象
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-11-14 14:45:36
 */
@Data
public class ProductconfRo {
    //借款期限(月)
    @ApiModelProperty(value = "借款期限(月)", required = true)
    private Integer borrowingTerm;
    //产品名称
    @ApiModelProperty(value = "产品名称", required = true)
    private String productName;
    //年化收益率
    @ApiModelProperty(value = "年化收益率", required = true)
    private BigDecimal interestRate;
    //起投金额
    @ApiModelProperty(value = "起投金额", required = true)
    private BigDecimal lowAmount;
    //最达投资金额
    @ApiModelProperty(value = "最达投资金额", required = true)
    private BigDecimal maxAmount;
    private String id;
}

