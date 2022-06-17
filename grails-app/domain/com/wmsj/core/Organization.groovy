package com.wmsj.core

import com.wmsj.annotation.Title

@Title(zh_CN = "机构信息")
class Organization{
    @Title(zh_CN = "机构全称")
    String name
    @Title(zh_CN = "机构简称")
    String shortName

    static belongsTo =[parent:Organization]
    static hasMany = [children:Organization]

    static constraints = {
        name blank: false, nullable: false,size:0..200
        shortName blank: true, nullable: true,size:0..100
        parent nullable: true;
    }

    String toString(){
        return name;
    }

}
