package com.yqg.order.controllor;

import com.yqg.common.core.BaseControllor;
import com.yqg.order.service.creditorinfo.CreditorinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 债权表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 11:02:36
 */
@RestController
@RequestMapping("creditorinfo" )
public class CreditorinfoController extends BaseControllor {
    @Autowired
    CreditorinfoService creditorInfoService;


}