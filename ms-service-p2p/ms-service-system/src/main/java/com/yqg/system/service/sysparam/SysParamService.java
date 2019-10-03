package com.yqg.system.service.sysparam;

import com.yqg.api.system.sysparam.bo.SysParamBo;
import com.yqg.common.core.BaseService;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.system.entity.SysParam;

/**
 * 系统参数
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
public interface SysParamService extends BaseService<SysParam> {

    SysParamBo sysValueByKey(String sysValue) throws BusinessException;
}