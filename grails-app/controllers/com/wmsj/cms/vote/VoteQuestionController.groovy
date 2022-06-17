package com.wmsj.cms.vote

class VoteQuestionController {

    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [save:'POST', update:'POST']

    def list = {
        if(!params.max) params.max='10'
        if(!params.offset) params.offset ='0'
        def criteria1=VoteQuestion.createCriteria()
        def criteria2=VoteQuestion.createCriteria()
        def objs=criteria1.list{
            if(params.title){
                like("title","%${params.title.trim()}%")

            }
            vote{
                eq("id",params.id?.toLong()?:-1l)
            }
            order("id","asc")
            firstResult(params.offset?.toInteger())
            maxResults(params.max?.toInteger())
        }
        def objsCount=criteria2.count{
            if(params.title){
                like("title","%${params.title.trim()}%")
            }
            vote{
                eq("id",params.id?.toLong()?:-1l)
            }
        }
        [ objs: objs, objsCount: objsCount,vote: Vote.get(params.id?.toLong()?:-1l)]
    }
    def show = {
        def voteQuestionInstance = VoteQuestion.get( params.id )
        return [ obj : voteQuestionInstance ]

    }

    def delete = {
        def voteQuestionInstance = VoteQuestion.get( params.id );
        if(voteQuestionInstance) {
            try {
                def voteDetails=VoteDetail.findAllByVoteQuestion(voteQuestionInstance);
                 voteDetails.each{
                    it.delete();
                }
                def id=voteQuestionInstance.voteId
                voteQuestionInstance.delete(flush: true)
                //flash.message = "Vote ${params.id} deleted"
                flash.message = "删除成功!"
                redirect(action:list,id: id)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                //flash.message = "VoteQuestion ${params.id} could not be deleted"
                flash.message = "存在其他数据引用该记录，无法删除"
                redirect(action:list,id: voteQuestionInstance.voteId)
            }
        }
        else {
            //flash.message = "VoteQuestion not found with id ${params.id}"
            flash.message = "未发现该记录"
            redirect(action:list,id: voteQuestionInstance?.voteId)
        }
    }
    def edit = {
        def voteQuestionInstance = VoteQuestion.get( params.id )
        if(!voteQuestionInstance) {
            //flash.message = "VoteQuestion not found with id ${params.id}"
            flash.message = "未发现该记录"
            redirect(action:list)
        }
        else {
            return [ voteQuestionInstance : voteQuestionInstance ]
        }
    }
    def update = {
        def voteQuestionInstance = VoteQuestion.get( params.id )
        if(voteQuestionInstance) {

            voteQuestionInstance.properties = params
            if(!voteQuestionInstance.hasErrors() && voteQuestionInstance.save(flush: true)) {
                //flash.message = "VoteQuestion ${params.id} updated"
                flash.message = "修改成功!"
                redirect(action:list,id:voteQuestionInstance.voteId)
            }
            else {
                render(view:'edit',model:[voteQuestionInstance:voteQuestionInstance])
            }
        }
        else {
            //flash.message = "Vote not found with id ${params.id}"
            flash.message = "未发现该记录"
            redirect(action:edit,id:params.id)
        }
    }
    def create = {
        def obj = new VoteQuestion()
        obj.properties = params
        return ['obj':obj]
    }

    def save = {
        def obj = new VoteQuestion(params)
        if(!obj.hasErrors() && obj.save()) {

            redirect(action:list,id: obj.voteId)
        }
        else {
            flash.message = "请检查数据格式!"
            render ('view':'create',model: ['obj':obj])
        }
    }
}
