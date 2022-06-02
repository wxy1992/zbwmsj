package com.bjrxkj.business

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.transaction.Transactional
import org.hibernate.criterion.CriteriaSpecification

class TradeController {
    def springSecurityService;
    def list(){
        def tradeTypes=TradeType.list([sort: 'sq',order: 'asc']);
        return [tradeTypes:tradeTypes];
    }

    def json(){
        if(!params.max) params.max='20';
        if(!params.offset) params.offset ='0';
        def currentUser=springSecurityService.currentUser;
        def sortMap = ['id':'desc'];
        def newsResult=Trade.createCriteria().list ([max: params.max.toInteger(),offset: params.offset.toInteger()]){
            createAlias('organization','o')
            eq("deleted",false)
            projections{
                property('id','id')
                property('title','title')
                property('o.name','organization')
                property('beginDate','beginDate')
                property('endDate','endDate')
                property('status','status')
            }
            if(!SpringSecurityUtils.ifAnyGranted("ROLE_ADMIN")){
                eq("o.id",currentUser.organizationId)
            }
            if(params.organization){
                eq("o.id",params.organization.toLong())
            }
            if(params.status){
                eq("status",params.status.toInteger())
            }
            if(params.title){
                like("title","%"+params.title.trim()+"%")
            }
            if(params.beginDate){
                ge("beginDate",Date.parse("yyyy-MM-dd HH:mm:ss",params.beginDate+" 00:00:00"))
            }
            if(params.endDate){
                le("endDate",Date.parse("yyyy-MM-dd HH:mm:ss",params.endDate+" 23:59:59"))
            }
            setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
            sortMap.keySet().each {st->
                order(st,sortMap[st])
            }
        }
        def map=[:]
        newsResult.resultList.each{row->
            row.statusName= Trade.STATUSMAP.get(row.status);
        }
        map.rows=newsResult.resultList;
        map.total=newsResult.totalCount;
        render "${map as JSON}";
    }

    def createOrEdit(){
        def baseUser=springSecurityService.currentUser;
        Trade trade;
        if(params.id){
            trade = Trade.get(params.id.toLong());
        }else{
            trade = new Trade(params);
        }
        def today=new Date();
        def tradeTypes=TradeType.list([sort: 'sq',order: 'asc']);
        return [user:baseUser,trade:trade,today:today,tradeTypes:tradeTypes]
    }

    @Transactional
    def saveOrUpdate(){
        def map=[:];
        map.result=false;
        map.message="操作失败";
        Trade trade;
        if(params.id){
            trade = Trade.get(params.id.toLong());
        }else{
            trade = new Trade(params);
        }
        trade.properties=params;
        if(!params."organization.id"){
            trade.organization=springSecurityService.currentUser.organization;
        }
        if(params.operation=='发布'){
            if(SpringSecurityUtils.ifAnyGranted("ROLE_ADMIN")){
                trade.status= 20;//发布中
            }else if(SpringSecurityUtils.ifAllGranted("ROLE_SUBADMIN")){
                trade.status= 10;//已提交
            }
        }else{//草稿
            trade.status= 0;
        }
        if(trade.save(flush: true)){
            map.result=true;
            map.message="操作成功";
        }else{
            log.error(trade.errors);
        }
        render map as JSON;
    }

    def deleteTrade(){
        def map=[:];
        map.result=false;
        map.message="网络错误，请重试！";
        if(params.id){
            int num=Trade.executeUpdate("update Trade set deleted=1 where id=?",[params.id]);
        }
        render "${map as JSON}";
    }
}
