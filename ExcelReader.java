package com.phx.utils;

import com.phx.model.Customer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {
    private static final String EXCEL03_EXTENSION=".xls";
    private static final String EXCEL07_EXTENSION=".xlsx";

    /**
     * 解析一行的数据
     * @param filePath
     * @param sheetName
     * @param sheetIndex
     * @param curRow
     * @param cellList
     * @return
     * @author phx
     */
    public static List<String> parseRows(String filePath, String sheetName, int sheetIndex, int curRow, List<String> cellList){
        List<String> dataList=new ArrayList<>();
        for (String cell:cellList
             ) {
            dataList.add(cell.trim());
        }
        return dataList;
    }

    /**
     * 读取excel文件
     * @param fileName
     * @throws Exception
     * @author phx
     */
    public static List<List<String>> readExcel(String fileName) throws Exception{
        List<List<String>> dataList=new ArrayList<>();
        if(fileName.endsWith(EXCEL03_EXTENSION)){
            ExcelXlsReader reader=new ExcelXlsReader();
            dataList= reader.process(fileName);
            System.out.println(dataList.size());
        }
        else if(fileName.endsWith(EXCEL07_EXTENSION)){
            ExcelXlsxReader reader=new ExcelXlsxReader();
            dataList = reader.process(fileName);
            System.out.println(dataList.size());
        }else {
            throw new Exception("文件格式错误");
        }
        return dataList;
    }


    /**
     * 解析成对应的对象
     * @param dataList
     * @param cls
     * @param <T>
     * @return
     * @author phx
     */
    public static <T>List<T> parseList(List<List<String>> dataList,Class<T> cls){
        List<T> resultList=new ArrayList<>();
        for (List<String> singleData:dataList
             ) {
            try {
                T t = parseClass(singleData, cls);
                resultList.add(t);
            }catch (Exception ee){
                ee.printStackTrace();
            }
        }
        return resultList;
    }


    /**
     * 解析单个对象
     * @param singleData
     * @param cls
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @author phx
     */
    private static <T>T parseClass(List<String> singleData,Class<T> cls) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Field[] fields = cls.getDeclaredFields();
        T object= cls.getDeclaredConstructor().newInstance();
        int i=0;
        for (Field field:fields
             ) {
            field.setAccessible(true);
            field.set(object,(Object)singleData.get(i));
            i++;
        }
        return object;
    }



    /**
     * 测试
     * @param args
     * @throws Exception
     */
    public static void main(String args[]) throws Exception {
        String filename="C:\\Users\\Lenovo\\Desktop\\customer.xlsx";
        List<List<String>> lists = readExcel(filename);
        List<Customer> customers = parseList(lists, Customer.class);

    }



}
