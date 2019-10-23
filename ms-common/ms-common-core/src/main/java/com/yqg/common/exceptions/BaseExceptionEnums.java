package com.yqg.common.exceptions;

import org.springframework.util.StringUtils;

import java.text.MessageFormat;

/**
 * 基础异常,只要继承IExceptionEnum即可
 * Created by gao on 2017/12/19.
 */
public enum BaseExceptionEnums implements IExceptionEnum {

    SYSTERM_ERROR(-1,"系统异常,请稍后再试……", "Sistemnya tidak normal, silakan coba nanti lagi"),
    PARAM_ERROR(-2,"参数异常", "Parameter tidak normal"),
    PARAM_CONTENT_TYPE_UNSUPPORTED(-3,"不支持的参数内容格式","Format konten parameter tidak didukung" ),
    RESULT_MORE_ERROR(-4, "返回结果多于一个","Hasil Kembalikan lebih dari satu"),
    NOT_LOGIN(-5,"用户未登录或登录状态已过期,请重新登录……", "Pengguna belum masuk atau status masuk sudah lewat. Silakan masuk kembali lagi"),
    LOGIN_ERROR(-6,"用户名或密码错误", "Nama pengguna atau kata sandi salah"),
    CPPCHA_ERROR(-7, "验证码错误","kode verifikasi salah"),
    CPPCHA_IMG_ERROR(-8, "图片验证码错误","kode verifikasi gambar salah"),
    REGEDITER_REPEAT_ERROR(-9,"该号码或地址请勿重复注册", "Jangan gandakan pendaftaran nomor atau alamat ini"),
    REGEDITER_NOT_ERROR(-10,"用户未注册,请先注册!", "Pengguna tidak terdaftar, silakan mendaftar terlebih dahulu!"),
    UPLOAD_EMPTY_ERROR(-11,"文件上传不能为空", "Pengunggahan file tidak boleh kosong"),
    UPLOAD_FILE_NAME_ERROR(-12, "文件名不合法","Nama file tidak sah"),
    UPLOAD_SAVE_ERROR(-13,"文件存储失败,请稍后再试……", "Penyimpanan file gagal, silakan coba nanti lagi"),
    UPLOAD_FILE_TYPE_ERROR(-14,"不支持的文件类型", "Jenis file tidak didukung"),
    UPLOAD_FILE_OVER_MAX_ERROR(-15, "上传文件大小超过限制","Ukuran file unggahan melebihi batas"),
    SERVICE_CALL_ERROR(-16,"服务调用异常", "pemindahan layanan eror"),
    SYSTEEM_MAINTAIN_ERROR(-17,"系统维护中,请稍后再来","系统维护中,维护时间 {0} ,请稍后再来","Sedang pemeliharaan sistem, waktu perawatan {0}, silakan kembali nanti lagi"),
    SYSTEEM_AUTH_ERROR(-18,"非法访问,无访问权限!","Akses ilegal, tidak ada hak akses!"),
    SIGN_VERIFY_ERROR(-19,"数据验签错误!","Kesalahan verifikasi data!"),
    TASK_NO_FUND_ERROR(-20,"任务不存在,请检查任务编号","任务不存在,请检查任务编号" ),
    TASK_CANCEL_ERROR(-21,"任务停止失败,请稍后再试", "任务停止失败,请稍后再试"),
    TASK_CANCEL_UNSUPPORT_ERROR(-22,"此任务不支持强制停止,请修改参数","此任务不支持强制停止,请修改参数" ),
    GET_LOCK_ERROR(-23,"获取锁失败","获取锁失败" ),

   ;


    private int code;
    private String message;
    private String messageI18n;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getMessageI18n() {
        return messageI18n;
    }


    private BaseExceptionEnums(int code, String message,String messageI18n) {
        this.code = code;
        this.message = message;
        this.messageI18n=messageI18n;
    }

    /************可选***************************/
    /**
     * 可选1、自定义异常信息,会覆盖枚举里原始定义的message
     *
     * @param message
     * @return
     */
    public BaseExceptionEnums setCustomMessage(String message) {
        this.message = message;
        return this;
    }
    //带国际化内容
    public BaseExceptionEnums setCustomMessage(String message,String messageI18n) {
        this.message = message;
        this.messageI18n=messageI18n;
        return this;
    }


    /**
     * 可选2 根据占位符填充动态信息
     */
    private String temlate;

    private BaseExceptionEnums(int code, String message,String messageI18n, String template) {
        this.code = code;
        this.message = message;
        this.messageI18n=messageI18n;
        this.temlate = template;
    }

    /**
     * 根据占位符填充动态信息
     *
     * @param ags
     * @return
     */
    public BaseExceptionEnums setDynamicMessage(String... ags) {
        String dynamicMsg = MessageFormat.format(this.temlate, ags);
        String dynamicMsgI18n = MessageFormat.format(this.temlate, ags);

        if (!StringUtils.isEmpty(dynamicMsg)) {
            this.message = dynamicMsg;
        }
        if (!StringUtils.isEmpty(dynamicMsgI18n)) {
            this.messageI18n = dynamicMsgI18n;
        }
        return this;
    }

}
