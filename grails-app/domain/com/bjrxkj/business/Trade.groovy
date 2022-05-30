package com.bjrxkj.business

import com.bjrxkj.annotation.Title
import com.bjrxkj.core.Organization

class Trade {
    @Title(zh_CN='标题')
    String title
    @Title(zh_CN='正文')
    String content
    @Title(zh_CN='状态')
    Integer status=StatusEnum.SUBMIT.code//00草稿，10已提交，20发布中，30已完成（待评价），40已结束
    @Title(zh_CN='开始时间')
    Date beginDate
    @Title(zh_CN='结束时间')
    Date endDate
    @Title(zh_CN='发布单位')
    Organization organization
    @Title(zh_CN='是否删除')
    Boolean deleted=false
    @Title(zh_CN='创建时间')
    Date dateCreated
    @Title(zh_CN='修改时间')
    Date lastUpdated

    static constraints = {
        title(nullable:false,size:0..500)
        status(nullable: false,min: 10)
        beginDate(nullable: false)
        endDate(nullable: true)
        organization(nullable: false)

    }

    static mapping = {
        content type: 'text'
    }


    enum StatusEnum {
        DRAFT(00, "草稿"),
        SUBMIT(10, "已提交"),
        PUBLISH(20, "发布中"),
        FINISHED(30, "已完成"),
        END(40, "已结束");

        private Integer code;
        private String value;

        private StatusEnum(Integer code, String value) {
            this.code = code;
            this.value = value;
        }
    }
}
