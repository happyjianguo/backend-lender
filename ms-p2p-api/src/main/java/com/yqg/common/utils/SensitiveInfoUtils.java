package com.yqg.common.utils;



import org.apache.commons.lang.StringUtils;

import java.util.Random;

/**
 * 数据脱敏工具类
 * @author alan
 */
public class SensitiveInfoUtils {

    /**
     * [中文姓名] 只显示第一个汉字，其他隐藏为2个星号<例子：李**>
     *
     * @param fullName
     * @return
     */
    public static String chineseName(String fullName) {
        if(StringUtils.isEmpty(fullName)){
            return "";
        }
        String name = StringUtils.left(fullName,1);
        return StringUtils.rightPad(name,StringUtils.length(fullName),"*");
    }

    /**
     * [身份证号] 显示最后四位，其他隐藏。共计18位或者15位。<例子：*************5762>
     *
     * @param idCard
     * @return
     */
    public static String idCardNum(String idCard) {
        if(StringUtils.isEmpty(idCard)){
            return "";
        }

        String num = StringUtils.right(idCard,4);
        return StringUtils.leftPad(num,StringUtils.length(idCard),"*");
    }

    /**
     * [手机号码] 前三位，后四位，其他隐藏<例子:138******1234>
     *
     * @param num
     * @return
     */
    public static String mobilePhone(String num) {
        if (StringUtils.isBlank(num)) {
            return "";
        }
        if (StringUtils.length(num) <= 7){
            return num;
        }
        return StringUtils.left(num, 3).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(num, 4), StringUtils.length(num), "*"), "***"));
    }

    /**
     * [银行卡号] 前六位，后四位，其他用星号隐藏每位1个星号<例子:6222600**********1234>
     *
     * @param cardNum
     * @return
     */
    public static String bankCard(String cardNum) {
        if (StringUtils.isBlank(cardNum)) {
            return "";
        }
        return StringUtils.left(cardNum, 6).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(cardNum, 4), StringUtils.length(cardNum), "*"), "******"));
    }

    /**
     * 生成随即密码
     * @param pwd_len 生成的密码的总长度
     * @return  密码的字符串
     */
    public static String genRandomNum(int pwd_len){
        //35是因为数组是从0开始的，26个字母+10个数字
        final int  maxNum = 36;
        int i;  //生成的随机数
        int count = 0; //生成的密码的长度
        char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
                'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
                'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

        StringBuffer pwd = new StringBuffer("");
        Random r = new Random();
        while(count < pwd_len){
            //生成随机数，取绝对值，防止生成负数，

            i = Math.abs(r.nextInt(maxNum));  //生成的数最大为36-1

            if (i >= 0 && i < str.length) {
                pwd.append(str[i]);
                count ++;
            }
        }

        return pwd.toString();
    }

}
