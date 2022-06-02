package com.bjrxkj.cms

import com.bjrxkj.cms.behaviour.Visit
import com.bjrxkj.core.BaseUser
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.SpringSecurityUtils
import org.hibernate.criterion.CriteriaSpecification
import ricicms.CmsService
import ricicms.CommonService

class NewsAdminController {
    CommonService commonService;
    SpringSecurityService springSecurityService;
    CmsService cmsService;
    def index (){ redirect(action:list(),params:params) }

    //列表
    def newsList(){
        Catalog catalog;
        if(params.catalogId){
            catalog=Catalog.get(params.catalogId.toLong());
        }
        return [catalog:catalog]
    }
    def list(){

    }
    def newsJsonSearch(def params,def currentUser){

    }

    def json(){
        if(!params.max) params.max='20';
        if(!params.offset) params.offset ='0';
        def currentUser=springSecurityService.currentUser;
        def sortMap = ['sequencer': 'asc','publishDate':'desc','id':'desc'];
        def newsResult=News.createCriteria().list ([max: params.max.toInteger(),offset: params.offset.toInteger()]){
            createAlias('catalog','c')
            projections{
                property('id','id')
                property('c.id','catalogId')
                property('c.name','catalogName')
                property('title','title')
                property('publishDate','publishDate')
                property('state','state')
                property('sequencer','sequencer')
            }
            if(params['catalog.id']){
                eq("c.id",params['catalog.id'].toLong())

            } else if (params['site.id']) {
                createAlias('c.site','s')
                eq("s.id", params['site.id'].toLong())
            }
            if(params.approve){
                eq("state",'发布')
            } else if (params.state) {
                eq("state", params.state)
            }
            if(params.state)
                eq("state",params.state)
            if(params.title)
                like("title","%"+params.title.trim()+"%")
            if(params.author)
                like("author","%"+params.author.trim()+"%")
            if(params.source)
                like("source","%"+params.source.trim()+"%")
            if(params.keywords)
                like("keywords","%"+params.keywords.trim()+"%")
            if(params.beginDate){
                ge("publishDate",Date.parse("yyyy-MM-dd HH:mm:ss",params.beginDate+" 00:00:00"))
            }
            if(params.endDate){
                le("publishDate",Date.parse("yyyy-MM-dd HH:mm:ss",params.endDate+" 23:59:59"))
            }
            setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
            sortMap.keySet().each {st->
                order(st,sortMap[st])
            }
        }
        def map=[:]
        map.rows=newsResult.resultList;
        map.total=newsResult.totalCount;
        render "${map as JSON}";
    }
    def createOrEdit(){
        def baseUser=springSecurityService.currentUser;
        News newsInstance;Catalog catalog;
        if(params.id){
            newsInstance = News.get(params.id.toLong());
        }else{
            newsInstance = new News(params);
        }
        return [user:baseUser,'newsInstance':newsInstance,catalog:catalog]
    }

    def saveOrUpdate(){
        def map=[:];
        map.result=false;
        map.message="网络错误，请重试！";
        try{
            if(!params['catalog.id']){
                map.message="请在左侧选择一项栏目！";
                render "${map as JSON}";
                return;
            }
            def baseUser=springSecurityService.currentUser;
            def newsInstance,catalog;
            if(params.id){
                params.remove('catalog');
                newsInstance = News.get(params.id.toLong());
                catalog=newsInstance.catalog;
            }else{
                newsInstance = new News();
                catalog=Catalog.get(params['catalog.id']);
            }
            if(params.expireDate){
                newsInstance.expireDate=Date.parse('yyyy-MM-dd',params.remove('expireDate'))
            }
            newsInstance.properties = params;
            newsInstance.publisher = baseUser;
            if(!newsInstance.author){
                newsInstance.author = baseUser.realName;
            }
            //需要审核
            if(params.state=='发布'&&((catalog?.needPreview||catalog?.needPubPreview)&&!SpringSecurityUtils.ifAnyGranted("ROLE_MANAGER,ROLE_ADMIN"))){
                newsInstance.state='初步审核';
            }
            if(newsInstance?.content)  {
                XmlSlurper slurper = new XmlSlurper(new org.ccil.cowan.tagsoup.Parser());
                newsInstance.contentStr =slurper.parseText(newsInstance?.content).text()
                if(newsInstance.contentStr?.size()>4000){
                    newsInstance.contentStr=newsInstance.contentStr[0..4000]
                }
            }
            def vFile = request.getFile('pictureFile');//封面
            def dirPath=grailsApplication.config.project.setting.imagesPath+File.separator+"${newsInstance.publishDate.format('yyyyMM')}";
            def picturepath=dirPath+File.separator+"${UUID.randomUUID().toString().replaceAll("-","")}.jpg";
            if( vFile && !vFile.isEmpty()){
                newsInstance.picture=picturepath;
            }
            if(!newsInstance.hasErrors() && newsInstance.save(flush:true)) {
                if(newsInstance.picture){
                    def dirfile=new File(dirPath);
                    if(!dirfile.exists()){
                        dirfile.mkdirs();
                    }
                    def picfile=new File(picturepath);
                    if( vFile && !vFile.isEmpty()){
                        vFile.transferTo(picfile);
                    }
                }
                def attachments=request.getParameterValues('attachmentId')?.toList().collect {it.toLong()};
                if(attachments&&attachments.size()>0){
                    Attachment.where {id in attachments}.updateAll(news: newsInstance);
                }
                map.result=true;
                map.message="操作成功";
            }else{
                log.error(newsInstance.errors.toString());
            }
        }catch(e){
            log.error(e.message);
        }
        render "${map as JSON}";
    }

    /**
     * 回收站-彻底删除，其他-回收站
     * @return
     */
    def deleteAll (){
        def map=[:];
        map.result=false;
        map.message="网络错误，请重试！";
        def ids = params.fields?.split(',').toList().collect {it.toLong()};
        try{
            ids.each{
                def oldNews=News.get(it);
                if(oldNews.state=='回收站'){
                    Attachment.where{news == oldNews}.deleteAll()
                    Visit.where{news == oldNews}.deleteAll()
                    oldNews.delete(flush:true);
                }else{
                    oldNews.state='回收站';
                    oldNews.save(flush:true);
                }
            }
            map.result=true;
            map.message="操作成功";
            cmsService.clearNewsCache();
        }catch(e){
            log.error(e.message);
        }
        render "${map as JSON}";
    }

    /**
     * 改变状态
     */
    def changeNewsState(){
        def map=[:];
        map.result=false;
        map.message="网络错误，请重试！";
        def ids = params.fields?.split(',').toList().collect {it.toLong()};
        try{
            def updateMap=[:],audit=springSecurityService.currentUser;
            updateMap.state=params.state;
            updateMap.backreason=params.backreason;
            if(['初步审核','拟发审核','退回'].contains(params.state)){
                updateMap.auditDate=new Date();
                updateMap.auditUser=audit;
            }else if(params.state=='发布'){
                updateMap.publisher=audit;
            }
            News.where{id in ids}.updateAll(updateMap);
            map.result=true;
            map.message="操作成功";
            cmsService.clearNewsCache();
        }catch(e){
            log.error(e.message);
        }
        render "${map as JSON}";
    }

    /**
     * 审批新闻
     */
    def approveAll(){
        def map=[:];
        map.result=false;
        map.message="网络错误，请重试！";
        if(SpringSecurityUtils.ifAnyGranted("ROLE_PREVIEW")){
            try{
                def ids = params.fields?.split(',').toList().collect {it.toLong()};
                News.where{id in ids}.updateAll(state: '发布');
                map.result=true;
                map.message="操作成功";
            }catch(e){
                log.error(e.message);
            }
        }
        render "${map as JSON}";
    }

    /*YYH
    * 后台资源管理-->图片管理
    * */
    def pictureList(){}
    def pictureJson(){
        params.max='5'
        if(!params.offset) params.offset ='0'
        def newsPicList=News.createCriteria().list{
            if(params.title)
                like("title","%"+params.title.trim()+"%")
            if(params.beginDate){
                ge("publishDate",Date.parse("yyyy-MM-dd HH:mm:ss",params.beginDate+" 00:00:00"))
            }
            if(params.endDate){
                le("publishDate",Date.parse("yyyy-MM-dd HH:mm:ss",params.endDate+" 23:59:59"))
            }
            isNotNull("picture")
            order("sequencer","asc")
            order("publishDate","desc")
            order("id","desc")
            firstResult(params.offset?.toInteger())
            maxResults(params.max?.toInteger())
        }
        def newsPicCount=News.createCriteria().count{
            if(params.title)
                like("title","%"+params.title.trim()+"%")
            if(params.beginDate){
                ge("publishDate",Date.parse("yyyy-MM-dd HH:mm:ss",params.beginDate+" 00:00:00"))
            }
            if(params.endDate){
                le("publishDate",Date.parse("yyyy-MM-dd HH:mm:ss",params.endDate+" 23:59:59"))
            }
            isNotNull("picture")
        }
        def map=[:],list=[];
        newsPicList.each{
            def m=[:];
            m.id=it.id;
            m.title=it.title;
            m.publishDate=it.publishDate.format("yyyy-MM-dd")
            list<<m;
        }
        map.rows=list;
        map.total=newsPicCount;
        render "${map as JSON}";

    }

    /*YYH
     * 后台资源管理-->视频管理
     * */
    //删除视频
    def deleteVideo (){
        def map=[:]
        def videoNews=News.get(params.id)
        def videoPath=videoNews.videoPath
        //删除数据库中存储的视频路径
        videoNews.videoPath=null;
        videoNews.save(flush:true)
        //删除文件存储的视频
        def videoLink=request.getRealPath("/videos/"+videoPath)
        def thisArtvideoLink = new File(videoLink)
        if (thisArtvideoLink.exists()) {
            thisArtvideoLink.delete()
        }
        map.result=true
        map.message="删除成功"
        if(videoNews.hasErrors()){
            map.result=false
            map.message="删除视频失败"
        }
        render "${map as JSON}";
    }

    def copyNews(){
        def map=[:];
        map.result=false;
        map.message="网络错误，请重试！";
        def ids = params.fields?.split(',').toList().collect {it.toLong()};
        ids.reverse()
        String state="发布"
        if(params.cid){
            def catalog1=Catalog.get(params.cid?.toLong());
            News.withTransaction {trastate->
                ids?.reverse().each{
                    def oldNews=News.get(it.toLong())
                    if(oldNews){
                        if(params.operation=='copy'){//复制
                            state=oldNews?.state
                            def newNews=new News()
                            newNews.properties = oldNews.properties
                            newNews.catalog=catalog1
                            newNews.clicknum=0
                            newNews.comments=null
                            if(newNews.save(flush:true)){
                                Attachment.findAllByNews(oldNews)?.each{a->
                                    def attachmentInstance=new Attachment()
                                    attachmentInstance.news=newNews
                                    attachmentInstance.name=a.name
                                    attachmentInstance.data=a.data
                                    attachmentInstance.save(flush:true)
                                }
                            }else{
                                trastate.setRollbackOnly();
                            }
                        }else if(params.operation=='move'){
                            oldNews.catalog=catalog1;
                            if(!oldNews.save(flush:true)){
                                map.result=false;
                                map.message="网络错误，请重试！";
                                log.error(oldNews.errors);
                                trastate.setRollbackOnly();
                            }
                        }
                    }
                }
            }
            map.result=true;
            map.message="操作成功";
        }
        render "${map as JSON}";
    }

    def menuJson(){
        def list = [],currentUser=springSecurityService.currentUser;
        if(params.userId&&['baseUser','baseRole'].contains(params.usage)){
            currentUser=BaseUser.get(params.userId);
        }
        def orgList = [];
        if(params.id =="#"){
            list = Site.createCriteria().list{
                order("sequencer","asc")
                order("id","asc")
            }
        }else{
            if(params.id.startsWith("P_")){
                params.id = params.id.replace("P_","");
            }
            list =  Catalog.createCriteria().list{
                if(params.type=="site"){
                    isNull('parent')
                    eq('site.id',params.id?.toLong())
                }else if(params.type=="catalog"){
                    eq('parent.id',params.id?.toLong()?:-1l)
                }
                order('sequencer','asc')
                order('id','asc')
            }
        }
        list.each {
            def map = [:];
            map.state = [:];
            if(params.id =="#"){
                map.id = "P_"+it.id;
                map.children = true;
                map.state.isleaf = false;
                map.type='site';
                map.state.type='site';
                map.text = it.companyName;
                map.name=it.name;
            }else{
                map.id = it.id;
                map.children = !it.children.isEmpty()
                map.state.isleaf = it.children.isEmpty();
                if(it.children.isEmpty()){
                    map.type='isleaf'
                }else{
                    map.type='catalog'
                }
                map.state.type='catalog';
                map.text = it.name;
            }
            orgList << map;
        }
        render "${orgList as JSON}";
    }

    def importZipFile(){
        def map=[:];
        map.result=false;
        if(!['insert','update'].contains(params.impType)){
            map.message='导入方式参数错误';
        }else if(!['toByte','toDoc'].contains(params.attachmentType)){
            map.message='附件存储方式参数错误';
        }else if (!params.impCatalogId){
            map.message='缺少导入栏目参数';
        }else{
            def zipFileData = request.getFile('zipFile');
            def path=request.servletContext.getRealPath("/temp_zip${new Date().format('yyyy-MM-dd')}.zip");
            def docfilePath="/uploads/docfile/";
            map.result=commonService.importZipFile(zipFileData,path,docfilePath,params);
        }
        cmsService.clearNewsCache();
        render "${map as JSON}";
    }

    /**
     * 外网栏目
     * @return
     */
    def catalogsFromSppweb(){
        render commonService.catalogsFromSppweb();
    }

    /**
     * 外网栏目
     * @return
     */
    def importNewsFromSppweb(){
        def map=commonService.importNewsFromSppweb(params);
        render "${map as JSON}";
    }

    /**
     * 获取选中的新闻导出
     * @return
     */
    def exportSelectNews(){
        if(params.exportNewsId){
            def ids = params.exportNewsId?.split(',')?.toList().collect {it.toLong()};
            try{
                List newsList=News.createCriteria().list {
                    inList('id',ids)
                }
                def today=new Date();
                def zippath="NEWS${today.format('yyyyMMdd')}${today.time}";
                commonService.mapToXML(newsList,zippath);
                File file=new File("${zippath}.zip");
                response.setContentType("application/x-msdownload;charset=UTF-8")
                response.addHeader("Content-Disposition", 'attachment; filename="' + new String("${file.name}".getBytes("GBK"),"ISO8859-1")+'"')
                OutputStream out = response.outputStream
                out.write(file.bytes)
                out.close();
                file.delete();
                File xmlfile=new File("${zippath}.xml");
                xmlfile.delete();
            }catch(e){
                log.error(e.message);
            }
        }
    }

}
