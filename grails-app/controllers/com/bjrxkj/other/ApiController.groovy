package com.bjrxkj.other

import com.bjrxkj.cms.Catalog
import com.bjrxkj.cms.News
import com.ricicms.utils.ServerResponse
import org.hibernate.criterion.CriteriaSpecification

class ApiController {

    def grailsApplication;

    /**
     * 栏目列表
     */
    def describeCatalogs(){
        if(!params.max) params.max='10';
        if(!params.offset) params.offset ='0';
        def sortMap = ['sequencer':'asc','id':'desc'];
        try{
            def catalogs=[];
            if(params.positions){
                def siteId=grailsApplication.config.project.setting.defaultSite;
                catalogs= Catalog.createCriteria().list ([max: params.max.toInteger(),
                                                          offset: params.offset.toInteger()]) {
                    projections{
                        property('id','id')
                        property('name','name')
                        property('allowComment','allowComment')
                        property('templateList','templateList')
                        property('templateDetail','templateDetail')
                    }
                    eq("positions",params.positions.toInteger())
                    eq("enabled",true)
                    eq("site.id",siteId.toLong());
                    setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
                    sortMap.keySet().each {st->
                        order(st,sortMap[st])
                    }
                }
            }
            render ServerResponse.success(catalogs);
        }catch(e){
            render ServerResponse.fail();
        }

    }

    /**
     * 稿件列表
     */
    def describeNewsByCatalog(){
        if(!params.max) params.max='10';
        if(!params.offset) params.offset ='0';
        def sortMap = ['sequencer':'asc','id':'desc'];
        try{
            def newsList=[];
            if(params.catalogId){
                newsList= News.createCriteria().list ([max   : params.max.toInteger(),
                                                       offset: params.offset.toInteger()]) {
                    projections{
                        property('id','id')
                        property('title','title')
                        property('subtitle','subtitle')
                        property('publishDate','publishDate')
                        property('picture','picture')
                        property('clicknum','clicknum')
                    }
                    eq("catalog.id",params.catalogId.toLong())
                    eq("state","发布")
                    setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
                    sortMap.keySet().each {st->
                        order(st,sortMap[st])
                    }
                }
            }
            render ServerResponse.success(newsList);
        }catch(e){
            render ServerResponse.fail();
        }
    }

    /**
     * 稿件详情
     * @return
     */
    def describeNewsDetail(){
        def news;
        try{
            if(params.newsId){
                news=News.createCriteria().get{
                    projections{
                        property('id','id')
                        property('title','title')
                        property('subtitle','subtitle')
                        property('publishDate','publishDate')
                        property('picture','picture')
                        property('clicknum','clicknum')
                        property('source','source')
                        property('author','author')
                        property('outline','outline')
                        property('redirectURL','redirectURL')
                        property('picture','picture')
                        property('content','content')
                        property('allowComment','allowComment')
                    }
                    eq("id",params.newsId.toLong())
                    eq("state","发布")
                    setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
                    maxResults(1)
                }
            }
            if(news){
                News.executeUpdate("update News set clicknum=clicknum+1 where id=?",[params.newsId.toLong()]);
                render ServerResponse.success(news);
            }else{
                render ServerResponse.fail("文章不存在");
            }
        }catch(e){
            log.error(e.message);
            render ServerResponse.fail();
        }
    }
}
