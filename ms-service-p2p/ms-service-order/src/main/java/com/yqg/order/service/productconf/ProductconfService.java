package com.yqg.order.service.productconf;

import com.yqg.api.order.productconf.ro.ProductconfRo;
import com.yqg.common.core.BaseService;
import com.yqg.order.entity.Productconf;

import java.util.List;

/**
 * 投资产品表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-11-14 14:45:36
 */
public interface ProductconfService extends BaseService<Productconf> {


    List<Productconf> productconfList(ProductconfRo ro) throws Exception;

}