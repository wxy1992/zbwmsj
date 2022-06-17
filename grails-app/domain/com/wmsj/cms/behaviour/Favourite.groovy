package com.wmsj.cms.behaviour

import com.wmsj.annotation.Title
import com.wmsj.core.BaseUser

class Favourite {
    @Title(zh_CN='收藏内容ID')//若type=comment,则newsid-commentid
    String contentId
    @Title(zh_CN='收藏类型')//news,comment
    String type
    @Title(zh_CN='发表人')
    BaseUser baseUser
    @Title(zh_CN='创建时间')
    Date dateCreated
    @Title(zh_CN='修改时间')
    Date lastUpdated
    static constraints = {
        contentId(nullable: true)
        type(nullable: true)
        baseUser(nullable: false)
    }
}
