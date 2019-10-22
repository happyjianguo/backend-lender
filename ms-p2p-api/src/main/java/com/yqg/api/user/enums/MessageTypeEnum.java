package com.yqg.api.user.enums;

import com.yqg.common.sender.IContentTemplate;

import java.text.MessageFormat;

/**
 * Created by Lixiangjun on 2019/5/20.
 */
public enum MessageTypeEnum implements IContentTemplate {
////    注册成功
//    REGIST_SUCCESS("注册成功", "恭喜您注册Do-It成功，您可以在我们平台选择优质债权获取高额且稳定的收益，如有任何问题，可联系我们的专职客服XXXXXXXXX"),
////    实名认证成功
//    ADVANCE_SUCCESS("实名认证成功", "恭喜您实名认证成功，请放心我们将严格保密您的个人信息。如有任何问题，可联系我们的专职客服XXXXXXXXX"),
////    实名认证审核中
//    ADVANCE_AUDIT("实名认证审核中", "您的实名认证提交成功，我们会尽快帮你审核"),
////    实名认证失败
//    ADVANCE_DEFEATED("实名认证失败", "您的实名认证失败，请重新认证。"),
//    //    下单成功
//    ORDER_SUCCESS("下单成功", "恭喜您下单成功，订单编号${}订单金额Rp${}，请您尽快完成支付"),
////    订单失效
//    ORDER_DEFEATED("订单失效", "您的订单编号${}，由于超出时间未完成支付，已经失效。"),
////    支付成功
//    PAY_SUCCESS("支付成功", "恭喜您支付成功，支付金额为Rp${}，您的账户总金额为Rp${}"),
////    购买成功
//    PURCHASE_SUCCESS("购买成功", "恭喜您成功支付给【${}】Rp${}"),
//
//    //利息清分
//    INTEREST_CLEARANCE("利息清分", "您支付给【${}】Rp【${}】为您赚取收益【${}】已到账"),
////    购买失败
//    PURCHASE_DEFEATED("购买失败", "您支付给【${}】Rp【${}】由于借款人账户原因，支付失败，我们已为您挑选其它优质借款人，点击查看>"),
////    还款成功
//    REPAYMENT_SUCCESS("还款成功", "您支付给【${}】Rp【${}】已经还款成功，我们将马上为您进行清分"),
////    还款清分
//    REPAYMENT_CLEARING("还款清分", "您支付给【${}】Rp【${}】已经完成清分，您的账户将增加【${}】"),
////    提现提交成功
//    TIXIAN_SUBMIT_SUCCESS("提现提交成功", "您提交的提现申请已经成功，资金会在T+3个工作日内达到您的银行卡"),
//    //    提现打款成功
//    TIXIAN_SUCCESS("提现打款成功", "资金Rp【${}】已经到达您的【${}】银行卡中，请注意查收"),
////    提现打款失败
//    TIXIAN_DEFEATED("提现打款失败", "您提交的提现申请打款失败，请检查银行卡是否正确，我们相关人员也会协助您处理，请保持手机通讯正常，谢谢~"),
//



    REGIST_SUCCESS("Pendaftaran Berhasil", "Selamat pendaftaran di Do-It berhasil, Anda dapat memilih kredit berkualitas tinggi di platform kami untuk mendapatkan penghasilan tinggi dan stabil. Jika Anda memiliki pertanyaan, silakan hubungi CS kami (021) 40530000"),
//    实名认证成功
    ADVANCE_SUCCESS("Otetentifikasi keaslian Nama berhasil", "Selamat sertifikasi nama asli Anda berhasil, yakinlah bahwa kami akan menjaga kerahasiaan informasi pribadi Anda. Jika Anda memiliki pertanyaan, silakan hubungi CS kami (021) 40530000"),
//    实名认证审核中
    ADVANCE_AUDIT("Pendaftaran dalam proses verifikasi", "Sertifikasi nama asli Anda berhasil dikirim dan kami akan segera membantu anda untuk verifikasi"),
//    实名认证失败
    ADVANCE_DEFEATED("实名认证审核中", "您的实名认证失败，请重新认证。"),
    //    下单成功  恭喜您下单成功，订单编号${}订单金额Rp${}，请您尽快完成支付
    ORDER_SUCCESS("Pesanan Berhasil", "Selamat pesanan Anda yang berhasil, jumlah pesanan ${orderNo}, nominal Rp ${amount}, harap lengkapi pembayaran sesegera mungkin"),
//    订单失效
    ORDER_DEFEATED("Permohonan salah", "Nomor pesanan Anda ${orderNo} telah kedaluwarsa karena pembayaran tidak tepat waktu."),
//    支付成功
    PAY_SUCCESS("Pembayaran Berhasil", "Selamat atas keberhasilan pembayaran Anda, jumlah pembayaran adalah Rp ${amount1}, jumlah total akun adalah Rp ${amount2}"),
//    购买成功
    PURCHASE_SUCCESS("Pembelian Berhasil", "Selamat pembayaran ke [${}] dengan jumlah Rp [${}] berhasi."),

    //利息清分
    INTEREST_CLEARANCE("利息清分", "Pembayaran anda untuk [${}] , Rp [${}] . Anda telah menerima penghasilan Rp [${}]."),
//    购买失败
    PURCHASE_DEFEATED("Pembelian Gagal", "Pembayaran atas [${}] Rp [${}] gagal, kami telah memilih peminjam berkualitas tinggi lain untuk Anda, klik untuk melihat>."),
//    还款成功
    REPAYMENT_SUCCESS("Pengembalian Berhasil", "Pembayaran atas [${}] Rp [${}] berhasil dilunasi, kami akan segera menghapuskan pinjaman anda."),
//    还款清分
    REPAYMENT_CLEARING("还款清分", "Anda membayar ke [${}] Rp [${}] telah dihapus, akun Anda akan meningkat [${}]."),
//    提现提交成功
    TIXIAN_SUBMIT_SUCCESS("Penarikan Tunai Berhasil Diajukan", "Permintaan penarikan yang Anda kirimkan telah berhasil dan dana masuk ke rekening Anda dalam D + 3 hari kerja."),
    //    提现打款成功
    TIXIAN_SUCCESS("Penarikan Tunai Berhasil Ditransfer", "Dana Rp [${}] telah mencapai [${}] kartu bank Anda, silakan periksa rekening anda."),
//    提现打款失败
    TIXIAN_DEFEATED("Penarikan Tunai Gagal Ditransfer", "Pengajuan Penarikan tunai anda gagal. Harap periksa apakah kartu bank anda benar. Personil kami akan membantu Anda mengatasinya. Harap pastikan Ponsel anda tetap menyala, terima kasih ~"),
    ;



    @Override
    public String getContent(String... params) {
        if (params != null && params.length > 0) {
            return MessageFormat.format(this.template, params);
        } else {
            return this.content;
        }
    }
    @Override
    public String getTemplateName() {
        return this.name();
    }
    @Override
    public String getTemplateId() {
        return template;
    }

    private String content;//内容模板
    private String template;//模板


    MessageTypeEnum(String template, String content ) {
        this.template = template;
        this.content = content;
    }
}
