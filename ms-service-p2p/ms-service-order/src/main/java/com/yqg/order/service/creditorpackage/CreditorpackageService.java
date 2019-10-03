package com.yqg.order.service.creditorpackage;

import com.yqg.api.creditorpackage.bo.CreditorpackageBo;
import com.yqg.api.order.orderorder.bo.OrderOrderBo;
import com.yqg.common.core.BaseService;
import com.yqg.common.core.response.BasePageResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.order.entity.Creditorpackage;

import java.util.List;

/**
 * 债权包表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-11-15 18:21:38
 */
public interface CreditorpackageService extends BaseService<Creditorpackage> {


    public Creditorpackage createCreditorpackage(Creditorpackage ro) throws Exception;

    public List<Creditorpackage> queryCreditorpackage(Creditorpackage ro) throws Exception;

    public void updateCreditorpackage(Creditorpackage ro) throws BusinessException;

    //public Creditorpackage findPackageByNo(String packageNo) throws Exception;;
}