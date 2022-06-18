package com.wmsj.business

import com.wmsj.annotation.Title
import com.wmsj.core.BaseUser
import com.wmsj.core.Organization
import org.grails.databinding.BindingFormat

class Trade {
    @Title(zh_CN='类型')
    TradeType tradeType
    @Title(zh_CN='标题')
    String title
    @Title(zh_CN='正文')
    String content
    @Title(zh_CN='服务方式')
    Integer way=1
    @Title(zh_CN='人数设置')
    Integer peopleNum
    @Title(zh_CN='状态')
    Integer status=10//00草稿，05退回，10已提交，20发布中，30已完成（待评价），40已结束
    @Title(zh_CN='开始时间')
    @BindingFormat("yyyy-MM-dd HH:mm")
    Date beginDate
    @Title(zh_CN='结束时间')
    @BindingFormat("yyyy-MM-dd HH:mm")
    Date endDate
    @Title(zh_CN='发布单位')
    Organization organization
    @Title(zh_CN='联系方式')
    String telephone
    @Title(zh_CN='服务地址')
    String address
    @Title('排序数值')
    int sequencer=9999999
    @Title('新闻图片')
    String picture
    @Title('退回原因')
    String backreason
    @Title('最后审核操作员')
    BaseUser auditUser
    @Title('审核时间')
    Date auditDate
    @Title(zh_CN='是否删除')
    Boolean deleted=false
    @Title(zh_CN='创建时间')
    Date dateCreated
    @Title(zh_CN='修改时间')
    Date lastUpdated

    static constraints = {
        title(nullable:false,size:0..500)
        way(nullable: false,inList: [1,2])
        peopleNum(nullable: true,min: 0)
        picture(nullable: true)
        status(nullable: false)
        beginDate(nullable: false)
        endDate(nullable: true)
        organization(nullable: false)
        telephone(nullable: false,size: 0..50)
        address(nullable: true,size: 0..200)
        backreason(nullable: true,size: 0..500)
        auditUser(nullable: true)
        auditDate(nullable: true)
        sequencer(nullable: true)
    }

    static mapping = {
        content type: 'text'
    }

    transient static final Map OPERATIONMAP=["保存":0,"退回":5,"提交":10,"发布":20,"完成":30,"结束":40];
    transient static final Map STATUSMAP=[0:"草稿",5:"退回",10:"已提交",20:"发布中",30:"已完成",40:"已结束"];
    transient static final Map WAYMAP=[1:"定点服务",2:"上门服务"];

    String toString(){
        return organization+"发布的【${title}】";
    }
}
