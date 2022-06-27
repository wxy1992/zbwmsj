package cms


import com.wmsj.cms.Catalog
import com.wmsj.business.Commentary
import com.wmsj.cms.News
import com.wmsj.cms.behaviour.Favourite
import com.wmsj.core.BaseUser
import com.wmsj.core.WxUser
import com.wmsj.utils.HttpClientUtils
import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.json.JSONObject
import org.hibernate.criterion.CriteriaSpecification

@Transactional
class CmsService {

    def grailsApplication;

    /**
     * 根据code获取用户id
     * @param params
     * @return
     */
    Map code2Userid(def params){
        def appid=grailsApplication.config.project.miniprogram.appid;
        def secret=grailsApplication.config.project.miniprogram.secret;
        def code2openidUrl=grailsApplication.config.project.miniprogram.code2openidUrl+
                "&appid=${appid}&secret=${secret}&js_code=${params.code}";
        def client=new HttpClientUtils();
        def result=client.httpsConnectionJson(code2openidUrl, "","GET",null);
        def result_json=new JSONObject(result);
        println "code2Userid_json："+result_json
        def resultMap=[:];
        resultMap["userid"]=null;
        resultMap["openid"]=result_json.openid?:"";
        resultMap["errcode"]=result_json.errcode;
        resultMap["errmsg"]=result_json.errmsg?:"";
        if(result_json.openid){
            def wxUser= WxUser.findByOpenId(result_json.openid);
            if(!wxUser){
                wxUser=new WxUser();
                wxUser.openId=resultMap["openid"];
                if(!wxUser.save(flush: true)){
                    resultMap["errmsg"]=wxUser.errors;
                }
            }
            resultMap["userid"]=wxUser.id;
        }
        return resultMap;
    }

    /**
     * 更新用户信息
     * @param params
     * @return
     */
    Boolean updateUserInfo(def params){
        println params
        Boolean result=false;
        if(params.userid){
            def user=WxUser.get(params.userid.toLong());
            user.nickname=params.nickName;
            user.avatarUrl=params.avatarUrl;
            user.province=params.province;
            user.city=params.city;
            user.language=params.language;
            user.gender=params.gender?.toInteger();
            if(user.save(flush: true)){
                result=true;
            }else{
                log.error(user.errors);
            }
        }
        return result;
    }

    /**
     * 获取AccessToken
     * @return
     */
    Map getAccessToken(){
        def appid=grailsApplication.config.project.miniprogram.appid;
        def secret=grailsApplication.config.project.miniprogram.secret;
        def getAccessTokenUrl=grailsApplication.config.project.miniprogram.getAccessToken+"&appid=${appid}&secret=${secret}";
        def client=new HttpClientUtils();
        def result=client.httpsConnectionJson(getAccessTokenUrl, "","GET",null);
        def result_json=new JSONObject(result);
        println "acsessToken_json："+result_json;
        def resultMap=[:];
        resultMap["access_token"]=result_json.access_token;
        resultMap["expires_in"]=result_json.expires_in;
        resultMap["errcode"]=result_json.errcode;
        resultMap["errmsg"]=result_json.errmsg?:"";
        return resultMap;
    }

    /**
     * 更新用户手机号
     * @param params
     * @return
     */
    Map updateUserTel(def params){
        if(!params.access_token||!params.userid){
            return false;
        }
        def getUserTelUrl=grailsApplication.config.project.miniprogram.getUserTel+
                "?access_token=${params.access_token}";
        def client=new HttpClientUtils();
        def result=client.httpsConnectionJson(getUserTelUrl, "{\"code\": \"${params.code}\"}","POST",null);
        def result_json=new JSONObject(result);
        println "userphone_json："+result_json;
        def resultMap=[:];
        resultMap["updated"]=false;
        if (result_json.errcode == 0 && result_json.phone_info) {
            def phone_json=new JSONObject(result_json.phone_info);
            println "phone_json："+phone_json;
            resultMap["phoneNumber"]=phone_json.phoneNumber;
            resultMap["purePhoneNumber"]=phone_json.purePhoneNumber;
            def user=WxUser.get(params.userid.toLong());
            user.tel=phone_json.phoneNumber;
            user.pureTel=phone_json.purePhoneNumber;
            if(user.save(flush: true)){
                resultMap["updated"]=true;
            }
        }
        return resultMap;
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
                property('positions','positions')
                property('allowComment','allowComment')
                property('templateList','templateList')
                property('templateDetail','templateDetail')
            }
            if(params.positions){
                eq("positions",params.positions.toInteger())
            }
            if(params.parentId){
                parent{
                    eq("id",params.parentId.toLong())
                }
            }
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

}
