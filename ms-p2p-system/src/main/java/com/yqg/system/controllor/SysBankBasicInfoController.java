package com.yqg.system.controllor;

import com.yqg.api.system.sysbankbasicinfo.SysBankBasicInfoServiceApi;
import com.yqg.api.system.sysbankbasicinfo.bo.SysBankBasicInfoBo;
import com.yqg.api.system.sysbankbasicinfo.ro.SysBankBasicInfoRo;
import com.yqg.common.core.BaseControllor;
import com.yqg.common.core.annocation.NotNeedLogin;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.system.entity.SysBankBasicInfo;
import com.yqg.system.service.sysbankbasicinfo.SysBankBasicInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 银行基础信息
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@RestController
//@RequestMapping("sysbankbasicinfo" )
public class SysBankBasicInfoController extends BaseControllor {
    @Autowired
    SysBankBasicInfoService sysbankbasicinfoService;

    @NotNeedLogin
    @ApiOperation(value = "查询支持银行卡列表", notes = "查询支持银行卡列表")
    @PostMapping(value = SysBankBasicInfoServiceApi.path_bankList)
    public BaseResponse<List<SysBankBasicInfoBo>> bankList() throws Exception {

        List<SysBankBasicInfoBo> response = this.sysbankbasicinfoService.bankList();

        return new BaseResponse<List<SysBankBasicInfoBo>>().successResponse(response);
    }

    @NotNeedLogin
    @ApiOperation(value = "通过银行code查询银行信息")
    @PostMapping(value = SysBankBasicInfoServiceApi.path_bankInfoByCode)
    public BaseResponse<SysBankBasicInfoBo> bankInfoByCode(@RequestBody SysBankBasicInfoRo ro) throws Exception{
        String bankCode = ro.getBankCode();
        if(StringUtils.isEmpty(bankCode)){
            throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
        }
        SysBankBasicInfoBo response = this.sysbankbasicinfoService.bankInfoByCode(bankCode);
        return new BaseResponse<SysBankBasicInfoBo>().successResponse(response);
    }
}