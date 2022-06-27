package com.wmsj.business

import grails.converters.JSON
import grails.transaction.Transactional
import org.hibernate.criterion.CriteriaSpecification

@Transactional
class CommentaryController {


    def list() {
    }

    def json() {
        if(!params.max) params.max='10';
        if(!params.offset) params.offset ='0';
        def result = Commentary.createCriteria().list([max:params.max.toInteger(), offset:params.offset.toInteger()]) {
            createAlias("apply","a")
            createAlias("a.trade","t")
            projections{
                property('id','id')
                property('a.id','applyId')
                property('creator.id','creatorId')
                property('score','score')
                property('createdBy','createdBy')
                property('content','content')
                property('dateCreated','dateCreated')
            }
            if (params?.tradeId) {
                eq("t.id", params?.tradeId.toLong())
            }
            if (params?.createdBy) {
                like("createdBy", "%${params?.createdBy?.trim()}%")
            }
            setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
            order("id", "desc")
        }
        def map = [:];
        map.total = result.totalCount;
        map.rows = result.resultList;
        render map as JSON;
    }


    def deleteCommentary(){
        def map=[:];
        map['result']=false;
        map['message']="网络错误";
        if(params.id){
            try{
                Commentary.executeUpdate("delete from Commentary where id=?",[params.id.toLong()]);
                map['result']=true;
                map['message']="操作成功";
            }catch(e){
                log.error(e.message);
            }
        }
        render map as JSON;
    }
}
