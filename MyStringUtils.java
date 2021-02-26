package com.phx.utils;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;


/**
 * 字符串工具类
 */
public class MyStringUtils extends StringUtils {

    /**
     * convertToUnderline 将驼峰命名自动转为下划线+小写字母
     * @param str 修改的字符串
     * @auther phx
     */
    public static final String convertToUnderline(String str){
        if(isBlank(str))
            return null;
        StringBuilder ret= new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char charAt=str.charAt(i);
            if(Character.isUpperCase(charAt)){
                if(i==0){
                    ret.append(String.valueOf(charAt).toLowerCase());
                    continue;
                }
                ret.append("_").append(String.valueOf(charAt).toLowerCase());
                continue;
            }
            ret.append(charAt);
        }
        return ret.toString();
    }



}
