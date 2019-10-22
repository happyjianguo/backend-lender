package com.yqg.user.service.useraccounthistory;

import com.yqg.api.user.useraccounthistory.bo.UserAccountHistoryTotalBo;
import com.yqg.api.user.useraccounthistory.ro.UserAccountChangeRo;
import com.yqg.api.user.useraccounthistory.ro.UserAccountHistoryTotalRo;
import com.yqg.api.user.useraccounthistory.ro.UserAccounthistoryRo;
import com.yqg.common.core.BaseService;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.user.entity.UserAccounthistory;

import java.util.List;

/**
 * 用户账户明细表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-11 10:43:36
 */
public interface UserAccounthistoryService extends BaseService<UserAccounthistory> {

    void insterUserAccountHistory(UserAccountChangeRo userAccounthistoryRo) throws BusinessException;

    /**
     * 查询指定日期各超级投资人的投资总金额
     * @param ro
     * @return
     */
    List<UserAccountHistoryTotalBo> getUserAccountHistoryTotal(UserAccountHistoryTotalRo ro);

    /**
     * 查询在此之前24小时内投资人的成功投资总金额
     * @param ro
     * @return
     */
    Object[] getSuccessInvest(UserAccountHistoryTotalRo ro);
}