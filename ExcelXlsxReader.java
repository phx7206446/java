package com.phx.utils;


import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于解决xlsx2007大数据量问题
 */
public class ExcelXlsxReader extends DefaultHandler {

    enum CellDataType{
        BOOL,ERROR,FORMULA,INLINESTR,SSTINDEX,NUMBER,DATE,NULL
    }

    /**
     * 共享字符串表
     */
    private SharedStringsTable sst;

    /**
     * 上一次的索引值
     */
    private String lastIndex;

    /**
     * 文件的绝对路径
     */
    private String filePath="";


    /**
     * 工作表索引
     */
    private Integer sheetIndex=0;


    /**
     * sheet名
     */
    private String sheetName="";

    /**
     * 总行数
     */
    private Integer totalRows=0;

    /**
     * 一行内的cell集合
     */
    private List<String> cellList=new ArrayList<String>();

    /**
     * 判断整行是否为空行的标记
     */
    private boolean flag=false;

    /**
     * 当前行
     */
    private Integer curRow=1;

    /**
     * 当前列
     */
    private Integer curCol=0;

    /**
     * T元素标识
     */
    private boolean isTElement;

    /**
     * 单元格数据类型，默认为字符串类型
     */
    private CellDataType nextDataType=CellDataType.SSTINDEX;

    /**
     * 定义前一个元素和当前元素的位置，用来计算其中的空的单元格数量，如a6和a8等
     */
    private String preRef=null,ref=null;

    /**
     * 定义该文档一行最大的单元格数，用来补全一行可能缺失的单元格
     */
    private String maxRef=null;

    /**
     * 单元格
     */
    private StylesTable stylesTable;


    private final DataFormatter formatter=new DataFormatter();

    /**
     * 单元格日期格式的索引
     */
    private short formatIndex;

    /**
     * 日期格式字符串
     */
    private String formatString;

    private List<List<String>> dataList;


    /**
     *
     * @param filename
     * @return
     * @throws Exception
     * @author phx
     */
    public List<List<String>> process(String filename) throws Exception{
        filePath=filename;
        dataList=new ArrayList<>();
        OPCPackage pkg=OPCPackage.open(filePath);
        XSSFReader xssfReader=new XSSFReader(pkg);
        stylesTable =xssfReader.getStylesTable();
        SharedStringsTable sst=xssfReader.getSharedStringsTable();
        XMLReader parser= XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
        this.sst=sst;
        parser.setContentHandler(this);
        XSSFReader.SheetIterator sheets= (XSSFReader.SheetIterator) xssfReader.getSheetsData();
        while(sheets.hasNext()){
            curRow=1;
            sheetIndex++;
            InputStream sheet=sheets.next();
            sheetName=sheets.getSheetName();
            InputSource sheetSource=new InputSource(sheet);
            parser.parse(sheetSource);
            sheet.close();
        }
        return dataList;
    }


    /**
     *
     * @param attributes
     * @author phx
     */
    public void setNextDataType(Attributes attributes){
        nextDataType=CellDataType.NUMBER;//cellType为空，则表示该单元格类型为数字
        formatIndex=-1;
        formatString=null;
        String cellType=attributes.getValue("t");//单元格类型
        String cellStyleStr=attributes.getValue("s");
        String columnData = attributes.getValue("r");//获取单元格的位置，如a1
        //处理数据类型
        if("b".equals(cellType)){
            //布尔值
            nextDataType=CellDataType.BOOL;
        }else if("e".equals(cellType)){
            //错误
            nextDataType=CellDataType.ERROR;
        }else if("inlineStr".equals(cellType)){
            nextDataType=CellDataType.INLINESTR;
        }else if("s".equals(cellType)){
            //字符串
            nextDataType=CellDataType.SSTINDEX;
        }else if("str".equals(cellType)){
            nextDataType=CellDataType.FORMULA;
        }

        //处理日期
        if(cellStyleStr!=null){
            int styleIndex=Integer.parseInt(cellStyleStr);
            XSSFCellStyle style=stylesTable.getStyleAt(styleIndex);
            formatIndex=style.getDataFormat();
            formatString=style.getDataFormatString();
            if(formatString.contains("m/d/yy")) {
                nextDataType = CellDataType.DATE;
                formatString="yyyy-mm-dd hh:mm:ss";
            }
            if(formatString==null){
                nextDataType=CellDataType.NULL;
                formatString= BuiltinFormats.getBuiltinFormat(formatIndex);
            }
        }

    }

    /**
     *  对解析出来的数据进行类型处理
     * @param value
     * 单元格的值，
     * value代表解析：BOOL的为0或1， ERROR的为内容值，FORMULA的为内容值，INLINESTR的为索引值需转换为内容值，
     * SSTINDEX的为索引值需转换为内容值， NUMBER为内容值，DATE为内容值
     * @param thisStr
     * @return
     * @author phx
     */
    public String getDataValue(String value,String thisStr){
        switch (nextDataType){
            case BOOL:
                char first=value.charAt(0);
                thisStr=first=='0'?"FALSE":"TRUE";
                break;
            case ERROR:
                thisStr="\"ERROR:"+value.toString()+'"';
                break;
            case FORMULA:
                thisStr='"'+value.toString()+'"';
                break;
            case INLINESTR:
                XSSFRichTextString rtsi=new XSSFRichTextString(value.toString());
                thisStr=rtsi.toString();
                rtsi=null;
                break;
            case SSTINDEX:
                String sstIndex=value.toString();
                try {
                    int idx=Integer.parseInt(sstIndex);
                    XSSFRichTextString rtss=new XSSFRichTextString(sst.getEntryAt(idx));//根据idx索引值获取内容值
                }catch (NumberFormatException exception){
                    thisStr=value.toLowerCase();
                }
                break;
            case NUMBER:
                if(formatString!=null){
                    thisStr=formatter.formatRawCellContents(Double.parseDouble(value),formatIndex,formatString).trim();
                }else {
                    thisStr=value;
                }
                thisStr=thisStr.replace("_","").trim();
                break;
            case DATE:
                thisStr=formatter.formatRawCellContents(Double.parseDouble(value),formatIndex,formatString);
                thisStr=thisStr.replace("T"," ");// 对日期字符串作特殊处理，去掉T
                break;
            default:
                thisStr="";
                break;
        }
        return thisStr;
    }


    /**
     * 统计空内容单元格数
     * @param ref
     * @param preRef
     * @return
     * @author phx
     */
    public Integer countNullCell(String ref,String preRef){
        //excel2007最大行数是1048576，最大列数是16384，最后一列列名是XFD
        String xfd=ref.replaceAll("\\d+","");
        String xfd_1=preRef.replaceAll("\\d+","");
        xfd=fillChar(xfd,3,'@',true);
        xfd_1=fillChar(xfd_1,3,'@',true);
        char[] letter=xfd.toCharArray();
        char[] letter_1=xfd_1.toCharArray();
        int res=(letter[0]-letter_1[0])*26*26+(letter[1]-letter_1[1])*26+(letter[2]-letter_1[2]);
        return res-1;
    }


    /**
     * @param str
     * @param len
     * @param let
     * @param isPre
     * @return
     * @author phx
     */
    public String fillChar(String str,int len,char let,boolean isPre){
        int len_1=str.length();
        if(len_1<len){
            if(isPre){
                for (int i = 0; i < len-len_1; i++) {
                    str=let+str;
                }
            }else {
                for (int i = 0; i < len - len_1; i++) {
                    str=str+let;
                }
            }
        }
        return str;
    }

    /**
     * 标签开始
     * @param uri
     * @param localName
     * @param name
     * @param attributes
     * @throws SAXException
     * @author phx
     */
    @Override
    public void startElement(String uri, String localName,String name, Attributes attributes) throws SAXException {
        if("c".equals(name)){
            if(preRef==null){
                preRef=attributes.getValue("r");
            }else {
                preRef=ref;
            }
            ref=attributes.getValue("r");
            this.setNextDataType(attributes);
        }
        if("t".equals(name)){
            isTElement=true;
        }else{
            isTElement=false;
        }
        lastIndex="";
    }


    /**
     * 第二个执行
     * 得到单元格对应的索引值或是内容值
     * 如果单元格类型是字符串、INLINESTR、数字、日期，lastIndex则是索引值
     * 如果单元格类型是布尔值、错误、公式，lastIndex则是内容值
     * @param ch
     * @param start
     * @param length
     * @throws SAXException
     * @author phx
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        lastIndex+=new String(ch,start,length);
    }


    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        //t元素也包含字符串
        if(isTElement){
            //将单元格内容加入rowlist中，在这之前先去掉字符串前后的空白符
            String value=lastIndex.trim();
            cellList.add(curCol,value);
            curCol++;
            isTElement=false;
            //如果里面某个单元格含有值，则标识该行不为空行
            if(value!=null&&MyStringUtils.isNotEmpty(value)){
                flag=true;
            }
        }else if("v".equals(name)){
            //v => 单元格的值，如果单元格是字符串，则v标签的值为该字符串在SST中的索引
            String value=this.getDataValue(lastIndex.trim(),"");
            if(!ref.equals(preRef)){
                int len=countNullCell(ref,preRef);
                for (int i = 0; i < len; i++) {
                    cellList.add(curCol,"");
                    curCol++;
                }
            }
            cellList.add(curCol,value);
            curCol++;
            if(value!=null&&MyStringUtils.isNotEmpty(value)){
                flag=true;
            }
        }else{
            if("row".equals(name)){
                if(curRow==1){
                    maxRef=ref;
                }
                if(maxRef!=null){
                    int len=countNullCell(maxRef,ref);
                    for (int i = 0; i <=len; i++) {
                        cellList.add(curCol,"");
                        curCol++;
                    }
                }
                if(flag&&curRow!=1){
                    List<String> lineData = ExcelReader.parseRows(filePath, sheetName, sheetIndex, curRow, cellList);
                    dataList.add(lineData);
                }
                cellList.clear();
                curRow++;
                curCol=0;
                preRef=null;
                ref=null;
                flag=false;
            }
        }
    }
}
