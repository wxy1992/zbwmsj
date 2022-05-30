package ricicms

import com.bjrxkj.cms.Catalog
import com.bjrxkj.cms.behaviour.Commentary
import com.bjrxkj.cms.News
import com.bjrxkj.cms.Site
import com.bjrxkj.cms.behaviour.Favourite
import com.bjrxkj.core.BaseUser
import grails.plugin.cache.GrailsConcurrentMapCache
import grails.transaction.Transactional
import org.hibernate.criterion.CriteriaSpecification

@Transactional
class CmsService {
    def grailsApplicaion;
    def grailsCacheManager;
    /**
     * 创建站点的默认栏目
     * @param site
     * @return
     */
    def createCatalogBySite(Site site){
        def catalogMap= Catalog.indexCatalogMap;
        catalogMap.keySet().eachWithIndex{de,i->
            def num=Catalog.countByNameAndSite(de,site);
            if(!num){
                def c=new Catalog(name:de,site: site,sequencer:i+1,showIndex: true,allowComment: true);
                if(c.save(flush: true)){
                    catalogMap[de].eachWithIndex{ca,j->
                        def c1=Catalog.countByParentAndName(c,ca);
                        if(!c1){
                            c1=new Catalog(name:ca,site: site,parent:c,sequencer:j+1,showIndex: true);
                            c1.save(flush: true);
                        }
                    }
                };
            }
        }
    }

    /**
     * 评论
     * @param params
     * @param baseUser
     * @return
     */
    Map submitComment(Map params, BaseUser baseUser){
        def map=[:];
        if(params.newsId&&params.content&&baseUser){
            def Commentary=new Commentary();
            Commentary.news= News.get(params.newsId.toLong());
            Commentary.content=params.content;
            Commentary.baseUser=baseUser;
            if(Commentary.save(flush: true)){
                map['result']=true;
                map['message']="评论成功";
            }else{
                map['result']=false;
                map['message']="评论失败";
            }
        }else{
            map['result']=false;
            map['message']="请登录并完善评论";
        }
        return map;
    }

    /**
     * 评论列表
     * @param params
     * @return
     */
    Map commentJson(def params) {
        params.max = Math.min(params.limit ? params.int('limit') : 10, 100);
        params.limit = params.max;
        if (!params.offset) params.offset = '0'
        if (!params.sort) params.sort = 'id'
        if (!params.order) params.order = 'desc'
        def rows = Commentary.createCriteria().list([max:params.max.toInteger(),offset:params.offset.toInteger()]) {
            createAlias('news','n')
            projections{
                property('id','id')
                property('baseUser.id','baseUserId')
                property('content','content')
                property('news.id','newsId')
                property('n.title','title')
                property('dateCreated','dateCreated')
            }
            if (params?.baseUserId) {
                eq("baseUser.id", params?.baseUserId)
            }
            if (params?.newsId) {
                eq("news.id", params?.newsId.toLong())
            }
            setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
            order(params.sort, params.order)
        }
        def map = [:];
        map.total = rows.totalCount;
        map.rows = rows.resultList;
        return map;
    }
    /**
     * 我的
     * @param params
     * @return
     */
    Map collectionJson(def params) {
        params.max = Math.min(params.limit ? params.int('limit') : 10, 100);
        params.limit = params.max;
        if (!params.offset) params.offset = '0'
        if (!params.sort) params.sort = 'id'
        if (!params.order) params.order = 'desc'
        def rows = Favourite.createCriteria().list([max:params.max.toInteger(),offset:params.offset.toInteger()]) {
            projections{
                property('contentId','contentId')
                property('type','type')
                property('baseUser.id','baseUserId')
                property('dateCreated','dateCreated')
            }
            if (params?.baseUserId) {
                eq("baseUser.id", params?.baseUserId)
            }
            if (params?.type) {
                eq("type", params?.type)
            }
            setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
            order(params.sort, params.order)
        }
        def map = [:];
        def rowsList=[];
        rows.resultList.each{
            def objmap=[:],nc=it.contentId.tokenize('-'),newsId=(nc?.getAt(0));
            objmap['contentId']=it.contentId;
            objmap['type']=it.type;
            objmap['newsId']=newsId;
            objmap['baseUserId']=it.baseUserId;
            objmap['title']=News.get(newsId.toLong()?:-1l).title;
            objmap['Commentary']='';
            if(it.type=='Commentary'){
                objmap['Commentary']=Commentary.get(nc?.getAt(1)?:-1l).content;
            }
            objmap['dateCreated']=it.dateCreated;
            rowsList<<objmap;
        }
        map.total = rows.totalCount;
        map.rows = rowsList;
        return map;
    }
    /**
     * 点赞收藏
     * @param params
     * @param baseUser
     * @return
     */
    Boolean commentOrNewsLike(def params,BaseUser baseUser){
        def res=false;
        if (params.contentId && baseUser && params.optype && params.type) {
            if(params.optype=='cancel'){
                Favourite.where { baseUser == baseUser && contentId == params.contentId}.deleteAll();
                res=true;
            }else{
                def col=new Favourite(baseUser:baseUser);
                col.contentId=params.contentId;
                col.type=params.type;
                if(col.save(flush: true)){
                    res=true;
                }else{
                    log.error(col.errors)
                }
            }
        }
        return res;
    }

    def siteLayoutSeting(def servletContext,def site){
        def template=site?.template;
        def siteId=site?.id;
        if(!servletContext.getAttribute("site-${siteId}")){
            def catalogs=[:];
            def clist=Catalog.executeQuery("select name,id from Catalog c where c.site.id=? and c.showIndex=?",[siteId,true]);
            clist.each{c->
                catalogs[c[0]]=c[1];
            }
            def contentMap=['template':template,'catalogs':catalogs,
                            'site':site,'staticResource':"${servletContext.contextPath}/sites/template/${template}"]
            servletContext.setAttribute("site-${siteId}",contentMap);
        }
    }

    def cacheByName(String cacheName){
        GrailsConcurrentMapCache cache = grailsCacheManager.getCache(cacheName);
        cache.allKeys.each { key ->
            println key
        }
    }

    def cacheEvictByDomain(String cacheName,String domains){
        GrailsConcurrentMapCache cache = grailsCacheManager.getCache(cacheName);
        cache.allKeys.each { key ->
            domains?.tokenize(',').each{word->
                if(key.toString().contains(word)){
                    cache.evict(key);
                }
            }
        }
    }
    def cacheSomeEvict(String cacheName,String keywords){
        GrailsConcurrentMapCache cache = grailsCacheManager.getCache(cacheName);
        cache.allKeys.each { key ->
            if(key.toString().contains(keywords)){
                cache.evict(key);
            }
        }
    }
    def cacheEvict(String cacheName,String cacheKey){
        GrailsConcurrentMapCache cache = grailsCacheManager.getCache(cacheName);
        cache.evict(cacheKey);
    }
    def cacheClear(String cacheName){
        GrailsConcurrentMapCache cache = grailsCacheManager.getCache(cacheName);
        cache.clear();
    }

    def clearNewsCache(){
        ['siteIndex','newsTopTag','newsTop','newsDetail','newsSearch'].each{cacheName->
            GrailsConcurrentMapCache cache = grailsCacheManager.getCache(cacheName);
            cache.clear();
        }
    }
}
