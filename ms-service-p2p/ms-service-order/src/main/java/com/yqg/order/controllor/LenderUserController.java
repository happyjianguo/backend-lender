package com.yqg.order.controllor;

import com.alibaba.fastjson.JSON;
import com.yqg.api.order.lenderuser.LenderUserServiceApi;
import com.yqg.api.order.lenderuser.bo.LenderUserBo;
import com.yqg.api.order.lenderuser.ro.LenderUserInfoRo;
import com.yqg.common.core.BaseControllor;
import com.yqg.common.core.annocation.NotNeedLogin;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.order.entity.LenderUser;
import com.yqg.order.service.lenderuser.LenderUserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 借款人信息表
 *
 * @author wu
 * @email wu@yishufu.com
 * @date 2018-09-03 11:15:41
 */
@RestController
public class LenderUserController extends BaseControllor {
    @Autowired
    LenderUserService lenderuserService;


    //查询借款人信息
    @ApiOperation(value = "查询借款人信息", notes = "查询借款人信息")
    @PostMapping(value = LenderUserServiceApi.path_selectLenderUserInfo)
    public BaseResponse selectScatterstandard(@RequestBody LenderUserInfoRo ro) throws Exception {
        logger.info("查询借款人信息请求参数:"+ JSON.toJSONString(ro));
        LenderUser lenderUser = lenderuserService.selectLenderUserInfoFromDoit(ro);
        LenderUserBo lenderUserBo = new LenderUserBo();
        BeanCoypUtil.copy(lenderUser, lenderUserBo);

        return new BaseResponse<LenderUserBo>().successResponse(lenderUserBo);

    }


}