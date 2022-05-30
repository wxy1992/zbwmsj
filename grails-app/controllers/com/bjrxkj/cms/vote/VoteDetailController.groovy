package com.bjrxkj.cms.vote

class VoteDetailController {
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [ save:'POST', update:'POST']


    def list = {
        if(!params.max) params.max='10'
        if(!params.offset) params.offset ='0'
        def criteria1=VoteDetail.createCriteria()
        def criteria2=VoteDetail.createCriteria()
        def objs=criteria1.list{
            if(params.title){
                like("title","%${params.title.trim()}%")

            }
            voteQuestion{
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
            voteQuestion{
                eq("id",params.id?.toLong()?:-1l)
            }
        }
        [ objs: objs, objsCount: objsCount,voteQuestion: VoteQuestion.get(params.id?.toLong()?:-1l)]
    }

    def show = {
        def voteDetailInstance = VoteDetail.get( params.id )

        if(!voteDetailInstance) {
            //flash.message = "VoteDetail not found with id ${params.id}"
            flash.message = "未发现该记录"
            redirect(action:list)
        }
        else { return [ voteDetailInstance : voteDetailInstance ] }
    }

    def delete = {
        def voteDetailInstance = VoteDetail.get( params.id )
        if(voteDetailInstance) {
            try {
                def id=voteDetailInstance.voteQuestionId
                voteDetailInstance.delete(flush: true)
                //flash.message = "VoteDetail ${params.id} deleted"
                flash.message = "删除成功!"
                redirect(action:list,id: id)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                //flash.message = "VoteDetail ${params.id} could not be deleted"
                flash.message = "存在其他数据引用该记录，无法删除"
                redirect(action:show,id:params.id)
            }
        }
        else {
            //flash.message = "VoteDetail not found with id ${params.id}"
            flash.message = " 未发现该记录"
            redirect(action:list)
        }
    }

    def edit = {
        def voteDetailInstance = VoteDetail.get( params.id )

        if(!voteDetailInstance) {
            //flash.message = "VoteDetail not found with id ${params.id}"
            flash.message = "未发现该记录"
            redirect(action:list,id: voteDetailInstance.voteQuestionId)
        }
        else {
            return [ voteDetailInstance : voteDetailInstance ]
        }
    }

    def update = {
        def voteDetailInstance = VoteDetail.get( params.id )
        if(voteDetailInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(voteDetailInstance.version > version) {
                    
                    //voteDetailInstance.errors.rejectValue("version", "voteDetail.optimistic.locking.failure", "Another user has updated this VoteDetail while you were editing.")
                    voteDetailInstance.errors.rejectValue("version", "voteDetail.optimistic.locking.failure", "该记录在你编辑过程中被其他人修改.")
                    render(view:'edit',model:[voteDetailInstance:voteDetailInstance])
                    return
                }
            }
            voteDetailInstance.properties = params
            if(!voteDetailInstance.hasErrors() && voteDetailInstance.save(flush: true)) {
                //flash.message = "VoteDetail ${params.id} updated"
                flash.message = "修改成功!"
                redirect(action:list,id:voteDetailInstance.voteQuestionId)
            }
            else {
                render(view:'edit',model:[voteDetailInstance:voteDetailInstance])
            }
        }
        else {
        	//flash.message = "VoteDetail not found with id ${params.id}"
            flash.message = "未发现该记录"
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def voteDetailInstance = new VoteDetail()
        voteDetailInstance.properties = params
        return ['voteDetailInstance':voteDetailInstance]
    }

    def save = {
        def voteDetailInstance = new VoteDetail(params)
        if(!voteDetailInstance.hasErrors() && voteDetailInstance.save()) {
        	//flash.message = "VoteDetail ${voteDetailInstance.id} created"
            flash.message = "创建成功!"
            redirect(action:list,id:voteDetailInstance.voteQuestionId)
        }
        else {
            render(view:'create',model:[voteDetailInstance:voteDetailInstance])
        }
    }
}
