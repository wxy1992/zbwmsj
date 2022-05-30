package com.bjrxkj.poi

import org.apache.poi.hssf.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFCell
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFWorkbook

/**
 * Groovy Builder that extracts data from
 * Microsoft Excel spreadsheets.
 * @author Goran Ehrsson
 */
class ExcelReadBuilder {
    def workbook
    def labels
    def row
    int type=2003
    ExcelReadBuilder(String fileName){
        init(new File(fileName));
    }
    ExcelReadBuilder(java.io.File excelFile){
        init(excelFile);
    }
    ExcelReadBuilder(int type,byte[] bytes){
        this.type=type;
        init(bytes);
    }
    ExcelReadBuilder(byte[] bytes, String typeStr){
        init(bytes,typeStr)
    }
    private void init(byte[] bytes, String typeStr){
        if(typeStr?.equals('.xls')){
            type=2003;
            HSSFRow.metaClass.getAt = { int idx ->
                def cell = delegate.getCell(idx)
                if(! cell) {
                    return null
                }
                def value
                def result = cell.cellType;
                if(result==HSSFCell.CELL_TYPE_FORMULA){
                    HSSFFormulaEvaluator fe = new HSSFFormulaEvaluator(workbook);
                    result=fe.evaluate(cell).cellType;
                }
                switch(result) {
                    case HSSFCell.CELL_TYPE_NUMERIC:
                        if(HSSFDateUtil.isCellDateFormatted(cell)) {
                            value = cell.dateCellValue
                        } else {
                            value = cell.numericCellValue
                        }
                        break
                    case HSSFCell.CELL_TYPE_BOOLEAN:
                        value = cell.booleanCellValue
                        break
                    case HSSFCell.CELL_TYPE_STRING:
                        value = cell.stringCellValue
                        break
                    default:
                        value = cell.toString();
                        break
                }
                return value
            }
        }
        if(typeStr?.equals('.xlsx')){
            type=2007;
            XSSFRow.metaClass.getAt = { int idx ->
                def cell = delegate.getCell(idx)
                if(! cell) {
                    return null
                }
                def value
                switch(cell.cellType) {
                    case XSSFCell.CELL_TYPE_NUMERIC:
                        if(HSSFDateUtil.isCellDateFormatted(cell)) {
                            value = cell.dateCellValue
                        } else {
                            value = cell.numericCellValue
                        }
                        break
                    case XSSFCell.CELL_TYPE_BOOLEAN:
                        value = cell.booleanCellValue
                        break
                    default:
                        value = cell.stringCellValue
                        break
                }
                return value
            }
        }
        def is=new ByteArrayInputStream(bytes)
        if(type==2003){
            workbook = new HSSFWorkbook(is)
        }
        if(type==2007){
            workbook = new XSSFWorkbook(is)
        }
    }
    private void init(byte[] bytes){
        if(type==2003){
            ExpandTo2003();
            workbook = new HSSFWorkbook(new ByteArrayInputStream(bytes))
        }
        if(type==2007){
            ExpandTo2007();
            workbook = new XSSFWorkbook(new ByteArrayInputStream(bytes))
        }
    }
    private void ExpandTo2003(){
        HSSFRow.metaClass.getAt = { int idx ->
            def cell = delegate.getCell(idx)
            if(! cell) {
                return null
            }
            def value
            switch(cell.cellType) {
                case HSSFCell.CELL_TYPE_NUMERIC:
                    if(HSSFDateUtil.isCellDateFormatted(cell)) {
                        value = cell.dateCellValue
                    } else {
                        value = cell.numericCellValue
                    }
                    break
                case HSSFCell.CELL_TYPE_BOOLEAN:
                    value = cell.booleanCellValue
                    break
                default:
                    value = cell.stringCellValue
                    break
            }
            return value
        }
    }
    private void ExpandTo2007(){
        XSSFRow.metaClass.getAt = { int idx ->
            def cell = delegate.getCell(idx)
            if(! cell) {
                return null
            }
            def value
            switch(cell.cellType) {
                case XSSFCell.CELL_TYPE_NUMERIC:
                    if(HSSFDateUtil.isCellDateFormatted(cell)) {
                        value = cell.dateCellValue
                    } else {
                        value = cell.numericCellValue
                    }
                    break
                case XSSFCell.CELL_TYPE_BOOLEAN:
                    value = cell.booleanCellValue
                    break
                default:
                    value = cell.stringCellValue
                    break
            }
            return value
        }
    }
    private void init(java.io.File excelFile){
        if(excelFile.name.toLowerCase().endsWith('.xls')){
            type=2003;
            ExpandTo2003();
        }
        if(excelFile.name.toLowerCase().endsWith('.xlsx')){
            type=2007;
            ExpandTo2007();
        }
        excelFile.withInputStream{is->
            if(type==2003){
                workbook = new HSSFWorkbook(is)
            }
            if(type==2007){
                workbook = new XSSFWorkbook(is)
            }
        }
    }

    def getSheet(idx) {
        def sheet
        if(! idx) idx = 0
        if(idx instanceof Number) {
            sheet = workbook.getSheetAt(idx)
        } else if(idx ==~ /^\d+$/) {
            sheet = workbook.getSheetAt(Integer.valueOf(idx))
        } else {
            sheet = workbook.getSheet(idx)
        }
        return sheet
    }

    def cell(idx) {
        if(labels && (idx instanceof String)) {
            idx = labels.indexOf(idx.toLowerCase())
        }
        return row[idx]
    }

    def propertyMissing(String name) {
        cell(name)
    }

    def eachLine(Map params = [:], Closure closure) {
        def offset = params.offset ?: 0
        def max = params.max ?: 9999999
        def sheet = getSheet(params.sheet)
        def rowIterator = sheet.rowIterator()
        def linesRead = 0

        if(params.labels) {
            labels = rowIterator.next().collect{it.toString().toLowerCase()}
        }
        offset.times{ rowIterator.next() }

        closure.setDelegate(this)

        while(rowIterator.hasNext() && linesRead++ < max) {
            row = rowIterator.next()
            closure.call(row)
        }
    }
}
/*

new ExcelBuilder("customers.xls").eachLine([labels:true]) {
  new Person(name:"$firstname $lastname",
    address:address, telephone:phone).save()
}

new ExcelBuilder("customers.xls").eachLine {
  println "First column on row ${it.rowNum} = ${cell(0)}"
}

import com.petrodata.poi.*
def erb=new ExcelReadBuilder(new File("c://b.xls"))
erb.eachLine([sheet:'sheet1',labels:true]) {
  println "${name}"
   println "First column on row ${it.rowNum} = ${cell(0)}"
}
println erb.labels
*
 */
