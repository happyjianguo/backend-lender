package com.yqg.api.system.sysoperatehistory.ro;


import com.yqg.common.core.request.BaseSessionIdRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 系统用户表 请求对象
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-09-20 15:52:10
 */
@Data
public class SysOperateHistoryAddRo extends BaseSessionIdRo {
    @ApiModelProperty(value = "修改字段记录描述", required = true)
    private String operateString;

    @ApiModelProperty(value = "操作人名", required = true)
    private String userName;

    @ApiModelProperty(value = "操作类型", required = true)
    private Integer type;

    @ApiModelProperty(value = "用户操作时的ip", required = true)
    private String ipAddress;

    @ApiModelProperty(value = "创建用户", required = true)
    private String createUser;
}
