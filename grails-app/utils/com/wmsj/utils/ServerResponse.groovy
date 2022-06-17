package com.wmsj.utils

import com.wmsj.annotation.Title
import grails.converters.JSON

class ServerResponse {
    boolean result;
    @Title(zh_CN='错误码')
    String code;
    @Title(zh_CN='错误信息')
    String error;
    @Title(zh_CN='返回值')
    Object data;


    static JSON success(Object data){
        def map=[:];
        map["Result"]=true;
        map["Data"]=data;
        return map as JSON;
    }

    static JSON fail(String error){
        def map=[:];
        map["Result"]=false;
        map["Code"]="-1";
        map["Error"]=error;
        return map as JSON;
    }

    static JSON fail(){
        def map=[:];
        map["Result"]=false;
        map["Code"]="-1";
        map["Error"]="系统异常";
        return map as JSON;
    }

    static JSON fail(String code,String error){
        def map=[:];
        map["Result"]=false;
        map["Code"]=code;
        map["Error"]=error;
        return map as JSON;
    }



}
