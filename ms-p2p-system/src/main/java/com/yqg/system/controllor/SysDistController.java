package com.yqg.system.controllor;

import com.yqg.api.system.sysdist.SysDistServiceApi;
import com.yqg.api.system.sysdist.bo.SysDistBo;
import com.yqg.api.system.sysdist.ro.SysDistRo;
import com.yqg.common.core.BaseControllor;
import com.yqg.common.core.annocation.NotNeedLogin;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.system.service.sysdist.SysDistService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 行政区划表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@RestController
//@RequestMapping("sysdist" )
public class SysDistController extends BaseControllor {
    @Autowired
    SysDistService sysdistService;

    @NotNeedLogin
    @ApiOperation(value = "通过key查询value", notes = "通过key查询value")
    @PostMapping(value = {SysDistServiceApi.path_getSystemDist,SysDistServiceApi.path_getSystemDistControl})
    public BaseResponse<List<SysDistBo>> sysDistList(@RequestBody SysDistRo ro) throws BusinessException {
        List<SysDistBo> response = this.sysdistService.getDistList(ro);

        return new BaseResponse<List<SysDistBo>>().successResponse(response);
    }
}