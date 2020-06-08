package com.yqg.common.enums;

public enum UserAccountBusinessTypeEnum {
    BUY_CREDITOR("购买债权","Pembelian Kreditor"),
    BUY_CREDITOR_SUCCESS("购买债权成功","Klaim Pembelian Sukses"),
    BUY_CREDITOR_FAIL("购买债权失败","Klaim Pembelian Gagal"),
    SERVICE_FEE("75%服务费","Biaya admin 75%"),
    INCOME("利息收入","Pendapatan bunga"),
    PAYBACK_INCOME("还款清分","Pengembalian Dana"),
    RECHARGE_APPLY("提现申请","Pendaftaran Penarikan Dana"),
    RECHARGE_SUCCESS("提现成功","Penarikan Berhasil"),
    RECHARGE_FAIL("提现失败","Penarikan Gagal"),
    CHARGE("充值","Isi ulang"),
    INSURANCE("75%服务费","Biaya asuransi")



    ;
//    private Integer type;
    private String name;
    private String enname;

//    UserAccountBusinessTypeEnum(Integer type, String name, String enname) {
//        this.type = type;
//        this.name=name;
//        this.enname=enname;
//    }
    UserAccountBusinessTypeEnum( String name, String enname) {
        this.name=name;
        this.enname=enname;
    }

    public String getEnname() {
        return enname;
    }

    public void setEnname(String enname) {
        this.enname = enname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public Integer getType() {
//        return type;
//    }
//
//    public void setType(Integer type) {
//        this.type = type;
//    }



}
