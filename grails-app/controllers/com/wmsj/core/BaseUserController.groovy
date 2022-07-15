package com.wmsj.core


import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.transaction.Transactional

class BaseUserController {
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    SpringSecurityService springSecurityService;
    /**
     * 用户管理列表
     */
    def list(){
    }

    /**
     * bootstrap table json
     */
    def json() {
        params.max = Math.min(params.limit ? params.int('limit') : 10, 100);
        params.limit = params.max;
        if (!params.offset) params.offset = '0'
        if (!params.sort) params.sort = 'id'
        if (!params.order) params.order = 'desc'

        def ecCount = BaseUser.createCriteria().count {
            if(params?.username){
                ilike("username","%${params.username?.trim()}%")
            }
            if(params?.realName){
                ilike("realName","%${params.realName?.trim()}%")
            }
        }
        def ecList = [];
        BaseUser.createCriteria().list {
            if(params?.username){
                ilike("username","%${params.username?.trim()}%")
            }
            if(params?.realName){
                ilike("realName","%${params.realName?.trim()}%")
            }
            order(params.sort, params.order)
            maxResults(params.max.toInteger())
            firstResult(params.offset.toInteger())
        }.each{ BaseUser user ->
            def map = [:]
            map.id = user?.id;
            map.username = user?.username;
            map.realName = user?.realName;
            map.phoneNumber = user?.phoneNumber;
            map.enabled = user?.enabled;
            map.roleName = BaseUserBaseRole.findAllByBaseUser(user)?.baseRole?.name;
            ecList << map;
        }
        def map = [:];
        map.total = ecCount;
        map.rows = ecList;
        render "${map as JSON}";
    }

    /**
     * 新增用户页面
     */
    def createOrEdit() {
        def roles = BaseRole.findAll();def user;def userRoles=[];
        if(params?.id){
            user = BaseUser.get(params?.id);
            userRoles = BaseUserBaseRole.findByBaseUser(user)?.baseRole;
        }else{
            user = new BaseUser();
        }
        return [baseUser: user, userRoles: userRoles, roles: roles];
    }

    /**
     * 校验用户名是否重复
     */
    def checkUnique(){
        def baseUser = BaseUser.findByUsername(params?.username)
        render baseUser? baseUser.id == params?.id?.toLong() :true
    }

    /**
     * 保存或更新角色信息
     * @return
     */
    @Transactional
    def saveOrUpdate() {
        def map = [:], baseUser;
        map.result = false;
        if(params?.id){
            baseUser = BaseUser.get(params?.id);
        }else{
            baseUser = new BaseUser();
        }
        baseUser.properties = params;
        if(baseUser.save(flush: true)){
            def roles=request.getParameterValues('roles');
            if(roles?.size()>0){
                BaseUserBaseRole.removeAll(baseUser);
                roles.each {roleId->
                    BaseUserBaseRole.create(baseUser, BaseRole.get(roleId),true)
                }
            }
            map.result = true
            map.message = "保存成功"
        }else if(baseUser.errors.getFieldError('username')){
            map.message = "该用户名已经存在"
        }else{
            map.message = "保存失败"
        }
        render "${map as JSON}";
    }

    /**
     * 删除角色
     * @return
     */
    @Transactional
    def deleteAll() {
        def map=[:];
        map.result=false;
        map.message="网络错误，请重试";
        def ids = params.fields?.split(',').toList();
        try{
            ids.each{id->
                def user = BaseUser.get(id);
                BaseUserBaseRole.removeAll(user)
                user.delete(flush: true)
            }
            map.result=true;
            map.message="操作成功";
        }catch(e){
            log.error(e.message);
        }
        render "${map as JSON}";
    }

    /**
     * 查看角色页面
     * @return
     */
    def show() {
        return [baseUser:BaseUser.read(params?.userId)];
    }

    /**
     * 设置用户启用/禁用
     * @return
     */
    def changeUserEnable(){
        def map=[:];
        map.result=false;
        map.message="网络错误，请重试";
        if(params.id){
            def user=BaseUser.get(params.id);
            if(user){
                user.enabled=Boolean.parseBoolean(params.state.toString());
                if(user.save(flush:true)){
                    map.result=true;
                    map.message="操作成功";
                }
            }
        }else{
            map.message="缺少参数";
        }
        render "${map as JSON}";
    }

}
