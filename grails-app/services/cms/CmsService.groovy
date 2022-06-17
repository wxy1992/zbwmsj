package cms

import com.wmsj.business.Apply
import com.wmsj.business.Trade
import com.wmsj.cms.Catalog
import com.wmsj.cms.behaviour.Commentary
import com.wmsj.cms.News
import com.wmsj.cms.behaviour.Favourite
import com.wmsj.core.BaseUser
import grails.transaction.Transactional
import org.hibernate.criterion.CriteriaSpecification

@Transactional
class CmsService {

    def grailsApplication;


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

    /**
     * 栏目列表
     * @param params
     * @return
     */
    List describeCatalogs(def params){
        def sortMap = ['sequencer':'asc','id':'desc'];
        def siteId=grailsApplication.config.project.setting.defaultSite;
        def catalogs= Catalog.createCriteria().list ([max   : params.max.toInteger(),
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
        return catalogs;
    }

    /**
     * 稿件列表
     * @param params
     * @return
     */
    Map describeNewsByCatalog(def params){
        def sortMap = ['sequencer':'asc','id':'desc'];
        def newsList= News.createCriteria().list ([max   : params.max.toInteger(),
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
        def resultMap=[:];
        resultMap["total"]=newsList.totalCount;
        resultMap["rows"]=newsList.resultList;
        return resultMap;
    }

    /**
     * 稿件详情
     * @return
     */
    Map describeNewsDetail(def params){
        Map news=News.createCriteria().get{
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
        if(news){
            News.executeUpdate("update News set clicknum=clicknum+1 where id=?",[params.newsId.toLong()]);
        }
        return news;
    }

}
