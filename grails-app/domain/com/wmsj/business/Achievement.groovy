package com.wmsj.business

import com.wmsj.annotation.Title
import com.wmsj.core.BaseUser
import org.grails.databinding.BindingFormat

@Title(zh_CN='服务成果')
class Achievement {
    @Title(zh_CN='标题')
    String title
    @Title(zh_CN='服务')
    Trade trade
    @Title(zh_CN='成果内容')
    String content
    @Title('操作人')
    BaseUser baseUser
    @Title(zh_CN='是否删除')
    Boolean deleted=false
    @Title(zh_CN='创建时间')
    @BindingFormat("yyyy-MM-dd HH:mm")
    Date dateCreated
    @Title(zh_CN='修改时间')
    @BindingFormat("yyyy-MM-dd HH:mm")
    Date lastUpdated
    static constraints = {
        title nullable: false,size: 0..200
        trade nullable: false,unique: true
        content nullable: false
        baseUser nullable: false
        deleted nullable: false
    }

    static mapping = {
        content type: 'text'
    }
}
