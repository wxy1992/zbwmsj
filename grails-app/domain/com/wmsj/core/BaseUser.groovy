package com.wmsj.core

import com.wmsj.annotation.Title

@Title(zh_CN = "用户表")
class BaseUser{
    transient springSecurityService;
    String id
    @Title(zh_CN = "用户名")
    String username
    @Title(zh_CN = "真实姓名")
    String realName
    @Title(zh_CN = "手机号")
    String phoneNumber
    @Title(zh_CN = "邮箱")
    String email
    @Title(zh_CN = "部门")
    String department
    @Title(zh_CN = "备注")
    String remark
    @Title(zh_CN = "所属机构")
    Organization organization;
    @Title(zh_CN = "头像")
    byte[] picture
    @Title(zh_CN = "用户密码")
    String password
    @Title(zh_CN = "是否禁用")
    boolean enabled = true
    @Title(zh_CN = "账号过期")
    boolean accountExpired
    @Title(zh_CN = "账号锁定")
    boolean accountLocked
    @Title(zh_CN = "密码过期")
    boolean passwordExpired
    @Title(zh_CN='创建时间')
    Date dateCreated
    @Title(zh_CN='修改时间')
    Date lastUpdated

    Set<BaseRole> getAuthorities() {
        (BaseUserBaseRole.findAllByBaseUser(this) as List<BaseUserBaseRole>)*.baseRole as Set<BaseRole>
    }

    def beforeInsert() {
        encodePassword()
    }

    def beforeUpdate() {
        if (isDirty('password')) {
            encodePassword()
        }
    }

    protected void encodePassword() {
        password = springSecurityService.encodePassword(password)
    }

    static constraints = {
        password nullable: false, blank: false, password: true
        username nullable: false, blank: false, unique: true
        realName nullable: true
        phoneNumber nullable: true
        email nullable: true
        department nullable: true
        remark nullable: true
        organization nullable: true
        picture nullable: true
    }

    static mapping = {
        password column: '`password`'
        id generator: "uuid", type:"string"
        username comment: '用户名'
        password comment: '用户密码'
        enabled comment: '是否禁用'
        accountExpired comment: '账号过期'
        accountLocked comment: '账号锁定'
        passwordExpired comment: '密码过期'
        picture type:'image'
        email column: 'email_address'
    }

    String toString(){
        return realName?:username;
    }
}
