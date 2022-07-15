package com.wmsj.core

import grails.converters.JSON
import org.hibernate.criterion.CriteriaSpecification

class OrganizationController {

    def createOrEdit(){
        def organization;
        if(params.id){
            organization= Organization.get(params.id.toLong())
        }else{
            organization=new Organization();
        }
        return [organization:organization];
    }

    def saveOrUpdate(){
        def organization,map=[:];
        map.result=false;
        map.message="网络错误，请重试";
        if(params.id){
            organization=Organization.get(params.id.toLong())
        }else{
            organization=new Organization();
        }
        try{
            organization.properties=params;
            if(organization.save(flush:true)){
                map.result=true;
                map.message="操作成功";
            }else{
                log.error(organization.errors);
            }
        }catch(e){
            log.error(e.message);
        }
        render "${map as JSON}";
    }

    def deleteOrganization(){
        def organization,map=[:];
        map.result=false;
        map.message="网络错误，请重试";
        if(params.id){
            organization=Organization.get(params.id.toLong());
            if(BaseUser.countByOrganization(organization)>0){
                map.message="请先删除该单位用户";
            }else{
                try{
                    organization.delete(flush: true);
                    map.result=true;
                    map.message="删除成功";
                }catch(e){
                    log.error(e.message);
                }
            }
        }
        render "${map as JSON}";
    }

    def list(){

    }

    def json(){
        if(!params.max) params.max='10'
        if(!params.offset) params.offset ='0'
        def result=Organization.createCriteria().list(max: params.max.toInteger(),
                offset: params.offset.toInteger()){
            createAlias("parent","p",CriteriaSpecification.LEFT_JOIN)
            projections{
                property("id","id")
                property("name","name")
                property("shortName","shortName")
                property("p.name","parentName")
            }
            if(params.name){
                like("name","%${params.name.trim()}%")
            }
            if(params.shortName){
                like("shortName","%${params.shortName.trim()}%")
            }
            setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
            order('id','asc')
        }
        def map=[:];
        map.rows=result.resultList;
        map.total=result.totalCount;
        render "${map as JSON}";
    }


}
