package com.yqg.system.service.sysdic;

import com.yqg.api.system.sysdicitem.bo.SysDicItemBo;
import com.yqg.common.core.BaseService;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.system.entity.SysDic;
import com.yqg.system.entity.SysDicItem;

import java.util.List;

/**
 * 字典表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
public interface SysDicService extends BaseService<SysDic> {

    /**
     * 通过字典dicCode查询用户*/
    List<SysDicItem> sysDicItemsListByDicCode(String dicCode) throws BusinessException;

    /**
     * 系统调用查询字典配置项*/
    List<SysDicItemBo> dicItemBoListByDicCode(String dicCode) throws BusinessException;
}