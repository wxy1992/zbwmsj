package com.wmsj.poi

import org.apache.poi.hssf.usermodel.HSSFRichTextString
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFRichTextString
import org.apache.poi.xssf.usermodel.XSSFWorkbook

//http://skepticalhumorist.blogspot.jp/2010/12/groovy-dslbuilders-poi-spreadsheets.html
class ExcelWriteBuilder {
    private Workbook workbook;// = new HSSFWorkbook()
    private Sheet sheet
    private int rows
    private File file
    def type
    ExcelWriteBuilder(){
        init(null);
    }
    ExcelWriteBuilder(String fileName){
        init(new File(fileName))
    }
    ExcelWriteBuilder(File excelFile){
          init(excelFile);
    }
    private void init(File excelFile){
       if(excelFile){
           if(excelFile.name.toLowerCase().endsWith('.xls')){
                       type=2003;
           }
           if(excelFile.name.toLowerCase().endsWith('.xlsx')){
                       type=2007;
           }
           file=excelFile;
           excelFile.withInputStream{is->
               if(type==2003){
                  workbook = new HSSFWorkbook(is)
               }
               if(type==2007){
                   workbook = new XSSFWorkbook(is)
               }
           }
       }else{
           type=2003;
           workbook=new HSSFWorkbook();
       }
    }
    Workbook workbook(Closure closure) {
        closure.delegate = this
        closure.call()
        workbook
    }

    void sheet(String name, Closure closure) {
        sheet = workbook.getSheet(name);
        if(!sheet)
        sheet = workbook.createSheet(name);
        rows = 1
        closure.delegate = this
        closure.call()
    }

    void row(List values) {
        Row row = sheet.createRow(rows++ as int)
        values.eachWithIndex {value, col ->
            Cell cell = row.createCell(col)
            switch (value) {
                case Date: cell.setCellValue((Date) value); break
                case Double: cell.setCellValue((Double) value); break
                case BigDecimal: cell.setCellValue(((BigDecimal) value).doubleValue()); break
                default:
                    def newValue;
                    if(type==2003)  newValue=new HSSFRichTextString("" + value)
                    if(type==2007)  newValue=new XSSFRichTextString("" + value)
                    cell.setCellValue(newValue?:''); break
            }
        }
    }
    void row(Map values) {
        Row row = sheet.createRow(rows++ as int)
        values.each{key,value ->
            Cell cell = row.createCell(key as int)
            switch (value) {
                case Date: cell.setCellValue((Date) value); break
                case Double: cell.setCellValue((Double) value); break
                case BigDecimal: cell.setCellValue(((BigDecimal) value).doubleValue()); break
                default:
                    def newValue;
                    if(type==2003)  newValue=new HSSFRichTextString("" + value)
                    if(type==2007)  newValue=new XSSFRichTextString("" + value)
                    cell.setCellValue(newValue?:''); break
            }
        }
    }
    void row(int rowNum, List values) {
        Row row = sheet.getRow(rowNum as int)
        if(!row) row = sheet.createRow(rowNum as int)
        values.eachWithIndex {value, col ->
            Cell cell = row.getCell(col)
            if(!cell) cell=row.createCell(col)
            switch (value) {
                case null: break
                case Date: cell.setCellValue((Date) value); break
                case Double: cell.setCellValue((Double) value); break
                case BigDecimal: cell.setCellValue(((BigDecimal) value).doubleValue()); break
                default:
                    def newValue;
                    if(type==2003)  newValue=new HSSFRichTextString("" + value)
                    if(type==2007)  newValue=new XSSFRichTextString("" + value)
                    cell.setCellValue(newValue?:''); break
            }
        }
    }
    void row(int rowNum, Map values) {
        Row row = sheet.getRow(rowNum as int)
        if(!row) row = sheet.createRow(rowNum as int)
        values.each{key,value ->
            Cell cell = row.getCell(key as int)
            if(!cell) cell=row.createCell(key as int)
            switch (value) {
                case Date: cell.setCellValue((Date) value); break
                case Double: cell.setCellValue((Double) value); break
                case BigDecimal: cell.setCellValue(((BigDecimal) value).doubleValue()); break
                default:
                    def newValue;
                    if(type==2003)  newValue=new HSSFRichTextString("" + value)
                    if(type==2007)  newValue=new XSSFRichTextString("" + value)
                    cell.setCellValue(newValue?:''); break
            }
        }
    }
    void mergedRegion(CellRangeAddress cellRange){
        sheet.addMergedRegion(cellRange);
    }
    public byte[] download() throws IOException{
        ByteArrayOutputStream excelStream = new ByteArrayOutputStream();
        workbook.write(excelStream)
        return   excelStream.toByteArray();
    }
}

