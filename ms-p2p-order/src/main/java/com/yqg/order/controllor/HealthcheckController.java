package com.yqg.order.controllor;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yqg.api.order.OrderOrderServiceApi;
import com.yqg.common.core.annocation.NotNeedLogin;
import com.yqg.common.core.response.BaseResponse;

/**
 * Remark:
 * Created by arief.halim on 2020.06.11.
 */
@RestController
public class HealthcheckController {
    
    @RequestMapping(value = OrderOrderServiceApi.path_healthcheck, method = RequestMethod.HEAD)
    @ResponseBody
    @NotNeedLogin
    public BaseResponse healthcheck() {
        return new BaseResponse().successResponse();
    }

}
