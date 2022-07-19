package com.wmsj.core

import com.wmsj.annotation.Title

@Title(zh_CN='微信信息')
class WxUser {
    @Title(zh_CN='微信id')
    String openId
    @Title(zh_CN='昵称')
    String nickname
    @Title(zh_CN='手机号')
    String tel
    @Title(zh_CN='手机号')
    String pureTel
    @Title(zh_CN='头像')
    String avatarUrl
    @Title(zh_CN='国籍')
    String country
    @Title(zh_CN='省份')
    String province
    @Title(zh_CN='城市')
    String city
    @Title(zh_CN='性别')
    String gender
    @Title(zh_CN='语言')
    String language
    @Title(zh_CN='姓名')
    String name
    @Title(zh_CN='身份证号')
    String idcard
    @Title(zh_CN='联系地址')
    String address
    @Title(zh_CN='经度')
    String lng
    @Title(zh_CN='纬度')
    String lat

    static constraints = {
        openId nullable: false,size: 0..100,unique: true
        nickname nullable: true,size: 0..100
        tel nullable: true,size: 0..20
        pureTel nullable: true,size: 0..20,unique: true
        avatarUrl nullable: true,size: 0..500
        country nullable: true,size: 0..100
        province nullable: true,size: 0..100
        city nullable: true,size: 0..100
        gender nullable: true,size: 0..10
        language nullable: true,size: 0..10
        name nullable: true,size: 0..100
        idcard nullable: true,size: 0..20
        address nullable: true,size: 0..300
        lng nullable: true,size: 0..50
        lat nullable: true,size: 0..50
    }

    String toString(){
        return tel;
    }
}
