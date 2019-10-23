/*
 * Copyright (c) 2014-2015 XXX, Inc. All Rights Reserved.
 */

package com.yqg.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Utility functions for date related logic.
 * @author niebiaofei
 *
 */
public class DateUtils {

    /**
     * Convert Long to String of Day for targeted time zone.
     * @param epochTime
     * @param
     * @return
     */
    public static String epochTimeToDay(long epochTime) {
        Date date = new Date(epochTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    /**
     * Convert Long to String of Day for targeted time zone.
     * @param epochTime second
     * @param timeZone
     * @return
     */
    public static String epochSecondToDay(long epochTime, String timeZone) {
        Date date = new Date(epochTime * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
        return simpleDateFormat.format(date);
    }

    /**
     * Convert current Date to String of Day
     * @return
     */
    public static String dateToDay() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    /**
     * Convert String of day to epoch time for targeted time zone.
     * @param day
     * @param timeZone
     * @return
     * @throws ParseException
     */
    public static long dayToMilliSeconds(String day, String timeZone) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
        return simpleDateFormat.parse(day).getTime();
    }

    /**
     * Convert String of day to epoch time for targeted time zone.
     * @param day
     * @param
     * @return
     * @throws ParseException
     */
    public static long timetoMilliSeconds(String day) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return simpleDateFormat.parse(day).getTime();
    }

    /**
     * Convert String of day to epoch time for targeted time zone.
     * @param day
     * @param timeZone
     * @return
     * @throws ParseException
     */
    public static long dayToSeconds(String day, String timeZone) throws ParseException {
        return dayToMilliSeconds(day, timeZone) / 1000;
    }

    /**
     * Convert String of day to epoch time for UTC
     * @param day
     * @return
     * @throws ParseException
     */
    public static long dayToUTCSeconds(String day) throws ParseException {
        return dayToSeconds(day, "UTC");
    }

    /**
     * Convert String of day to Date type
     *
     * @param time
     * @param format
     * @return
     * @throws ParseException
     */
    public static Date strToDate(String time, String format) throws ParseException {
        DateFormat formater = new SimpleDateFormat(format);
        return formater.parse(time);
    }

    /**
     * Get day of week
     *
     * @param date
     * @return
     */
    public static int getDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * date to string yyyy-MM-dd
     * @param date
     * @return
     */
    public static String DateToString(Date date) {
        SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");
        return sFormat.format(date);
    }

    /**
     * date to string yyyy-MM-dd HH:mm:ss
     * @param date
     * @return
     */
    public static String DateToString2(Date date) {
        SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sFormat.format(date);
    }

    public static String DateToString3(Date date) {
        SimpleDateFormat sFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return sFormat.format(date);
    }

    public static String DateToString4(Date date) {
        SimpleDateFormat sFormat = new SimpleDateFormat("yyyyMMdd");
        return sFormat.format(date);
    }

    public static String formDate(Date date, String dateStr) {
        SimpleDateFormat sFormat = new SimpleDateFormat(dateStr);
        return sFormat.format(date);
    }

    /**
     * 比较两个时间
     * @param
     * @param
     */
    public static boolean compareDateTime(Date currentDate, Date endDate) {
        if (currentDate.getTime() > endDate.getTime()) {
            return true;
        } else {
            return false;
        }
    }

    public static String decrMonthToString(int count) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -count);
        Date m = c.getTime();
        String mon = format.format(m);
        return mon;
    }

    public static String decrDayToString(int count) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, -count);
        Date d = c.getTime();
        String day = format.format(d);
        return day;
    }
    
    public static Date stringToDate(String time) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=format.parse(time);
        return date;
    }


    public static Date stringToDate2(String time) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date=format.parse(time);
        return date;
    }


    /**
     * 获取相差天数
     * @param currentDate
     * @param setDate
     * @return
     */
    public static long getDateTimeDifference(Date currentDate, Date setDate) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        long nd = 1000 * 24 * 60 * 60;//一天的毫秒数
                long nh = 1000*60*60;//一小时的毫秒数
        //        long nm = 1000*60;//一分钟的毫秒数
        //        long ns = 1000;//一秒钟的毫秒数long diff;
        //获得两个时间的毫秒时间差异
        long diff = 0;
        try {
            diff = sd.parse(sd.format(currentDate)).getTime()
                    - sd.parse(sd.format(setDate)).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long day = diff / nd;//计算差多少天
                long hour = diff%nd/nh;//计算差多少小时
        //        long min = diff%nd%nh/nm;//计算差多少分钟
        //        long sec = diff%nd%nh%nm/ns;//计算差多少秒//输出结果
        return day;
    }

    /**
     * 日期+n天
     * @param date
     * @return date
     */
    public static Date addDate(Date date,int day){
        Calendar   calendar   =   new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE,day);//把日期往后增加一天.整数往后推,负数往前移动
        date=calendar.getTime();   //这个时间就是日期往后推一天的结果
        return date;
    }

    /**
     * 计算逾期专用
     * @param smdate
     * @param bdate
     * @return
     * @throws ParseException
     */
    public static int daysBetween(String smdate,String bdate) throws ParseException{
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    public static int compare_date(Date date1,Date date2) {
        int i = date1.compareTo(date2);
        return i;

    }
    public static Date stringToDate4(String time) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date=format.parse(time);
        return date;
    }
    public static Date stringToDate5(String time) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date date=format.parse(time);
        return date;
    }

    /**
     * 获取相差分钟
     * @param currentDate
     * @param setDate
     * @return
     */
    public static long getDateTimeDifferenceMin(Date currentDate, Date setDate) {
        long between=(currentDate.getTime()-setDate.getTime())/1000;//除以1000是为了转换成秒
        long min=between/60;
        return min;
    }

    public static void main(String[] args) {

    }

    /**
     * 当前时间加n分钟
     * @return
     */
    public static Date  addMin(int n) throws ParseException {
        long curren = System.currentTimeMillis();
        curren += n * 60 * 1000;
        Date da = new Date(curren);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return  stringToDate(dateFormat.format(da));
    }
    /**
     * 当前时间减n分钟
     * @return
     */
    public static Date  redMin(int n)  throws ParseException {
        long curren = System.currentTimeMillis();
        curren -= n * 60 * 1000;
        Date da = new Date(curren);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return  stringToDate(dateFormat.format(da));
    }



}
