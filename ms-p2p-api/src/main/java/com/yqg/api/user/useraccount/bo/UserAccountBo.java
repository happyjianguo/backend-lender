package com.yqg.api.user.useraccount.bo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
/**
 * 用户账户表 业务对象
 *
 * @author wu
 * @email wsc@yishufu.com
 * @date 2018-08-31 14:16:46
 */
@Data
public class UserAccountBo {
    private String id;
    private String userUuid;
    private BigDecimal currentBalance;
    private BigDecimal lockedBalance;
    private BigDecimal investingBanlance;
    private Integer size;
}

