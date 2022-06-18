package business

import com.wmsj.business.Apply
import com.wmsj.business.Trade
import com.wmsj.business.TradeType
import com.wmsj.cms.behaviour.Commentary
import com.wmsj.core.BaseUser
import com.wmsj.core.WxUser
import grails.transaction.Transactional
import org.hibernate.criterion.CriteriaSpecification

@Transactional
class TradeService {

    /**
     * 服务类型
     * @param params
     * @return
     */
    List describeTradeTypes(def params){
        def sortMap = ['sq':'asc','id':'desc'];
        def typeResult= TradeType.createCriteria().list() {
            projections{
                property('id','id')
                property('name','name')
            }
            setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
            sortMap.keySet().each {st->
                order(st,sortMap[st])
            }
        }
        return typeResult;
    }
    /**
     * 服务列表
     * @param params
     * @return
     */
    Map describeTrades(def params,def wxUser){
        def sortMap = ['sequencer':'asc','id':'desc'];
        def tradeResult= Trade.createCriteria().list([max   : params.max.toInteger(),
                                                      offset: params.offset.toInteger()]) {
            createAlias("organization","o")
            projections{
                property('id','id')
                property('title','title')
                property('o.name','organizationName')
                property('peopleNum','peopleNum')
                property('dateCreated','dateCreated')
                property('status','status')
            }
            if(params.tradeTypeId){
                eq("tradeType.id",params.tradeTypeId.toLong())
            }
            if(params.status){
                eq("status",params.status.toInteger())
            }
            ge("status",20)
            setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
            sortMap.keySet().each {st->
                order(st,sortMap[st])
            }
        }
        def tradeList=tradeResult.resultList;
        //报名情况
        def applynum=Apply.createCriteria().list{
            projections {
                count("creator.id","num")
                groupProperty("trade.id","tradeId")
            }
            inList("trade.id",tradeList*.id)
            eq("approve",true)
            eq("deleted",false)
        }
        def resultMap=[:];
        tradeList.each{tradeMap->
            def tradeId=tradeMap?.get("id");
            def currentApplyNum= applynum.find{return it.getAt(1)==tradeId}?.getAt(0);
            tradeMap["remain"]=tradeMap.getAt("peopleNum")-(currentApplyNum?:0);
            tradeMap["isApplied"]=Apply.createCriteria().count{
                eq("creator.id",wxUser.id)
                eq("trade.id",tradeId)
                eq("deleted",false)
            }
        }
        resultMap["total"]=tradeResult.totalCount;
        resultMap["rows"]=tradeList;
        return resultMap;
    }

    /**
     * 服务详情
     * @param params
     * @return
     */
    Map describeTradeDetail(def params, WxUser wxUser){
        Map trade;
        if(params.tradeId){
            trade=Trade.createCriteria().get {
                createAlias("organization","o")
                projections{
                    property('id','id')
                    property('title','title')
                    property('content','content')
                    property('way','way')
                    property('status','status')
                    property('peopleNum','peopleNum')
                    property('beginDate','beginDate')
                    property('endDate','endDate')
                    property('o.id','organizationId')
                    property('o.name','organizationName')
                    property('telephone','telephone')
                    property('address','address')
                }
                setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
                eq("id",params.tradeId.toLong())
                maxResults(1)
            }
            trade["isApplied"]=Apply.createCriteria().count{
                eq("creator.id",wxUser.id)
                eq("trade.id",params.tradeId.toLong())
                eq("deleted",false)
            }
            trade["isApplied"]=Commentary.createCriteria().count{
                createAlias("apply","a")
                createAlias("a.trade","t")
                eq("creator.id",wxUser.id)
                eq("t.id",params.tradeId.toLong())
            }
        }
        return trade;
    }

    /**
     * 报名服务
     * @param params
     * @return
     */
    Map tradeApply(def params, WxUser wxUser){
        def resultMap=[:];
        resultMap.result=false;
        resultMap.data=null;
        if (params.tradeId && params.name && params.idcard && params.telephone && params.address) {
            Apply apply=new Apply();
            apply.trade=Trade.get(params.tradeId.toLong());
            apply.name=params.name.trim();
            apply.idcard=params.idcard.trim();
            apply.telephone=params.telephone.trim();
            apply.address=params.address.trim();
            apply.creator=wxUser;
            if(apply.save(flush: true)){
                resultMap.result=true;
                resultMap.data=apply.id;
            }else{
                log.error(apply.errors);
            }
        }
        return resultMap;
    }

    /**
     * 评论
     * @param params
     * @param baseUser
     * @return
     */
    Map submitCommentary(Map params, WxUser wxUser){
        def map=[:];
        map['result']=false;
        map['message']="请完善评论";
        map['data']=0;
        if(params.applyId&&params.score&&params.content&&wxUser){
            def commentary=new Commentary();
            commentary.apply= Apply.get(params.applyId.toLong());
            commentary.score=params.score.toInteger();
            commentary.content=params.content;
            commentary.creator=wxUser;
            commentary.createdBy=wxUser.name;
            if(commentary.save(flush: true)){
                map['result']=true;
                map['message']="评论成功";
                map['data']=commentary.id;
            }else{
                log.error(commentary.errors);
                map['result']=false;
                map['message']="评论失败";
            }
        }
        return map;
    }


    /**
     * 评论列表
     * @param params
     * @return
     */
    Map commentaryJson(def params) {
        def result = Commentary.createCriteria().list([max:params.max.toInteger(),offset:params.offset.toInteger()]) {
            createAlias("apply","a")
            createAlias("a.trade","t")
            projections{
                property('id','id')
                property('creator.id','creatorId')
                property('score','score')
                property('createdBy','createdBy')
                property('content','content')
                property('dateCreated','dateCreated')
            }
            if (params?.tradeId) {
                eq("t.id", params?.tradeId.toLong())
            }
            setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
            order("id", "desc")
        }
        def map = [:];
        map.total = result.totalCount;
        map.rows = result.resultList;
        return map;
    }
}
