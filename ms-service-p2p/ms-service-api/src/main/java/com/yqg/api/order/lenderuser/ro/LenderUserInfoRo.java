package com.yqg.api.order.lenderuser.ro;

import com.yqg.common.core.request.BaseSessionIdRo;
import lombok.Data;

/**
 * Created by liyixing on 2018/9/10.
 */
@Data
public class LenderUserInfoRo extends BaseSessionIdRo {

    private String creditorNo;

}
