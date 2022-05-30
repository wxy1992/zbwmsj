package com.bjrxkj.core

import com.bjrxkj.annotation.Title

@Title(zh_CN = "用户表")
class BaseUser{
    transient springSecurityService;
    String id
    String casid;
    @Title(zh_CN = "用户名")
    String username
    @Title(zh_CN = "真实姓名")
    String realName
    @Title(zh_CN = "性别")
    String sex
    @Title(zh_CN = "身份证号")
    String cardId
    @Title(zh_CN = "手机号")
    String phoneNumber
    @Title(zh_CN = "邮箱")
    String email
    @Title(zh_CN = "部门")
    String department
    @Title(zh_CN = "工号")
    String workId
    @Title(zh_CN = "备注")
    String remark
    @Title(zh_CN = "所属机构")
    String officeIds
    @Title(zh_CN = "管理机构")
    String manageOfficeIds
    @Title(zh_CN = "所属机构")
    Organization organization;
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
    @Title(zh_CN = '可维护站点')
    String sitestr
    @Title(zh_CN = '可维护栏目')
    String catalogstr
    @Title(zh_CN='创建时间')
    Date dateCreated
    @Title(zh_CN='修改时间')
    Date lastUpdated
    @Title(zh_CN = "证书编号")
    String ukey
    @Title(zh_CN = "ip")
    String ipAddress
    @Title(zh_CN = "头像")
    byte[] picture
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

    static List getUserCatalogs(BaseUser baseUser){
        List usercatas=[];
        if(baseUser.catalogstr){
            usercatas=baseUser.catalogstr.tokenize(',')
        };
//        if(baseUser.catalogstr){
//            usercatas=baseUser.catalogstr?.tokenize(',');
//        }
//        def roleList=baseUser.getAuthorities()?.findAll{it.catalogstr};
//        if(roleList.size()>0){
//            def rolestr='';
//            roleList.each{ BaseRole role->
//                if(rolestr){
//                    rolestr+=',';
//                }
//                rolestr+=role.catalogstr;
//            };
//            rolecatas=rolestr?.tokenize(',');
//            if(usercatas.size()>0){
//                rolecatas.retainAll(usercatas);
//            }
//        }else{
//            return usercatas;
//        }
        return usercatas;
    }

    static constraints = {
        casid nullable: true;
        password nullable: false, blank: false, password: true
        username nullable: false, blank: false, unique: true
        realName nullable: true
        sitestr nullable: true
        ukey nullable: true
        sex nullable: true
        cardId nullable: true
        phoneNumber nullable: true
        email nullable: true
        department nullable: true
        workId nullable: true
        remark nullable: true
        officeIds nullable: true
        manageOfficeIds nullable: true
        organization nullable: true
        catalogstr nullable: true
        ipAddress nullable: true
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
        sitestr comment: '所属站点'
//        picture type:'image'
        email column: 'email_address'
    }

    String toString(){
        return realName?:username;
    }
}