package com.phx.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 */
public class DateUtils {


    /**
     * 字符串转date (自定义格式)
     * @param date
     * @param format
     * @return
     */
    public static Date strToDate(String date,String format) {
        try {
            if (StringUtils.isAnyBlank(date, format)) {
                return null;
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            return simpleDateFormat.parse(date);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 字符串转date(默认格式：yyyy/MM/dd HH:mm:ss)
     * @param date
     * @return
     */
    public static Date strToDateSecond(String date){
        return strToDate(date,"yyyy/MM/dd HH:mm:ss");
    }


    /**
     * 字符串转date yyyy/MM/dd
     * @param date
     * @return
     */
    public static Date strToDateDay(String date){
        return strToDate(date,"yyyy/MM/dd");
    }



    /**
     * date转字符串（自定义格式）
     *
     * @param date
     * @return
     */
    public static String dateToStr(Date date,String format){
        try {
            if(date==null||StringUtils.isBlank(format)){
                return null;
            }
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat(format);
            return simpleDateFormat.format(date);
        }catch (Exception ee){
            ee.printStackTrace();
        }
        return null;
    }

    /**
     * date转字符串（默认格式：yyyy/MM/dd HH:mm:ss）
     *
     * @param date
     * @return
     */
    public static String dateToStrSecond(Date date){
        return dateToStr(date,"yyyy/MM/dd HH:mm:ss");
    }

    /**
     * date转字符串（默认格式 yyyy/MM/dd
     * @param date
     * @return
     */
    public static String dateToStrDay(Date date){
        return dateToStr(date,"yyyy/MM/dd");
    }


    /**
     * 获取一天的起始与结束时间
     * @param date
     * @return
     */
    public static JSONObject get_day_startAndEnd(Date date) {
        JSONObject jsonObject = new JSONObject();
        try {
            if(date == null) {
                return jsonObject;
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
            String day = simpleDateFormat.format(date);
            String start_date = day + " 00:00:00";
            String end_date = day + " 23:59:59";
            simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            // 日期
            jsonObject.put("start_day_date", simpleDateFormat.parse(start_date));
            jsonObject.put("end_day_date", simpleDateFormat.parse(end_date));
            // 字符串
            jsonObject.put("start_day_str", start_date);
            jsonObject.put("end_day_str", end_date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 获取某一天的起始时间和结束时间
     *
     * @param date
     * @return
     */
    public static JSONObject get_day_startAndEnd(String date, String format) {
        try {
            if(StringUtils.isAnyBlank(date, format)) {
                return null;
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            Date day = simpleDateFormat.parse(date);
            return get_day_startAndEnd(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }
}
