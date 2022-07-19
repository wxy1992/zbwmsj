package com.wmsj.business

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
            createAlias('tradeType','t')
            eq("deleted",false)
            projections{
                property('id','id')
                property('title','title')
                property('t.name','typeName')
                property('o.name','organization')
                property('beginDate','beginDate')
                property('endDate','endDate')
                property('status','status')
                property('backreason','backreason')
                property('way','way')
            }
            if(SpringSecurityUtils.ifAnyGranted("ROLE_ADMIN")){//管理员
                if(params.operation=='todo'){
                    eq("status",10)//待办已提交服务
                }else{
                    or{
                        inList("status",[10,20,30])
                        and{
                            eq("o.id",currentUser.organizationId)
                            inList("status",[0,5])
                        }
                    }
                }
            }else{//二级用户只管理本单位服务
                eq("o.id",currentUser.organizationId)
                if(params.operation=='todo'){//待办退回服务
                    eq("status",5)
                }
            }
            if(params.tradeType){
                eq("t.id",params.tradeType.toLong())
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
            row.statusName = Trade.STATUSMAP.get(row.status);
            row.wayName = Trade.WAYMAP.get(row.way);
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
        if(trade.endDate.before(trade.beginDate)){
            map.message="结束时间不能早于开始时间";
            render map as JSON;
            return;
        }
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

    /**
     * 删除工单
     * @return
     */
    def deleteTrade(){
        def map=[:];
        map.result=false;
        map.message="数据不存在";
        if(params.id){
            int num=Trade.executeUpdate("update Trade set deleted=1 where id=?",[params.id?.toLong()]);
            if(num>0){
                map.result=true;
                map.message="操作成功";
            }
        }
        render "${map as JSON}";
    }


    /**
     * 审核工单
      * @return
     */
    def changeTradeStatus(){
        def map=[:];
        map.result=false;
        map.message="网络错误，请重试";
        def operations=Trade.OPERATIONMAP;
        if(!["提交","发布","退回"].contains(params.operation)&&!operations.get(params.operation)){
            render "${map as JSON}";
            return;
        }
        def ids = params.fields?.split(',').toList().collect {it.toLong()};
        try{
            def updateMap=[:];
            updateMap.status=operations.get(params.operation);
            updateMap.backreason=params.backreason;
            updateMap.auditDate=new Date();
            updateMap.auditUser=springSecurityService.currentUser;
            Trade.where{id in ids}.updateAll(updateMap);
            if(updateMap.status==30){//服务结束，所有报名《已完成》状态
                Apply.executeUpdate("update Apply set status=20 where trade.id in (${params.fields})");
            }
            map.result=true;
            map.message="操作成功";
        }catch(e){
            log.error(e.message);
        }
        render "${map as JSON}";
    }

    /**
     * 服务成果
     * @return
     */
    def achievement(){
        Achievement achievement;
        Trade trade;
        if(params.tradeId){
            achievement=Achievement.createCriteria().get {
                eq("trade.id",params.tradeId.toLong())
                maxResults(1)
            }
            if(!achievement){
                achievement=new Achievement();
                trade=Trade.get(params.tradeId.toLong());
                achievement.trade=trade;
            }
        }
        return [achievement:achievement,trade:trade];
    }

    /**
     * 保存服务成果
     * @return
     */
    def saveAchievement() {
        def map = [:];
        map.result = false;
        map.message = "网络错误";
        if (params.tradeId) {
            try {
                Achievement achievement = Achievement.createCriteria().get {
                    eq("trade.id", params.tradeId.toLong())
                    maxResults(1)
                }
                if(!achievement){
                    achievement=new Achievement();
                    achievement.trade=Trade.get(params.tradeId.toLong());
                }
                achievement.properties=params;
                achievement.baseUser=springSecurityService.currentUser;
                if(achievement.save(flush: true)){
                    map.result = true;
                    map.message = "保存成功";
                }else{
                    log.error(achievement.errors);
                }
            } catch (e) {
                log.error(e.message);
            }
        }
        render "${map as JSON}";
    }
}
