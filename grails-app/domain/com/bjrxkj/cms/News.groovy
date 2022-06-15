package com.bjrxkj.cms

import com.bjrxkj.cms.behaviour.Commentary
import com.bjrxkj.core.BaseUser
import com.bjrxkj.annotation.Title
import org.grails.databinding.BindingFormat


@Title('新闻')
class News {
    static searchable = true
    @Title('隶属栏目')
    Catalog catalog
    @Title('标题')
    String title
    @Title('副标题 ')
    String subtitle
    @Title('来源')
    String source
    @Title('作者')
    String author
    @Title('关键字 ')
    String keywords
    @Title('大纲概要')
    String outline
    @Title('发布者')
    BaseUser publisher
    @Title('发布日期')
    @BindingFormat("yyyy-MM-dd HH:mm")
    Date publishDate=new Date()
    @Title('过期日期')
    @BindingFormat("yyyy-MM-dd HH:mm")
    Date expireDate=new Date().parse('yyyy-MM-dd','2077-07-07 23:59')
    @Title('跳转链接')
    String redirectURL
    @Title('新闻图片')
    String picture
    @Title('访问量')
    int clicknum=0
    @Title('排序数值')
    int sequencer=9999999
    @Title('新闻内容')
    String content
    @Title('内容文本')
    String contentStr
    @Title('允许评论')
    boolean allowComment=true
    @Title('退回原因')
    String backreason
    @Title('状态')
    String state
    @Title('最后审核操作员')
    BaseUser auditUser
    @Title('审核时间')
    Date auditDate
    Date dateCreated
    Date lastUpdated
    static hasMany = [comments: Commentary]
    static constraints = {
        state(size:1..200,blank: false,nullable:false,inList: ['草稿','退回','已提交','发布','回收站'])
        title(size:1..2000,blank: false,nullable: false)
        subtitle(size:0..2000,blank: true,nullable:true)
        source(size:0..100,blank: true,nullable:true)
        author(size:0..100,blank: true,nullable:true)
        keywords(size:0..200,blank: true,nullable:true)
        outline(size:0..2000,blank: true,nullable:true)
        publisher(nullable:false)
        publishDate(nullable:false)
        expireDate(nullable:false)
        redirectURL(size:0..400,blank: true,nullable:true)
        picture(size:0..400,nullable:true)
        content(blank: true,nullable:true)
        auditDate(nullable:true)
        auditUser(nullable:true)
        contentStr(blank: true,nullable:true)
        backreason(blank: true,nullable:true)
        allowComment(nullable:true)
    }
    static mapping = {
        //comments sort:"id"
        content type:'text'
        contentStr type:'text'
    }

    String toString(){
        return title
    }

    List attachments(Integer type){
        def list=Attachment.createCriteria().list {
            eq('news.id',this.id)
            if(type){
                eq('type',type)
            }
            order('sq','asc')
            order('id','asc')
        }
        return list;
    }

//    def afterInsert={
//        ['newsTopTag','newsTop','siteIndex'].each{cacheName->
//            GrailsConcurrentMapCache cache = grailsCacheManager.getCache(cacheName);
//            cache.clear();
//        }
//    }
//
//    def afterUpdate={
//        ['newsTopTag','newsTop','siteIndex','newsDetail','newsSearch'].each{cacheName->
//            GrailsConcurrentMapCache cache = grailsCacheManager.getCache(cacheName);
//            cache.clear();
//        }
//    }
}
