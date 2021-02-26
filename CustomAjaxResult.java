package com.phx.utils;

/**
 * 返回页面的数据类
 */
public class CustomAjaxResult {
    public static final int success = 0;
    public static final int error = -1;
    private int code;
    private String msg;
    private Object data;

//    返回的数据条数
    private Integer count;

    private CustomAjaxResult(int code, String msg, Object data, Integer count) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.count = count;
    }

    private CustomAjaxResult(int code){
        this.code=code;
    }

    private CustomAjaxResult(int code,Object data){
        this.code=code;
        this.data=data;
    }

    private CustomAjaxResult(int code,String msg){
        this.code=code;
        this.msg=msg;
    }

    private CustomAjaxResult(int code,String msg,Object data){
        this.code=code;
        this.msg=msg;
        this.data=data;
    }


    public static CustomAjaxResult success(){
        return new CustomAjaxResult(0);
    }

    public static CustomAjaxResult success(String msg){
        return new CustomAjaxResult(0,msg);
    }


    public static CustomAjaxResult success(String msg,Object data){
        return new CustomAjaxResult(0,msg,data);
    }

    public static CustomAjaxResult success(Object data){
        return new CustomAjaxResult(0,data);
    }
    public static CustomAjaxResult success(String msg,Integer count,Object data){
        return new CustomAjaxResult(0,msg,data,count);
    }

    public boolean isSuccess(){
        return this.code==0;
    }



    public static CustomAjaxResult error(){
        return new CustomAjaxResult(-1);
    }

    public static CustomAjaxResult error(String msg){
        return new CustomAjaxResult(-1,msg);
    }


    public static CustomAjaxResult error(String msg,Object data)
    {
        return new CustomAjaxResult(-1,msg,data);
    }

    public static CustomAjaxResult error(int code,Object data) {
        return new CustomAjaxResult(code, data);
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
