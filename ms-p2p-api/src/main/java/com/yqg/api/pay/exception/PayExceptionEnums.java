package com.yqg.api.pay.exception;

import com.yqg.common.exceptions.IExceptionEnum;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;

public enum PayExceptionEnums implements IExceptionEnum {

    USER_NOT_REALNAME(10001,"","Silahkan sertifikasi nama lengkap anda"),      //未实名

    USER_REALNAME_ING(10009,"认证中","认证中"),      //认证中
    USER_NOT_BINDCARD(10002,"","Silahkan masukkan informasi kartu bank anda"), //未绑卡
    BUY_ONBORROW(10003,"","Anda memiliki pinjaman yang sedang diproses di Do-It, sementara tidak bisa melakukan pembelian"),   //do-it有进行中的借款
    //超出最大投资金额,无法投资,如有疑问请联系客服
    BUY_OVERTOP(10004,"","Jumlah nominal melebihi limit maksimum , sementara tidak bisa investasi. Jika menemui kendala, bisa menghubungi customer service kami"),
    BUY_CREDITORIN_IS_NULL(10005,"","Kredit tidak ada"),   //债权不存在
    BUY_OVER_CURRENT(10006,"","Jumlah nominal melebihi limit maksimum, investasi gagal. Silahkan perbaharui kembali"),     //超出最大投资金额,投资失败，请重试
    CAN_NOT_BUY(10009,"","Nama anda tidak ada dalam daftar pembelian"),     //您不在系统可购买名单中
    //学生不允许购买
    STUDENT_CANNOT_BUY(10007,"","Anda tidak dapat melakukan pembelian untuk sementara waktu. Jika menemui kendal, bisa menghubungi customer service kami"),
    ACCOUNT_ERROR(10008,"","账户操作错误，请重试")
    ;


    private int code;
    private String message;
    private String messageI18n;
    private String temlate;

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getMessageI18n() {
        return this.messageI18n;
    }

    private PayExceptionEnums(int code, String message, String messageI18n) {
        this.code = code;
        this.message = message;
        this.messageI18n = messageI18n;
    }

    public PayExceptionEnums setCustomMessage(String message) {
        this.message = message;
        return this;
    }

    public PayExceptionEnums setCustomMessage(String message, String messageI18n) {
        this.message = message;
        this.messageI18n = messageI18n;
        return this;
    }

    private PayExceptionEnums(int code, String message, String messageI18n, String template) {
        this.code = code;
        this.message = message;
        this.messageI18n = messageI18n;
        this.temlate = template;
    }

    public PayExceptionEnums setDynamicMessage(String... ags) {
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
