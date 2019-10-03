package com.yqg.order.service.lenderuser;

import com.yqg.api.order.lenderuser.ro.LenderUserInfoRo;
import com.yqg.common.core.BaseService;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.order.entity.LenderUser;

/**
 * 借款人信息表
 *
 * @author wu
 * @email wu@yishufu.com
 * @date 2018-09-03 11:15:41
 */
public interface LenderUserService extends BaseService<LenderUser> {

    /**
     * 去doit查询借款人信息
     * */
    LenderUser selectLenderUserInfoFromDoit(LenderUserInfoRo ro)throws BusinessException;

}