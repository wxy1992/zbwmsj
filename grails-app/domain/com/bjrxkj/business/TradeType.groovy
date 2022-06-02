package com.bjrxkj.business

import com.bjrxkj.annotation.Title

class TradeType {
    @Title(zh_CN='类型名称')
    String name
    @Title(zh_CN='排序')
    Integer sq=999

    static constraints = {
        name(nullable:false,size:0..500)
        sq(nullable: false)
    }

    String toString(){
        return name;
    }
}
