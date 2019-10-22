package com.yqg.system.controllor;

import com.yqg.common.core.BaseControllor;
import com.yqg.system.service.sysapph5.SysAppH5Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * app H5 url集合表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@RestController
@RequestMapping("sysapph5" )
public class SysAppH5Controller extends BaseControllor {
    @Autowired
    SysAppH5Service sysAppH5Service;


}