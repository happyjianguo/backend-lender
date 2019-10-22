package com.yqg.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * 订单生成
 * @author  zhaoruifeng
 */
@Slf4j
public class OrderNoCreator {


    public static void main(String[] args) {
        System.out.println(createOrderNo());
    }



//    private static final String machine ="01";

    //获取毫秒内唯一号码
    public static String createOrderNo() {
        String dateFormat = DateUtils.formDate(new Date(), "yyMMdd");
        String timeFormat = DateUtils.formDate(new Date(), "HHmmss");
//        StringBuilder builder = new StringBuilder(machine);
        StringBuilder builder = new StringBuilder();
        builder.append(dateFormat).append(timeFormat).append(IDCreator.create());
        return builder.toString();
    }

}

class IDCreator {
    private static Long second = 0L;
    private static Integer seed = 0;

    private synchronized static String getId() {
        if (second == 0)
            second = System.currentTimeMillis();
        if (second != System.currentTimeMillis()) {
            second = System.currentTimeMillis();
            seed = 0;
            return second.toString() + seed;
        } else {
            return second.toString() + ++seed;
        }
    }

    public static String create() {
        String id = getId();
        return id.substring(id.length() - 4, id.length());
    }
}