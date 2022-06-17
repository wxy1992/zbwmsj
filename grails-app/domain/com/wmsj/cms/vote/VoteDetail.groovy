package com.wmsj.cms.vote
//选项表
class VoteDetail {
    VoteQuestion voteQuestion
	String title

	Date dateCreated
	Date lastUpdated
    static constraints = {
        title(size:1..400,blank: false,nullable:false)
        voteQuestion(nullable: false)
    }
}
