package com.phx.utils;

import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * JSON 工具类
 */
public class JSONUtil {


    /**
     * 返回一个对应的json对象
     * @param object
     * @auther phx
     * @return
     */
    public static final JSONObject create_new_object(Object object){
        if(object!=null) {
            try {
                JSONObject jsonObject = new JSONObject();
                Field[] allFields = FieldUtil.getAllFields(object);
                for (int i = 0; i < allFields.length; ++i) {
                    Field f = allFields[i];
                    f.setAccessible(true);
                    Object o = f.get(object);
                    if(o!=null){
                        jsonObject.put(f.getName(),o);
                    }
                }
                return jsonObject;
            }catch (IllegalAccessException e){
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }


    /**
     * 返回一个对应的List<JSONObject>对象
     * @param objectList
     * @param <T>
     * @auther phx
     * @return
     */
    //<T>List 表示返回类型为List的泛型方法
    public static final <T>List<JSONObject> create_new_List(List<T> objectList){
        List<JSONObject> jsonObjectList = new ArrayList();
        if (objectList == null) {
            return jsonObjectList;
        } else {
            Iterator iterator = objectList.iterator();
            while(iterator.hasNext()) {
                Object object = iterator.next();
                jsonObjectList.add(create_new_object(object));
            }
            return jsonObjectList;
        }
    }


    // TODO: 2021/2/4 jsonobject convert to object
    // TODO: 2021/2/4 list<jsonobject> convert to list<object>




}
