package com.phx.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FieldUtil {

    /**
     * 获取该类的所有属性
     * @param object
     * @auther phx
     * @return
     */
    public static final Field[] getAllFields(Object object){
        Class clazz = object.getClass();
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null){
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);
        return fields;
    }

    /**
     * 重写
     * @param cls
     * @return
     */
    public static final Field[] getAllFields(Class<?> cls){

        List<Field> fieldList = new ArrayList<>();
        while (cls != null){
            fieldList.addAll(new ArrayList<>(Arrays.asList(cls.getDeclaredFields())));
            cls = cls.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);
        return fields;
    }



    /**
     * 返回该类所声明的所有属性
     * @param object
     * @return
     * @auther phx
     */
    public static final Field[] getDeclaredFields(Object object){
        Class clazz=object.getClass();
        return clazz.getDeclaredFields();
    }


    /**
     * 重写
     * @param cls
     * @return
     */
    public static final Field[] getDeclaredFields(Class<?> cls){
        return cls.getDeclaredFields();
    }
}
