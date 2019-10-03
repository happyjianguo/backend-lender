package com.yqg.user.service.useruser.impl;

import ai.advance.sdk.client.OpenApiClient;
import com.alibaba.fastjson.JSON;
import com.yqg.api.user.useraddressdetail.ro.AdvanceRo;
import com.yqg.api.user.useraddressdetail.ro.BirthAddressRo;
import com.yqg.api.user.useraddressdetail.ro.IdentityCheckResultRo;
import com.yqg.api.user.useraddressdetail.ro.LiveAddressRo;
import com.yqg.api.user.userbank.ro.UserBankRo;
import com.yqg.api.user.useruser.bo.UserBankAuthStatus;
import com.yqg.api.user.useruser.bo.UserBo;
import com.yqg.api.user.useruser.bo.UserLoginBo;
import com.yqg.api.user.useruser.ro.*;
import com.yqg.common.core.response.BasePageResponse;
import com.yqg.common.dao.ExtendQueryCondition;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.redis.BaseRedisKeyEnums;
import com.yqg.common.sender.CaptchaUtils;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.common.utils.DESUtils;
import com.yqg.common.utils.DateUtils;
import com.yqg.common.utils.UuidUtil;
import com.yqg.user.config.AdvanceConfig;
import com.yqg.user.dao.UserDao;
import com.yqg.user.entity.UserAddressDetail;
import com.yqg.user.entity.UserBank;
import com.yqg.user.entity.UserUser;
import com.yqg.user.enums.UserAuthStatusEnum;
import com.yqg.user.enums.UserExceptionEnums;
import com.yqg.user.enums.UserTypeEnum;
import com.yqg.user.service.UserCommonServiceImpl;
import com.yqg.user.service.useraccount.UserAccountService;
import com.yqg.user.service.useruser.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * 用户基本信息表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:20:30
 */
@Service("userService")
public class UserServiceImpl extends UserCommonServiceImpl implements UserService {
    @Autowired
    protected UserDao userDao;
    @Autowired
    private AdvanceConfig advanceConfig;
    @Autowired
    protected UserAccountService userAccountService;

    @Override
    public UserBo userIsExist(UserRo ro) throws BusinessException {
        UserUser userUser1 = new UserUser();
        userUser1.setIdCardNo(ro.getIdCardNo());
        userUser1.setDisabled(0);
        UserUser user1 = this.findOne(userUser1);

        UserUser userUser2 = new UserUser();
        userUser2.setMobileNumberDES(DESUtils.encrypt(ro.getMobileNumber()));
        userUser2.setDisabled(0);
        UserUser user2 = userService.findOne(userUser2);

        UserBo userBo = new UserBo();
        if(user1==null&&user2==null){
            userBo.setIsExist("0");
        }
        if(user1!=null){
            BeanCoypUtil.copy(user1,userBo);
            userBo.setIsExist("1");
        }
        if(user2!=null){
            BeanCoypUtil.copy(user2,userBo);
            userBo.setIsExist("1");
        }
        return userBo;
    }

    @Override
    public UserUser findById(String id) throws BusinessException {
        return this.userDao.findOneById(id, new UserUser());
    }

    @Override
    public <E> E findById(String id, Class<E> boClass) throws BusinessException {
        UserUser useruser = findById(id);
        E bo = BeanCoypUtil.copyToNewObject(useruser, boClass);
        return bo;
    }

    @Override
    public UserUser findOne(UserUser entity) throws BusinessException {
        return this.userDao.findOne(entity);
    }

    @Override
    public UserUser findUserByMobile(String mobileNumber) throws BusinessException{
        UserUser search = new UserUser();
        logger.info(mobileNumber+"--------------------------------"+DESUtils.encrypt(mobileNumber));
        search.setMobileNumberDES(DESUtils.encrypt(mobileNumber));
        logger.info(search+"--------------------------------");
        return findOne(search);
    }

    @Override
    public UserBo findOneByMobileOrId(UserReq ro) throws BusinessException {
        UserUser search = new UserUser();
        if(!StringUtils.isEmpty(ro.getMobileNumber())){
            search.setMobileNumber(ro.getMobileNumber());
        }
        if(!StringUtils.isEmpty(ro.getUserUuid())){
            search.setId(ro.getUserUuid());
        }

        return this.findOne(search,UserBo.class);
    }

    @Override
    public UserBo findOneByRealNameOrId(UserReq ro) throws BusinessException {
        UserUser search = new UserUser();
        if(!StringUtils.isEmpty(ro.getRealName())){
            search.setRealName(ro.getRealName());
        }
        if(!StringUtils.isEmpty(ro.getUserUuid())){
            search.setId(ro.getUserUuid());
        }

        return this.findOne(search,UserBo.class);
    }

    @Override
    public <E> E findOne(UserUser entity, Class<E> boClass) throws BusinessException {
        UserUser useruser = findOne(entity);
        E bo = BeanCoypUtil.copyToNewObject(useruser, boClass);
        return bo;
    }


    @Override
    public List<UserUser> findList(UserUser entity) throws BusinessException {
        return this.userDao.findForList(entity);
    }

    @Override
    public <E> List<E> findList(UserUser entity, Class<E> boClass) throws BusinessException {
        List<UserUser> useruserList = findList(entity);
        List<E> boList = new ArrayList<>();
        for (UserUser useruser : useruserList) {
            E bo = BeanCoypUtil.copyToNewObject(useruser, boClass);
            boList.add(bo);
        }
        return boList;
    }

    @Override
    @Transactional
    public String addOne(UserUser entity) throws BusinessException {
        return userDao.addOne(entity);
    }

    @Override
    @Transactional
    public void deleteById(String id) throws BusinessException {
        userDao.deleteOne(findById(id));
    }

    @Override
    @Transactional
    public void deleteOne(UserUser entity) throws BusinessException {
        this.userDao.deleteOne(entity);
    }

    @Override
    @Transactional
    public void updateOne(UserUser entity) throws BusinessException {
        this.userDao.updateOne(entity);
    }

    @Override
    public UserBo checkUser(UserRo ro) throws BusinessException {
        ro.setMobileNumber(this.getRealIdPhoneNumber(ro.getMobileNumber()));        //格式化手机号
        UserUser userSearch = new UserUser();
        if(StringUtils.isEmpty(ro.getMobileNumber())){
            throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
        }
        userSearch.setMobileNumber(ro.getMobileNumber());
        UserUser result = this.findOne(userSearch);
        UserBo response = new UserBo();

        if(result != null){
            response.setIsExist("1");
        }else {
            response.setIsExist("0");
        }

        return response;
    }

    @Override
    @Transactional
    public UserLoginBo login(UserRegistRo ro) throws BusinessException {

        String mobile = ro.getMobileNumber();
        captchaUtils.checkSmsCaptcha(CaptchaUtils.CaptchaType.LOGIN, mobile, ro.getSmsCode());    //校验短信验证码

        mobile = this.getRealIdPhoneNumber(ro.getMobileNumber());        //格式化手机号
        ro.setMobileNumber(this.getRealIdPhoneNumber(ro.getMobileNumber()));        //格式化手机号

        UserUser userObj = this.findUserByMobile(mobile);
        UserLoginBo userLoginBo = new UserLoginBo();

        String userId = "";
        if(userObj == null){    //用户为空则注册
            userId = userService.registerUser(ro);
            userLoginBo.setAuthStatus(0);
        }else{  //用户存在则登录
            userId = userObj.getId();
            userLoginBo.setAuthStatus(userObj.getAuthStatus());
            userLoginBo.setUsername(userObj.getRealName());
        }

        String sessionId = UuidUtil.create();
        redisUtil.set(BaseRedisKeyEnums.USER_SESSION_KEY.appendToDefaultKey(sessionId), userId);
        userLoginBo.setSessionId(sessionId);
        userLoginBo.setMobileNumber(mobile);
        userLoginBo.setUserId(userId);


        usrLoginHistoryService.addPcLoginHistory(ro.getClientIp(), userId);   //添加登录记录
        return userLoginBo;
    }

    @Override
    @Transactional
    public String registerUser(UserRegistRo ro) throws BusinessException {
        UserUser addInfo = new UserUser();
        String mobileNumber = ro.getMobileNumber();

        if(ro.getType() != null){
            if(ro.getType() == 0){       //type == 0　普通用户, 其他为借款学生
                addInfo.setUserType(UserTypeEnum.NORMAL_USER.getType());     //普通用户
            }else {
                addInfo.setUserType(UserTypeEnum.STUDENT.getType());     //借款学生
            }
        }else {
            throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
        }

        addInfo.setMobileNumber(mobileNumber);
        addInfo.setMobileNumberDES(DESUtils.encrypt(mobileNumber));
        return this.addOne(addInfo);
    }

    @Override
    public UserBo userBasicInfo(String userId) throws BusinessException {
        if(StringUtils.isEmpty(userId)){
            throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
        }
        UserUser result = this.findById(userId);    //查询用户信息
        UserBo response = new UserBo();
        BeanUtils.copyProperties(result,response);

        UserBank search = new UserBank();
        search.setUserUuid(userId);
        UserBank userBank = this.userBankService.findOne(search);   //查询用户银行卡信息
        if(userBank != null){
            response.setBankCardNo(userBank.getBankNumberNo());
            response.setBankName(userBank.getBankName());
        }

        return response;
    }

    @Override
    public UserBankAuthStatus userAuthBankInfo(String userId) throws BusinessException {
        UserBankAuthStatus response = new UserBankAuthStatus();

        UserBo info = this.userBasicInfo(userId);
        Integer authStatus = info.getAuthStatus();

        if(authStatus != null && authStatus == UserAuthStatusEnum.NOT_PASS.getType()){
            response.setAuthStatus(2);
        }
        if(authStatus != null && (authStatus == UserAuthStatusEnum.MANAGE_PASS.getType() || authStatus ==
                UserAuthStatusEnum.PASS.getType() || info.getUserType() == 5)){
            response.setAuthStatus(1);
        }
        if(!StringUtils.isEmpty(info.getBankCardNo())){
            response.setBankStatus(1);
        }
        if(info.getUserType() != null && info.getUserType() == 5){
            response.setAuthStatus(1);
        }

        return response;
    }

    @Override
    @Transactional
    public Boolean advanceVerify(UserNameAuthRo ro) throws BusinessException {
        //UserUser userSearch = new UserUser();
        String idCardNo = ro.getIdCardNo().trim();
        String name = ro.getRealName().trim();  //用户姓名
        Boolean authFlag = false;   //实名认证成功标识

        if(redisUtil.tryLock(ro, 1800)){
            UserUser p2pUser = new UserUser();
            p2pUser.setId(ro.getUserId());
            UserUser userUser = this.findOne(p2pUser);     //查询用户信息
            if(userUser.getAuthStatus() >= UserAuthStatusEnum.NOT_PASS.getType() && userUser.getAuthStatus() <= UserAuthStatusEnum.MANAGE_PASS.getType()){
                throw new BusinessException(UserExceptionEnums.USER_IDCARD_EXIST);  //用户身份证号已存在
            }

            BeanUtils.copyProperties(ro,userUser);
            OpenApiClient client = new OpenApiClient(advanceConfig.getApiHost(), advanceConfig.getAccessKey(), advanceConfig.getSecretKey());
            Map<String, String> identityCheck = new HashMap<>();
            identityCheck.put("name", name);
            identityCheck.put("idNumber", idCardNo);

            String response = client.request(
                    advanceConfig.getIdentityCheckApi(), JSON.toJSONString(identityCheck));
            AdvanceRo advanceResponse = JSON.parseObject(response, AdvanceRo.class);

            if (advanceResponse.getCode().equals("SUCCESS")){
                logger.info("Real-name authentication results:{}", response);
                IdentityCheckResultRo data = JSON.parseObject(advanceResponse.getData(), IdentityCheckResultRo.class);
                if (!StringUtils.isEmpty(data.getIdNumber()) && !StringUtils.isEmpty(data.getName())) {

                    userUser.setRealName(data.getName());
                    userUser.setUserName(data.getName());
                    userUser.setIdCardNo(data.getIdNumber());
                    userUser.setUpdateTime(new Date());
                    userUser.setAuthStatus(UserAuthStatusEnum.PASS.getType());
                    this.updateOne(userUser);    //更新用户信息

                    authFlag = true;
                }
            }else {     //将用户放入后台实名失败审核列表
                userUser.setRealName(ro.getRealName());
                userUser.setUserName(ro.getRealName());
                userUser.setAuthStatus(UserAuthStatusEnum.NOT_PASS.getType());
                userUser.setUpdateTime(new Date());
                this.updateOne(userUser);
            }

            UserAddressDetail userAddressDetail = new UserAddressDetail();
            BirthAddressRo birthAddressRequest = ro.getBirthAddressRo();
            userAddressDetail.setUserUuid(ro.getUserId());
            userAddressDetail.setAddressType(1);    //1居住地址
            userAddressDetail.setProvince(birthAddressRequest.getBirthProvince());
            userAddressDetail.setCity(birthAddressRequest.getBirthCity());
            userAddressDetail.setBigDirect(birthAddressRequest.getBirthBigDirect());
            userAddressDetail.setSmallDirect(birthAddressRequest.getBirthSmallDirect());
            userAddressDetailService.addInfo(userAddressDetail);

            UserAddressDetail companyAddress = new UserAddressDetail();
            LiveAddressRo liveAddressRequset = ro.getLiveAddressRo();
            companyAddress.setUserUuid(ro.getUserId());
            companyAddress.setAddressType(2);    //2公司地址
            companyAddress.setProvince(liveAddressRequset.getLiveProvince());
            companyAddress.setCity(liveAddressRequset.getLiveCity());
            companyAddress.setBigDirect(liveAddressRequset.getLiveBigDirect());
            companyAddress.setSmallDirect(liveAddressRequset.getLiveSmallDirect());
            companyAddress.setDetailed(liveAddressRequset.getLiveDetailed());
            userAddressDetailService.addInfo(companyAddress);


        }else {
            throw new BusinessException(UserExceptionEnums.REPEAT_SUBMIT);
        }

        return authFlag;
    }

    public List<UserBo> userListByType(UserTypeSearchRo ro) throws BusinessException {
        UserUser search = new UserUser();
        Integer userType = ro.getUserType();
        if(userType == null || userType == 0){  //排除普通用户
            throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
        }
        search.setUserType(ro.getUserType());
        List<UserBo> response = this.findList(search, UserBo.class);    //查询用户信息

        for(UserBo cell:response){
            UserBankRo bankSearch = new UserBankRo();
            bankSearch.setUserUuid(cell.getId());
            UserBank userBank = this.userBankService.getUserBankInfo(bankSearch);   //查询用户银行卡信息
            if(userBank != null){
                cell.setBankCardNo(userBank.getBankNumberNo());
                cell.setBankName(userBank.getBankName());
                cell.setBankCode(userBank.getBankCode());
                cell.setBankCardName(userBank.getBankCardName());
            }
        }
        return response;

    }

    @Override
    public BasePageResponse<UserUser> userAuthFailedListByPage(UserAuthSearchRo ro) throws BusinessException,ParseException {
        String timeMin = ro.getTimeMin();
        String timeMax = ro.getTimeMax();

        ExtendQueryCondition extendQueryCondition = new ExtendQueryCondition();

        UserUser entity = new UserUser();

        Map<ExtendQueryCondition.CompareType, Object> map = new HashMap<>();

        if(!StringUtils.isEmpty(timeMax)){
            map.put(ExtendQueryCondition.CompareType.LTE_TIME, DateUtils.stringToDate(timeMax + " 23:59:59"));
        }
        if(!StringUtils.isEmpty(timeMin)){
            map.put(ExtendQueryCondition.CompareType.GTE_TIME, DateUtils.stringToDate(timeMin + " 00:00:00"));
        }
        if(!StringUtils.isEmpty(timeMax) || !StringUtils.isEmpty(timeMin)){
            extendQueryCondition.addCompareQueryMap(UserUser.updateTime_Field, map);
            entity.setExtendQueryCondition(extendQueryCondition);
        }
        if(!StringUtils.isEmpty(ro.getMobile())){
            entity.setMobileNumber(ro.getMobile());
        }
        if(!StringUtils.isEmpty(ro.getRealname())){
            entity.setRealName(ro.getRealname());
        }
        entity.setAuthStatus(UserAuthStatusEnum.NOT_PASS.getType());
        entity.setDisabled(0);
        ro.setSortProperty("sort");
        ro.setSortDirection(Sort.Direction.DESC);

        Page<UserUser> userUserPage = userDao.findForPage(entity, ro.convertPageRequest());

        BasePageResponse<UserUser> response = new BasePageResponse<>(userUserPage);
        response.setContent(userUserPage.getContent());

        return response;
    }

    @Override
    @Transactional
    public void checkUserAuthStatus(UserAuthSearchRo ro) throws BusinessException{
        Integer pass = ro.getPass();    //0.审核通过,1.审核拒绝
        String userId = ro.getUserUuid();

        if(pass == null || StringUtils.isEmpty(userId)){
            throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
        }

        UserUser result = this.findById(userId);
        if(result.getAuthStatus() > UserAuthStatusEnum.NOT_PASS.getType()){
            throw new BusinessException(UserExceptionEnums.USER_AUTH_NOT_REPEAT);
        }
        if(pass == 0){
            result.setAuthStatus(UserAuthStatusEnum.PASS.getType());
        }else {
            result.setAuthStatus(UserAuthStatusEnum.REFUSE.getType());
        }
        result.setUpdateTime(new Date());
        this.updateOne(result);
    }

    //回显用户实名认证
    @Override
    public UserNameAuthRo userAuthCheckInfo(UserAuthSearchRo ro) throws BusinessException {
        //UserNameAuthRo response = new UserNameAuthRo();
        String userId = ro.getUserUuid();
        if(StringUtils.isEmpty(userId)){
            throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
        }
        UserUser search = new UserUser();
        search.setId(userId);
        UserNameAuthRo response = this.findOne(search, UserNameAuthRo.class);

        UserAddressDetail liveSearch = new UserAddressDetail();
        liveSearch.setAddressType(1);   //1居住地址
        liveSearch.setUserUuid(userId);
        UserAddressDetail liveAddressDetail = this.userAddressDetailService.findOne(liveSearch);
        if(liveAddressDetail != null){
            BirthAddressRo birthAddress = new BirthAddressRo();
            birthAddress.setBirthBigDirect(liveAddressDetail.getBigDirect());
            birthAddress.setBirthCity(liveAddressDetail.getCity());
            birthAddress.setBirthDetailed(liveAddressDetail.getDetailed());
            birthAddress.setBirthProvince(liveAddressDetail.getProvince());
            birthAddress.setBirthSmallDirect(liveAddressDetail.getSmallDirect());
            response.setBirthAddressRo(birthAddress);
        }
        liveSearch.setAddressType(2);   //2公司地址
        UserAddressDetail companyAddress = this.userAddressDetailService.findOne(liveSearch);
        if(companyAddress != null){
            LiveAddressRo liveAddress = new LiveAddressRo();
            liveAddress.setLiveBigDirect(companyAddress.getBigDirect());
            liveAddress.setLiveCity(companyAddress.getCity());
            liveAddress.setLiveProvince(companyAddress.getProvince());
            liveAddress.setLiveSmallDirect(companyAddress.getSmallDirect());
            liveAddress.setLiveDetailed(companyAddress.getDetailed());
            response.setLiveAddressRo(liveAddress);
        }

        return response;

    }

    //超级投资人添加余额
    @Override
    @Transactional
    public void superUserAccountAdd(SuperUserAccountAddRo ro) throws BusinessException {
        String password = ro.getPassword();
        if(!password.equals(controlConfig.getPassword())){
            throw new BusinessException(UserExceptionEnums.CONTROL_PASSWORD_ERROR);
        }

        this.userAccountService.addUserAccountCurrent(ro.getUserUuid(), new BigDecimal(ro.getAmount()));
    }

    public static String getRealIdPhoneNumber(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return phone;
        }
        phone = phone.replaceAll("\\s", "")
                .replaceAll("-", "");
        if (phone.startsWith("628")) {
            return phone.substring(2);
        } else if (phone.startsWith("+628")) {
            return phone.substring(3);
        } else if (phone.startsWith("08")) {
            return phone.substring(1);
        }
        return phone;
    }

    public String getSmsCodeMobile(String phone) {
        phone = phone.replaceAll("\\s", "")
                .replaceAll("-", "");

        if (phone.startsWith("08")) {
            return "62"+phone.substring(1);
        } else if (phone.startsWith("8")) {
            return "62"+phone;
        } else if (phone.startsWith("+628")) {
            return phone.substring(1);
        }
        return phone;
    }

}