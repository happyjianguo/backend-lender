package com.yqg.api.system.sms.bo;

import com.yqg.common.core.BaseBo;
import java.util.Date;
import lombok.Data;

/**
 * Created by Lixiangjun on 2019/6/10.
 */
@Data
public class SysSmsMessageInfoBo extends BaseBo{
    private Integer smsType;
    private String smsConent;
    private String smsCode;
    private String mobile;
    private String batchId;
    private Date createTime;
}
