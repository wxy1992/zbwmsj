package com.wmsj.cms.behaviour

import com.bjrxkj.cms.News
import com.bjrxkj.cms.Site
import com.bjrxkj.annotation.Title

@Title(zh_CN='访问信息')
class Visit {
    @Title(zh_CN='新闻')
    News news
    @Title(zh_CN='访问者IP')
    String fromip
    @Title(zh_CN='访问内容')
    String domainName
    @Title(zh_CN='访问内容id')
    String vid
    Date dateCreated
    Date lastUpdated
    Site site
    static constraints = {
        news(nullable:true)
        site(nullable:true)
        fromip(size:0..200,blank: true,nullable:true)
        domainName(size:0..200,blank: true,nullable:true)
        vid(size:0..2000,blank: true,nullable:true)
    }
}
