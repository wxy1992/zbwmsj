package com.wmsj.cms.vote

class VoteAnswer {
	VoteQuestion voteQuestion
	String title
	Date dateCreated
	Date lastUpdated
    static constraints = {
		title(size:1..500,blank: false,nullable:false)
		voteQuestion(nullable:false)
    }
}
