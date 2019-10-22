package com.yqg.system.service.syssms;

import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.SmsServiceUtil;
import com.yqg.common.sender.IContentTemplate;
import com.yqg.common.sender.ISmsSender;
import com.yqg.system.entity.SysSmsMessageInfo;
import com.yqg.system.service.syssms.impl.SysSmsMessageServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CommonSmsSender implements ISmsSender {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SmsServiceUtil smsServiceUtil;
    @Autowired
    private SysSmsMessageServiceImpl sysSmsMessageService;


    @Override
    @Transactional
    public void send(String mobileNumber, IContentTemplate template, String... params) {

        String content = template.getContent(params);
        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher smsCode = p.matcher(content);
        String trim = smsCode.replaceAll("").trim();
        logger.info("发送短信给:{},短信模板名称:{},模板编号:{}。短信内容:{}",
                mobileNumber, template.getTemplateName(), template.getTemplateId(), content);
        this.smsServiceUtil.sendTypeSmsCode("LOGIN", mobileNumber, content);
        SysSmsMessageInfo sysSmsMessageInfo = new SysSmsMessageInfo();
        if(!StringUtils.isEmpty(template)){
            if("LOGIN_TEMP".equals(template.getTemplateName())){
                sysSmsMessageInfo.setSmsType(2);
            }else if("PAYPSSWORD_RESET_TEMP".equals(template.getTemplateName())){
                sysSmsMessageInfo.setSmsType(3);
            }
        }
        sysSmsMessageInfo.setSmsConent(content);
        sysSmsMessageInfo.setMobile(mobileNumber);
        sysSmsMessageInfo.setSmsCode(trim);
        try {
            sysSmsMessageService.addOne(sysSmsMessageInfo);
            logger.info("添加短信日志信息：{}",sysSmsMessageInfo);
        } catch (BusinessException e) {
            logger.info("短信信息内容：{}",sysSmsMessageInfo);
            e.printStackTrace();
        }
    }
}
