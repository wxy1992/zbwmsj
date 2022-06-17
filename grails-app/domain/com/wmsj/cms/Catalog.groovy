package com.wmsj.cms

import com.wmsj.annotation.Title

class Catalog {
    @Title(zh_CN='站点')
    Site site
    @Title(zh_CN='栏目名称')
    String name
    @Title(zh_CN='列表模板')
    String templateList
    @Title(zh_CN='内容模板')
    String templateDetail
    @Title(zh_CN='是否启用')
    Boolean enabled=true
    @Title(zh_CN='需要审核')
    Boolean needPreview=false
    @Title(zh_CN='排序号')
    Integer sequencer=9999999
    @Title(zh_CN='展示位置')
    Integer positions=0//首页为1
    @Title(zh_CN='是否显示详情')
    Boolean showFirst=false
    @Title(zh_CN='是否显示日期来源作者')
    Boolean showTimeAuthor=true
    @Title(zh_CN='不显示子栏目')
    Boolean nochildren=false
    @Title('允许评论')
    Boolean allowComment=true
    @Title(zh_CN='创建时间')
    Date dateCreated
    @Title(zh_CN='修改时间')
    Date lastUpdated

    static belongsTo =[parent:Catalog]
    static hasMany = [children:Catalog]
    static constraints = {
        name(size:0..100,blank: false)
        parent(nullable:true)
        site(nullable:false)
        needPreview(nullable:true)
        enabled(nullable:true)
        sequencer(nullable:true)
        templateList(size:0..50,blank: true, nullable:true)
        templateDetail(size:0..50,blank: true, nullable:true)
        positions(nullable:true)
        showFirst(nullable:true)
        showTimeAuthor(nullable:true)
        nochildren(nullable:true)
        allowComment(nullable:true)
    }

    String toString(){
        return name
    }

    static mapping = {
        children sort:"id"
    }

    static String  hasCatalog(Catalog cat,List ids){
        if(cat.parent){
            if(ids.contains(cat.parentId)){
                return  cat.parent.name;
            }
            return hasCatalog(cat.parent,ids)
        }else{
            return null;
        }
    }

}
