package com.yqg.system.service.syssms;

import com.yqg.common.sender.IContentTemplate;

import java.text.MessageFormat;

/**
 * 内容模板
 * Created by gao on 2018/6/23.
 */
public enum SmsContentTemplate implements IContentTemplate {

    //TODO 短信模板存储在数据库中
    /**
     * 注册验证码
     */
    REGIST_TEMP("4253", "注册验证码 {0}，若非本人操作，请勿泄露!"),
    /**
     * 登录验证码
     */
    LOGIN_TEMP("4254", "Kode Verifikasi {0} ,jika anda tidak melakukannya,jangan disebarkan"),

//    LOGIN_TEMP("4254", "登录验证码 {0}，若非本人操作，请勿泄露!"),
    /*
     * 密码重置验证码
     * 您正在重置密码,验证码 {0}，若非本人操作，请勿泄露
     */
    PAYPSSWORD_RESET_TEMP("4255", "Kode verifikasi SMS Anda adalah {0}. Anda sedang mengatur ulang kata sandi menggunakan nomor telepon. Jika Anda tidak melakukannya, abaikan pesanini!");

    private String templateId;//模板id
    private String template;//内容模板

    @Override
    public String getContent(String... params) {
        if (params != null && params.length > 0) {
            return MessageFormat.format(this.template, params);
        } else {
            return this.template;
        }
    }
    @Override
    public String getTemplateName() {
        return this.name();
    }
    @Override
    public String getTemplateId() {
        return templateId;
    }


    SmsContentTemplate(String templateId, String template) {
        this.templateId = templateId;
        this.template = template;
    }

}
