package com.yqg.pay.util;

import org.springframework.util.StringUtils;

/**
 * Created by Administrator on 2018/12/27.
 */
public class ToolUtils {
    //保存前两位后两位,中间*代替
    public static String getYanMa(String str){
        if(!StringUtils.isEmpty(str)&&str.length()>3){
            String str1 = str.substring(0,2);//截取前两位
            String str2 = str.substring(str.length()-2,str.length());//截取后两位
            str=str1+"***"+str2;
        }
        return str;
    }

    public static void main(String[] args) {
        String str = "123456789";
        if(!StringUtils.isEmpty(str)&&str.length()>3){
            String str1 = str.substring(0,2);//截取前两位
            String str2 = str.substring(str.length()-2,str.length());//截取后两位
            str=str1+"***"+str2;
        }

    }
}
