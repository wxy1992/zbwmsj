package com.wmsj.business

import grails.converters.JSON
import grails.transaction.Transactional
import org.hibernate.criterion.CriteriaSpecification

@Transactional
class ApplyController {
    def list() {
    }

    def json() {
        if(!params.max) params.max='10';
        if(!params.offset) params.offset ='0';
        def result = Apply.createCriteria().list([max:params.max.toInteger(), offset:params.offset.toInteger()]) {
            projections{
                property('id','id')
                property('creator.id','creatorId')
                property('name','name')
                property('idcard','idcard')
                property('telephone','telephone')
                property('status','status')
                property('dateCreated','dateCreated')
            }
            if (params?.tradeId) {
                eq("trade.id", params?.tradeId.toLong())
            }
            if (params?.name) {
                like("name", "%${params?.name?.trim()}%")
            }
            if (params?.telephone) {
                like("telephone", "%${params?.telephone?.trim()}%")
            }
            if (params?.idcard) {
                like("idcard", "%${params?.idcard?.trim()}%")
            }
            setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
            order("id", "desc")
        }
        def map = [:];
        map.total = result.totalCount;
        map.rows = result.resultList;
        render map as JSON;
    }

}
