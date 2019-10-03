package com.yqg.order.service.scatterstandard;

import com.yqg.api.order.scatterstandard.bo.ScatterstandardBo;
import com.yqg.api.order.scatterstandard.ro.ScatterstandardPageRo;
import com.yqg.api.order.scatterstandard.ro.ScatterstandardRo;
import com.yqg.common.core.BaseService;
import com.yqg.common.core.response.BasePageResponse;
import com.yqg.common.dynamicdata.TargetDataSource;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.order.entity.Scatterstandard;
import org.springframework.data.domain.Page;

import java.text.ParseException;
import java.util.List;

/**
 * 散标表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 11:02:36
 */
public interface ScatterstandardService extends BaseService<Scatterstandard> {


    public void deal6HoursLaterOrder() throws BusinessException, ParseException, IllegalAccessException;

    void userRepay(ScatterstandardRo ro);

    /**
     * 分页查询散标列表
     * @param scatterstandardPageRo
     * @return
     */
    @TargetDataSource(dataSource = "readDruidDataSource")
    BasePageResponse<ScatterstandardBo> queryForPage(ScatterstandardPageRo scatterstandardPageRo) throws BusinessException;


    @TargetDataSource(dataSource = "readDruidDataSource")
    Page<Scatterstandard> queryList(ScatterstandardPageRo scatterstandardPageRo) throws BusinessException;

    void deal30MinLaterOrder() throws Exception;

    List<Scatterstandard> selectCreditorinfo(ScatterstandardRo ro) throws BusinessException;

    void loanFlowStandard() throws BusinessException;
}