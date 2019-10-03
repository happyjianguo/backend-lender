package com.yqg.user.enums;

import com.yqg.common.exceptions.IExceptionEnum;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;

/**
 * Created by alan on 2018/9/3.
 */
public enum UserExceptionEnums implements IExceptionEnum {
    REPEAT_SUBMIT(5001, "Jangan melakukan pembayaran ulang"),    //请勿重复提交
    USER_NOT_EXIST(5002, "Pengguna tidak ada"),         //用户不存在
    USER_NOT_AUTH(5003, "Nama asli tidak sesuai dengan pengguna"),      //用户未实名认证
    USER_BANKCARD_EXIST(5004, "Pengguna telah menggunakan kartu bank tersebut"),        //用户已绑卡
    USER_BINDCARD_ERROR(5005, "Pendaftaran kartu bank gagal"),      //绑卡失败
    USER_NAME_ERROR(5006, "Ada kesalahan nama pengguna"),           //用户姓名有误
    USER_IDCARD_EXIST(5007, "Nama asli sesuai dengan pengguna"),    //用户已实名认证
    USER_AUTH_FAILED(5008, "Otentikasi nama asli pengguna gagal"),  //用户实名认证失败
    USER_ACCOUNT_LOCKED(5009,"Silahkan mencoba lagi"),              //请稍后再试
    STUDENT_LOAN_CHECKING(5010,"Pinjaman sedang menunggu verifikasi, silakan kirim nanti"),     //借款待审核,请稍后提交
    STUDENT_LOAN_ERROR(5011,"Ada kesalahan informasi pinjaman, silakan hubungi bagian bersangkutan"),   //借款信息异常,请联系开发人员
    STUDENT_LOAN_NOT_INIT(5012,"Informasi pinjaman mahasiswa tidak diinisialisasi"),            //学生借款信息未初始化
    USER_AUTH_NOT_REPEAT(5013,"Jangan mengulang kembali verifikasi nama asli pengguna"),        //请勿重复审核实名认证用户
    STUDENT_LOAN_STATUS_ERROR(5014,"Status permohonan pinjaman mahasiswa ada kesalahan"),       //学生借款订单状态有误
    CONTROL_PASSWORD_ERROR(5015, "Kata sandi salah"),//密码有误
    AMOUNT_ERROR(5016,"金额错误")
    ;

    private int code;
    private String message;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    private UserExceptionEnums(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 可选2 根据占位符填充动态信息
     */
    private String temlate;
    private UserExceptionEnums(int code, String message, String template) {
        this.code = code;
        this.message = message;
        this.temlate=template;
    }

    /**
     * 根据占位符填充动态信息
     *
     * @param ags
     * @return
     */
    public UserExceptionEnums setDynamicMessage(String... ags) {
        String dynamicMsg = MessageFormat.format(this.temlate, ags);
        if (!StringUtils.isEmpty(dynamicMsg)) {
            this.message = dynamicMsg;
        }
        return this;
    }
}
