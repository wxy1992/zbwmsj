package com.wmsj.business

import com.wmsj.annotation.Title
import com.wmsj.core.BaseUser
import com.wmsj.core.WxUser
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
    @Title(zh_CN='报名用户')
    WxUser creator
    @Title(zh_CN='状态')
    Integer status=10//10报名成功，20已完成，30已评价
    @Title(zh_CN='审核通过')
    Boolean approve=true
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
        creator(nullable: false)
        status(nullable: false)
    }

    String toString(){
        return "【${name}】报名了："+trade;
    }

    transient static final Map STATUSMAP=[10:"报名成功",20:"已完成",30:"已评价"];

}
