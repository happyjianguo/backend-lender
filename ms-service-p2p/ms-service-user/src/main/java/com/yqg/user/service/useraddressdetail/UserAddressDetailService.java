package com.yqg.user.service.useraddressdetail;

import com.yqg.common.core.BaseService;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.user.entity.UserAddressDetail;

/**
 * 用户地址信息表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:20:30
 */
public interface UserAddressDetailService extends BaseService<UserAddressDetail> {

    /**
     * 添加用户地址信息
     * */
    void addInfo(UserAddressDetail addInfo) throws BusinessException;
}