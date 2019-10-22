package com.yqg.user.controllor;

import com.yqg.api.user.useraccount.bo.UserAccountBo;
import com.yqg.api.user.useruser.UserServiceApi;
import com.yqg.api.user.useruser.bo.ImageCaptchBo;
import com.yqg.api.user.useruser.bo.UserBankAuthStatus;
import com.yqg.api.user.useruser.bo.UserBo;
import com.yqg.api.user.useruser.bo.UserLoginBo;
import com.yqg.api.user.useruser.ro.*;
import com.yqg.common.core.BaseControllor;
import com.yqg.common.core.annocation.NotNeedLogin;
import com.yqg.common.core.request.BaseSessionIdRo;
import com.yqg.common.core.response.BasePageResponse;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.sender.CaptchaUtils;
import com.yqg.user.entity.UserMessage;
import com.yqg.user.entity.UserUser;
import com.yqg.user.service.usermessage.UserMessageService;
import com.yqg.user.service.useruser.UserService;
import com.yqg.user.service.usrloginhistory.UsrLoginHistoryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户基本信息表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:20:30
 */
@RestController
//@RequestMapping("useruser" )
public class UserController extends BaseControllor {
    @Autowired
    UserService userService;
    @Autowired
    UsrLoginHistoryService usrLoginHistoryService;
    @Autowired
    UserMessageService userMessageService;

    @NotNeedLogin
    @ApiOperation(value = "根据userId查用户信息", notes = "根据userId查用户信息")
    @PostMapping(value = UserServiceApi.path_findUserById)
    public BaseResponse findUserById(@RequestBody UserReq ro) throws Exception {
        UserUser user = userService.findById(ro.getUserUuid());
        UserBo userBo = new UserBo();
        BeanUtils.copyProperties(user,userBo);
        return new BaseResponse<UserBo>().successResponse(userBo);
    }

    @NotNeedLogin
    @ApiOperation(value = "查询用户是否存在", notes = "查询用户是否存在")
    @PostMapping(value = UserServiceApi.path_userIsExist)
    public BaseResponse userIsExist(@RequestBody UserRo ro) throws Exception {
        UserBo userBo = userService.userIsExist(ro);
        return new BaseResponse<UserBo>().successResponse(userBo);
    }

    @NotNeedLogin
    @ApiOperation(value = "注册查询用户是否存在", notes = "注册查询用户是否存在")
    @PostMapping(value = UserServiceApi.path_checkUserExist)
    public BaseResponse checkUserExist(@RequestBody UserRo ro) throws Exception {
        UserBo userBo = userService.checkUser(ro);
        return new BaseResponse<UserBo>().successResponse(userBo);
    }

    @NotNeedLogin
    @ApiOperation(value = "图片验证码", notes = "获取图片验证码,Base64")
    @GetMapping(value = UserServiceApi.path_getImageCapcha)
    public BaseResponse<ImageCaptchBo> getImageCapcha() throws Exception {
        //生成图片验证码
        Map<CaptchaUtils.ImgCaptchaInfoType, String> imgCaptchaInfo = captchaUtils.getImgCaptcha();
        ImageCaptchBo imageCaptchBo = new ImageCaptchBo();
        imageCaptchBo.setImgSessionId(imgCaptchaInfo.get(CaptchaUtils.ImgCaptchaInfoType.IMG_SESSION));
        imageCaptchBo.setImgBase64(imgCaptchaInfo.get(CaptchaUtils.ImgCaptchaInfoType.IMG_BASE64));
        return new BaseResponse<ImageCaptchBo>().successResponse(imageCaptchBo);
    }

//    @NotNeedLogin
//    @ApiOperation(value = "短信验证码", notes = "获取短信验证码")
//    @PostMapping(value = UserServiceApi.path_sendRegisterCapcha)
//    public BaseResponse getSmsCode(@RequestBody SmsCodeRo ro) throws Exception {
//        String imgSessionId = ro.getImgSessionId();
//        String imgCaptch = ro.getImgCaptch();
//        String mobile = this.userService.getSmsCodeMobile(ro.getMobileNumber());
//
//        captchaUtils.checkImgCaptcha(imgSessionId, imgCaptch);  //校验图片验证码
//
//        /*UserUser userObj = userService.findUserByMobile(mobile);
//        if(userObj == null){    //用户为空则注册
//            captchaUtils.sendSmsCaptcha(CaptchaUtils.CaptchaType.LOGIN, ro.getMobileNumber());
//        }else{  //用户存在则注册
//            captchaUtils.sendSmsCaptcha(CaptchaUtils.CaptchaType.LOGIN, ro.getMobileNumber());
//        }*/
//
//        captchaUtils.sendSmsCaptcha(CaptchaUtils.CaptchaType.LOGIN, mobile);
//        return successResponse();
//    }

    @NotNeedLogin
    @ApiOperation(value = "用户登录或注册", notes = "用户登录或注册")
    @PostMapping(value = UserServiceApi.path_userLogin)
    public BaseResponse<UserLoginBo> userLogin(@RequestBody UserRegistRo ro) throws Exception {
        UserLoginBo userLoginBo = this.userService.login(ro);

        return new BaseResponse<UserLoginBo>().successResponse(userLoginBo);
    }

    @ApiOperation(value = "用户个人信息查询", notes = "用户个人信息查询")
    @PostMapping(value = UserServiceApi.path_userBasicInfo)
    public BaseResponse<UserBo> userBasicInfo(@RequestBody BaseSessionIdRo ro) throws Exception {
        String userId = ro.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
        }

        UserBo response = this.userService.userBasicInfo(userId);
        return new BaseResponse<UserBo>().successResponse(response);
    }

    @NotNeedLogin
    @ApiOperation(value = "通过用户id查询用户实名绑卡状态", notes = "通过用户id查询用户实名绑卡状态")
    @PostMapping(value = UserServiceApi.path_userAuthBankStatus)
    public BaseResponse<UserBankAuthStatus> userAuthBankInfo(@RequestBody UserAuthBankStatusRo ro) throws Exception {
        String userId = ro.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
        }

        UserBankAuthStatus response = this.userService.userAuthBankInfo(userId);
        return new BaseResponse<UserBankAuthStatus>().successResponse(response);
    }

    @ApiOperation(value = "advance认证", notes = "advance认证")
    @PostMapping(value = UserServiceApi.path_userAdvanceVerify)
    public BaseResponse advanceVerify(@RequestBody UserNameAuthRo ro) throws Exception {
        return new BaseResponse().successResponse(this.userService.advanceVerify(ro));
    }

    @NotNeedLogin
    @ApiOperation(value = "通过type查询用户", notes = "查询超级投资人,资金托管账户,收入账户")
    @PostMapping(value = UserServiceApi.path_userListByType)
    public BaseResponse<UserAccountBo> userListByType(@RequestBody UserTypeSearchRo ro) throws Exception {
        return new BaseResponse<UserAccountBo>().successResponse(this.userService.userListByType(ro));
    }

    @ApiOperation(value = "分页查询审核失败用户列表", notes = "分页查询审核失败用户列表")
    @PostMapping(value = UserServiceApi.path_userAuthFailedListByPage)
    public BaseResponse<BasePageResponse<UserUser>> userAuthFailedListByPage(@RequestBody UserAuthSearchRo ro) throws Exception {
        return new BaseResponse<BasePageResponse<UserUser>>().successResponse(this.userService.userAuthFailedListByPage(ro));
    }

    @ApiOperation(value = "审核实名认证拒绝用户", notes = "审核实名认证拒绝用户")
    @PostMapping(value = UserServiceApi.path_checkUserAuthStatus)
    public BaseResponse<Object> checkUserAuthStatus(@RequestBody UserAuthSearchRo ro) throws Exception {
        this.userService.checkUserAuthStatus(ro);
        return new BaseResponse<Object>().successResponse();
    }

    @ApiOperation(value = "反显实名认证信息", notes = "反显实名认证信息")
    @PostMapping(value = UserServiceApi.path_userAuthCheckInfo)
    public BaseResponse<Object> userAuthCheckInfo(@RequestBody UserAuthSearchRo ro) throws Exception {
        return new BaseResponse<Object>().successResponse(this.userService.userAuthCheckInfo(ro));
    }

    @ApiOperation(value = "通过用户session查询用户实名绑卡状态", notes = "通过用户session查询用户实名绑卡状态")
    @PostMapping(value = UserServiceApi.path_userAuthBankInfoBySession)
    public BaseResponse<UserBankAuthStatus> userAuthBankInfoBySession(@RequestBody BaseSessionIdRo ro) throws Exception {
        String userId = ro.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
        }
        UserBankAuthStatus response = this.userService.userAuthBankInfo(userId);
        return new BaseResponse<UserBankAuthStatus>().successResponse(response);
    }

    @ApiOperation(value = "为超级用户增加资金", notes = "为超级用户增加资金")
    @PostMapping(value = UserServiceApi.path_superUserAccountAdd)
    public BaseResponse<Object> superUserAccountAdd(@RequestBody SuperUserAccountAddRo ro) throws Exception {
        this.userService.superUserAccountAdd(ro);
        return new BaseResponse<Object>().successResponse();
    }

    @NotNeedLogin
    @ApiOperation(value = "通过手机号查询用户", notes = "通过手机号查询用户")
    @PostMapping(value = UserServiceApi.path_findUserByMobileOrId)
    public BaseResponse<Object> findOneByMobileOrId(@RequestBody UserReq ro) throws Exception {

        return new BaseResponse<Object>().successResponse(this.userService.findOneByMobileOrId(ro));
    }

    @NotNeedLogin
    @ApiOperation(value = "通过手机号或姓名查询用户", notes = "通过手机号或姓名查询用户")
    @PostMapping(value = UserServiceApi.path_findOneByMobileOrName)
    public BaseResponse<UserBo> findOneByMobileOrName(@RequestBody UserReq ro) throws Exception {

        return new BaseResponse<UserBo>().successResponse(this.userService.findOneByMobileOrName(ro));
    }

    @ApiOperation("重置交易密码")
    @RequestMapping(UserServiceApi.path_resetPayPassword)
    public BaseResponse<Object> resetPayPassword(@RequestBody PayPasswordRo payPasswordro) throws Exception {
        this.userService.resetPayPassword(payPasswordro);
        return new BaseResponse<Object>().successResponse();
    }

    @ApiOperation("上传头像")
    @RequestMapping(UserServiceApi.path_uploadHeadImage)
    public BaseResponse<Object> uploadHeadImage(@RequestBody UserNameAuthRo ro)throws BusinessException{
        this.userService.uploadHeadImage(ro);
        return new BaseResponse<Object>().successResponse();
    }

    @NotNeedLogin
    @ApiOperation("添加超级投资人")
    @RequestMapping(UserServiceApi.path_addSuperInvestor)
    public BaseResponse<Object> addSuperInvestor(@RequestBody UserReq ro) throws BusinessException {
        this.userService.addSuperInvestor(ro);
        return new BaseResponse<Object>().successResponse();
    }

    @NotNeedLogin
    @ApiOperation("添加机构投资人")
    @RequestMapping(UserServiceApi.path_addCompanyInvestor)
    public BaseResponse<Object> addCompanyInvestor(@RequestBody UserReq ro) throws Exception {
        this.userService.addCompanyInvestor(ro);
        return new BaseResponse<Object>().successResponse();
    }

    @NotNeedLogin
    @ApiOperation(value = "发送通知消息")
    @PostMapping(value = UserServiceApi.path_addUserMessage)
    public BaseResponse<Object> addUserMessage(@RequestBody MessageRo ro) throws Exception {
        this.userMessageService.addUserMessage(ro);
        return new BaseResponse<Object>().successResponse();
    }

    @ApiOperation(value = "通知消息列表", notes = "通知消息列表分页显示")
    @PostMapping(value = UserServiceApi.path_messageList)
    public BaseResponse<BasePageResponse<UserMessage>> messageList(@RequestBody MessageListRo ro) throws Exception {

        return new BaseResponse<BasePageResponse<UserMessage>>().successResponse(this.userMessageService.getMessageList(ro));
    }

    @ApiOperation(value = "通知消息已读")
    @PostMapping(value = UserServiceApi.path_updateUserMessage)
    public BaseResponse<Object> updateUserMessage(@RequestBody MessageListRo ro) throws Exception {
        this.userMessageService.updateUserMessage(ro);
        return new BaseResponse<Object>().successResponse();
    }
}