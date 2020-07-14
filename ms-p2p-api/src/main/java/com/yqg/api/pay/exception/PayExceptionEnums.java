package com.yqg.api.pay.exception;

import com.yqg.common.exceptions.IExceptionEnum;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;

public enum PayExceptionEnums implements IExceptionEnum {

    USER_NOT_REALNAME(10001,"Silahkan lengkapi informasi akun anda","Silahkan lengkapi informasi akun anda"),      //未实名

    USER_REALNAME_ING(10009,"Verifikasi Bank Gagal","认证中"),      //认证中
    USER_NOT_BINDCARD(10002,"Silahkan lengkapi informasi akun anda","Silahkan masukkan informasi kartu bank anda"), //未绑卡
    BUY_ONBORROW(10003,"Anda memiliki pinjaman yang sedang diproses di Do-It, sementara tidak bisa melakukan pembelian","Anda memiliki pinjaman yang sedang diproses di Do-It, sementara tidak bisa melakukan pembelian"),   //do-it有进行中的借款
    //超出最大投资金额,无法投资,如有疑问请联系客服
    BUY_OVERTOP(10004,"Jumlah nominal melebihi limit maksimum , sementara tidak bisa investasi. Jika menemui kendala, bisa menghubungi customer service kami","Jumlah nominal melebihi limit maksimum , sementara tidak bisa investasi. Jika menemui kendala, bisa menghubungi customer service kami"),
    BUY_CREDITORIN_IS_NULL(10005,"Jumlah kredit yang anda miliki tidak mencukupi","Jumlah kredit yang anda miliki tidak mencukupi"),   //债权不存在
    BUY_OVER_CURRENT(10006,"Jumlah nominal melebihi limit maksimum, investasi gagal. Silahkan perbaharui kembali","Jumlah nominal melebihi limit maksimum, investasi gagal. Silahkan perbaharui kembali"),     //超出最大投资金额,投资失败，请重试
    CAN_NOT_BUY(10009,"Nama anda tidak ada dalam daftar pembelian","Nama anda tidak ada dalam daftar pembelian"),     //您不在系统可购买名单中
    //学生不允许购买
    STUDENT_CANNOT_BUY(10007,"Anda tidak dapat melakukan pembelian untuk sementara waktu. Jika menemui kendala, bisa menghubungi customer service kami","Anda tidak dapat melakukan pembelian untuk sementara waktu. Jika menemui kendala, bisa menghubungi customer service kami"),
    ACCOUNT_ERROR(10008,"Gagal,Akun bermasalah","账户操作错误，请重试"),
    ORDER_NOT_INVESTING(10008,"Gagal order,status salah","Gagal order,status salah"),
    NO_AVAILABLE_ORDER(10009,"Maaf,Saat ini tidak ada pinjaman yang ditemukan","Maaf,Saat ini tidak ada pinjaman yang ditemukan"), //未绑卡
    WRONG_PWD(10010,"Password salah","Password salah"), //未绑卡
    FAILED_TO_SEND_DIGISIGN(10011,"Gagal mengirim data,cobalah beberapa saat lagi","Gagal mengirim data,cobalah beberapa saat lagi"), //未绑卡
    DIGISIGN_PENDING(10012,"Dokumen belum di tandatangani","Dokumen belum di tandatangani"), //未绑卡
    NO_ORDER_TO_CHECK(10013,"Paramater orderNo kosong,periksa kembali","Paramater orderNo kosong,periksa kembali"), //未绑卡
    ORDER_NOT_FOUND(10014,"Order Tidak Ditemukan","Order Tidak Ditemukan"),
    ORDER_ALREADY_SIGN(10015,"Order Sudah di Sign","Order Sudah di Sign"),
    UNKNOWN_ORDER(10016,"Order Bermasalah,Silahkan hubungi Support","Order Bermasalah,Silahkan hubungi Support");


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
