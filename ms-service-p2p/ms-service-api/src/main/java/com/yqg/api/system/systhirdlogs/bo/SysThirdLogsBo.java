package com.yqg.api.system.systhirdlogs.bo;

import lombok.Data;
import java.util.List;
/**
 * 第三方日志信息 业务对象
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@Data
public class SysThirdLogsBo {
    private String userUuid;
    private String orderNo;
    private Integer thirdType;
    private String logType;
    private String request;
    private String response;
    private Integer timeUsed;
    private String transNo;
}

