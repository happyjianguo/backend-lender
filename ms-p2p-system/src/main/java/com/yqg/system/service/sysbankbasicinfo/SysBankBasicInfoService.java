package com.yqg.system.service.sysbankbasicinfo;

import com.yqg.api.system.sysbankbasicinfo.bo.SysBankBasicInfoBo;
import com.yqg.common.core.BaseService;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.system.entity.SysBankBasicInfo;

import java.util.List;

/**
 * 银行基础信息
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
public interface SysBankBasicInfoService extends BaseService<SysBankBasicInfo> {

    List<SysBankBasicInfoBo> bankList() throws BusinessException;

    /**
     * 通过银行code查询银行信息*/
    SysBankBasicInfoBo bankInfoByCode(String bankCode) throws BusinessException;
}