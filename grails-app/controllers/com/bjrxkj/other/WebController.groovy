package com.bjrxkj.other

import com.bjrxkj.cms.Attachment
import com.bjrxkj.cms.Catalog
import com.bjrxkj.cms.News
import com.bjrxkj.cms.Site
import com.bjrxkj.core.BaseUser
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import javax.servlet.http.Cookie

class WebController {
    SpringSecurityService springSecurityService;
    def commonService;
    def cmsService;
    def index() {
        render springSecurityService.encodePassword('123456');
    }

    def list = {
        def sites= Site.createCriteria().list {
            order("sequencer","asc")
            order("id","asc")
        }
        if(!params.max) params.max='10'
        if(!params.offset) params.offset ='0'
        def criteria1= Catalog.createCriteria()
        def criteria2=Catalog.createCriteria()
        def baseUser=BaseUser.get(springSecurityService.getPrincipal().id)
        def site1=Site.list().getAt(0)
        if(baseUser && baseUser.site) site1=baseUser.site
        def objs=criteria1.list{
            if(params["catalog.id"]){
                parent{
                    eq("id",params["catalog.id"].toLong())
                }
            }
            if(params.name){
                like("name","%${params.name.trim()}%")
            }
            if(params["site.id"]){
                site{
                    eq("id",params["site.id"].toLong())
                }
            }
            order("id","asc")
            firstResult(params.offset?.toInteger())
            maxResults(params.max?.toInteger())
        }
        def objsCount=criteria2.count{
            if(params["catalog.id"]){
                parent{
                    eq("id",params["catalog.id"].toLong())
                }
            }
            if(params.name){
                like("name","%${params.name.trim()}%")
            }
            if(params["site.id"]){
                site{
                    eq("id",params["site.id"].toLong())
                }
            }

        }
        [ objs: objs, objsCount: objsCount ,site1:site1,tid:params.tid,sites:sites]
    }

  /*  def getFile={
        println params;
        def file=Attachment.get(params.id?.toLong())
        if(file){
            try{
                response.setContentType("application/x-msdownload;charset=GBK")
                response.addHeader("Content-Disposition", 'attachment; filename="' + new String("${file.filename}".getBytes("GBK"),"ISO8859-1")+'"')
                OutputStream out = response.outputStream
                out.write(file.filedata)
                out.close()
            }
            catch(Exception e){
                //render "没有这个文件"
                e.printStackTrace()
                return
            }

        }else{
            //throw new Exception('无法下载')
            render "没有这个文件"
            return
        }

    }*/

    def getFile(){
        def file= Attachment.get(params.id);
        if(file){
            try{
                def outdata=file.data;
                if(!outdata){
                    outdata=new File(file.filepath).bytes
                }
                if(file.type==3){
                    response.setContentType("image/jpeg");
                }else{
                    response.setContentType("application/x-msdownload;charset=GBK")
                    response.addHeader("Content-Disposition", 'attachment; filename="' + new String("${file.name}".getBytes("GBK"),"ISO8859-1")+'"')
                }
                response.setContentLength(outdata.size());
                OutputStream out = response.outputStream;
                out.write(outdata);
                out.close()
            }
            catch(Exception e){
                //render "没有这个文件"
                return
            }

        }else{
            //throw new Exception('无法下载')
            render "没有这个文件"
            return
        }

    }

    def cleanAllNews(){
        render commonService.cleanAllNews(params);
    }

    /**
     * 评论
     * @return
     */
    def submitComment(){
        def map=[:];
        params.fromip=commonService.getIpAddr(request);
        map=cmsService.submitComment(params,springSecurityService.currentUser);
        render text: "${map as JSON}",encoding: "UTF-8"
    }

    /**
     * 评论列表
     * @return
     */
    def commentJson(){
        def map=cmsService.commentJson(params);
        render text: "${map as JSON}",encoding: "UTF-8"
    }

    /**
     * 评论列表
     * @return
     */
    def collectionJson(){
        def map=cmsService.collectionJson(params);
        render text: "${map as JSON}",encoding: "UTF-8"
    }
    /**
     * 评论点赞、新闻收藏
     * @return
     */
    def commentOrNewsLike(){
        def baseUser=springSecurityService.currentUser;
        def res=cmsService.commentOrNewsLike(params,baseUser);
        render text: res,encoding: "UTF-8"
    }


    /**
     * 百问百答详情
     * @return
     */
    def questionAnswerDetail(){
        def obj;
        if(params.id&&params.id.isNumber()){
            obj=QuestionAnswer.get(params.id.toInteger())
        }
        return [obj:obj]
    }

    /**
     * 新闻浏览量
     */
    def newsVisitnum(){
        def map=[:];
        if(params.id){
            def news= News.get(params.id.toLong());
            news.clicknum=news.clicknum+1;
            news.save(flush: true);
        }
        render map as JSON;
    }

    /**
     * 清楚缓存
     * @return
     */
    def clearIndexCache(){
        def map=[result:'ok'];
        try{
            cmsService.cacheClear(params.name?:'siteIndex');
            this.clearCookies();
        }catch(e){
            map=[result:'fail'];
        }
        render map as JSON;
    }

    def clearCookies(){
        def map=[result:'ok'];
        try{
            Cookie[] cookies = request.getCookies();
            cookies.each {cookie->
                if(cookie&&cookie.name){
                    if(cookie.name.startsWith("JCY_CERT")){
                        cookie.setMaxAge(0);
                    }
                }
            }
        }catch(e){
            map=[result:'fail'];
        }
        render map as JSON;
    }
}
