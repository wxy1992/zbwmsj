package com.bjrxkj.cms

import grails.converters.JSON

class SiteController {

    def createOrEdit(){
        def site;
        if(params.id){
            site=Site.get(params.id.toLong())
        }else{
            site=new Site();
        }
        return [site:site];
    }

    def saveOrUpdate(){
        def site,map=[:];
        map.result=false;
        map.message="网络错误，请重试！";
        if(params.id){
            site=Site.get(params.id.toLong())
        }else{
            site=new Site();
        }
        try{
            site.properties=params;
            if(site.homesite&&Site.countByNameNotEqualAndHomesite(site.name,true)>0){
                map.message="主站已经存在";
            }else{
                if(site.save(flush:true)){
                    map.result=true;
                    map.message="操作成功";
                }else{
                    log.error(site.errors);
                }
            }
        }catch(e){
            log.error(e.message);
        }
        render "${map as JSON}";
    }

    def deleteSite(){
        def site,map=[:];
        map.result=false;
        map.message="网络错误，请重试！";
        if(params.id){
            site=Site.get(params.id.toLong());
            if(Catalog.countBySite(site)>0){
                map.message="请先删除该站点栏目！";
            }else{
                try{
                    def sitename=site.name;
                    site.delete(flush: true);
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

    def siteJsonSearch(def params) {
        return Site.whereLazy {
            ilike("companyName", "%${params.siteName?.trim()}%")
            ilike("domainName", "%${params.siteDomain?.trim()}%")
        }
    }

    def json(){
        if(!params.max) params.max='10'
        if(!params.offset) params.offset ='0'
        def objs=siteJsonSearch(params).list(max: params.max.toInteger(),
                offset: params.offset.toInteger(),
                {
                    order('sequencer','asc')
                    order('id','desc')
                }
        )
        def objsCount=siteJsonSearch().count()
        def map=[:];
        map.rows=objs;
        map.total=objsCount;
        render "${map as JSON}";
    }

    /**
     * 站点封面
     * @return
     */
    def picture(){
        if(!(params.id && params.id.isLong())){
            render "not allowed"
            return
        }
        def siteInstance = Site.get( params.id )
        if(siteInstance && siteInstance.picture){
            try{
                response.setContentType("image/JPEG")
                response.setContentLength(siteInstance.picture.size())
                response.setHeader('filename', "${siteInstance.id}")
                OutputStream out = response.outputStream
                out.write(siteInstance.picture)
                out.close()
            }catch(Exception e){
                log.error 'user give up download the pic file,Site !'
            }
        }
    }

    def preview() {
        def map = [:]
        def site = Site.get(params.id)
        map.name = site.name
        map.result = true
        render "${map as JSON}";
    }

}
