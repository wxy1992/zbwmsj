package com.wmsj.cms.vote

import com.wmsj.cms.News

// 问卷表
class Vote {
	String title
    String  content
	Date dateCreated
	Date lastUpdated
    String state //草稿 发布 关闭
    News news
    static constraints = {
		title(size:1..500,blank: false,nullable:false)
        content(size:1..2000,blank: false,nullable:false)
        state(size:1..50,blank: false,nullable:false)
        news(nullable: true)
    }
}
