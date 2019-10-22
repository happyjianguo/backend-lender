package com.yqg.system.controllor;

import com.yqg.common.core.BaseControllor;
import com.yqg.system.service.syspaymentchannel.SysPaymentChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@RestController
@RequestMapping("syspaymentchannel" )
public class SysPaymentChannelController extends BaseControllor {
    @Autowired
    SysPaymentChannelService syspaymentchannelService;


}