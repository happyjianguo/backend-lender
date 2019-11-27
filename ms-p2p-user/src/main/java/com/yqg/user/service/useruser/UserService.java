package com.yqg.user.service.useruser;

import com.yqg.api.user.useraccount.bo.UserAccountBo;
import com.yqg.api.user.useruser.bo.UserBankAuthStatus;
import com.yqg.api.user.useruser.bo.UserBo;
import com.yqg.api.user.useruser.bo.UserLoginBo;
import com.yqg.api.user.useruser.ro.*;
import com.yqg.common.core.BaseService;
import com.yqg.common.core.response.BasePageResponse;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.user.entity.UserAccount;
import com.yqg.user.entity.UserUser;

import java.text.ParseException;
import java.util.List;

/**
 * 用户基本信息表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:20:30
 */
public interface UserService extends BaseService<UserUser> {

    /**
     * 通过手机号查询用户
     * @param mobileNumber */
    UserUser findUserByMobile(String mobileNumber) throws BusinessException;

    /*手机号查询用户*/
    UserBo findOneByMobileOrId(UserReq ro) throws BusinessException;

    /*手机号或姓名 查询用户*/
    UserBo findOneByMobileOrName(UserReq ro) throws BusinessException;

    //查询用户是否存在
    UserBo userIsExist(UserRo ro) throws BusinessException ;

    /**
     * 注册查询*/
    UserBo checkUser(UserRo ro) throws BusinessException;

    /**
     * 注册添加用户*/
    String registerUser(UserRegistRo ro) throws BusinessException;

    /**
     * 用户注册*/
    UserLoginBo login(UserRegistRo ro) throws BusinessException;

    /**
     * 查询用户及银行卡信息
     * */
    UserBo userBasicInfo(String userId) throws BusinessException;

    /**
     * 通过用户id查询用户实名及绑卡状态*/
    UserBankAuthStatus userAuthBankInfo(String userId) throws BusinessException;

    /**
     * advance实名认证
     * */
    //ahalim: Remove advance.ai
    // Boolean advanceVerify(UserNameAuthRo ro) throws BusinessException;

    /**
     * 通过用户type查询用户*/
    UserAccountBo userListByType(UserTypeSearchRo ro) throws BusinessException;

    /*
    * 分页查询审核失败用户列表
    * */
    BasePageResponse<UserUser> userAuthFailedListByPage(UserAuthSearchRo ro) throws BusinessException,ParseException;

    /*
    * 审核实名认证拒绝用户
    * */
    void checkUserAuthStatus(UserAuthSearchRo ro) throws BusinessException;

    /**
     * 反显实名认证信息*/
    UserNameAuthRo userAuthCheckInfo(UserAuthSearchRo ro) throws BusinessException;

    /*超级投资人添加余额*/
    void superUserAccountAdd(SuperUserAccountAddRo ro) throws BusinessException;

    String getSmsCodeMobile(String phone);

    /**
     * 重置交易密码
     * @param ro
     * @throws Exception
     */
    void resetPayPassword(PayPasswordRo ro)throws Exception;

    /**
     * 上传头像
     * @param ro
     * @throws BusinessException
     */
    void uploadHeadImage(UserNameAuthRo ro)throws BusinessException;

    /**
     * 添加超级投资者
     * @param ro
     * @throws BusinessException
     */
    void addSuperInvestor(UserReq ro)throws BusinessException;

    /**
     * 添加机构投资人
     * @param ro
     * @throws Exception
     */
    void addCompanyInvestor(UserReq ro) throws Exception;
}