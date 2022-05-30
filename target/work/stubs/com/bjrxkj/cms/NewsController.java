package com.bjrxkj.cms;

import java.lang.*;
import java.io.*;
import java.net.*;
import java.util.*;
import groovy.lang.*;
import groovy.util.*;

public class NewsController
  extends java.lang.Object  implements
    groovy.lang.GroovyObject {
;
public  groovy.lang.MetaClass getMetaClass() { return (groovy.lang.MetaClass)null;}
public  void setMetaClass(groovy.lang.MetaClass mc) { }
public  java.lang.Object invokeMethod(java.lang.String method, java.lang.Object arguments) { return null;}
public  java.lang.Object getProperty(java.lang.String property) { return null;}
public  void setProperty(java.lang.String property, java.lang.Object value) { }
public  ricicms.CommonService getCommonService() { return (ricicms.CommonService)null;}
public  void setCommonService(ricicms.CommonService value) { }
public  grails.plugin.springsecurity.SpringSecurityService getSpringSecurityService() { return (grails.plugin.springsecurity.SpringSecurityService)null;}
public  void setSpringSecurityService(grails.plugin.springsecurity.SpringSecurityService value) { }
public  java.lang.Object newsTopSearch(java.lang.Object params, java.lang.Object catalog) { return null;}
@grails.plugin.cache.Cacheable(value="newsTop", key="#id + 'list'") public  java.lang.Object top() { return null;}
@grails.plugin.cache.Cacheable(value="newsTopTag", key="#idTag + 'topTag'") public  java.lang.Object topTag() { return null;}
@grails.plugin.cache.Cacheable(value="newsSearch", key="#keyword + 'search'") public  java.lang.Object search() { return null;}
@grails.plugin.cache.Cacheable(value="newsDetail") public  java.lang.Object detail() { return null;}
public  java.lang.Object mp4() { return null;}
public  java.lang.Object picture() { return null;}
public  java.lang.Object upload() { return null;}
public  java.lang.Object downloadSrcFile() { return null;}
public  java.lang.Object lzxyDetail() { return null;}
public  java.lang.Object integrity() { return null;}
public  java.lang.Object downloadPicture() { return null;}
}
