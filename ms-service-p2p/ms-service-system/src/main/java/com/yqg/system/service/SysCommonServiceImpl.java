package com.yqg.system.service;

import com.yqg.common.redis.RedisUtil;
import com.yqg.system.service.sysapph5.SysAppH5Service;
import com.yqg.system.service.sysbankbasicinfo.SysBankBasicInfoService;
import com.yqg.system.service.sysdic.SysDicService;
import com.yqg.system.service.sysdicitem.SysDicItemService;
import com.yqg.system.service.sysdist.SysDistService;
import com.yqg.system.service.sysparam.SysParamService;
import com.yqg.system.service.syspaymentchannel.SysPaymentChannelService;
import com.yqg.system.service.syspermission.SysPermissionService;
import com.yqg.system.service.sysrole.SysRoleService;
import com.yqg.system.service.sysrolepermission.SysRolePermissionService;
import com.yqg.system.service.systhirdlogs.SysThirdLogsService;
import com.yqg.system.service.sysuser.SysUserService;
import com.yqg.system.service.sysuserrole.SysUserRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 公共服务注入类
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@Component
public abstract class SysCommonServiceImpl {

    /**
     * 日志组件
     */
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected SysAppH5Service sysAppH5Service;
    @Autowired
    protected SysBankBasicInfoService sysBankBasicInfoService;
    @Autowired
    protected SysDicService sysDicService;
    @Autowired
    protected SysDicItemService sysDicItemService;
    @Autowired
    protected SysDistService sysDistService;
    @Autowired
    protected SysParamService sysParamService;
    @Autowired
    protected SysPaymentChannelService sysPaymentChannelService;
    @Autowired
    protected SysThirdLogsService sysThirdLogsService;
    @Autowired
    protected SysUserService sysUserService;
    @Autowired
    protected RedisUtil redisUtil;
    @Autowired
    protected SysRolePermissionService sysRolePermissionService;
    @Autowired
    protected SysPermissionService sysPermissionService;
    @Autowired
    protected SysUserRoleService sysUserRoleService;
    @Autowired
    protected SysRoleService sysRoleService;


}
