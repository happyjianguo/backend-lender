package com.yqg.system.enums;

import com.yqg.common.exceptions.IExceptionEnum;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;

/**
 * Created by Lixiangjun on 2019/5/14.
 */
public enum SystemExceptionEnums implements IExceptionEnum {
    REPEAT_SUBMIT(6001, "", "Jangan melakukan pembayaran ulang"),    //请勿重复提交
    MANAGE_EDIT_SYS_ROLE_ERROR(6002,"","修改系统角色失败"),
    MOBILE_NOT_EXIST(6003,"Silakan masukkan nomor ponsel Anda.","请输入手机号"),

    ;
    private int code;
    private String message;
    private String messageI18n;
    private String temlate;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getMessageI18n() {
        return this.messageI18n;
    }

    private SystemExceptionEnums(int code, String message, String messageI18n) {
        this.code = code;
        this.message = message;
        this.messageI18n = messageI18n;
    }

    public SystemExceptionEnums setCustomMessage(String message) {
        this.message = message;
        return this;
    }

    public SystemExceptionEnums setCustomMessage(String message, String messageI18n) {
        this.message = message;
        this.messageI18n = messageI18n;
        return this;
    }

    private SystemExceptionEnums(int code, String message, String messageI18n, String template) {
        this.code = code;
        this.message = message;
        this.messageI18n = messageI18n;
        this.temlate = template;
    }

    public SystemExceptionEnums setDynamicMessage(String... ags) {
        String dynamicMsg = MessageFormat.format(this.temlate, ags);
        String dynamicMsgI18n = MessageFormat.format(this.temlate, ags);
        if(!StringUtils.isEmpty(dynamicMsg)) {
            this.message = dynamicMsg;
        }

        if(!StringUtils.isEmpty(dynamicMsgI18n)) {
            this.messageI18n = dynamicMsgI18n;
        }

        return this;
    }
}
