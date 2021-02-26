package com.phx.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.ArrayUtils;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 参数过滤
 */
public class ParamFilter {


    /**
     * 检查所需参数是否为空
     * @param object
     * @param require_fields
     * @auther phx
     * @return
     */
    public static final CustomAjaxResult requiredParamCheck(Object object,String [] require_fields)  {
        if (object != null && require_fields != null && require_fields.length != 0) {
            Object value=null;
            Field[] allFields = FieldUtil.getAllFields(object);
            try {
//                for循环中的++i与i++结果相同
                for (int i = 0; i < allFields.length; ++i) {
                    Field f = allFields[i];
//                使得private属性也可取得
                    f.setAccessible(true);
                    for (int j = 0; j < require_fields.length; ++j) {
                        if (f.getName().equals(require_fields[j])) {
                            value = f.get(object);
                            if (value == null || value instanceof String && MyStringUtils.isBlank(value.toString())) {
                                return CustomAjaxResult.error("信息填写不完整!");
                            }
                        }
                    }
                }
                return CustomAjaxResult.success();
            }catch (IllegalAccessException e){
                return CustomAjaxResult.error("数据必填校验(基于字段)发生异常!");
            }
        }else {
            return CustomAjaxResult.error("信息填写不完整!");
        }
    }


    /**
     * 将不需要的参数设置为null
     * @param object
     * @param required_fields （包括必填和选填参数）
     * @auther phx
     */
    public static final void set_null(Object object,String[] required_fields) {
        if(object!=null&&required_fields!=null&&required_fields.length!=0){
            Field[] allFields = FieldUtil.getAllFields(object);

            try {
                label1:
                for (int i = 0; i < allFields.length; ++i) {
                    Field f = allFields[i];
                    f.setAccessible(true);
                    for (int j = 0; j < required_fields.length; ++j) {
                        if (f.getName().equals(required_fields[j])) {
                            continue label1;
                        }
                    }
                    f.set(object, (Object)null);
                }
            }catch (IllegalAccessException e){
                e.printStackTrace();
            }
        }
    }


    /**
     * 返回所需的json数据，去除不需返回的数据
     * @param object
     * @param required_param
     * @auther phx
     * @return
     */
    public static final JSONObject setReturnParam(Object object,String[] required_param)
    {
        if(object!=null&&required_param!=null&&required_param.length!=0){
            Field[] allFields = FieldUtil.getAllFields(object);
            JSONObject jsonObject = new JSONObject();
            try {
                for (int i = 0; i < allFields.length; ++i) {
                    Field f = allFields[i];
                    f.setAccessible(true);
                    for (int j = 0; j < required_param.length; ++j) {
                        if (f.getName().equals(required_param[j])) {
                            jsonObject.put(f.getName(), f.get(object));
                        }
                    }
                }
                return jsonObject;
            }catch (IllegalAccessException e){
                e.printStackTrace();
                return null;
            }
        }else {
            return null;
        }
    }


    /**
     * 返回list对象
     * @param objectList
     * @param required_param
     * @param <T>
     * @auther phx
     * @return
     */
    public static final  <T>List<JSONObject> setReturnParamList(List<T> objectList,String[] required_param){
        if(objectList!=null){
            List<JSONObject> jsonObjectList=new ArrayList<>();
            Iterator<T> iterator = objectList.iterator();
            while (iterator.hasNext()){
                T next = iterator.next();
                JSONObject jsonObject = setReturnParam(next, required_param);
                jsonObjectList.add(jsonObject);
            }
            return jsonObjectList;
        }
        return null;
    }





    /**
     * 检查参数同时将不需参数设置为空
     * @param object
     * @param required_fields
     * @param option_fields
     * @auther phx
     * @return
     */
    public static final CustomAjaxResult check_set_null(Object object,String[] required_fields,String [] option_fields){
        if(object!=null&&required_fields!=null&&required_fields.length!=0){
            CustomAjaxResult customAjaxResult = requiredParamCheck(object, required_fields);
            if (!customAjaxResult.isSuccess()) {
                return customAjaxResult;
            } else {
//                合并可选参数和必填参数
                String[] no_null_fields= (String[]) ArrayUtils.addAll(required_fields, option_fields);
                set_null(object, no_null_fields);
                return customAjaxResult;
            }
        }else {
            return CustomAjaxResult.error("信息填写不完整!");
        }
    }





}
