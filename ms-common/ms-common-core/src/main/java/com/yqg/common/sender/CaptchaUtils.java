package com.yqg.common.sender;

import com.yqg.common.config.CommonConfig;
import com.yqg.common.context.ApplicationContextProvider;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.redis.BaseRedisKeyEnums;
import com.yqg.common.redis.RedisUtil;
import com.yqg.common.utils.UuidUtil;
import com.yqg.common.exceptions.BaseExceptionEnums;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 验证码工具类
 * Created by gao on 2018/6/22.
 */
@Component
public class CaptchaUtils {
    /**
     * 日志对象
     */
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 缓存工具
     */
    @Autowired
    RedisUtil redisUtil;

    @Autowired
    CommonConfig commonConfig;

    /**
     * 验证码类型
     */
    public enum CaptchaType {
        //图片、注册、登录、重置密码
        REGISTER, LOGIN, PASSWORD_REST
    }

    /**
     * 验证码类型
     */
    public enum ImgCaptchaInfoType {
        //图片
        IMG_BASE64, IMG_SESSION
    }

    /**
     * 获取图片验证码
     *
     * @return
     * @throws BusinessException
     */
    public Map<ImgCaptchaInfoType, String> getImgCaptcha() throws BusinessException {
        Map<ImgCaptchaInfoType, String> imgCaptcha = new HashMap<>();
        try {
            String[] imgCaptchaInfo = ImgCaptchaUtil.generateImgCode();
            imgCaptcha.put(ImgCaptchaInfoType.IMG_BASE64, imgCaptchaInfo[0]);
            String imgSessionId = UuidUtil.create();
            imgCaptcha.put(ImgCaptchaInfoType.IMG_SESSION, imgSessionId);
            redisUtil.set(BaseRedisKeyEnums.CAPTCHA_IMAGE_KEY.appendToDefaultKey(imgSessionId), imgCaptchaInfo[1]);
            logger.debug("Image SessionId:{},Image code:{}", imgSessionId, imgCaptchaInfo[1]);
        } catch (IOException e) {
            logger.error("Image verification code generation failed, Exception: {}", e);
            throw new BusinessException(BaseExceptionEnums.SYSTERM_ERROR);
        }
        return imgCaptcha;
    }

    /**
     * Validate image captcha
     *
     * @param imgSessionId
     * @param imgCaptchaReceived
     * @throws BusinessException
     */
    public void checkImgCaptcha(String imgSessionId, String imgCaptchaReceived) throws BusinessException {
        if (!StringUtils.isEmpty(imgCaptchaReceived)) {
            BaseRedisKeyEnums keyEnums = BaseRedisKeyEnums.CAPTCHA_IMAGE_KEY.appendToDefaultKey(imgSessionId);
            String imgCaptchaSaved = redisUtil.get(keyEnums);
            if (!imgCaptchaReceived.equals(imgCaptchaSaved)) {
                //Verification failed
                throw new BusinessException(BaseExceptionEnums.CPPCHA_IMG_ERROR);
            } else {
                //Verification success, Delete from redis
                redisUtil.delete(keyEnums);
            }
        } else {
            throw new BusinessException(BaseExceptionEnums.CPPCHA_IMG_ERROR);
        }
    }

    /**
     * 发送方式
     */
    enum SendType {
        //短信、邮件
        SMS, EMAIL
    }

    /**
     * 发送短信验证码
     *
     * @param smsSender       发送器
     * @param captchaType     验证码类型
     * @param contentTemplate 短信内容模板
     * @param mobileNumber    用户手机号
     */
    public void sendSmsCaptcha(ISmsSender smsSender, CaptchaType captchaType, IContentTemplate contentTemplate, String mobileNumber) {
        String captcha = this.createCaptcha(SendType.SMS, captchaType, mobileNumber);
        if (!ApplicationContextProvider.isProdProfile() && !commonConfig.isSendSmsCaptcha()) {
            logger.info("Non-production environment and configured not to send SMS, skip sending SMS to {}",mobileNumber);
            return;
        }
        smsSender.send(mobileNumber, contentTemplate, captcha);
    }

    /**
     * Validate SMS verification code
     *
     * @param captchaType     验证码类型
     * @param mobileNumber    用户手机号
     * @param captchaReceived 收到的短信验证码
     */
    public void checkSmsCaptcha(CaptchaType captchaType, String mobileNumber, String captchaReceived) throws BusinessException {
        //ahalim: Doesn't need to check sms when validating captcha
        // if (!ApplicationContextProvider.isProdProfile() && !commonConfig.isSendSmsCaptcha()) {
        //     logger.info("Non-production environment and configured not to send verification code, skip verification");
        //     return;
        // }

        this.checkCaptcha(SendType.SMS, captchaType, mobileNumber, captchaReceived);
    }

    public void sendEmailCaptcha() {
        //TODO 邮件验证码
    }

    public void checkEmailCaptcha() {
        //TODO 邮件验证码
    }

    /**
     * Generate captcha for SMS verification
     *
     * @return
     */
    private String createNumberCaptcha() {
        Random random = new Random();
        StringBuilder captcha = new StringBuilder();

        for (int i = 0; i < commonConfig.getCaptchaLength(); i++) {
            captcha.append(random.nextInt(10));
        }
        return captcha.toString();
    }

    /**
     * Generate SMS verification code
     *
     * @param captchaType
     * @param userInfo
     */
    public String createCaptcha(SendType sendType, CaptchaType captchaType, String userInfo) {
        String captcha = this.createNumberCaptcha();
        switch (captchaType) {
            case REGISTER:
                redisUtil.set(BaseRedisKeyEnums.CAPTCHA_REGISTER_KEY.
                        appendToDefaultKey(sendType.name()).appendToCurrentKey(userInfo), captcha);
                break;
            case LOGIN:
                redisUtil.set(BaseRedisKeyEnums.CAPTCHA_LOGIN_KEY.
                        appendToDefaultKey(sendType.name()).appendToCurrentKey(userInfo), captcha);
                break;
            case PASSWORD_REST:
                redisUtil.set(BaseRedisKeyEnums.CAPTCHA_PASSWORD_REST_KEY.
                        appendToDefaultKey(sendType.name()).appendToCurrentKey(userInfo), captcha);
                break;
        }
        return captcha;
    }

    /**
     * Validate SMS verification code
     *
     * @param captchaType
     * @param userInfo
     * @param captchaReceived
     * @return
     */
    public void checkCaptcha(SendType sendType, CaptchaType captchaType, String userInfo, String captchaReceived) throws BusinessException {

        if (!StringUtils.isEmpty(captchaReceived)) {
            BaseRedisKeyEnums captchaSentKey = null;
            String captchaSent = "";
            switch (captchaType) {
                case REGISTER:
                    captchaSentKey = BaseRedisKeyEnums.
                            CAPTCHA_REGISTER_KEY.appendToDefaultKey(sendType.name()).appendToCurrentKey(userInfo);
                    break;
                case LOGIN:
                    captchaSentKey = BaseRedisKeyEnums.
                            CAPTCHA_LOGIN_KEY.appendToDefaultKey(sendType.name()).appendToCurrentKey(userInfo);
                    break;
                case PASSWORD_REST:
                    captchaSentKey = BaseRedisKeyEnums.
                            CAPTCHA_PASSWORD_REST_KEY.appendToDefaultKey(sendType.name()).appendToCurrentKey(userInfo);
                    break;
            }
            if (captchaSentKey != null) {
                captchaSent = redisUtil.get(captchaSentKey);
            }
            if (!captchaReceived.equals(captchaSent)) {
                //Verification failed
                throw new BusinessException(BaseExceptionEnums.CPPCHA_ERROR);
            } else {
                //Verification success, Delete from redis
                redisUtil.delete(captchaSentKey);
            }
        } else {
            throw new BusinessException(BaseExceptionEnums.CPPCHA_ERROR);
        }
    }


}
