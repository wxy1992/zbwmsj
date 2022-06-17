package com.wmsj.other

import business.TradeService
import com.wmsj.utils.ServerResponse
import org.springframework.web.bind.annotation.RestController

@RestController
class TradeApiController {

    TradeService tradeService;

    /**
     * 服务类型
     */
    def describeTradeTypes(){
        try {
            def typeList = tradeService.describeTradeTypes(params);
            render ServerResponse.success(typeList);
        }catch(e){
            log.error(e.message);
            render ServerResponse.fail();
        }
    }


    /**
     * 服务列表
     * */
    def describeTrades(){
        if(!params.max) params.max='10';
        if(!params.offset) params.offset ='0';
        def sortMap = ['sequencer':'asc','id':'desc'];
        try {
            def tradeList = tradeService.describeTrades(params);
            render ServerResponse.success(tradeList);
        }catch(e){
            log.error(e.message);
            render ServerResponse.fail();
        }
    }

    /**
     * 服务详情
     * */
    def describeTradeDetail(){
        Map trade;
        try{
            if(params.tradeId){
                trade = tradeService.describeTradeDetail(params);
            }
            render ServerResponse.success(trade);
        }catch(e){
            log.error(e.message);
            render ServerResponse.fail();
        }
    }

    /**
     * 报名服务
     * */
    def tradeApply(){
        Map result=[:];
        try{
            result=tradeService.tradeApply(params);
            render ServerResponse.success(result);
        }catch(e){
            log.error(e.message);
            render ServerResponse.fail();
        }
    }

    /**
     * 评价服务
     */
    Map submitCommentary(def params){
        def baseUser=request.getAttribute("baseUser");
        Map result=[:];
        try{
            result=tradeService.submitCommentary(params,baseUser);
            render ServerResponse.success(result);
        }catch(e){
            log.error(e.message);
            render ServerResponse.fail();
        }
    }

    /**
     * 评价列表
     * @return
     */
    def commentaryJson(){
        if(!params.max) params.max='10';
        if(!params.offset) params.offset ='0';
        def commentaryList=[];
        try{
            if(params.tradeId){
                commentaryList=tradeService.commentaryJson(params);
            }
            render ServerResponse.success(commentaryList);
        }catch(e){
            log.error(e.message);
            render ServerResponse.fail();
        }
    }
}
