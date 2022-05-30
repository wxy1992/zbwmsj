package com.bjrxkj.cms

import grails.plugin.springsecurity.SpringSecurityService
import grails.converters.JSON

class CatalogController {

    SpringSecurityService springSecurityService
    //新增和修改
    def createOrEdit(){
        Site site;
        List<Catalog> siteCatalog=[];
        Catalog catalogInstance;
        if(params.id && params.site!="#"){
            catalogInstance=Catalog.get(params.id)
        }
        if(params.site){   //新增栏目，站点为传过来的站点值
            if(params.site=="#"){
                site=Site.findById(params.id?.replace('P_','')?:-1l)
            }else{
                //修改多级栏目，先判断该栏目所属站点
                if(params.parents && params.parents.length>2){
                    def siteId=(params.parents[params.parents.length-2]).replace('P_','');
                    site=Site.findById(siteId?:-1l)
                }else{
                    site=Site.findById(params.site?.replace('P_','')?:-1l)
                }
            }
        }else{   //通过列表下进行修改栏目，站点为该栏目站点
               site=catalogInstance?.site
        }
        siteCatalog=Catalog.findAllBySite(site);
        return ['catalogInstance':catalogInstance,site:site,siteCatalog:siteCatalog]
    }
    //保存或修改
   def saveOrUpdate(){
       def map=[:]
       Catalog catalogInstance;
       if(params.catalogId){
           catalogInstance=Catalog.get(params.catalogId)
       }else{
           catalogInstance = new Catalog()
       }
       catalogInstance.properties=params;
       if(params['parent.id']?.startsWith('P_')){
           catalogInstance.parent=null;
       }
       if(catalogInstance.parent){
           catalogInstance.parentIds=Catalog.parentUrl(catalogInstance.parent,'');
           catalogInstance.parentNames=Catalog.parentName(catalogInstance.parent,'');
       }
       catalogInstance.save(flush:true)
       if(catalogInstance.hasErrors()) {
           log.error(catalogInstance.errors.toString());
       }
       else {
           map.result=true;
           map.message="操作成功";
       }
       render "${map as JSON}";
   }
    //列表
    def list(){
        params.siteId=(params.siteId&&params.siteId?.startsWith("P_"))?params.siteId?.replace('P_',''):params.siteId;
    }
    def catalogJsonSearch(def params,boolean isList){
        return Catalog.withCriteria{
            if (params.keywords) {
                like("name", "%" + params.keywords + "%")
            }
            site {
                if (params.siteId) {
                    eq("id", params.siteId.toLong())
                }
                order('sequencer','asc')
            }
            if(isList){
                order('sequencer','asc')
                order('id','desc')
                maxResults(params.max.toInteger())
                firstResult(params.offset.toInteger())
            }else{
                projections{
                    count('id')
                }
            }
        }
    }
    def json(){
        if(!params.max) params.max='10'
        if(!params.offset) params.offset ='0'
        def objs,objsCount
        if(params.siteId){
            objs=catalogJsonSearch(params,true);
            objsCount=catalogJsonSearch(params,false);
        }else{
            objsCount=0
        }
        def map=[:],list=[];
        objs.each{Catalog
            def m=[:];
            m.id=it.id;
            m.name=it.name;
            m.parent=it.parent?it.parent.name:""
            m.needPreview=(it.needPreview||it.needPubPreview)?'是':'否';
            list<<m;
        }
        map.rows=list;
        map.total=objsCount;
        render "${map as JSON}";
    }

    def batchDelete(){
        def map=[:];
        map.result=false;
        map.message="网络错误，请重试！";
        def ids = params.fields?.split(',').toList().collect {it.toLong()};
        try{
            def num=News.createCriteria().count{
                inList('catalog.id',ids)
            }
            def numc=Catalog.createCriteria().count{
                inList('parent.id',ids)
            }
            if(num>0||numc>0){
                map.message="请先删除该栏目及其子栏目下内容";
            }else{
                Catalog.where {id in ids}.deleteAll();
                map.result=true;
                map.message="操作成功";
            }
        }catch(e){
            map.message="请先删除该栏目及其子栏目下内容";
        }
        render "${map as JSON}"
    }

    //删除栏目
    def delCatalog(){
        def map=[:]
        def site
        def catalogInstance=Catalog.get(params.id);
        if(catalogInstance){
            site=catalogInstance?.site?.id
            def newsNum=News.countByCatalog(catalogInstance);
            if(newsNum>0){
                map.result=false
                map.message="请先删除该栏目稿件信息"
            }else{
               catalogInstance.delete(flush:true)
               if(catalogInstance.hasErrors()){
                  map.result=false
                  map.message="请先删除该栏目稿件信息"
               }else{
                  map.result=true
                  map.message="删除成功"
               }
            }
        }else{
            map.result=false
            map.message="该数据不存在"
        }
        map.site=site
        render "${map as JSON}";
    }

    /**
     * 修改栏目父栏目路径
     */
    def changeCatalogParentIds(){
        Catalog.findAllByParentIsNotNull().each{c->
            c.parentIds=Catalog.parentUrl(c?.parent,'');
            c.parentNames=Catalog.parentName(c?.parent,'');
            println "${c.id}-${c.parentIds}"
            c.save(flush:true);
        }
    }
}
