package com.yqg.order.controllor;

import com.alibaba.fastjson.JSON;
import com.yqg.api.creditorpackage.CreditorpackageServiceApi;
import com.yqg.api.creditorpackage.bo.CreditorpackageBo;
import com.yqg.api.order.orderorder.OrderOrderServiceApi;
import com.yqg.api.order.orderorder.bo.OrderOrderBo;
import com.yqg.api.order.orderorder.ro.OrderOrderPageRo;
import com.yqg.common.core.BaseControllor;
import com.yqg.common.core.annocation.NotNeedLogin;
import com.yqg.common.core.response.BasePageResponse;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.order.entity.Creditorpackage;
import com.yqg.order.service.creditorpackage.CreditorpackageService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

/**
 * 债权包表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-11-15 18:21:38
 */
@RestController
public class CreditorpackageController extends BaseControllor {
    @Autowired
    CreditorpackageService creditorpackageService;

    @NotNeedLogin
    @ApiOperation(value = "生成债权包", notes = "生成债权包")
    @PostMapping(value = CreditorpackageServiceApi.path_createCreditorpackage)
    public BaseResponse createCreditorpackage(@RequestBody Creditorpackage ro) throws Exception {
        logger.info("生成债权包请求参数:"+ JSON.toJSONString(ro));
        return new BaseResponse<Object>().successResponse(creditorpackageService.createCreditorpackage(ro));
    }

    @NotNeedLogin
    @ApiOperation(value = "查询债权包", notes = "查询债权包")
    @PostMapping(value = CreditorpackageServiceApi.path_queryCreditorpackage)
    public BaseResponse queryCreditorpackage(@RequestBody Creditorpackage ro) throws Exception {
        logger.info("查询债权包请求参数:"+ JSON.toJSONString(ro));
        return new BaseResponse<Object>().successResponse(creditorpackageService.queryCreditorpackage(ro));
    }
    @NotNeedLogin
    @ApiOperation(value = "更改债权包", notes = "更改债权包")
    @PostMapping(value = CreditorpackageServiceApi.path_updateCreditorpackage)
    public BaseResponse updateCreditorpackage(@RequestBody Creditorpackage ro) throws Exception {
        logger.info("更改债权包请求参数:"+ JSON.toJSONString(ro));
        creditorpackageService.updateCreditorpackage(ro);
        return new BaseResponse<Object>().successResponse();
    }

}