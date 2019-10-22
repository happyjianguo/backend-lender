package com.yqg.user.service;

import com.yqg.user.service.useruser.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 公共服务注入类
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-11 10:43:36
 */
@Component
public abstract class UserAccounthistoryCommonServiceImpl {

    /**
     * 日志组件
     */
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected UserService userService;


}
