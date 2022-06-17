package com.wmsj.cms

import com.wmsj.core.BaseUser
import com.wmsj.annotation.Title;

@Title(zh_CN='快速链接')
class QuickLink {
    @Title(zh_CN='标题')
    String title
    @Title(zh_CN='跳转链接')
    String redirectURL
    @Title(zh_CN='发布人')
	BaseUser publisher

    static constraints = {
		title(size:1..200,blank: false)
		redirectURL(size:1..400,blank: false,nullable:false)
		publisher(nullable:false)
    }
}
