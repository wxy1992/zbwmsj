package com.bjrxkj.core

import com.bjrxkj.annotation.Title

@Title(zh_CN = "机构信息")
class Organization{
    @Title(zh_CN = "单位ID")
    String id
    String casid
    @Title(zh_CN = "机构编码")
    String code
    @Title(zh_CN = "机构简称")
    String shortName
    @Title(zh_CN = "机构全称")
    String name
    @Title(zh_CN = "父级全称")
    String parentName
    @Title(zh_CN = "父级ID")
    Organization parentId
    @Title(zh_CN = "父级全路径")
    String path;
    @Title(zh_CN = "所属国家名称")
    String nationName;
    @Title(zh_CN = "所属国家ID")
    String nationId;
    @Title(zh_CN = "所属省份名称")
    String provinceName;
    @Title(zh_CN = "所属省份ID")
    String provinceId;
    @Title(zh_CN = "所属城市名称")
    String cityName;
    @Title(zh_CN = "所属城市ID")
    String cityId;
    @Title(zh_CN = "所属区县")
    String countyName;
    @Title(zh_CN = "所属区县ID")
    String countyId;
    Date dateCreated;
    Date lastUpdated;

    static constraints = {
        casid nullable: true;
        code nullable: false;
        name blank: false, nullable: false,size:0..200
        shortName blank: true, nullable: true,size:0..100
        name blank: false, nullable: false,size:0..50
        parentName nullable: true;
        parentId nullable: true;
        path  nullable: true;
        nationName  nullable: true;
        nationId  nullable: true;
        provinceName  nullable: true;
        provinceId  nullable: true;
        cityName  nullable: true;
        cityId  nullable: true;
        countyName  nullable: true;
        countyId  nullable: true;
    }

    static mapping = {
        comment "机构信息"
        id generator: 'uuid'
    }

    String toString(){
        return name;
    }

}
