package com.wmsj.business

import com.wmsj.annotation.Title
import com.wmsj.business.Apply
import com.wmsj.core.BaseUser
import com.wmsj.core.WxUser
import org.grails.databinding.BindingFormat

@Title(zh_CN='评论')
class Commentary {
    @Title(zh_CN='报名信息')
    Apply apply
    @Title(zh_CN='评论人姓名')
    String createdBy
    @Title(zh_CN='评分')
    Integer score=5
    @Title(zh_CN='内容')
    String content
    @Title(zh_CN='评论人')
    WxUser creator
    @Title(zh_CN='是否审核通过')
    Integer state=0 //0未审核，1通过，2不通过
    @Title(zh_CN='评论时间')
    @BindingFormat("yyyy-MM-dd HH:mm")
    Date dateCreated
    @Title(zh_CN='修改时间')
    @BindingFormat("yyyy-MM-dd HH:mm")
    Date lastUpdated
    static constraints = {
        apply(nullable: false)
        createdBy(nullable: false,size: 0..100)
        creator(nullable: false)
        score(nullable: false,min: 0)
        content(size:1..2000,nullable: false)
        state nullable: false,inList: [0,1,2]
    }

    static mapping = {
        sort:"id"
        order:"desc"
    }

    String toString(){
        return "${createdBy}：${content} "
    }
}
