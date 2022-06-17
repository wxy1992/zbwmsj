package com.bjrxkj.business

import com.bjrxkj.annotation.Title
import org.grails.databinding.BindingFormat

@Title(zh_CN='标题')
class Apply {
    @Title(zh_CN='标题')
    Trade trade
    @Title(zh_CN='申请人')
    String name
    @Title(zh_CN='身份证号')
    String idcard
    @Title(zh_CN='联系方式')
    String telephone
    @Title(zh_CN='联系地址')
    String address
    @Title(zh_CN='审核通过')
    Boolean approve=false
    @Title(zh_CN='是否删除')
    Boolean deleted=false
    @Title(zh_CN='报名时间')
    @BindingFormat("yyyy-MM-dd HH:mm")
    Date dateCreated
    @Title(zh_CN='修改时间')
    @BindingFormat("yyyy-MM-dd HH:mm")
    Date lastUpdated

    static constraints = {
        trade(nullable: false)
        name(nullable: false,size:0..50)
        idcard(nullable: false,size:0..20)
        telephone(nullable: false,size:0..20)
        address(nullable: true,size:0..200)
    }

    String toString(){
        return "【${name}】报名了："+trade;
    }
}
