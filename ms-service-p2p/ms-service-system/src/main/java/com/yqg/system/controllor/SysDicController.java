package com.yqg.system.controllor;

import com.yqg.api.system.sysdic.SysDicServiceApi;
import com.yqg.api.system.sysdic.ro.SysDicRo;
import com.yqg.api.system.sysdic.ro.SysDicSearchRo;
import com.yqg.api.system.sysdicitem.bo.SysDicItemBo;
import com.yqg.common.core.BaseControllor;
import com.yqg.common.core.annocation.NotNeedLogin;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.system.entity.SysDicItem;
import com.yqg.system.service.sysdic.SysDicService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 字典表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@RestController
public class SysDicController extends BaseControllor {
    @Autowired
    SysDicService sysdicService;


    @ApiOperation(value = "字典查询", notes = "字典查询")
    @PostMapping(value = SysDicServiceApi.path_dicItemListByCode)
    public BaseResponse<List<SysDicItem>> dicItemListByCode(@RequestBody SysDicRo ro ) throws BusinessException{
        String dicCode = ro.getDicCode();
        if(StringUtils.isEmpty(dicCode)){
            throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
        }
        return new BaseResponse<List<SysDicItem>>().successResponse(this.sysdicService.sysDicItemsListByDicCode(dicCode));
    }


    @NotNeedLogin
    @ApiOperation(value = "字典查询无session", notes = "字典查询无session")
    @PostMapping(value = SysDicServiceApi.path_dicItemBoListByDicCode)
    public BaseResponse<List<SysDicItemBo>> dicItemBoListByDicCode(@RequestBody SysDicSearchRo ro ) throws BusinessException{
        String dicCode = ro.getDicCode();
        if(StringUtils.isEmpty(dicCode)){
            throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
        }
        return new BaseResponse<List<SysDicItemBo>>().successResponse(this.sysdicService.dicItemBoListByDicCode(dicCode));
    }
}