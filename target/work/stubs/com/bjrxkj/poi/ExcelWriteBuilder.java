package com.bjrxkj.poi;

import java.lang.*;
import java.io.*;
import java.net.*;
import java.util.*;
import groovy.lang.*;
import groovy.util.*;

public class ExcelWriteBuilder
  extends java.lang.Object  implements
    groovy.lang.GroovyObject {
;
public ExcelWriteBuilder
() {}
public ExcelWriteBuilder
(java.lang.String fileName) {}
public ExcelWriteBuilder
(java.io.File excelFile) {}
public  groovy.lang.MetaClass getMetaClass() { return (groovy.lang.MetaClass)null;}
public  void setMetaClass(groovy.lang.MetaClass mc) { }
public  java.lang.Object invokeMethod(java.lang.String method, java.lang.Object arguments) { return null;}
public  java.lang.Object getProperty(java.lang.String property) { return null;}
public  void setProperty(java.lang.String property, java.lang.Object value) { }
public  java.lang.Object getType() { return null;}
public  void setType(java.lang.Object value) { }
public  org.apache.poi.ss.usermodel.Workbook workbook(groovy.lang.Closure closure) { return (org.apache.poi.ss.usermodel.Workbook)null;}
public  void sheet(java.lang.String name, groovy.lang.Closure closure) { }
public  void row(java.util.List values) { }
public  void row(java.util.Map values) { }
public  void row(int rowNum, java.util.List values) { }
public  void row(int rowNum, java.util.Map values) { }
public  void mergedRegion(org.apache.poi.ss.util.CellRangeAddress cellRange) { }
public  byte[] download()throws java.io.IOException { return (byte[])null;}
}
