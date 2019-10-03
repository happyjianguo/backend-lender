package com.yqg.order.controllor;

import com.yqg.api.order.productconf.ProductconfServiceApi;
import com.yqg.api.order.productconf.ro.ProductconfRo;
import com.yqg.common.core.BaseControllor;
import com.yqg.common.core.annocation.NotNeedLogin;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.order.entity.Productconf;
import com.yqg.order.service.productconf.impl.ProductconfServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 投资产品表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-11-14 14:45:36
 */
@RestController
public class ProductconfController extends BaseControllor {
    @Autowired
    ProductconfServiceImpl productconfService;

    @NotNeedLogin
    @ApiOperation(value = "查询产品列表", notes = "查询产品列表")
    @PostMapping(value = ProductconfServiceApi.path_productconfList)
    public BaseResponse productconfList(@RequestBody ProductconfRo ro) throws Exception {
        List<Productconf> list = productconfService.productconfList(ro);
        return new BaseResponse<List<Productconf>>().successResponse(list);

    }
    //查询产品详情
    @NotNeedLogin
    @ApiOperation(value = "查询产品详情", notes = "查询产品详情")
    @PostMapping(value = ProductconfServiceApi.path_selectProductconfById)
    public BaseResponse selectProductconfById(@RequestBody ProductconfRo ro) throws Exception {
        Productconf productconf = productconfService.findById(ro.getId());
        return new BaseResponse<Productconf>().successResponse(productconf);
    }

}