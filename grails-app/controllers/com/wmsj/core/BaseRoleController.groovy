package com.wmsj.core

import com.wmsj.cms.Catalog
import grails.converters.JSON
import grails.transaction.Transactional

class BaseRoleController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    /**
     * 角色管理列表页
     */
    def list() {

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

        def ecCount = BaseRole.createCriteria().count {
            if(params?.keywords){
                or{
                    ilike("name","%${params.keywords?.trim()}%")
                    ilike("description","%${params.keywords?.trim()}%")
                }
            }
        }
        def ecList = BaseRole.createCriteria().list {
            if(params?.keywords){
                or{
                    ilike("name","%${params.keywords?.trim()}%")
                    ilike("description","%${params.keywords?.trim()}%")
                }
            }
            order(params.sort, params.order)
            maxResults(params.max.toInteger())
            firstResult(params.offset.toInteger())
        }
        def map = [:];
        map.total = ecCount;
        map.rows = ecList;
        render "${map as JSON}";
    }

    /**
     * 新增角色页面
     */
    def create() {
    }

    /**
     * 编辑角色页面
     */
    def createOrEdit() {
        def baseRole, catalogNames = '';
        if (params.id) {
            baseRole = BaseRole.get(params?.id);
            if (baseRole.catalogstr) {
                catalogNames = Catalog.createCriteria().list {
                    projections {
                        distinct('name')
                    }
                    inList('id', baseRole.catalogList().collect { it.toLong() })

                }.join(',');
            }
        } else {
            baseRole = new BaseRole();
        }
        return [baseRole: baseRole, catalogNames: catalogNames];
    }

    /**
     * 保存或更新角色信息
     * @return
     */
    @Transactional
    def saveOrUpdate() {
        def map = [:], baseRole;
        if(params?.id){
            baseRole = BaseRole.get(params?.id);
            baseRole.properties = params;
        }else{
            baseRole = new BaseRole(params);
        }
        if(baseRole.save(flush: true)){
            map.result = true
            map.message = "操作成功"
        }else{
            log.error(baseRole.errors);
            map.result = false
            map.message = "网络错误，请重试"
        }
        render "${map as JSON}";
    }

    /**
     * 删除角色
     * @return
     */
    @Transactional
    def deleteAll() {
        def map = [:]
        def ids = params.fields?.tokenize(',');
        try{
            ids.each{
                def baseRole=BaseRole.get(it);
                if(BaseUserBaseRole.countByBaseRole(baseRole)>0){
                    map.result = false
                    map.message = "该角色下已关联用户"
                }else{
                    BaseUserBaseRole.removeAll(baseRole)
                    baseRole.delete(flush: true)
                    map.result = true
                    map.message = "删除成功"
                }
            }
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
        return [baseRole:BaseRole.read(params?.roleId)];
    }

    /**
     * 菜单树页面
     * @return
     */
    def menuTree() {
        def role = BaseRole.read(params?.roleId)
        return [role: role]
    }

    /**
     * 菜单树JSON
     * @return
     */
    def menuJson() {
        def menus = Menu.findAllByParentIsNull();
        def list = [];
        def role = BaseRole.read(params?.id);
        def permissions = role?.menuItems?.split(",");
        render "${getMenu(menus, list, permissions) as JSON}";
    }

    /**
     * 拼装菜单树JSON
     * @param menus
     * @param list
     * @param permissions
     * @return
     */
    def getMenu(def menus,def list,def permissions){
        menus.each {menu->
            def map = [:];
            map.id = menu.id;
            map.text = menu.name;
            map.children = []
            map.state = [opened:true];
            if(permissions && permissions?.contains("${menu.id}") && menu.children.isEmpty()){
                map.state.selected = true
            }else{
                map.state.selected = false
            }
            if(!menu.children.isEmpty()){
                def temList = [];
                def children = Menu.createCriteria().list {
                    eq("parent",menu)
                    order("sortNum","asc")
                }
                map.children = getMenu(children,temList,permissions);
                map.type = "root";
            }else{
                map.type = "root";
            }
            list << map;
        }
        return list;
    }

    /**
     * 保存权限配置
     * @return
     */
    @Transactional
    def saveConfigure() {
        def map = [:]
        def role = BaseRole.get(params.roleId);
        role.menuItems = params.permission;
        if(role.save(flush: true)){
            map.result = true;
            map.message = "配置成功";
        }else{
            map.result = false;
            map.message = "配置失败";
        }
        render "${map as JSON}"
    }

}
