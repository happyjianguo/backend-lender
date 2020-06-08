package com.yqg.api.order.scatterstandard.ro;

import com.yqg.common.core.request.BasePageRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by liyixing on 2018/9/5.
 */
@Data
public class ScatterstandardPageRo extends BasePageRo {

//    @ApiModelProperty(value = "理财年化利率", required = true)
//    private BigDecimal yearRateFin;
//    //债权总金额
//    @ApiModelProperty(value = "债权总金额", required = true)
//    private BigDecimal amountApply;
    //期限
    @ApiModelProperty(value = "期限列表", required = true)
    private List<String> terms;
//    @ApiModelProperty(value = "状态", required = true)
//    private Integer status;
    @ApiModelProperty(value = "年龄上限", required = true)
    private Integer upperAge;
    @ApiModelProperty(value = "年龄下限", required = true)
    private Integer lowerAge;
    @ApiModelProperty(value = "性别", required = true)
    private Integer sex;
    @ApiModelProperty(value = "省")
    private String province;
    @ApiModelProperty(value = "市")
    private String city;
    @ApiModelProperty(value = "tujuan")
    private String borrowingPurpose;
//    @ApiModelProperty(value = "区")
//    private String area;
    @ApiModelProperty(value = "金额区间列表", required = true)
    private List<String> amountApplys;


}
