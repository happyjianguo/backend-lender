package com.yqg.system.controllor;

import com.yqg.common.core.BaseControllor;
import com.yqg.system.service.sysdicitem.SysDicItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 字典项表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@RestController
@RequestMapping("sysdicitem" )
public class SysDicItemController extends BaseControllor {
    @Autowired
    SysDicItemService sysdicitemService;


}