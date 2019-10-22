package com.yqg.api.user.userbank.bo;

import lombok.Data;
import java.util.List;
/**
 * 用户银行卡信息 业务对象
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:20:30
 */
@Data
public class UserBankBo {
    private String userUuid;
    private String bankId;
    private String bankName;
    private String bankCode;
    private String bankNumberNo;
    private String bankCardName;
    private Integer status;
    private Integer bankorder;
    private Integer isRecent;
    private Integer type;
    private Integer sort;
}

