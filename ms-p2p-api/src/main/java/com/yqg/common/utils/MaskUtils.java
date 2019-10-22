package com.yqg.common.utils;

import org.springframework.util.StringUtils;

/**
 * Remark:
 * Created by huwei on 19.5.17.
 */
public class MaskUtils {

    public static String maskString(String s, MaskUtils.Type type){
        String ss = "";
        switch (type){
            case MOBILE:
                if(!StringUtils.isEmpty(s)&&s.length()>3){
                    String str1 = s.substring(0,2);//截取前两位
                    String str2 = s.substring(s.length()-2,s.length());//截取后两位
                    ss=str1+"***"+str2;
                }
                break;
            case ADDRESS:
                break;
            case BIRTHDAY:
                if(!StringUtils.isEmpty(s)&&s.length()>3){
                    String str1 = s.substring(2,s.length());//截取后两位
                    ss="**"+str1;
                }
                break;
            case NAME:
                if(!StringUtils.isEmpty(s)&&s.length()>3){
                    String str1 = s.substring(0,2);//截取前两位
                    String str2 = s.substring(s.length()-2,s.length());//截取后两位
                    ss=str1+"***"+str2;
                }
                break;
            case IDCARDNO:
                if(!StringUtils.isEmpty(s)&&s.length()>3){
                    String str1 = s.substring(0,2);//截取前两位
                    String str2 = s.substring(s.length()-2,s.length());//截取后两位
                    ss=str1+"***"+str2;
                }
                break;
        }
        return ss;
    }


    public static enum Type{
        MOBILE,
        ADDRESS,
        BIRTHDAY,
        NAME,
        IDCARDNO
        ;
        private Type(){}
    }
}
