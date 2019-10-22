package com.yqg.api.order.creditorinfo.bo;

import com.yqg.api.order.scatterstandard.bo.ScatterstandardBo;
import com.yqg.common.core.response.BasePageResponse;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Remark:
 * Created by huwei on 19.6.26.
 */
@Data
public class ScatterstandardListBo {
    private BasePageResponse<ScatterstandardBo> bo;
    private BigDecimal amount;
}
