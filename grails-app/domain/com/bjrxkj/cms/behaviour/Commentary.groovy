package com.bjrxkj.cms.behaviour

import com.bjrxkj.annotation.Title
import com.bjrxkj.cms.News
import com.bjrxkj.core.BaseUser

@Title(zh_CN='评论')
class Commentary {
    @Title(zh_CN='新闻')
    News news
    @Title(zh_CN='发表人')
    BaseUser baseUser
    @Title(zh_CN='状态')
    Integer state=0
    @Title(zh_CN='内容')
    String content
    @Title(zh_CN='点赞数')
    Integer clicknum=0
    Date dateCreated
    Date lastUpdated
    static constraints = {
        news(nullable: false)
        baseUser(size:1..50,nullable: false)
        content(size:1..2000,nullable: false)
        state(nullable:false)
        clicknum(nullable: true)
    }

    static mapping = {
        sort:"id"
        order:"desc"
    }

    String toString(){
        return "${baseUser.toString()}发表对${news.title}的评论：${content} "
    }
}
