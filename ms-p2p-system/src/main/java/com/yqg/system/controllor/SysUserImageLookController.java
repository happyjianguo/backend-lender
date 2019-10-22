package com.yqg.system.controllor;

import com.yqg.api.system.sysuser.SysUserServiceApi;
import com.yqg.api.system.sysuser.bo.SysUserImageBo;
import com.yqg.api.system.sysuser.ro.SysUserImageRo;
import com.yqg.common.core.BaseControllor;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.system.service.sysuser.SysUserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 查看用户图片控制器
 * Created by Lixiangjun on 2019/6/14.
 */
@RestController
public class SysUserImageLookController extends BaseControllor{
    @Autowired
    SysUserService sysUserService;

    @ApiOperation(value = "查看用户图片", notes = "查看用户图片")
    @PostMapping(value = SysUserServiceApi.path_sysUserImageLook)
    public BaseResponse<SysUserImageBo> sysUserLogin(@RequestBody SysUserImageRo ro) throws BusinessException {
        SysUserImageBo imageBo = sysUserService.lookUserImage(ro);
        return new BaseResponse<SysUserImageBo>().successResponse(imageBo);
    }
}
