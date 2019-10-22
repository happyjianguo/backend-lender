package com.yqg.order.service.repaymentplan;

import com.yqg.api.order.orderorder.bo.RepaymentPlanBo;
import com.yqg.api.order.orderorder.ro.RepaymentPlanRo;
import com.yqg.common.core.BaseService;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.order.entity.RepaymentPlan;

import java.util.List;

/**
 * Created by Lixiangjun on 2019/7/1.
 */

public interface RepaymentPlanService extends BaseService<RepaymentPlan> {

    /**
     * 存入还款计划
     * @param ro
     * @throws BusinessException
     */
    public void addRepaymentPlan(RepaymentPlanRo ro)throws BusinessException;

    /**
     * 查看还款计划
     * @param ro
     * @throws BusinessException
     */
    public List<RepaymentPlanBo> findRepaymentPlanList(RepaymentPlanRo ro)throws Exception;

    /**
     * 更新还款计划
     * @param ro
     * @throws BusinessException
     */
    public void updateStatus(RepaymentPlanRo ro)throws BusinessException;
}
