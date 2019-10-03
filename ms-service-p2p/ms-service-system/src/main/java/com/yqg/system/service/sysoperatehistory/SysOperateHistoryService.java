package com.yqg.system.service.sysoperatehistory;

import com.yqg.api.system.sysoperatehistory.ro.SysOperateHistoryAddRo;
import com.yqg.common.core.BaseService;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.system.entity.SysOperateHistory;

public interface SysOperateHistoryService extends BaseService<SysOperateHistory> {

    /**
     * 添加用户操作记录*/
    void addOperateHistory(SysOperateHistoryAddRo ro) throws BusinessException;
}
