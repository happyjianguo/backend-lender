package com.yqg.system.controllor;

import com.yqg.common.core.BaseControllor;
import com.yqg.system.service.systhirdlogs.SysThirdLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 第三方日志信息
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@RestController
@RequestMapping("systhirdlogs" )
public class SysThirdLogsController extends BaseControllor {
    @Autowired
    SysThirdLogsService systhirdlogsService;


}