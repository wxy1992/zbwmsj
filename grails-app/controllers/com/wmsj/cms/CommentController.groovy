package com.wmsj.cms

import com.wmsj.cms.behaviour.Commentary


class CommentController {

    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:['POST','GET'], save:'POST', update:'POST']

    def list = {
    	if(!params.max) params.max='10'
        if(!params.offset) params.offset ='0'
        def criteria1= Commentary.createCriteria()
		def criteria2=Commentary.createCriteria()
		def objs=criteria2.list{
        	if(params['news.id'])
				news{
        			eq("id",params['news.id'].toLong())
        		}
        		order("lastUpdated","desc")
        		firstResult(params.offset?.toInteger())
            	maxResults(params.max?.toInteger())
			}
    	def objsCount=criteria1.count{
        	if(params['news.id'])
				news{
        			eq("id",params['news.id'].toLong())
        		}
			}
        [ objsCount: objsCount, objs: objs ]
    }

    def show = {
        def commentaryInstance = Commentary.get( params.id )

        if(!commentaryInstance) {
            //flash.message = "Commentary not found with id ${params.id}"
            flash.message = "Commentary 未发现该记录 id ${params.id}"
            redirect(action:list)
        }
        else { return [ commentaryInstance : commentaryInstance ] }
    }

    def delete = {
        def commentaryInstance = Commentary.get( params.id )
        commentaryInstance.delete()
        render "1"
    }

    def edit = {
        def commentaryInstance = Commentary.get( params.id )

        if(!commentaryInstance) {
            //flash.message = "Commentary not found with id ${params.id}"
            flash.message = "Commentary 未发现该记录 id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ commentaryInstance : commentaryInstance ]
        }
    }

    def update = {
        def commentaryInstance = Commentary.get( params.id )
        if(commentaryInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(commentaryInstance.version > version) {

                    //commentaryInstance.errors.rejectValue("version", "commentary.optimistic.locking.failure", "Another user has updated this Commentary while you were editing.")
                    commentaryInstance.errors.rejectValue("version", "commentary.optimistic.locking.failure", "该记录在你编辑过程中被其他人修改.")
                    render(view:'edit',model:[commentaryInstance:commentaryInstance])
                    return
                }
            }
            commentaryInstance.properties = params
            if(!commentaryInstance.hasErrors() && commentaryInstance.save()) {
                //flash.message = "Commentary ${params.id} updated"
                flash.message = "修改成功!"
                redirect(action:show,id:commentaryInstance.id)
            }
            else {
                render(view:'edit',model:[commentaryInstance:commentaryInstance])
            }
        }
        else {
        	//flash.message = "Commentary not found with id ${params.id}"
            flash.message = "Commentary 未发现该记录 id ${params.id}"
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def commentaryInstance = new Commentary()
        commentaryInstance.properties = params
        return ['commentaryInstance':commentaryInstance]
    }

    def save = {
        def commentaryInstance = new Commentary(params)
        if(!commentaryInstance.hasErrors() && commentaryInstance.save()) {
        	//flash.message = "Commentary ${commentaryInstance.id} created"
            flash.message = "创建成功!"
            redirect(action:show,id:commentaryInstance.id)
        }
        else {
            render(view:'create',model:[commentaryInstance:commentaryInstance])
        }
    }
    def ajaxSave={
    	def commentaryInstance = new Commentary(params)
    	if(!commentaryInstance.hasErrors() && commentaryInstance.save()) {
    		render '留言成功!'
    	}else{
    		render '留言失败!'
    	}
    }
    def deleteAll={
    	def ids=request.getParameterValues("ids")
		ids.each{
				def commentary=Commentary.get(it.toLong())
				commentary.delete()
		}
		flash.message='操作成功'
		redirect(uri:"/commentary/list?news.id=${params['news.id']}")
    }
    def publishAll={
    	def ids=request.getParameterValues("ids")
		ids.each{
				def commentary=Commentary.get(it.toLong())
				commentary.state='通过'
				commentary.save()
		}
		flash.message='操作成功'
		redirect(uri:"/commentary/list?news.id=${params['news.id']}")
    }
    def backAll={
    	def ids=request.getParameterValues("ids")
		ids.each{
				def commentary=Commentary.get(it.toLong())
				commentary.state='未通过'
				commentary.save()
		}
		flash.message='操作成功'
		redirect(uri:"/commentary/list?news.id=${params['news.id']}")
    }
}
