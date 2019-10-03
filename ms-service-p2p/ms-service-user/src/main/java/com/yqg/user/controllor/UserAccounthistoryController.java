package com.yqg.user.controllor;

import com.yqg.common.core.BaseControllor;
import com.yqg.user.service.useraccounthistory.UserAccounthistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户账户明细表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-11 10:43:36
 */
@RestController
public class UserAccounthistoryController extends BaseControllor {
    @Autowired
    UserAccounthistoryService useraccounthistoryService;


}