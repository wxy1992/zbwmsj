package com.wmsj.cms

import cms.CommonService
import com.wmsj.business.Trade
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.SpringSecurityUtils

import java.text.SimpleDateFormat

class CmsController {
    CommonService commonService;
    SpringSecurityService springSecurityService;
    def index(){
        redirect(action: 'workSpace')
    }
    def workSpace(){
        def map=[:],current=new Date();
        map.num1=News.count();
        map.num2=Catalog.count();
        map.num3= News.createCriteria().list{
            projections{
                sum("clicknum")
            }
            eq("state","发布")
        }?.getAt(0);
        map.num4=News.createCriteria().count{
            ge('dateCreated',Date.parse('yyyy-MM-dd HH:mm:ss',current.format('yyyy-MM-dd 00:00:00')))
            le('dateCreated',Date.parse('yyyy-MM-dd HH:mm:ss',current.format('yyyy-MM-dd 23:59:59')))
        };
        map.newsupdate= News.createCriteria().list {
            order('dateCreated', 'desc')
            maxResults(10)
        }
        map.colors=['primary','warning','info','success','warning'];
        map.current = current;
        return [result:map];
    }
    /**
     * 待办事项气泡数字
     * @return
     */
    def countMyTask(){
        def currentUser=springSecurityService.currentUser;
        def todoTradeNum= Trade.createCriteria().count{
            if(SpringSecurityUtils.ifAnyGranted("ROLE_ADMIN")){
                eq("status",10)
            }else{
                createAlias('organization','o')
                eq("o.id",currentUser.organizationId)
                eq("status",5)
            }
        }
        def todoNewsNum= News.createCriteria().count{
            if(SpringSecurityUtils.ifAnyGranted("ROLE_ADMIN")){//管理员
                eq("state","已提交")//待办已提交服务
            }else{//二级用户只管理本单位
                createAlias('publisher','p')
                createAlias('p.organization','o')
                eq("o.id",currentUser.organizationId)
                eq("state","退回")
            }
        }
        def map=[:];
        map.todoTradeNum=todoTradeNum?:0;
        map.todoNewsNum=todoNewsNum?:0;
        render "${map as JSON}";
    }

    def catalogVisitAWeekJson(){
        def map=[:];
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//格式工具
        Date da = simpleDateFormat.parse(new Date().format('yyyy-MM-dd'));
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(da);
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        def day1=simpleDateFormat.format(calendar.getTime());
        map.dateX=[];
        for(int i=1;i<=7;i++){
            calendar.add(Calendar.DAY_OF_MONTH, +1);
            map.dateX<<simpleDateFormat.format(calendar.getTime());
        }
        def catalogsTop4=News.createCriteria().list{
            projections{
                property('clicknum','nums')
                groupProperty('catalog')
            }
            ge('dateCreated',Date.parse('yyyy-MM-dd HH:mm:ss',"${day1} 00:00:00"))
            le('dateCreated',new Date())
            order('nums','desc')
            maxResults(5)
        }
        def list=News.createCriteria().list{
            projections{
                property('clicknum','nums')
                groupProperty('catalog')
                groupProperty('dateCreated')
            }
            ge('dateCreated',Date.parse('yyyy-MM-dd HH:mm:ss',"${day1} 00:00:00"))
            le('dateCreated',new Date())
            order('nums','desc')
        }
        map.dataLegend=catalogsTop4.collect {"ID:${it[1].id}-${it[1].name}"};
        def mlist=[];
        catalogsTop4.each {cat->
            def dateList=[];
            map.dateX.each {d->
                def date_it1=Date.parse('yyyy-MM-dd HH:mm:ss',"${d} 00:00:00");
                def date_it2=Date.parse('yyyy-MM-dd HH:mm:ss',"${d} 23:59:59");
                def ss=list.findAll{return it[2]>=date_it1&&it[2]<=date_it2&&it[1].id==cat[1].id}.sum{it[0]}?:0;
                dateList<<ss;
            }
            def m=[:];
            m.name="ID:${cat[1].id}-${cat[1].name}";
            m.type='line';
            m.stack='访问量';
            m.data=dateList;
            mlist<<m;
        }
        map.seriesData=mlist;
        render "${map as JSON}";
    }
    def quickLinks={
        def criteria1=QuickLink.createCriteria()
        def quickLinks=criteria1.list{
            if(springSecurityService.getPrincipal().id){
                publisher{
                    eq("id",springSecurityService.getPrincipal().id)
                }
            }
            else{
                isNull("publisher")
            }
            if(params.title){
                like("title","%${params.title.trim()}%")
            }
            order("id","asc")
        }
        render(view:'quickLinks',model:[quickLinks:quickLinks])
    }
    def list={
        def baseUser=BaseUser.get(springSecurityService.getPrincipal().id)
        def site1=Site.list().getAt(0)
        if(baseUser && baseUser.site) site1=baseUser.site
        if(!params.max) params.max='10'
        if(!params.offset) params.offset ='0'
        if(!params.searchType) params.searchType ='sa2'
        String keyword=params.keyword?.trim()
        def objs=[]
        def objsCount=0
        if(keyword){
            if(params.searchType=='sa1'){
                objs=News.createCriteria().list {
                    if(params.range && params.range.isLong()){
                        createAlias("catalog","c")
                        createAlias("c.site","s")
                        eq("s.id",params.range.toLong())
                    }
                    or{
                        like("title","%${keyword}%")
                        like("contentStr","%${keyword}%")
                    }
                    isNotNull("picture")
                    order("sequencer","asc")
                    order("publishDate","desc")
                    order("id","desc")
                    firstResult(params.offset?.toInteger())
                    maxResults(params.max?.toInteger())
                }
                objsCount=News.createCriteria().count {
                    if(params.range && params.range.isLong()){
                        createAlias("catalog","c")
                        createAlias("c.site","s")
                        eq("s.id",params.range.toLong())
                    }
                    or{
                        like("title","%${keyword}%")
                        like("contentStr","%${keyword}%")
                    }
                    isNotNull("picture")
                }
            }
            else if(params.searchType=='sa2'){
                objs=News.createCriteria().list {
                    if(params.range && params.range.isLong()){
                        createAlias("catalog","c")
                        createAlias("c.site","s")
                        eq("s.id",params.range.toLong())
                    }
                    or{
                        like("title","%${keyword}%")
                        like("contentStr","%${keyword}%")
                    }
                    order("sequencer","asc")
                    order("publishDate","desc")
                    order("id","desc")
                    firstResult(params.offset?.toInteger())
                    maxResults(params.max?.toInteger())
                }
                objsCount=News.createCriteria().count {
                    if(params.range && params.range.isLong()){
                        createAlias("catalog","c")
                        createAlias("c.site","s")
                        eq("s.id",params.range.toLong())
                    }
                    or{
                        like("title","%${keyword}%")
                        like("contentStr","%${keyword}%")
                    }
                }
            }
            else if(params.searchType=='sa3'){
                objs=Attachment.createCriteria().list {
                    if(params.range && params.range.isLong()){
                        createAlias("news","n")
                        createAlias("n.catalog","c")
                        createAlias("c.site","s")
                        eq("s.id",params.range.toLong())
                    }
                    like("name","%${keyword}%")
                    order("id","desc")
                    firstResult(params.offset?.toInteger())
                    maxResults(params.max?.toInteger())
                }
                objsCount=Attachment.createCriteria().count {
                    if(params.range && params.range.isLong()){
                        createAlias("news","n")
                        createAlias("n.catalog","c")
                        createAlias("c.site","s")
                        eq("s.id",params.range.toLong())
                    }
                    like("name","%${keyword}%")
                }
            }
        }
        def view="list${params.searchType}"
        render (view:view,model: ['objs':objs,'objsCount':objsCount,site1:site1])
        return

    }
    def myList={
        if(!params.max) params.max='10'
        if(!params.offset) params.offset ='0'
        def baseUser=BaseUser.get(springSecurityService.getPrincipal().id)
        def site1=Site.list().getAt(0)
        if(baseUser && baseUser.site) site1=baseUser.site
        def objs=News.createCriteria().list{
            publisher{
                eq("id",baseUser.id)
            }
            order("sequencer","asc")
            order("publishDate","desc")
            order("id","desc")
            firstResult(params.offset?.toInteger())
            maxResults(params.max?.toInteger())

        }
        def objsCount=News.createCriteria().count{
            publisher{
                eq("id",baseUser.id)
            }
        }
        return[objs:objs,objsCount:objsCount,site1:site1]
    }
    def copyNews ={
        def ids=request.getParameterValues("ids")
        if(params.cid){
            def catalog1=Catalog.get(params.cid?.toLong())
            ids.each{
                def oldNews=News.get(it.toLong())
                oldNews.comments=null
                def newNews=new News()
                newNews.properties = oldNews.properties
                newNews.catalog=catalog1
                newNews.clicknum=0
                newNews.save(flush:true)
                Attachment.findAllByNews(oldNews)?.each{a->
                    def attachmentInstance=new Attachment()
                    attachmentInstance.news=newNews
                    attachmentInstance.name=a.name
                    attachmentInstance.save(flush:true)
                }
            }
            flash.message=g.message(code:'flash.message.ok4')
        }
        redirect(action: 'workSpace')
    }
    def manage={
        render (view:'manage'+params.id)
        return
    }
    def show={
        render (view:'show'+params.id)
        return
    }

    def changePasswd(){
        def baseUser = springSecurityService.currentUser
        return [baseUser:baseUser]
    }

    def changePasswdSave() {
        def map = [:];
        if (params?.id) {
            if (params.newPass != params.newPass1) {
                map.result = false;
                map.message = "新密码两次输入不一致";
            }else{
                def baseUser = BaseUser.get(params?.id);
                baseUser.properties=params;
                baseUser.password = params.newPass.trim()
                if (baseUser.save(flush: true)) {
                    map.result = true;
                    map.message = "操作成功";
                }else{
                    log.error(baseUser.errors);
                }
            }
        } else {
            map.message = "缺少参数！";
        }
        render "${map as JSON}";
    }


    /**
     * 封面图片
     * @return
     */
    def picture(){
        println "================"
        if(!(params.id && params.id.isLong())){
            return
        }
        commonService.picture(params,request,response);
    }

    /**
     * 附件
     * @return
     */
    def getFile(){
        if(!(params.id && params.id.isLong())){
            return
        }
        commonService.getFile(params,response);
    }

}
