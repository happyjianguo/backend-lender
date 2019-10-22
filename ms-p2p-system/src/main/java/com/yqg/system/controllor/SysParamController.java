package com.yqg.system.controllor;

import com.yqg.api.system.sysparam.SysParamServiceApi;
import com.yqg.api.system.sysparam.bo.SysParamBo;
import com.yqg.api.system.sysparam.ro.SysParamRo;
import com.yqg.common.core.BaseControllor;
import com.yqg.common.core.annocation.NotNeedLogin;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.system.service.sysparam.SysParamService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 系统参数
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@RestController
//@RequestMapping("sysparam" )
public class SysParamController extends BaseControllor {
    @Autowired
    SysParamService sysparamService;

    @NotNeedLogin
    @ApiOperation(value = "通过key查询value", notes = "通过key查询value")
    @PostMapping(value = SysParamServiceApi.path_sysParamValueBykey)
    public BaseResponse<SysParamBo> sysParamValueBykey(@RequestBody SysParamRo ro) throws Exception {
        String sysKey = ro.getSysKey();
        if(StringUtils.isEmpty(sysKey)){
            throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
        }
        SysParamBo reuslt = sysparamService.sysValueByKey(sysKey);

        return new BaseResponse<SysParamBo>().successResponse(reuslt);
    }

}