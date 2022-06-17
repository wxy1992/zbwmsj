package com.wmsj.cms

import com.bjrxkj.annotation.Title

class Site {
    @Title(zh_CN='代码')
    String name
    @Title(zh_CN='域名')
    String domainName
    @Title(zh_CN='机构全称')
    String companyName
    @Title(zh_CN='显示名称')
    String shortName
    @Title(zh_CN='站点模板')
    String template
    @Title(zh_CN='站长邮箱')
    String adminEmail
    @Title(zh_CN='版权所有')
    String copyright
    @Title(zh_CN='备案号')
    String code
    @Title(zh_CN='地址')
    String address
    @Title(zh_CN='访问地址')
    String url
    @Title(zh_CN='浮动新闻图片')
    byte[] picture
    @Title(zh_CN='排序号')
    Integer sequencer=9999
    @Title(zh_CN='设为主站')
    Boolean homesite=false
    @Title(zh_CN='是否启用')
    Boolean enabled=true
    Date dateCreated
    Date lastUpdated

    static constraints = {
        name(size:1..50,blank: false,nullable:false,unique:true)
        domainName(size:1..500,blank: false,nullable:false)
        companyName(size:0..200,blank: true,nullable:true)
        shortName(size:0..200,blank: true,nullable:true)
        adminEmail(size:0..200,blank: true,nullable:true)
        template(size:0..200,blank: true,nullable:true)
        copyright(size:0..400,blank: true,nullable:true)
        code(size:0..200,blank: true,nullable:true)
        url(size:0..500,blank: true,nullable:true)
        picture(nullable:true,size:0..8000)
        sequencer(nullable:true)
        enabled(nullable:true)
        address(nullable:true)
        homesite(nullable:true)
    }

    static mapping = {
        picture type:'image'
    }

    static Site home(){
        return Site.createCriteria().get {
            eq('homesite',true)
            eq('enabled',true)
            order('id','desc')
            maxResults(1)
        }
    }
}
