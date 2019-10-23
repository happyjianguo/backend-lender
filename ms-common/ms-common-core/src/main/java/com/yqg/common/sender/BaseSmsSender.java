package com.yqg.common.sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 基础短信发送器
 * Created by gao on 2018/6/23.
 */
@Component
public class BaseSmsSender implements ISmsSender {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void send(String mobileNumber, IContentTemplate template, String... params) {

        String content = template.getContent(params);

        logger.info("发送短信给:{},短信模板名称:{},模板编号:{}。短信内容:{}",
                mobileNumber, template.getTemplateName(), template.getTemplateId(), content);
    }
}
