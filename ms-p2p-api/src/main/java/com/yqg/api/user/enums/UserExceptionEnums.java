package com.yqg.api.user.enums;

import com.yqg.common.exceptions.IExceptionEnum;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;

/**
 * Created by alan on 2018/9/3.
 */
public enum UserExceptionEnums implements IExceptionEnum {
    REPEAT_SUBMIT(5001, "Jangan melakukan pembayaran ulang","Jangan melakukan pembayaran ulang"),    //请勿重复提交
    USER_NOT_EXIST(5002,"Pengguna tidak ditemukan", "Pengguna tidak ada"),         //用户不存在
    USER_NOT_AUTH(5003, "Nama asli tidak sesuai dengan pengguna","Nama asli tidak sesuai dengan pengguna"),      //用户未实名认证
    USER_BANKCARD_EXIST(5004,"Pengguna lain telah menggunakan kartu bank tersebut", "Pengguna telah menggunakan kartu bank tersebut"),        //用户已绑卡
    USER_BINDCARD_ERROR(5005,"Pendaftaran kartu bank gagal", "Pendaftaran kartu bank gagal"),      //绑卡失败
    USER_NAME_ERROR(5006,"Ada kesalahan nama pengguna", "Ada kesalahan nama pengguna"),           //用户姓名有误
    USER_IDCARD_EXIST(5007,"Pengguna sudah terdaftar", "Pengguna sudah terdaftar"),    //用户已实名认证
    USER_AUTH_FAILED(5008,"Otentifikasi nama asli gagal", "Otentifikasi nama asil gagal"),  //用户实名认证失败
    USER_ACCOUNT_LOCKED(5009,"Silahkan mencoba lagi","Silahkan mencoba lagi"),              //请稍后再试
    STUDENT_LOAN_CHECKING(5010,"Pinjaman sedang menunggu verifikasi, silakan coba lagi nanti","Pinjaman sedang menunggu verifikasi, silakan kirim nanti"),     //借款待审核,请稍后提交
    STUDENT_LOAN_ERROR(5011,"Ada kesalahan informasi pinjaman, silakan hubungi bagian bersangkutan","Ada kesalahan informasi pinjaman, silakan hubungi bagian bersangkutan"),   //借款信息异常,请联系开发人员
    STUDENT_LOAN_NOT_INIT(5012,"","Informasi pinjaman mahasiswa tidak diinisialisasi"),            //学生借款信息未初始化
    USER_AUTH_NOT_REPEAT(5013,"Jangan mengulang kembali verifikasi nama asli pengguna","Jangan mengulang kembali verifikasi nama asli pengguna"),        //请勿重复审核实名认证用户
    STUDENT_LOAN_STATUS_ERROR(5014,"","Status permohonan pinjaman mahasiswa ada kesalahan"),       //学生借款订单状态有误
    CONTROL_PASSWORD_ERROR(5015, "Kata sandi salah","Kata sandi salah"),//密码有误
    AMOUNT_ERROR(5016,"Nominal Salah","Nominal Salah"),
    CART_NOT_FOUND_GOODS_ERROR(5017,"Item yang sesuai dalam keranjang belanja tidak ada.","Item yang sesuai dalam keranjang belanja tidak ada."),//购物车内对应商品不存在
    USER_RESET_PAY_PASSWORD_ERROR(5018,"Reset password transaksi gagal","Reset password transaksi gagal"),   //重置交易密码失败
    USER_RESET_UPLOADHEAD_ERROR(5019,"Gagal mengunggah foto","Gagal mengunggah foto"),   //上传头像失败
    USER_RESET_UPDATEBANKCARD_ERROR(5020,"Penggantian kartu saat ini hanya mendukung modifikasi nomor kartu bank, dan bank tidak dapat diganti.","Penggantian kartu saat ini hanya mendukung modifikasi nomor kartu bank, dan bank tidak dapat diganti."),   //换卡目前只支持修改银行卡号，不可更换所在银行
    USER_RESET_OPCODE_ERROR(5021,"Kode operasi salah","Kode operasi salah"),   //操作码不正确
    USER_YES_EXIST(5022,"Akun sudah ada", "Akun sudah ada"),         //用户已存在
    PAY_PASSWORD_ERROR(5023,"Password transaksi salah. Coba lagi", "Password transaksi salah. Coba lagi"),//交易密码不正确，请再次输入。
    USER_NOT_SUPPER_INVESTORS(5024,"Akun tidak ditemukan, pembelian tidak dapat dilakukan", "Akun tidak ditemukan, pembelian tidak dapat dilakukan"),//交易密码不正确，请再次输入。
    BANKINFO_IS_NULL(5025,"Informasi kartu bank kosong", "Informasi kartu bank kosong"),
    COMPANY_INVESTOR_ERROR(5026,"penambahan investor gagal", "penambahan investor gagal"),
    FIRST_REGISTER(5027,"Registrasi berhasil. Team kami akan segera menghubungi Anda dalam 2x24 jam. Terima kasih telah melakukan registrasi.", "Registrasi berhasil. Team kami akan segera menghubungi Anda dalam 2x24 jam. Terima kasih telah melakukan registrasi."),
    IDCARD_DUPLICATE(5028,"Registrasi gagal. No KTP Anda sudah terdaftar. Mohon pastikan kembali no KTP Anda.", "Registrasi gagal. No KTP Anda sudah terdaftar. Mohon pastikan kembali no KTP Anda."),
    USER_NOT_ACTIVATE(5029,"Mohon maaf, User Anda sedang dalam verifikasi. Team kami akan segera menghubungi Anda dalam 2x24 jam sejak registrasi.", "Mohon maaf, User Anda sedang dalam verifikasi. Team kami akan segera menghubungi Anda dalam 2x24 jam sejak registrasi."),
    USER_DEACTIVATE(5030,"Anda telah melakukan de-aktifasi akun ini.", "Anda telah melakukan de-aktifasi akun ini."),
    UPDATE_BANKCARD_FAILED(5031,"Gagal melakukan update bank.", "Gagal melakukan update bank."),
    REGISTRATION_FAILED(5031,"Gagal melakukan registrasi, silahkan mencoba kembali.", "Gagal melakukan registrasi, silahkan mencoba kembali."),
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

    private UserExceptionEnums(int code, String message, String messageI18n) {
        this.code = code;
        this.message = message;
        this.messageI18n = messageI18n;
    }

    public UserExceptionEnums setCustomMessage(String message) {
        this.message = message;
        return this;
    }

    public UserExceptionEnums setCustomMessage(String message, String messageI18n) {
        this.message = message;
        this.messageI18n = messageI18n;
        return this;
    }

    private UserExceptionEnums(int code, String message, String messageI18n, String template) {
        this.code = code;
        this.message = message;
        this.messageI18n = messageI18n;
        this.temlate = template;
    }

    public UserExceptionEnums setDynamicMessage(String... ags) {
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
