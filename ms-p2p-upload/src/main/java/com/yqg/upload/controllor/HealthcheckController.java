package com.yqg.upload.controllor;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yqg.api.upload.UploadServiceApi;
import com.yqg.common.core.annocation.NotNeedLogin;
import com.yqg.common.core.response.BaseResponse;

/**
 * Remark:
 * Created by arief.halim on 2020.06.11.
 */
@RestController
public class HealthcheckController {
    
    @RequestMapping(value = UploadServiceApi.path_healthcheck, method = RequestMethod.HEAD)
    @ResponseBody
    @NotNeedLogin
    public BaseResponse healthcheck() {
        return new BaseResponse().successResponse();
    }

}
