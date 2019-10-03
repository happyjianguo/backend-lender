package com.yqg.api.system.syspaymentchannel.bo;

import lombok.Data;
import java.util.List;
/**
 *  业务对象
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@Data
public class SysPaymentChannelBo {
    private String paymentChannelCode;
    private String paymentChannelName;
    private Integer type;
    private Integer status;
}

