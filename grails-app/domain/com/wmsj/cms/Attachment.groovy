package com.wmsj.cms

import com.wmsj.annotation.Title

@Title(zh_CN='附件表')
class Attachment {
    @Title(zh_CN='新闻')
    News news
    @Title(zh_CN='文件名')
    String name
    @Title(zh_CN='扩展名')
    String suffix
    @Title(zh_CN='文件路径')
    String filepath
    @Title(zh_CN='创建时间')
    Date dateCreated
    @Title(zh_CN='修改时间')
    Date lastUpdated
    @Title(zh_CN='文件类型')
    Integer type=1//1附件，2视频，3图片
    @Title(zh_CN='文件路径')
    Integer sq

    static constraints = {
        news(nullable:true)
        filepath(nullable:false,size:0..500)
        type(nullable:false)
        name(size:0..500,nullable:false)
        sq(nullable: true)
    }

    String toString() {
        return name;
    }
}
