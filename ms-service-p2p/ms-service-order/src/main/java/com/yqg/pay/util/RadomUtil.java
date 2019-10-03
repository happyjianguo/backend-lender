package com.yqg.pay.util;

import java.util.Random;

/**
 * Created by Administrator on 2018/11/20.
 */
public class RadomUtil {
    /**
     * 获取n位随机数
     * @param n
     * @return
     */
    public static String getRandom(int n) {
        Random random = new Random();
        String result = "";
        for (int i = 1; i < n; i++) {
            result += random.nextInt(10);
        }
        return result;
    }
}
