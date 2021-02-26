package com.phx.utils;

import org.apache.commons.lang3.StringUtils;

public class PhoneUtils {
    
    /**
     * 手机号中间4位打*号
     *
     * @param phone
     * @return
     */
    public static String hide_phone(String phone) {
        if(StringUtils.isBlank(phone)) {
            return phone;
        }
        phone = phone.substring(0, 3) + "****" + phone.substring(7);
        return phone;
    }
}
