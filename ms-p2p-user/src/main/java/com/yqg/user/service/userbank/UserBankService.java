package com.yqg.user.service.userbank;

import com.yqg.api.user.userbank.ro.UserBankRo;
import com.yqg.api.user.userbank.ro.UserBindBankCardRo;
import com.yqg.common.core.BaseService;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.user.entity.UserBank;

/**
 * 用户银行卡信息
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:20:30
 */
public interface UserBankService extends BaseService<UserBank> {

    /**
     * 用户绑卡*/
    void bindBankCard(UserBindBankCardRo ro) throws Exception;

    /**
     * 根据userId获取用户银行卡信息
     * @param userBankRo
     * @return
     * @throws BusinessException
     */
    UserBank getUserBankInfo(UserBankRo userBankRo) throws BusinessException;

    /**
     * 用户更换银行卡（支持同行内更换）
     * @param ro
     * @throws BusinessException
     */
    void updateBankCard(UserBindBankCardRo ro) throws BusinessException;
}