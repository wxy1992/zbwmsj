package com.bjrxkj.cms

import com.bjrxkj.cms.behaviour.Visit
import grails.converters.JSON

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
}
