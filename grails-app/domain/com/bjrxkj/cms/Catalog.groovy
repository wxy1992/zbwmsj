package com.bjrxkj.cms

import com.bjrxkj.annotation.Title

class Catalog {
    @Title(zh_CN='站点')
    Site site
    @Title(zh_CN='栏目名称')
    String name
    @Title(zh_CN='列表模板')
    String templateTop
    @Title(zh_CN='内容模板')
    String templateDetail
    @Title(zh_CN='创建时间')
    Date dateCreated
    @Title(zh_CN='修改时间')
    Date lastUpdated
    @Title(zh_CN='是否禁用')
    Boolean disabled=false
    @Title(zh_CN='需要初步审核')
    Boolean needPreview=false
    @Title(zh_CN='需要拟发审核')
    Boolean needPubPreview=false
    @Title(zh_CN='排序号')
    Integer sequencer=9999999
    @Title(zh_CN='父目录路径')
    String parentIds
    @Title(zh_CN='父目录路径')
    String parentNames
    @Title(zh_CN='是否首页显示')
    Boolean showIndex=false
    @Title(zh_CN='是否显示详情')
    Boolean showFirst=false
    @Title(zh_CN='是否显示日期来源作者')
    Boolean showTimeAuthor=true
    @Title(zh_CN='不显示子栏目')
    Boolean nochildren=false
    @Title('允许评论')
    Boolean allowComment=true

    static belongsTo =[parent:Catalog]
    static hasMany = [children:Catalog]
    static constraints = {
        name(size:0..100,blank: false)
        parent(nullable:true)
        site(nullable:false)
        needPreview(nullable:true)
        needPubPreview(nullable:true)
        disabled(nullable:true)
        sequencer(nullable:true)
        templateTop(size:0..50,blank: true, nullable:true)
        templateDetail(size:0..50,blank: true, nullable:true)
        parentIds(nullable:true)
        parentNames(nullable:true)
        showIndex(nullable:true)
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
    static getAll(){
        def catalogs=Catalog.list()
        catalogs=catalogs.sort{it.id}
        return catalogs
    }
    String findLocation(){
        if(this.parent){
            def names=this.getMapName(this.parent,this.name)
            return '当前位置：'+names
        }else{
            return '当前位置：'+this.name
        }
    }
    static String getMapName(Catalog cat,String mapName){
        if(cat.parent){
            return getMapName(cat.parent,cat.name+'-'+mapName)
        }else{
            return cat.name+'-'+mapName
        }
    }

    static List getMapName(Catalog cat,List mapName){
        if(cat.parent){
            return getMapName(cat.parent,mapName<<cat)
        }else{
            return mapName<<cat
        }
    }

    def beforeInsert(){
        List parentlist=Catalog.getMapName(this,[]);
        this.parentIds=parentlist.collect{it.id}.join(',');
        this.parentNames=parentlist.collect{it.name}.join(',');
    }

    def beforeUpdate(){
        List parentlist=Catalog.getMapName(this,[]);
        this.parentIds=parentlist.collect{it.id}.join(',');
        this.parentNames=parentlist.collect{it.name}.join(',');
    }

    static String parentUrl(Catalog cat,String mapName){
        if(cat?.parent){
            return parentUrl(cat?.parent,cat?.id+','+mapName)
        }else{
            return cat?.id+','+mapName;
        }
    }
    static String parentName(Catalog cat,String mapName){
        if(cat?.parent){
            return parentName(cat?.parent,cat?.name+','+mapName)
        }else{
            return cat?.name+','+mapName;
        }
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
