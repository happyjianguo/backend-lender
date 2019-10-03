package com.yqg.order.service.creditorinfo;

import com.yqg.api.order.creditorinfo.ro.CreditorinfoRo;
import com.yqg.common.dynamicdata.TargetDataSource;
import com.yqg.common.core.response.BasePageResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.core.BaseService;
import com.yqg.order.entity.Creditorinfo;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;

/**
 * 债权表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 11:02:36
 */
public interface CreditorinfoService extends BaseService<Creditorinfo> {


    /**
     * 标的是否推送过
     * @param creditorNo
     * @return
     */
    @TargetDataSource(dataSource = "readDruidDataSource")
    boolean isSendCreditorinfo(String creditorNo) throws BusinessException;

    /**
     * 接收推标
     * */
    void sendCreditorinfo(CreditorinfoRo ro) throws BusinessException, ParseException;
}