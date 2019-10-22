package com.yqg.order.dao;

import com.yqg.common.dao.BaseDao;
import com.yqg.order.entity.Creditorinfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 债权表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 11:02:36
 */
@Repository
public interface CreditorinfoDao extends BaseDao<Creditorinfo, String> {

    @Query(value = "SELECT amountApply FROM creditorInfo WHERE disabled=0 GROUP BY amountApply",nativeQuery = true)
    List<BigDecimal> getApplyAmounts();

}
