package com.wmsj.cms.vote

import com.bjrxkj.cms.News
import grails.plugin.springsecurity.SpringSecurityService

class VoteController {
    // the delete, save and update actions only accept POST requests
    static allowedMethods = [save:'POST', update:'POST']
    SpringSecurityService springSecurityService

    def all={
        return
    }
    def list = {
        if(!params.max) params.max='10'
		if(!params.offset) params.offset='0'
        if(!params.id) params.id='0'
		def criteria1=Vote.createCriteria()
	    def criteria2=Vote.createCriteria()
        News  news11=News.get(params.id.toLong())
		def objs=criteria1.list{
			if(params.title){
				or{
					like("title","%${params.title.trim()}%")
					like("content","%${params.title.trim()}%")
				}
			}
            if(news11){
                news{
                    eq("id",news11.id)
                }
            }
            if(params.state){
                eq("state",params.state)
            }
			order("id","desc")
        	firstResult(params.offset?.toInteger())
        	maxResults(params.max?.toInteger())
		}
		def objsCount=criteria2.count{
            if(params.title){
                or{
                    like("title","%${params.title.trim()}%")
                    like("content","%${params.title.trim()}%")
                }
            }
            if(news11){
                news{
                    eq("id",news11.id)
                }
            }
            if(params.state){
                eq("state",params.state)
            }
		}
        [ objs: objs, objsCount: objsCount,news11:news11]
    }
    def voteList={
        if(!params.max) params.max='10'
        if(!params.offset) params.offset='0'
        def criteria1=Vote.createCriteria()
        def criteria2=Vote.createCriteria()
        def objs=criteria1.list{
            if(params.title){
                or{
                    like("title","%${params.title.trim()}%")
                    like("content","%${params.title.trim()}%")
                }
            }
            if(params.state){
                eq("state",params.state)
            }
           // ne("state","草稿")

            eq("state","发布")
            order("id","desc")
            firstResult(params.offset?.toInteger())
            maxResults(params.max?.toInteger())
        }
        def objsCount=criteria2.count{
           eq("state","发布")
            //ne("state","草稿")
            if(params.title){
                or{
                    like("title","%${params.title.trim()}%")
                    like("content","%${params.title.trim()}%")
                }
            }
        }
        [ objs: objs, objsCount: objsCount]
    }

    def show = {
        def voteInstance = Vote.get( params.id )
        return [ voteInstance : voteInstance]
    }

    def delete = {
        def voteInstance = Vote.get( params.id)
        if(voteInstance) {
            def id=  voteInstance.newsId
            try {
                def voteQuestions=VoteQuestion.findAllByVote(voteInstance);
                voteQuestions.each {voteQuestion->
                def voteDetails=VoteDetail.findAllByVoteQuestion(voteQuestion);
                       voteDetails.each {
                         it.delete();
                     }
                    voteQuestion.delete();
                  }
                voteInstance.delete(flush: true);
                //flash.message = "Vote ${params.id} deleted"
                flash.message = "删除成功!"
                redirect(action:list,id:id)
            }
            catch(Exception e) {
                //flash.message = "Vote ${params.id} could not be deleted"
                flash.message = "存在其他数据引用该记录，无法删除"
                 redirect(action:list,id:id)
            }
        }
        else {
            //flash.message = "Vote not found with id ${params.id}"
            flash.message = "未发现该记录"
            redirect(action:list)
        }
    }

    def edit = {
        def voteInstance = Vote.get( params.id )
        if(!voteInstance) {
            //flash.message = "Vote not found with id ${params.id}"
            flash.message = "未发现该记录"
            redirect(action:list)
        } else {
            return [ voteInstance : voteInstance]
        }
    }

    def update = {
        def voteInstance = Vote.get( params.id )
        if(voteInstance) {
            voteInstance.properties = params
            if(!voteInstance.hasErrors() && voteInstance.save(flush: true)) {
                //flash.message = "Vote ${params.id} updated"
                flash.message = "修改成功!"
                redirect(action:list,id:voteInstance.newsId)
            }
            else {
                render(view:'edit',model:[voteInstance:voteInstance])
            }
        }
        else {
        	//flash.message = "Vote not found with id ${params.id}"
            flash.message = "未发现该记录"
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def obj = new Vote(params)
        return ['voteInstance':obj]
    }

    def save = {
        def obj = new Vote(params)
        if(!obj.hasErrors() && obj.save()) {
        	//flash.message = "Vote ${voteInstance.id} created"
            redirect(action:list,id: obj.newsId)
        }
        else {
            flash.message = "请检查数据格式!"
			render ('view':'create',model: ['voteInstance':obj])
        }
    }

    def index={
       def vote=Vote.get(params.id?.toLong()?:-1l)
        if (vote && vote.state!="草稿"){
         }
        else{
            vote=null
        }
        return [vote:vote]
    }

    def saveVote={
        def vote1=Vote.get(params.id?.toLong()?:-1l)
        def allvq= []
        def vrNum=0
        if(vote1){
            allvq=VoteQuestion.findAllByVote(vote1)
            allvq.each{ q->
                def allvr= request.getParameterValues("vd${q.id}")
                allvr.each{ r->
                    def vr=new VoteResult()
                    vr.fromip=request.getRemoteAddr()
                    def vd=VoteDetail.get(r.toLong())
                    if(vd){
                        vr.voteDetail= vd
                        vr.save(flush: true)
                    }
                }
            }
        }


        flash.message="操作成功"
        redirect(action: 'index',id:params.id )
    }
}
