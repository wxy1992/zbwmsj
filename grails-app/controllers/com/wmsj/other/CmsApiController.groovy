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
     * 根据code换取openid
     * @return
     */
    def code2Userid() {
        try {
            def result=[:];
            if(params.code){
                result = cmsService.code2Userid(params);
            }
            render ServerResponse.success(result);
        } catch (e) {
            render ServerResponse.fail(e.message);
        }
    }

    /**
     * 更新用户信息
     * @return
     */
    def updateUserInfo(){
        try {
            def result=[:];
            if(params.userid){
                result = cmsService.updateUserInfo(params);
            }
            render ServerResponse.success(result);
        } catch (e) {
            render ServerResponse.fail(e.message);
        }
    }

    /**
     * 获取AccessToken
     */
    def getAccessToken(){
        try {
            def result=cmsService.getAccessToken();
            render ServerResponse.success(result);
        } catch (e) {
            render ServerResponse.fail(e.message);
        }
    }

    /**
     * 更新用户信息
     * @param params
     * @return
     */
    def updateUserTel(){
        try {
            def result=[:];
            if(params.userid&&params.access_token){
                result=cmsService.updateUserTel(params);
            }
            render ServerResponse.success(result);
        } catch (e) {
            render ServerResponse.fail(e.message);
        }
    }


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
            render ServerResponse.fail(e.message);
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
            render ServerResponse.fail(e.message);
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
            render ServerResponse.fail(e.message);
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
