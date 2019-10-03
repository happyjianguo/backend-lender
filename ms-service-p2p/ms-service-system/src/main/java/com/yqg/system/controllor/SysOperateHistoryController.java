package com.yqg.system.controllor;


import com.yqg.api.system.sysoperatehistory.SysOperateHistoryServiceApi;
import com.yqg.api.system.sysoperatehistory.ro.SysOperateHistoryAddRo;
import com.yqg.common.core.BaseControllor;
import com.yqg.common.core.annocation.NotNeedLogin;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.system.service.sysoperatehistory.SysOperateHistoryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统用户表
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-09-20 15:52:10
 */
@RestController
public class SysOperateHistoryController extends BaseControllor {
    @Autowired
    SysOperateHistoryService sysOperateHistoryService;

    //@NotNeedLogin
    @ApiOperation(value = "添加用户操作记录", notes = "添加用户操作记录")
    @PostMapping(value = SysOperateHistoryServiceApi.path_addSysOperateHistory)
    public BaseResponse sysUserLogin(@RequestBody SysOperateHistoryAddRo ro) throws BusinessException {
        this.sysOperateHistoryService.addOperateHistory(ro);
        return new BaseResponse().successResponse();
    }
}
