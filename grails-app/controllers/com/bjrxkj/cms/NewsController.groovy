package com.bjrxkj.cms

import com.bjrxkj.cms.behaviour.Visit
import com.bjrxkj.image.ImageUtils
import grails.plugin.cache.Cacheable
import grails.plugin.springsecurity.SpringSecurityService
import grails.util.Environment
import org.hibernate.criterion.CriteriaSpecification
import ricicms.CommonService
import javax.servlet.ServletOutputStream
import java.awt.Font

class NewsController {
    CommonService commonService;
    SpringSecurityService springSecurityService;

    private boolean checkIfTemplateGspExists(template,attrs) {
        def templateFile
        if(Environment.getCurrent()==Environment.PRODUCTION){
            templateFile=new File(servletContext.getRealPath('/WEB-INF/grails-app/views'+template+'.gsp'));
        }
        if(Environment.getCurrent()==Environment.DEVELOPMENT){
            templateFile=new File(new File(".").getCanonicalPath()+'/grails-app/views'+template+'.gsp');
        }
        return templateFile.exists();
    }

    def newsTopSearch(def params,def catalog){
        return News.whereLazy {
            if(catalog){
                if(!catalog.nochildren&&(params.isParent||catalog.children)){
                    catalog{
                        or{
                            eq("parent.id",catalog.id)
                            eq("id",catalog.id)
                        }
                    }
                }
                else{
                    eq("catalog.id",catalog.id)
                }
            }
            eq("state","发布")
            eq("approve",true)
            gt("expireDate",new Date())
        }
    }

    @Cacheable(value='newsTop',key="#id + 'list'")
    def top() {
        if(!springSecurityService.isLoggedIn()&&Catalog.controlCatalog().contains(params.id.toLong())){
            redirect(uri: '/login/auth?loginType=show')
            return;
        }

        Catalog catalog
        if (params['id'] && params['id']?.toString()?.isNumber()) {
            catalog = Catalog.get(params['id']?.toString()?.toLong())
        }

        if (!catalog) {
            flash.message = g.message(code: 'flash.message.none')
            redirect(uri: "/")
            return;
        }

        def layouts='cms';
        if (!params.offset) params.offset = '0'
        if (!params.max) params.max = '30'
        if (catalog.pageMax) {
            params.max = catalog?.pageMax.toString()
        } else if (catalog.site.pageMax) {
            params.max = catalog.site.pageMax.toString()
        } else if(catalog.showFirst){
            params.max = '1'
        }
        def sortMap = ['sequencer': 'asc'];
        if (catalog?.isAsc) {
            sortMap["publishDate"] = "asc";
            sortMap["id"] = "asc";
        } else {
            sortMap["publishDate"] = "desc";
            sortMap["id"] = "desc";
        }
        def result=News.createCriteria().list([max: params.max.toInteger(),
                                               offset: params.offset.toInteger()]) {
            if(catalog){
                createAlias('catalog','c')
                if(!catalog.nochildren&&(params.isParent||catalog.children)){
                    def cats=catalog.children.id.unique();
                    cats<<catalog.id;
                    inList("c.id",cats.unique())
                }
                else{
                    eq("c.id",catalog.id)
                }
            }
            eq("state","发布")
            eq("approve",true)
            gt("expireDate",new Date())
            sortMap.keySet().each {st->
                order(st,sortMap[st])
            }
        }
        def objs=result.resultList;
        if(catalog.showFirst&&objs[0]){
            def newsId=objs[0].id;
            redirect(uri: "/news/detail/${newsId}.html")
            return;
        }
        def objsCount=result.totalCount;

        def templatePath="/sites/${catalog?.site?.name}";//默认 sites/网站name/top
        if (catalog?.site?.template) {
            templatePath="/sites/template/${catalog?.site?.template}";// /sites/template/index03/top
        }
        def tempView = "${templatePath}/top";
        if(params.view && checkIfTemplateGspExists("${templatePath}/${params.view}", params)){
            tempView = "${templatePath}/${params.view}";
        }else if(catalog?.templateTop&&checkIfTemplateGspExists("${templatePath}/${catalog?.templateTop}", params)){
            tempView = "${templatePath}/${catalog?.templateTop}";
        }else if (checkIfTemplateGspExists("${templatePath}/top${catalog?.id}", params)) {
            tempView = "${templatePath}/top${catalog?.id}";
        }
        render(view: tempView, model: [catalog: catalog, newsInstanceList: objs, layouts: layouts, newsInstanceTotal: objsCount])
    }

    @Cacheable(value='newsTopTag',key="#idTag + 'topTag'")
    def topTag(){
        Catalog catalog
        if (params['idTag'] && params['idTag']?.toString()?.isNumber()) {
            catalog = Catalog.get(params['idTag']?.toString()?.toLong())
            if(catalog){
                if (!params.offsetTag) params.offsetTag = '0'
                if (!params.maxTag) params.maxTag = '30'
                def sortMap = ['sequencer': 'asc'];
                if (catalog?.isAsc) {
                    sortMap["publishDate"] = "asc";
                    sortMap["id"] = "asc";
                } else {
                    sortMap["publishDate"] = "desc";
                    sortMap["id"] = "desc";
                }
                def join_newsid = [],join_catalogid=[];//引用到此栏目的新闻id
                if (params.includecite == 'true') {//包含引用新闻
                    join_newsid = CatalogRelation.createCriteria().list {
                        projections {
                            property('newsfrom.id')
                        }
                        eq('to.id', catalog.id)
                    }
                }
                if(params.catalogplus == 'true'){
                    join_catalogid=catalog.catalogplus?.tokenize(',').collect{it.toLong()};
                }
                def result=News.createCriteria().list([max: params.maxTag.toInteger(),
                                                       offset: params.offsetTag.toInteger()]){
                    projections{
                        property('id','id')
                        property('title','title')
                        property('catalog','catalog')
                        property('subtitle','subtitle')
                        property('author','author')
                        property('outline','outline')
                        property('contentStr','contentStr')
                        property('picturepath','picturepath')
                        property('publishDate','publishDate')
                    }
                    if(catalog){
                        createAlias('catalog','c')
                        or{
                            if (join_newsid.size() > 0) {
                                inList('id', join_newsid)
                            }
                            if(join_catalogid.size()>0){
                                inList("c.id",join_catalogid)
                            }
                            if(params.isParentTag){//包含子栏目
                                createAlias('c.parent', 'p', CriteriaSpecification.LEFT_JOIN)
                                or {
                                    eq("c.id", catalog.id)
                                    eq("p.id", catalog.id)
                                }
                            }else{
                                if(params.addCatalogs){//聚合其他栏目
                                    def cats=params.addCatalogs.tokenize(',').collect{it.toLong()};//包含其他栏目
                                    cats<<catalog.id;
                                    inList("c.id",cats.unique())
                                }else{
                                    eq("c.id",catalog.id)
                                }
                            }

                        }
                        if(!springSecurityService.isLoggedIn()){//未登录只能查看非登录可见
                            or{
                                ne('c.seerange',2)
                                isNull('c.seerange')
                            }
                        }
                    }
                    eq("state","发布")
                    eq("approve",true)
                    gt("expireDate",new Date())
                    setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP)
                    sortMap.keySet().each {st->
                        order(st,sortMap[st])
                    }
                }
                def objs=result.resultList;
                def objsCount=result.totalCount;
                def view = 'topTag'
                if (params.viewTag) {
                    view = "${params.viewTag}"
                    render(view: view, model: [catalog: catalog, newsInstanceList: objs, newsInstanceTotal: objsCount])
                    return
                } else {
                    String templatePath="/sites/${catalog?.site?.name}";
                    if (catalog?.site?.template) {
                        templatePath="/sites/template/${catalog?.site?.template}";
                    }
                    String tempView = "${templatePath}/top${catalog?.id}"
                    if (checkIfTemplateGspExists(tempView, params)) {
                        render(view: tempView, model: [catalog: catalog, newsInstanceList: objs, newsInstanceTotal: objsCount])
                        return
                    }
                    tempView = "${templatePath}/top"
                    if (checkIfTemplateGspExists(tempView, params)) {
                        render(view: tempView, model: [catalog: catalog, newsInstanceList: objs, newsInstanceTotal: objsCount])
                        return
                    }
                }
            }else{
                render ''
            }
        }else{
            render ''
        }
    }

    @Cacheable(value='newsSearch',key="#keyword + 'search'")
    def search(){
        if(!params.offset) params.offset ='0'
        if(!params.max){
            if(params.site){
                params.max=Site.findByCode(params.site)?.pageMax?:'10'
            }
            else{
                params.max='30'
            }
        }
        def criteria1=News.createCriteria()
        def criteria2=News.createCriteria()
        def keyWords = params?.keyword?.tokenize(" ")
        def objs=criteria1.list{
            if(params.site && params.site.isLong()){
                catalog{
                    site{
                        eq("id",params.site.toLong())
                    }
                }
            }
            if(params.keyword){
                or{
                    like("title","%${keyWords.join("%")}%")
//                    like("content","%${keyWords.join("%")}%")
//                    like("outline","%${keyWords.join("%")}%")
//                    like("keywords","%${keyWords.join("%")}%")
                }
            }
            if(params.catalogId&&params.catalogId!='all'){
                eq('catalog.id',params.catalogId.toLong())
            }
            eq("state","发布")
            eq("approve",true)
            gt("expireDate",new Date())
//            order("sequencer","asc")
//            order("publishDate","desc")
            order("id","desc")
            firstResult(params.offset?.toInteger())
            maxResults(params.max?.toInteger())

        }
        def objsCount=criteria2.count{
            if(params.site  && params.site.isLong()){
                catalog{
                    site{
                        eq("id",params.site.toLong())
                    }
                }
            }
            if(params.keyword){
                or{
                    like("title","%${params.keyword}%")
//                    like("content","%${params.keyword}%")
//                    like("outline","%${params.keyword}%")
//                    like("keywords","%${params.keyword}%")
                }
            }
            if(params.catalogId&&params.catalogId!='all'){
                eq('catalog.id',params.catalogId.toLong())
            }
            eq("state","发布")
            eq("approve",true)
            gt("expireDate",new Date())
        }
        def view="/sites/${params.site}/${params.view?:'search'}";
        if(checkIfTemplateGspExists(view,params)){
            render (view:view,model:[newsInstanceList:objs,newsInstanceTotal:objsCount,keyword:params.keyword])
            return
        }else{
            render "no search template"
        }
        return
    }

    @Cacheable(value='newsDetail')
    def detail() {
        News newsInstance
        if(params.id && params.id.isLong())   {
            newsInstance =News.get( params.id )
        }
        //def layouts ='com.bjrxkj.cms'
        if(!newsInstance) {
            flash.message= g.message(code:'flash.message.none')
            redirect(uri:"/")
        }
        else {
            if(!springSecurityService.isLoggedIn()&&Catalog.controlCatalog().contains(newsInstance.catalogId)){
                redirect(uri: '/login/auth?loginType=show')
                return;
            }
            XmlSlurper slurper = new XmlSlurper(new org.ccil.cowan.tagsoup.Parser());
            String content=slurper.parseText(newsInstance.content?:'');
            if(!newsInstance.content&&Attachment.countByNews(newsInstance)==1){
                def attactment=Attachment.findByNews(newsInstance);
                redirect(uri: "/web/getFile/${attactment.id}");
                return;
            }
            if(newsInstance.redirectURL){
                redirect(url:newsInstance.redirectURL)
                return
            }else{
                def catalog=newsInstance.catalog;
                String templatePath="/sites/${catalog?.site?.name}";
                if (catalog?.site?.template) {
                    templatePath="/sites/template/${catalog?.site?.template}";
                }
                def view="${templatePath}/detail"
                if(catalog?.templateDetail){
                    view="${templatePath}/${catalog?.templateDetail}"
                }
                if(params.view){
                    view="${templatePath}/${params.view}"
                    render(view:view,model:[ newsInstance : newsInstance,catalog:catalog])
                    return
                }
                String tempView="${templatePath}/detail${catalog?.id}"
                if(checkIfTemplateGspExists(tempView,params)){
                    render (view:tempView,model:[newsInstance : newsInstance,catalog:catalog])
                    return
                }
                render(view:view,model:[newsInstance : newsInstance,catalog:catalog])
                return
            }
        }
    }

    def mp4(){
        def movie = News.get( params.id.toLong() )
        // Parse range specifier
        response.setHeader "Cache-Control", "no-store, must-revalidate"
        response.setHeader "Expires", "Sat, 26 Jul 1997 05:00:00 GMT"
        response.setHeader "Accept-Ranges", "bytes"

        ServletOutputStream oStream = response.outputStream

        // :TODO: a temporary fixed value, which should reflect the movie.contentType
        String contentType = "video/mp4"

        response.contentType = contentType
        response.setHeader "Content-Length", movie.mp4.length
        oStream << new BufferedInputStream(movie.mp4)
    }

    //无图片时显示默认图片

//    def picture(){
//        def b
//        if(!(params.id && params.id.isLong())){
//            render '新闻不存在'
//            return
//        }
//        def newsInstance = News.get(params.id.toLong())
//        if(newsInstance && newsInstance.picture){
//            b=newsInstance.picture
//        }else{
//            b=commonService.imageBytes(request);
//            newsInstance.picture=b
//        }
//        String watermark=newsInstance.catalog?.site?.watermark
//        if(watermark){
//            b= ImageUtils.pressText(watermark,newsInstance.picture,"微软雅黑", Font.BOLD+Font.ITALIC,255,15,5, 0);
//        }
//        try{
//            response.setContentType("image/jpeg");
//            response.setContentLength(b.size());
//            OutputStream out = response.outputStream
//            b.eachByte {
//                out.write(it)
//            }
//            out.close()
//            render "";
//            return;
//        }catch(Exception e){
//            return
//        }
//    }
//    @Cacheable(value='picture',key="#id + 'picture'")
    def picture(){
        println params
        def picturePath;
        if(!(params.id && params.id.isLong())){
            render '新闻不存在'
            return
        }
        def newsInstance = News.get(params.id.toLong());
        if(newsInstance && newsInstance.picture){
            picturePath=newsInstance.picture;
        }else{
            picturePath=request.servletContext.getRealPath("/sites/template/default.png");
        }
        try{
            def outdata=new File(picturePath).bytes;
            response.setContentType("image/jpeg");
            response.setContentLength(outdata.size());
            OutputStream out = response.outputStream;
            out.write(outdata);
            out.close();
        }catch(Exception e){
            return
        }
    }

    def upload() {
        def config = grailsApplication.config.ckeditor
        String sFilePath = "/${params.space}/${params.type}/${params.path}";
        String sFileName = sFilePath.substring(sFilePath.lastIndexOf("/") + 1)
        sFilePath.replace("/", File.separator)
        if (sFilePath) {
            FileInputStream fileInputStream = null
            try {
                String sFileExt = sFilePath
                if (sFileExt == "jpg" || sFileExt == "jpeg")
                    response.setContentType("image/jpeg")
                response.setHeader('filename', sFileName)
                OutputStream out = response.outputStream
                fileInputStream = new FileInputStream(config?.upload?.basedir + sFilePath)
                response.setContentLength(fileInputStream.available())
                int bytesRead
                byte[] buffer = new byte[8096]
                while ((bytesRead = fileInputStream.read(buffer, 0, 8096)) != -1)
                    out.write(buffer, 0, bytesRead)
                out.flush()
                out.close()
            } catch (Exception e) {
                log.error(e.getMessage())
            }
            finally {
                if (fileInputStream != null)
                    fileInputStream.close()
            }
        }
    }

    def downloadSrcFile() {
        String path="/uploads/docfile/${params.filepath}";
        File file=new File(path);
        response.setContentType("application/x-msdownload;charset=GBK")
        response.addHeader("Content-Disposition", 'attachment; filename="' + new String("${file.name}".getBytes("GBK"),"ISO8859-1")+'"')
        OutputStream out = response.outputStream
        out.write(file.bytes)
        out.close();
    }

    def lzxyDetail(){
        def obj = Integrity.get(params.id?.toLong() ?: -1l);
        def list = IntegrityDetail.findAllByIntegrity(obj,[sort: 'sq']);
        render(view:'/sites/gjgzw/lzxyDetail',model:[obj: obj, list: list])
        return
    }
    def integrity() {
        if(!params.max) params.max='15'

        if(!params.offset) params.offset ='0'
        def dateStr = new Date().format("yyyy-MM-dd")
        def totals, list
        totals = Integrity.createCriteria().count {
            eq("status", "发布")
            le('publishDate', Date.parse("yyyy-MM-dd HH:mm:ss", "${dateStr} 23:59:59"))
        }
        list = Integrity.createCriteria().list {
            eq("status", "发布")
            le('publishDate', Date.parse("yyyy-MM-dd HH:mm:ss", "${dateStr} 23:59:59"))
            order("publishDate", "desc")
            order("id", "desc")
            firstResult(params.offset?.toInteger())
            maxResults(params.max?.toInteger())
        }
        render(view:'/sites/gjgzw/lzxyList',model:[newsInstanceList:list,newsInstanceTotal:totals])
        return
    }

    def downloadPicture(){
        def newsList=News.createCriteria().list {
            isNotNull('picture')
//            isNull('picturepath')
        }
        newsList.each{News news->
            def picture=news.picture;
            //uploads/images/202012/1.jpg
            def dirPath=grailsApplication.config.project.setting.imagesPath+File.separator+"${news.publishDate.format('yyyyMM')}";
            def map=[:]; map.status=0;map.message="网络错误，请重试";map.file_id='';
            try{
                def dirfile=new File(dirPath);
                if(!dirfile.exists()){
                    dirfile.mkdirs();
                }
                String uuid = UUID.randomUUID().toString().replaceAll("-","");
                def picturepath=dirPath+File.separator+"${uuid}.jpg";
                def file=new File(picturepath);
                if(!file.exists()){
                    file.createNewFile();
                }
                FileOutputStream out = new FileOutputStream(file);
                try{
                    out.write(picture);
                    news.picturepath=picturepath;
                    news.save(flush: true);
                }catch(e){
                    log.error(e.message);
                }finally{
                    out.close();
                }
            }catch(e){
                log.error(e.message);
            }
        }
        render 'finish'
    }


}
