package com.wmsj.cms

import com.wmsj.business.Trade
import com.wmsj.core.Organization
import grails.converters.JSON
import org.hibernate.criterion.CriteriaSpecification

class ChartController {

    def index() { }
    /**
     *新闻访问量top10
     */
    def newsVisitBarChart(){ }
    /**
     * 柱状图
     * @return
     */
    def newsVisitBarChartJson(){
        def map=[:];
        def list= News.createCriteria().list{
            projections{
                count('clicknum','nums')
                groupProperty('title')
            }
            order('nums','desc')
            maxResults(10)
        }
        map.seriesData=list.collect{it[0]};
        map.newsName=list.collect{it[1]};
        render "${map as JSON}";
    }
    /**
     * 栏目访问量top10
     */
    def catalogVisitPieJson(){
        def map=[:];
        def list=News.createCriteria().list{
            projections{
                property('clicknum','nums')
                groupProperty('catalog')
            }
            order('clicknum','desc')
            maxResults(5)
        }
        def lmap=[];
        list.each {
           def m=[:];
            m.value=it[0];
            m.name="${it[1].name}";
            lmap<<m;
        }
        map.seriesData=lmap;
        map.catalogsName=list.collect{it[1].name};
        render "${map as JSON}";
    }

    def basicSiteInfo(){

    }

    /**
     * 文章发布量排名
     */
    def publishRank(){

    }

    def publishRankJson(){
        List list=[];
        def orgnizations= Organization.createCriteria().list {
            projections{
                property("id","id")
                property("name","name")
            }
            order("id","asc")
            setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
        }
        def oids=orgnizations.collect {it?.getAt("id")};
        def newsNum=News.createCriteria().list{
            createAlias("publisher","p")
            createAlias("p.organization","o")
            projections{
                count('id',"num")
                groupProperty("o.id","oid")
            }
            inList("o.id",oids)
            eq("state","发布")
            setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
        }
        def tradeNum= Trade.createCriteria().list{
            createAlias("organization","o")
            projections{
                count('id',"num")
                groupProperty("o.id","oid")
            }
            inList("o.id",oids)
            ge("status",20)
            eq("deleted",false)
            setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
        }
        orgnizations.each{org->
            Map publishRank=[:];
            publishRank["name"]=org.getAt("name");
            publishRank["newsNum"]=newsNum.find{it.getAt("oid")==org.id}?.getAt("num")?:0;
            publishRank["tradeNum"]=tradeNum.find{it.getAt("oid")==org.id}?.getAt("num")?:0;
            list<<publishRank;
        }
        render list as JSON;
    }
}
