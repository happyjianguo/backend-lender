package com.yqg.system.service.sysdicitem;

import com.yqg.common.core.BaseService;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.system.entity.SysDicItem;

import java.util.List;

/**
 * 字典项表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
public interface SysDicItemService extends BaseService<SysDicItem> {

    List<SysDicItem> sysDicItemListByParentId(String parentId) throws BusinessException;
}