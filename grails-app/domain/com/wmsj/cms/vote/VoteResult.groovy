package com.wmsj.cms.vote

import com.bjrxkj.core.BaseUser

class VoteResult {
    VoteDetail voteDetail
	String fromip
    BaseUser baseUser
	Date dateCreated
	Date lastUpdated
    static constraints = {
		fromip(size:0..200,blank: true,nullable:true)
        voteDetail(nullable:false)
        baseUser(nullable:true)
    }
}
