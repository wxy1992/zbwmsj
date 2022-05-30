package com.bjrxkj.cms.vote
//问题表
class VoteQuestion {
	Vote vote
	String title
	String content
    String type //单选 多选
	Date dateCreated
	Date lastUpdated 
    static constraints = {
		title(size:1..500,blank: false,nullable:false)
        type(size:1..50,blank: false,nullable:false)
		content(size:1..8000,blank: true,nullable:true)
		vote(nullable:false)
    }
}
