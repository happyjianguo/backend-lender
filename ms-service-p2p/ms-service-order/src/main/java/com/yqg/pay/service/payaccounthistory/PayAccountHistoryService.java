package com.yqg.pay.service.payaccounthistory;

import com.yqg.api.pay.payaccounthistory.ro.PayAccountPageRo;
import com.yqg.common.dynamicdata.TargetDataSource;
import com.yqg.common.core.response.BasePageResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.core.BaseService;
import com.yqg.pay.entity.PayAccountHistory;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;

/**
 * 资金流水表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 17:28:20
 */
public interface PayAccountHistoryService extends BaseService<PayAccountHistory> {

    /*理财资金流水查询*/
    BasePageResponse<PayAccountHistory> payAccountListByPage(PayAccountPageRo ro) throws BusinessException,ParseException;
}