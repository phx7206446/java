package com.phx.utils;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * excel 工具类
 */
public class ExcelBuilder {

    /**
     * 获取对应的SXSSFWorkbook对象
     * @param sheetName sheet名
     * @param sxssfWorkbook
     * @param title 列名
     * @param values 数据
     * @return
     * @author phx
     */
    public static final SXSSFWorkbook getSXSSFWorkbook(String sheetName,SXSSFWorkbook sxssfWorkbook,String [] title,String[][] values){
        if(sxssfWorkbook==null){
            sxssfWorkbook=new SXSSFWorkbook();
        }
        Sheet sheet = sxssfWorkbook.createSheet(sheetName);

        Row row = sheet.createRow(0);
        CellStyle style = sxssfWorkbook.createCellStyle();
        style.setAlignment((short)2);
        Cell cell = null;
        int i;
        for(i = 0; i < title.length; ++i) {
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }

        for(i = 0; i < values.length; ++i) {
            row = sheet.createRow(i + 1);

            for(int j = 0; j < values[i].length; ++j) {
                row.createCell(j).setCellValue(values[i][j]);
            }
        }
        return sxssfWorkbook;
    }


    /**
     * 设置返回头参数
     * @param response
     * @param fileName
     * @author phx
     */
    public static void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } catch (UnsupportedEncodingException var3) {
                var3.printStackTrace();
            }
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }



}
