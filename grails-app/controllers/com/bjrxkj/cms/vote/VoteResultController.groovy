package com.bjrxkj.cms.vote

class VoteResultController {
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ voteResultInstanceList: VoteResult.list( params ), voteResultInstanceTotal: VoteResult.count() ]
    }

    def show = {
        def voteResultInstance = VoteResult.get( params.id )

        if(!voteResultInstance) {
            //flash.message = "VoteResult not found with id ${params.id}"
            flash.message = "VoteResult 未发现该记录 id ${params.id}"
            redirect(action:list)
        }
        else { return [ voteResultInstance : voteResultInstance ] }
    }

    def delete = {
        def voteResultInstance = VoteResult.get( params.id )
        if(voteResultInstance) {
            try {
                voteResultInstance.delete()
                //flash.message = "VoteResult ${params.id} deleted"
                flash.message = "删除成功!"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                //flash.message = "VoteResult ${params.id} could not be deleted"
                flash.message = "存在其他数据引用该记录，无法删除"
                redirect(action:show,id:params.id)
            }
        }
        else {
            //flash.message = "VoteResult not found with id ${params.id}"
            flash.message = "VoteResult 未发现该记录 id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def voteResultInstance = VoteResult.get( params.id )

        if(!voteResultInstance) {
            //flash.message = "VoteResult not found with id ${params.id}"
            flash.message = "VoteResult 未发现该记录 id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ voteResultInstance : voteResultInstance ]
        }
    }

    def update = {
        def voteResultInstance = VoteResult.get( params.id )
        if(voteResultInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(voteResultInstance.version > version) {
                    
                    //voteResultInstance.errors.rejectValue("version", "voteResult.optimistic.locking.failure", "Another user has updated this VoteResult while you were editing.")
                    voteResultInstance.errors.rejectValue("version", "voteResult.optimistic.locking.failure", "该记录在你编辑过程中被其他人修改.")
                    render(view:'edit',model:[voteResultInstance:voteResultInstance])
                    return
                }
            }
            voteResultInstance.properties = params
            if(!voteResultInstance.hasErrors() && voteResultInstance.save()) {
                //flash.message = "VoteResult ${params.id} updated"
                flash.message = "修改成功!"
                redirect(action:show,id:voteResultInstance.id)
            }
            else {
                render(view:'edit',model:[voteResultInstance:voteResultInstance])
            }
        }
        else {
        	//flash.message = "VoteResult not found with id ${params.id}"
            flash.message = "VoteResult 未发现该记录 id ${params.id}"
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def voteResultInstance = new VoteResult()
        voteResultInstance.properties = params
        return ['voteResultInstance':voteResultInstance]
    }

    def save = {
        def voteResultInstance = new VoteResult(params)
        if(!voteResultInstance.hasErrors() && voteResultInstance.save()) {
        	//flash.message = "VoteResult ${voteResultInstance.id} created"
            flash.message = "创建成功!"
            redirect(action:show,id:voteResultInstance.id)
        }
        else {
            render(view:'create',model:[voteResultInstance:voteResultInstance])
        }
    }
}
