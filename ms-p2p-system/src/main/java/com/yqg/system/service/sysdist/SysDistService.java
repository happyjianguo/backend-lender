package com.yqg.system.service.sysdist;

import com.yqg.api.system.sysdist.bo.SysDistBo;
import com.yqg.api.system.sysdist.ro.SysDistRo;
import com.yqg.common.core.BaseService;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.system.entity.SysDist;

import java.util.List;

/**
 * 行政区划表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
public interface SysDistService extends BaseService<SysDist> {

    List<SysDistBo> getDistList(SysDistRo ro) throws BusinessException;
}