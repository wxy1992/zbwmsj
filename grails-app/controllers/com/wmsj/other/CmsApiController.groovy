package com.wmsj.other

import cms.CmsService
import cms.CommonService
import com.wmsj.utils.ServerResponse
import org.springframework.web.bind.annotation.RestController

@RestController
class CmsApiController {
    CommonService commonService;
    CmsService cmsService;

    /**
     * 栏目列表
     */
    def describeCatalogs(){
        if(!params.max) params.max='10';
        if(!params.offset) params.offset ='0';
        try{
            def catalogs=cmsService.describeCatalogs(params);
            render ServerResponse.success(catalogs);
        }catch(e){
            render ServerResponse.fail();
        }
    }

    /**
     * 稿件列表
     */
    def describeNewsByCatalog(){
        if(!params.max) params.max='10';
        if(!params.offset) params.offset ='0';
        try{
            def newsList=[];
            if(params.catalogId){
                newsList=cmsService.describeNewsByCatalog(params);
            }
            render ServerResponse.success(newsList);
        }catch(e){
            render ServerResponse.fail();
        }
    }

    /**
     * 稿件详情
     * @return
     */
    def describeNewsDetail(){
        def news;
        try{
            if(params.newsId){
                news=cmsService.describeNewsDetail(params);
                if(!news){
                    render ServerResponse.fail("文章不存在");
                }
            }
            render ServerResponse.success(news);
        }catch(e){
            log.error(e.message);
            render ServerResponse.fail();
        }
    }

    /**
     * 封面图片
     * @return
     */
    def picture(){
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
