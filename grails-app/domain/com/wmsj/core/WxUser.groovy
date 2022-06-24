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
    Integer gender
    @Title(zh_CN='语言')
    Integer language
    @Title(zh_CN='姓名')
    String name
    @Title(zh_CN='身份证号')
    String idcard
    @Title(zh_CN='联系地址')
    String address

    static constraints = {
        openId nullable: false,size: 0..100,unique: true
        nickname nullable: true,size: 0..100
        name nullable: true,size: 0..100
        idcard nullable: true,size: 0..20
        tel nullable: true,size: 0..20,unique: true
        pureTel nullable: true,size: 0..20,unique: true
        address nullable: true
        avatarUrl nullable: true
    }
}
