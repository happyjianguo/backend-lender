package com.yqg.user.service.useruser.impl;

import com.yqg.api.system.sysbankbasicinfo.bo.SysBankBasicInfoBo;
import com.yqg.api.system.sysbankbasicinfo.ro.SysBankBasicInfoRo;
import com.yqg.api.system.sysparam.ro.SysParamRo;
import com.yqg.api.user.enums.*;
import com.yqg.api.user.useraccount.bo.UserAccountBo;
import com.yqg.api.user.useraddressdetail.ro.BirthAddressRo;
import com.yqg.api.user.useraddressdetail.ro.LiveAddressRo;
import com.yqg.api.user.userbank.ro.UserBankRo;
import com.yqg.api.user.userbank.ro.UserBindBankCardRo;
import com.yqg.api.user.useruser.bo.UserBankAuthStatus;
import com.yqg.api.user.useruser.bo.UserBo;
import com.yqg.api.user.useruser.bo.UserLoginBo;
import com.yqg.api.user.useruser.ro.*;
import com.yqg.common.config.CommonConfig;
import com.yqg.common.core.response.BasePageResponse;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.dao.ExtendQueryCondition;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.redis.BaseRedisKeyEnums;
import com.yqg.common.redis.IRedisKeyEnum;
import com.yqg.common.redis.PayParamContants;
import com.yqg.common.sender.CaptchaUtils;
import com.yqg.common.utils.*;
import com.yqg.user.config.AdvanceConfig;
import com.yqg.user.dao.UserAccountDao;
import com.yqg.user.dao.UserBankDao;
import com.yqg.user.dao.UserDao;
import com.yqg.user.entity.UserAccount;
import com.yqg.user.entity.UserAddressDetail;
import com.yqg.user.entity.UserBank;
import com.yqg.user.entity.UserUser;
import com.yqg.user.service.UserCommonServiceImpl;
import com.yqg.user.service.useraccount.UserAccountService;
import com.yqg.user.service.userbank.impl.UserBankServiceImpl;
import com.yqg.user.service.useraddressdetail.impl.UserAddressDetailServiceImpl;
import com.yqg.user.service.usermessage.impl.UserMessageServiceImpl;
import com.yqg.user.service.useruser.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
    @Autowired
    protected UserAccountDao userAccountDao;
    @Autowired
    UserBankServiceImpl userBankService;
    @Autowired
    UserAddressDetailServiceImpl userAddressDetailService;
    @Autowired
    protected UserBankDao userBankDao;
    @Autowired
    private SmsServiceUtil smsServiceUtil;
    @Autowired
    UserMessageServiceImpl userMessageService;
    @Autowired
    private PayParamContants payParamContants;

    @Autowired
    CommonConfig commonConfig;


    @Value("${count.smsCount}")
    private String smsCount;

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
    public UserBo findOneByMobileOrName(UserReq ro) throws BusinessException {
        UserUser search = new UserUser();
        if(!StringUtils.isEmpty(ro.getMobileNumber())){
            search.setMobileNumber(ro.getMobileNumber());
        }
        if(!StringUtils.isEmpty(ro.getRealName())){
            search.setRealName(ro.getRealName());
        }
        List<UserUser> list = findList(search);
        UserBo userBo = new UserBo();
        if(!CollectionUtils.isEmpty(list)){
            BeanUtils.copyProperties(list.get(0), userBo);

        }
        return userBo;
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
    @Transactional
    public void deactivateUser(String userId) throws BusinessException {
        if(StringUtils.isEmpty(userId)){
            throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
        }
        
        UserUser result = this.findById(userId); 
        result.setAuthStatus(UserAuthStatusEnum.DEACTIVATE.getType());

        this.userDao.updateOne(result);
    }

    @Override
    @Transactional
    public void activateUser(String userId) throws BusinessException {
        if(StringUtils.isEmpty(userId)){
            throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
        }
        
        UserUser result = this.findById(userId); 
        result.setAuthStatus(UserAuthStatusEnum.PASS.getType());

        this.userDao.updateOne(result);
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
        mobile = this.getSmsCodeMobile(mobile);
        int smsCount = Integer.valueOf(this.smsCount);//From configuration file
        IRedisKeyEnum iRedisKeyEnum = UserRedisKeyEnums.USER_SESSION_SMS_KEY_COUNT;
        String paramValue = redisUtil.get(iRedisKeyEnum.appendToDefaultKey(mobile + CaptchaUtils.CaptchaType.LOGIN));
        logger.info("sms count{}", paramValue);

//        if (!StringUtils.isEmpty(paramValue) && Integer.valueOf(paramValue) <= smsCount) {
//            this.smsServiceUtil.smsVerifyCode(mobile, ro.getSmsCode());
//        } else {
//            captchaUtils.checkSmsCaptcha(CaptchaUtils.CaptchaType.LOGIN, mobile, ro.getSmsCode());    //校验短信验证码
//        }
    
        mobile = this.getRealIdPhoneNumber(ro.getMobileNumber());        //格式化手机号
        ro.setMobileNumber(this.getRealIdPhoneNumber(ro.getMobileNumber()));        //格式化手机号

        UserUser userObj = this.findUserByMobile(mobile);
        UserLoginBo userLoginBo = new UserLoginBo();

        String userId = "";
        if(userObj == null){    //Register if user is empty
            logger.info("User not found, registering new user with mobile number {}",mobile);
            userId = userService.registerUser(ro);
            //Send message notification
            MessageRo messageRo = new MessageRo();
            messageRo.setUserId(userId);
            messageRo.setMessageTypeEnum(MessageTypeEnum.REGIST_SUCCESS);
            userMessageService.addUserMessage(messageRo);
            userLoginBo.setAuthStatus(0);
        }else{  //Login if user is exist
            if(userObj.getAuthStatus() == UserAuthStatusEnum.DEACTIVATE.getType()){
                throw new BusinessException(UserExceptionEnums.USER_DEACTIVATE);
            }
            else if(userObj.getAuthStatus() == UserAuthStatusEnum.NOT_PASS.getType()){
                throw new BusinessException(UserExceptionEnums.USER_NOT_ACTIVATE);
            }
            else{
                userId = userObj.getId();
                userLoginBo.setAuthStatus(userObj.getAuthStatus());
                userLoginBo.setUsername(userObj.getRealName());
                userLoginBo.setHeadImage(userObj.getHeadImage());
            }
        }

        String sessionId = UuidUtil.create();
        redisUtil.set(BaseRedisKeyEnums.USER_SESSION_KEY.appendToDefaultKey(sessionId), userId);
        userLoginBo.setSessionId(sessionId);
        userLoginBo.setMobileNumber(mobile);
        userLoginBo.setUserId(userId);

//        usrLoginHistoryService.addPcLoginHistory(ro.getClientIp(), userId);   //Add login record
        return userLoginBo;
    }

    @Override
    @Transactional
    public String registerUser(UserRegistRo ro) throws BusinessException {
        UserUser addInfo = new UserUser();
        String mobileNumber = ro.getMobileNumber();

        if(ro.getType() != null){
            if(ro.getType() == 0){       //type == 0　Normal User, other types are student
                addInfo.setUserType(UserTypeEnum.NORMAL_ACCOUNT.getType());
            }else {
//                addInfo.setUserType(UserTypeEnum.STUDENT.getType());
            }
        }else {
            throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
        }

        addInfo.setMobileNumber(mobileNumber);
        addInfo.setMobileNumberDES(DESUtils.encrypt(mobileNumber));
        //Default avatar /MyUpload/img/v/a/o/e53c8034a52244bb9f70b71313f7ea22.png

        SysParamRo sysParamRo = new SysParamRo();
        sysParamRo.setSysKey(payParamContants.USER_HEADIMAGE);

//        String sysValue = sysParamService.sysValueByKey(sysParamRo).getData().getSysValue();
//        logger.info("Defaul Avatar: {}",sysValue);
        addInfo.setHeadImage("/MyUpload/img/v/a/o/e53c8034a52244bb9f70b71313f7ea22.png");
        return this.addOne(addInfo);
    }

    @Override
    public List<UserUser> getAllUserUser() throws BusinessException {
        UserUser search = new UserUser();
        search.setDisabled(0);

        List<UserUser> result = this.userDao.findForList(search);
        for (UserUser user : result){
            user.setPayPwd("");
            user.setMobileNumberDES("");
        }

        return result;
    }

    @Override
    public UserBo userBasicInfo(String userId) throws BusinessException {
        if(StringUtils.isEmpty(userId)){
            throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
        }
        
        UserUser result = this.findById(userId);    //查询用户信息

        logger.info(" result.id: {} - userId: {} auth status：{}", result.getId(), userId, result.getAuthStatus().toString());

        if(result.getAuthStatus() == UserAuthStatusEnum.NOT_PASS.getType()){
            throw new BusinessException(UserExceptionEnums.USER_NOT_ACTIVATE);
        }

        if(result.getAuthStatus() == UserAuthStatusEnum.DEACTIVATE.getType()){
            throw new BusinessException(UserExceptionEnums.USER_DEACTIVATE);
        }
        UserBo response = new UserBo();
        BeanUtils.copyProperties(result,response);

        UserBank search = new UserBank();
        search.setUserUuid(userId);
        UserBank userBank = this.userBankService.findOne(search);   //查询用户银行卡信息
        if(userBank != null){
            response.setBankCardNo(userBank.getBankNumberNo());
            response.setBankName(userBank.getBankName());
            response.setBankCode(userBank.getBankCode());
            response.setMobileNumber(result.getMobileNumber());
        }

        UserAddressDetail address1 = new UserAddressDetail();
        address1.setUserUuid(userId);
        address1.setAddressType(1);
        address1.setDisabled(0);
        UserAddressDetail birthAddressDetail = this.userAddressDetailService.findOne(address1); 

        logger.info(" birthAddressDetail result.id: {} - userId: {}", birthAddressDetail.getId(), userId);

        response.setBirthProvince(birthAddressDetail.getProvince());
        response.setBirthCity(birthAddressDetail.getCity());
        response.setBirthDetailed(birthAddressDetail.getDetailed());

        UserAddressDetail address2 = new UserAddressDetail();
        address2.setUserUuid(userId);
        address2.setAddressType(2);
        address2.setDisabled(0);
        UserAddressDetail liveAddressDetail = this.userAddressDetailService.findOne(address2); 

        logger.info(" liveAddressDetail result.id: {} - userId: {}", liveAddressDetail.getId(), userId);

        response.setLiveProvince(liveAddressDetail.getProvince());
        response.setLiveCity(liveAddressDetail.getCity());
        response.setLiveDetailed(liveAddressDetail.getDetailed());

        return response;
    }

    public UserBo userBasicInfoView(String userId) throws BusinessException {
        if(StringUtils.isEmpty(userId)){
            throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
        }
        
        UserUser result = this.findById(userId);    //查询用户信息

        logger.info(" result.id: {} - userId: {} auth status：{}", result.getId(), userId, result.getAuthStatus().toString());

        UserBo response = new UserBo();
        BeanUtils.copyProperties(result,response);

        UserBank search = new UserBank();
        search.setUserUuid(userId);
        UserBank userBank = this.userBankService.findOne(search);   //查询用户银行卡信息
        if(userBank != null){
            response.setBankCardNo(userBank.getBankNumberNo());
            response.setBankName(userBank.getBankName());
            response.setBankCode(userBank.getBankCode());
            response.setMobileNumber(result.getMobileNumber());
        }

        UserAddressDetail address1 = new UserAddressDetail();
        address1.setUserUuid(userId);
        address1.setAddressType(1);
        address1.setDisabled(0);
        UserAddressDetail birthAddressDetail = this.userAddressDetailService.findOne(address1); 

        logger.info(" birthAddressDetail result.id: {} - userId: {}", birthAddressDetail.getId(), userId);

        response.setBirthProvince(birthAddressDetail.getProvince());
        response.setBirthCity(birthAddressDetail.getCity());
        response.setBirthDetailed(birthAddressDetail.getDetailed());

        UserAddressDetail address2 = new UserAddressDetail();
        address2.setUserUuid(userId);
        address2.setAddressType(2);
        address2.setDisabled(0);
        UserAddressDetail liveAddressDetail = this.userAddressDetailService.findOne(address2); 

        logger.info(" liveAddressDetail result.id: {} - userId: {}", liveAddressDetail.getId(), userId);

        response.setLiveProvince(liveAddressDetail.getProvince());
        response.setLiveCity(liveAddressDetail.getCity());
        response.setLiveDetailed(liveAddressDetail.getDetailed());

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
                UserAuthStatusEnum.PASS.getType() )){
            response.setAuthStatus(1);
        }
        if(!StringUtils.isEmpty(info.getBankCardNo())){
            response.setBankStatus(1);
        }

        return response;
    }

    // rizky add manual verify to replace advance ai
    @Override
    @Transactional
    public Boolean manualVerify(UserNameAuthRo ro) throws BusinessException {
        String idCardNo = ro.getIdCardNo().trim();
        boolean authFlag = false;   // User detail authentication flag

        UserUser checkIDCard = new UserUser();
        checkIDCard.setIdCardNo(idCardNo);
        List<UserUser> idCardResult = this.findList(checkIDCard);

        if (!CollectionUtils.isEmpty(idCardResult)) {
            logger.info("ID Card already exist，id card：{}", idCardNo);
            throw new BusinessException(UserExceptionEnums.IDCARD_DUPLICATE);
        }

        UserUser userUser = updateUserUser(ro, true);

        authFlag = true;

        UserAccount account = new UserAccount();
        account.setUserUuid(ro.getUserId());
        List<UserAccount> userAccounts = userAccountDao.findForList(account);
        if (CollectionUtils.isEmpty(userAccounts)) {
            account.setType(userUser.getUserType());
            userAccountDao.addOne(account);
        } else {
            logger.info("User Already Exist，userAccounts：{}", userAccounts);
        }
        logger.info("Success add account，account：{}", account);

        //Send success message notification
        MessageRo messageRo = new MessageRo();
        messageRo.setUserId(ro.getUserId());
        messageRo.setMessageTypeEnum(MessageTypeEnum.ADVANCE_SUCCESS);

        userMessageService.addUserMessage(messageRo);

        upsertAddress(ro, 1);
        upsertAddress(ro, 2);

        return authFlag;
    }

    private UserUser updateUserUser(UserNameAuthRo ro, Boolean isNewUser) throws BusinessException{
        String idCardNo = ro.getIdCardNo().trim();
        String name = ro.getRealName().trim();  //user name

        UserUser p2pUser = new UserUser();
        p2pUser.setId(ro.getUserId());
        UserUser userUser = this.findOne(p2pUser);     //Query user information

        if (userUser == null || "".equals(userUser.getId())) {
            throw new BusinessException(UserExceptionEnums.USER_NOT_EXIST);  //User ID number already exists
        }

        if(isNewUser){
            //Janhsen: change super investor can register first then will contact by team later for activation
            if (userUser.getAuthStatus() >= UserAuthStatusEnum.NOT_PASS.getType()) {
                throw new BusinessException(UserExceptionEnums.USER_NOT_ACTIVATE);  //User ID number already exists
            }
            userUser.setAuthStatus(UserAuthStatusEnum.NOT_PASS.getType());
        }
        
        userUser.setIdCardImage(ro.getIdCardImage());
        userUser.setSuratKuasaImage(ro.getSuratKuasaImage());

        userUser.setRealName(name);
        userUser.setUserName(name);
        userUser.setIdCardNo(idCardNo);
        userUser.setUpdateTime(new Date());
        
        userUser.setHeadImage("");
        userUser.setAge(ro.getAge());
        userUser.setSex(ro.getSex());
        userUser.setEducation(ro.getEducation());
        userUser.setJob(ro.getJob());
        userUser.setWorkField(ro.getWorkField());
        userUser.setReligion(ro.getReligion());
        userUser.setNpwpNo(ro.getNpwpNo());
        userUser.setYearSalary(ro.getYearSalary());
        userUser.setWorkTime(ro.getWorkTime());
        userUser.setUserType(UserTypeEnum.SUPPER_INVESTORS.getType());
        userUser.setIsinsurance(ro.getIsInsurance());
        userUser.setCompanyName(ro.getCompanyName());
        userUser.setBirthDate(ro.getBirthDate());

        if(ro.getSalaryHomeValue().equals("true")){
            userUser.setSalaryHomeValue("1");
        }
        else {
            userUser.setSalaryHomeValue("0");
        }
        userUser.setOtherSalaryFrom(ro.getOtherSalaryFrom());
        if(!StringUtils.isEmpty(ro.getBankCode()) && !StringUtils.isEmpty(ro.getBankNumberNo()))
        {
            bindBankCard(ro.getBankCode(), ro.getBankNumberNo(), ro.getBankHolderName(), ro.getUserId());
        }
        else if(isNewUser){
            try {
                userUser.setPayPwd(SignUtils.generateMd5(ro.getPayPwd()));
            } catch (Exception e) {
                throw new BusinessException(UserExceptionEnums.REGISTRATION_FAILED);
            }
        }
        this.updateOne(userUser);

        return userUser;
    }

    //address type 1 for live, 2 for company address
    private void upsertAddress(UserNameAuthRo ro, int addressType) throws BusinessException{
        UserAddressDetail addressDetail = new UserAddressDetail();
        addressDetail.setUserUuid(ro.getUserId());
        addressDetail.setAddressType(addressType);
        addressDetail.setDisabled(0);
        UserAddressDetail userAddress = this.userAddressDetailService.findOne(addressDetail);
        if(addressType == 1){
            BirthAddressRo birthAddressRequest = ro.getBirthAddressRo();
            if(userAddress != null && !"".equals(userAddress.getUserUuid())){
                userAddress.setProvince(birthAddressRequest.getBirthProvince());
                userAddress.setCity(birthAddressRequest.getBirthCity());
                userAddressDetailService.updateOne(userAddress);
            }
            else{
                UserAddressDetail userAddressDetail = new UserAddressDetail();
                userAddressDetail.setUserUuid(ro.getUserId());
                userAddressDetail.setAddressType(addressType);    //1 Residential Address
                userAddressDetail.setProvince(birthAddressRequest.getBirthProvince());
                userAddressDetail.setCity(birthAddressRequest.getBirthCity());
                userAddressDetailService.addInfo(userAddressDetail);
            }
        }
        else{
            LiveAddressRo liveAddressRequset = ro.getLiveAddressRo();
            if(userAddress != null && !"".equals(userAddress.getUserUuid())){
                userAddress.setProvince(liveAddressRequset.getLiveProvince());
                userAddress.setCity(liveAddressRequset.getLiveCity());
                userAddress.setDetailed(liveAddressRequset.getLiveDetailed());
                userAddressDetailService.updateOne(userAddress);
            }
            else{
                UserAddressDetail userAddressDetail = new UserAddressDetail();
                userAddressDetail.setUserUuid(ro.getUserId());
                userAddressDetail.setAddressType(addressType);    //2 company Address
                userAddressDetail.setProvince(liveAddressRequset.getLiveProvince());
                userAddressDetail.setCity(liveAddressRequset.getLiveCity());
                userAddressDetail.setDetailed(liveAddressRequset.getLiveDetailed());
                userAddressDetailService.addInfo(userAddressDetail);
            }
        }
    }

    private void bindBankCard(String bankCode, String bankNumber, String cardHolderName, String userId) throws BusinessException{
        logger.info("bankCode: {} bankNumber: {} cardHolderName: {} userId: {}", bankCode, bankNumber, cardHolderName, userId);
        UserBindBankCardRo bankRo = new UserBindBankCardRo();
        bankRo.setBankCode(bankCode);
        bankRo.setBankNumberNo(bankNumber);
        bankRo.setBankHolderName(cardHolderName);
        bankRo.setUserId(userId);
        userBankService.bindBankCard(bankRo);
    }

    @Override
    @Transactional
    public Boolean manualVerifyEdit(UserNameAuthRo ro) throws BusinessException {
        
        boolean authFlag = false;   // User detail authentication flag

        updateUserUser(ro, false);

        authFlag = true;
        
        upsertAddress(ro, 1);
        upsertAddress(ro, 2);

        return authFlag;
    }

    @Override
    @Transactional
    public Boolean manualVerifyEditControl(UserNameAuthRo ro, String userId) throws BusinessException {
        
        boolean authFlag = false;   // User detail authentication flag

        ro.setUserId(userId);
        updateUserUser(ro, false);

        authFlag = true;
        
        upsertAddress(ro, 1);
        upsertAddress(ro, 2);

        return authFlag;
    }

    //ahalim: Remove advance.ai
//     @Override
//     @Transactional
//     public Boolean advanceVerify(UserNameAuthRo ro) throws BusinessException {
//         //UserUser userSearch = new UserUser();
//         String idCardNo = ro.getIdCardNo().trim();
//         String name = ro.getRealName().trim();  //用户姓名
//         Boolean authFlag = false;   //实名认证成功标识

//         if(redisUtil.tryLock(ro, 1800)){
//             UserUser p2pUser = new UserUser();
//             p2pUser.setId(ro.getUserId());
//             UserUser userUser = this.findOne(p2pUser);     //Query user information
//             //Determine whether it is a super investor
//             if(UserTypeEnum.SUPPER_INVESTORS.getType() != userUser.getUserType()){
//                 throw new BusinessException(UserExceptionEnums.USER_NOT_SUPPER_INVESTORS);  //Not our user this time, can not be real-name / purchase
//             }
//             if(userUser.getAuthStatus() >= UserAuthStatusEnum.NOT_PASS.getType() && userUser.getAuthStatus() <= UserAuthStatusEnum.MANAGE_PASS.getType()){
//                 throw new BusinessException(UserExceptionEnums.USER_IDCARD_EXIST);  //用户身份证号已存在
//             }

//             BeanUtils.copyProperties(ro,userUser);
//             OpenApiClient client = new OpenApiClient(advanceConfig.getApiHost(), advanceConfig.getAccessKey(), advanceConfig.getSecretKey());
//             Map<String, String> identityCheck = new HashMap<>();
//             identityCheck.put("name", name);
//             identityCheck.put("idNumber", idCardNo);

//             String response = client.request(
//                     advanceConfig.getIdentityCheckApi(), JSON.toJSONString(identityCheck));
//             AdvanceRo advanceResponse = JSON.parseObject(response, AdvanceRo.class);

//             userUser.setIdCardImage(ro.getIdCardImage());

//             //发送消息通知
//             MessageRo messageRo = new MessageRo();
//             messageRo.setUserId(ro.getUserId());

//             if (advanceResponse.getCode().equals("SUCCESS")){
//                 logger.info("Real-name authentication results:{}", response);
//                 IdentityCheckResultRo data = JSON.parseObject(advanceResponse.getData(), IdentityCheckResultRo.class);
//                 if (!StringUtils.isEmpty(data.getIdNumber()) && !StringUtils.isEmpty(data.getName())) {

//                     userUser.setRealName(data.getName());
//                     userUser.setUserName(data.getName());
//                     userUser.setIdCardNo(data.getIdNumber());
//                     userUser.setUpdateTime(new Date());
//                     userUser.setAuthStatus(UserAuthStatusEnum.PASS.getType());
//                     userUser.setHeadImage("");
//                     this.updateOne(userUser);    //更新用户信息

//                     authFlag = true;
//                     //创建账户
//                     UserAccount account = new UserAccount();
//                     account.setUserUuid(ro.getUserId());
//                     List<UserAccount> userAccounts = userAccountDao.findForList(account);
//                     if(CollectionUtils.isEmpty(userAccounts)){
//                         account.setType(userUser.getUserType());
//                         userAccountDao.addOne(account);
//                     }else {
//                         logger.info("实名认证通过添加账户，账户已存在，userAccounts：{}",userAccounts);
//                     }
//                     logger.info("实名认证通过添加的账户成功，account：{}",account);
//                     //发送实名认证成功消息通知
//                     messageRo.setMessageTypeEnum(MessageTypeEnum.ADVANCE_SUCCESS);

//                 }
//             }else {     //将用户放入后台实名失败审核列表
//                 userUser.setRealName(ro.getRealName());
//                 userUser.setUserName(ro.getRealName());
//                 userUser.setAuthStatus(UserAuthStatusEnum.NOT_PASS.getType());
//                 userUser.setUpdateTime(new Date());
//                 userUser.setHeadImage("");
//                 this.updateOne(userUser);
//                 //发送实名认证审核中消息通知
//                 messageRo.setMessageTypeEnum(MessageTypeEnum.ADVANCE_AUDIT);
//             }

//             userMessageService.addUserMessage(messageRo);

//             UserAddressDetail userAddressDetail = new UserAddressDetail();
//             BirthAddressRo birthAddressRequest = ro.getBirthAddressRo();
//             userAddressDetail.setUserUuid(ro.getUserId());
//             userAddressDetail.setAddressType(1);    //1居住地址
//             userAddressDetail.setProvince(birthAddressRequest.getBirthProvince());
//             userAddressDetail.setCity(birthAddressRequest.getBirthCity());
// //            userAddressDetail.setBigDirect(birthAddressRequest.getBirthBigDirect());
// //            userAddressDetail.setSmallDirect(birthAddressRequest.getBirthSmallDirect());
//             userAddressDetailService.addInfo(userAddressDetail);

//             UserAddressDetail companyAddress = new UserAddressDetail();
//             LiveAddressRo liveAddressRequset = ro.getLiveAddressRo();
//             companyAddress.setUserUuid(ro.getUserId());
//             companyAddress.setAddressType(2);    //2公司地址
//             companyAddress.setProvince(liveAddressRequset.getLiveProvince());
//             companyAddress.setCity(liveAddressRequset.getLiveCity());
// //            companyAddress.setBigDirect(liveAddressRequset.getLiveBigDirect());
// //            companyAddress.setSmallDirect(liveAddressRequset.getLiveSmallDirect());
//             companyAddress.setDetailed(liveAddressRequset.getLiveDetailed());
//             userAddressDetailService.addInfo(companyAddress);


//         }else {
//             throw new BusinessException(UserExceptionEnums.REPEAT_SUBMIT);
//         }

//         return authFlag;
//     }

    public UserAccountBo userListByType(UserTypeSearchRo ro) throws BusinessException {
        Integer userType = ro.getUserType();
        if (userType == null || userType == 0) {  //排除普通用户
            throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
        }

        List<UserUser> response = new ArrayList<>();
//                List<UserUser> response = this.userDao.getUserByTypeAndBankCode(ro.getUserType(), ro.getBankType());    //根据 用户类型 和 银行类型 查询单一的账户id

        UserBank userBankF = new UserBank();
        UserUser userUser = new UserUser();
        userUser.setUserType(ro.getUserType());
        userUser.setDisabled(0);
        response = this.userDao.findForList(userUser);
        for (UserUser user:response){
            UserBank userBank = new UserBank();
            userBank.setDisabled(0);
            userBank.setUserUuid(user.getId());
            userBank.setBankCode(ro.getBankType());
            userBank = this.userBankDao.findOne(userBank);
            if (userBank != null){
                userBankF = userBank;
            }
        }


//        if (response != null) {
//            userUser = response.get(0);
//        }

        UserAccount account = new UserAccount();
        account.setUserUuid(userBankF.getUserUuid());
        account.setDisabled(0);
        account = this.userAccountDao.findOne(account);

        return BeanCoypUtil.copyToNewObject(account, UserAccountBo.class);

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

        if(StringUtils.isEmpty(ro.getPass())){
            entity.setAuthStatus(UserAuthStatusEnum.NOT_PASS.getType());
        }else {
            entity.setAuthStatus(ro.getPass());
        }

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
        //发送消息通知
        MessageRo messageRo = new MessageRo();
        messageRo.setUserId(userId);

        UserBank entity = new UserBank();
        entity.setUserUuid(userId);
        List<UserBank> forList = userBankDao.findForList(entity);
        UserBank userBank=null;
        if(!CollectionUtils.isEmpty(forList)){
            userBank = forList.get(0);
        }
        if(pass == 0){
            result.setAuthStatus(UserAuthStatusEnum.MANAGE_PASS.getType());
            messageRo.setMessageTypeEnum(MessageTypeEnum.ADVANCE_SUCCESS);

            userBank.setStatus(UserBankCardBinEnum.SUCCESS.getType());

            //创建账户
            UserAccount account = new UserAccount();
            account.setUserUuid(userId);
            List<UserAccount> userAccounts = userAccountDao.findForList(account);
            if(CollectionUtils.isEmpty(userAccounts)){
                account.setType(result.getUserType());
                userAccountDao.addOne(account);
            }else {
                logger.info("人工审核实名认证通过添加账户,账户已存在，userAccounts：{}",userAccounts);
            }
            logger.info("人工审核实名认证通过添加的账户成功，account：{}",account);

        }else {
            result.setAuthStatus(UserAuthStatusEnum.REFUSE.getType());
            result.setCause(ro.getCause());
            messageRo.setMessageTypeEnum(MessageTypeEnum.ADVANCE_DEFEATED);

            userBank.setStatus(UserBankCardBinEnum.PENDING.getType());
        }
        userBankDao.updateOne(userBank);

        userMessageService.addUserMessage(messageRo);
        result.setRemark(ro.getRemark());
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

        //this.userAccountService.addUserAccountCurrent(ro.getUserUuid(), new BigDecimal(ro.getAmount()));
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

    @Override
    @Transactional
    public void resetPayPassword(PayPasswordRo ro)throws Exception {
        UserUser userUser = new UserUser();
        userUser.setId(ro.getUserId());
        List<UserUser> userList = this.userDao.findForList(userUser);
        if (CollectionUtils.isEmpty(userList)) {
            throw new BusinessException(UserExceptionEnums.USER_NOT_EXIST);
        }
        userUser = userList.get(0);
        String mobileNumber = userUser.getMobileNumber();
        mobileNumber = this.getSmsCodeMobile(mobileNumber);
        //校验短信验证码
        captchaUtils.checkSmsCaptcha(CaptchaUtils.CaptchaType.PASSWORD_REST, mobileNumber, ro.getSmsCode());

        userUser.setPayPwd(SignUtils.generateMd5(ro.getPayPwd()));
        userUser.setUpdateTime(new Date());
       try{
           this.userDao.updateOne(userUser);
       }catch (Exception e){
           throw new BusinessException(UserExceptionEnums.USER_RESET_PAY_PASSWORD_ERROR);
       }
    }

    @Override
    @Transactional
    public void uploadHeadImage(UserNameAuthRo ro) throws BusinessException {
        UserUser userUser = userDao.findOne(ro.getUserId());
        if(!StringUtils.isEmpty(ro.getHeadImage()) && userUser!=null){
            userUser.setHeadImage(ro.getHeadImage());
            userDao.updateOne(userUser);
        }else {
            throw new BusinessException(UserExceptionEnums.USER_RESET_UPLOADHEAD_ERROR);
        }
    }

    @Override
    @Transactional
    public void addSuperInvestor(UserReq ro) throws BusinessException {


        SysParamRo sysParamRo = new SysParamRo();
        sysParamRo.setSysKey(payParamContants.USER_REFUND_PASSWORD_KEY);

        String sysValue = sysParamService.sysValueByKey(sysParamRo).getData().getSysValue();

        if(!sysValue.equals(ro.getOpcode())){
            throw new BusinessException(UserExceptionEnums.USER_RESET_OPCODE_ERROR);
        }

//        IRedisKeyEnum iRedisKeyEnum = UserRedisKeyEnums.USER_REFUND_PASSWORD_KEY;
//        String paramValue = redisUtil.get(iRedisKeyEnum);
//        if(!paramValue.equals(ro.getOpcode())){
//            throw new BusinessException(UserExceptionEnums.USER_RESET_OPCODE_ERROR);
//        }

        UserUser user = new UserUser();
        String mobile = ro.getMobileNumber();
        mobile = this.getRealIdPhoneNumber(mobile);
        user.setMobileNumber(mobile);
        UserUser userUser = userDao.findOne(user);
        if(userUser!=null){
            userUser.setUserType(UserTypeEnum.SUPPER_INVESTORS.getType());
            UserUser entity = new UserUser();
            BeanUtils.copyProperties(userUser,entity);
            userDao.updateOne(entity);
        }else {
            throw new BusinessException(UserExceptionEnums.USER_NOT_EXIST);
        }

    }

    @Override
    @Transactional
    public void addCompanyInvestor(UserReq ro) throws Exception {

        SysParamRo sysParamRo = new SysParamRo();
        sysParamRo.setSysKey(payParamContants.USER_REFUND_PASSWORD_KEY);

        String sysValue = sysParamService.sysValueByKey(sysParamRo).getData().getSysValue();

        if(!sysValue.equals(ro.getOpcode())){
            throw new BusinessException(UserExceptionEnums.USER_RESET_OPCODE_ERROR);
        }

//        IRedisKeyEnum iRedisKeyEnum = UserRedisKeyEnums.USER_REFUND_PASSWORD_KEY;
//        String paramValue = redisUtil.get(iRedisKeyEnum);
//        if(!paramValue.equals(ro.getOpcode())){
//            throw new BusinessException(UserExceptionEnums.USER_RESET_OPCODE_ERROR);
//        }
        UserUser user = new UserUser();
        String mobile = ro.getMobileNumber();
        mobile = this.getRealIdPhoneNumber(mobile);
        user.setMobileNumber(mobile);
        user.setMobileNumberDES(DESUtils.encrypt(mobile));
        UserUser userUser = userDao.findOne(user);
        if(userUser!=null){
            throw new BusinessException(UserExceptionEnums.USER_YES_EXIST);
        }else {
            user.setUserType(UserTypeEnum.BRANCH_INVESTORS.getType());
            user.setCompanyName(ro.getCompanyName());
            String userPwd = SensitiveInfoUtils.genRandomNum(8);    /*生成随机密码*/
            user.setPayPwd(SignUtils.generateMd5(userPwd));
            user.setAuthStatus(UserAuthStatusEnum.PASS.getType());
            if(!StringUtils.isEmpty(ro.getWithholding()) && "1".equals(ro.getWithholding())){
                //是否支持代扣 1.是  0.否
                user.setWithholding(1);
            }
            userDao.addOne(user);
            logger.info("添加机构投资人成功，user：{}",user);
            UserBankRo bankRo = new UserBankRo();
            bankRo.setUserUuid(user.getId());
            bankRo.setBankCode(ro.getBankCode());
            bankRo.setBankNumberNo(ro.getBankNumberNo());
            if(redisUtil.tryLock(bankRo,1000)){
                String userId = bankRo.getUserUuid();
                if(StringUtils.isEmpty(userId)){
                    throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
                }

                UserUser userInfo = userService.findById(userId);   //查询用户信息
                if(userInfo ==null){
                    throw new BusinessException(UserExceptionEnums.USER_NOT_EXIST);
                }

                UserBank search = new UserBank();
                search.setUserUuid(userId);
                UserBank userBank = this.userBankDao.findOne(search);   //查询用户是否已经绑卡
                if(userBank == null){   //未绑卡
                    SysBankBasicInfoRo sysBankBasicInfoRo = new SysBankBasicInfoRo();
                    sysBankBasicInfoRo.setBankCode(bankRo.getBankCode());
                    sysBankBasicInfoRo.setUserId(user.getId());
                    String sessionId = UuidUtil.create();
                    redisUtil.set(BaseRedisKeyEnums.USER_SESSION_KEY.appendToDefaultKey(sessionId), user.getId());
                    sysBankBasicInfoRo.setSessionId(sessionId);
                    //通过银行code查询银行信息
                    BaseResponse<SysBankBasicInfoBo> bankResponse = sysBankBasicThirdService.bankInfoByCode(sysBankBasicInfoRo);
                    if(bankResponse ==null || !bankResponse.isSuccess() || bankResponse.getCode() != 0){
                        logger.error("未查询到对应code的银行卡信息");
                        throw new BusinessException(BaseExceptionEnums.SERVICE_CALL_ERROR);
                    }
                    UserBank cardSearch = new UserBank();
                    cardSearch.setUserUuid(userInfo.getId());
                    Sort sort = new Sort(Sort.Direction.DESC, UserBank.sort_Field);
                    List<UserBank> userBankList = this.userBankDao.findForList(cardSearch, sort);   //查询最新的一条绑卡记录的bankOrder
                    Integer maxOrder = 0;
                    if(!CollectionUtils.isEmpty(userBankList)){
                        maxOrder = userBankList.get(0).getBankorder() + 1;
                    }
                    UserBindBankCardRo bindBankCardRo = new UserBindBankCardRo();
                    bindBankCardRo.setBankCode(ro.getBankCode());
                    bindBankCardRo.setBankNumberNo(ro.getBankNumberNo());
                    this.userBankService.addUserBankCard(userInfo, bindBankCardRo,bankResponse.getData(), maxOrder);

                }else {     //银行卡已存在
                    if (userBank.getStatus() == UserBankCardBinEnum.SUCCESS.getType() || userBank.getStatus() == UserBankCardBinEnum.PENDING.getType()
                            || userBank.getStatus() == UserBankCardBinEnum.NOT.getType()) {
                        throw new BusinessException(UserExceptionEnums.USER_BANKCARD_EXIST);
                    }
                    if(userBank.getStatus() == UserBankCardBinEnum.FAILED.getType()){
                        throw new BusinessException(UserExceptionEnums.USER_BINDCARD_ERROR);
                    }
                }
            }else {
                throw new BusinessException(UserExceptionEnums.REPEAT_SUBMIT);
            }

            //查看添加后的机构投资人
            UserUser userInvestor = new UserUser();
            userInvestor.setMobileNumber(mobile);
            userInvestor.setMobileNumberDES(DESUtils.encrypt(mobile));
            UserUser userA = userDao.findOne(userInvestor);

            if(userA!=null){

                UserAccount account = new UserAccount();
                account.setUserUuid(userA.getId());
                List<UserAccount> userAccounts = userAccountDao.findForList(account);
                if(CollectionUtils.isEmpty(userAccounts)){
                    account.setType(userA.getUserType());
                    userAccountDao.addOne(account);
                    logger.info("添加机构投资人的账户成功，account：{}",account);
                }else {
                    logger.info("添加机构投资人的账户,账户已存在，userAccounts：{}",userAccounts);
                }
            }else {
                throw new BusinessException(UserExceptionEnums.COMPANY_INVESTOR_ERROR);
            }

            String investormobile = this.getSmsCodeMobile(ro.getMobileNumber());

            String content = "Password transaksi adalah : "+userPwd+",Mohon simpan baik-baik。";

            logger.info("发送短信给:{},短信内容:{}",
                    investormobile, content);
            this.smsServiceUtil.sendTypeSmsCode("LOGIN", investormobile, content);
        }

    }



}