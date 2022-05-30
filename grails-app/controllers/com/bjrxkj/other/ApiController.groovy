package com.bjrxkj.other

import com.bjrxkj.core.BaseRole
import com.bjrxkj.core.BaseUser
import com.bjrxkj.core.BaseUserBaseRole
import com.bjrxkj.core.Organization
import com.ricicms.utils.AESCoder
import grails.converters.JSON

class ApiController {
    private static final CAS_KEY = "1234567890abcdef"

    /**
     * ----------------------------------同步用户----------------------------------
     * @return
     */
    def userSaveOrUpdate() {
        def map = [:];
        def params = request.JSON ?: params;
        def dataDes = AESCoder.decryptAES(params?.data, CAS_KEY);
        def md5str = AESCoder.getMD5(dataDes);
        if (md5str != params?.sign) {
            map.result = false
            map.message = "数据摘要验证失败"
        } else {
            println "--------------------userSaveOrUpdate保存用户-----------------------:" + dataDes
            def baseUser, jsonObj = JSON.parse(dataDes);
            try {
                if (jsonObj.id) {
                    baseUser = BaseUser.findByCasid(jsonObj.id);
                }
                if (!baseUser) {
                    baseUser = new BaseUser();
                    baseUser.casid = jsonObj.id;
                }
                baseUser.username = jsonObj.username;
                baseUser.password = baseUser.username;
                baseUser.realName = jsonObj.realName;
                baseUser.sex = jsonObj.sex;
                baseUser.cardId = jsonObj.cardId;
                baseUser.phoneNumber = jsonObj.phoneNumber;
                baseUser.email = jsonObj.emailAddress;
                baseUser.department = jsonObj.department;
                baseUser.workId = jsonObj.workId;
                baseUser.remark = jsonObj.remark;
                baseUser.officeIds = jsonObj.officeIds;
                baseUser.manageOfficeIds = jsonObj.manageOfficeIds;
                baseUser.remark = jsonObj.remark;
                if (jsonObj.officeIds) {
                    baseUser.organization = Organization.findByCasid(jsonObj.officeIds.tokenize(',')?.getAt(0) ?: '-1');
                }
                if (baseUser.save(flush: true)) {
                    if (jsonObj.userRoles.tokenize(',').contains("超级管理员")) {
                        new BaseUserBaseRole(baseUser: baseUser, baseRole: BaseRole.findByAuthority('ROLE_ADMIN')).save(flush: true)
                    }
                    log.error("用户${jsonObj.username}更新成功！");
                    map.result = true
                    map.message = "操作成功"
                } else {
                    log.error("用户${jsonObj.username}更新失败：${baseUser.errors}");
                    map.result = false
                    map.message = "操作失败：${baseUser.errors}"
                }
            } catch (e) {
                log.error("用户${jsonObj.username}更新异常：${e.message}");
                map.result = false
                map.message = "操作失败：${e.message}"
            }
        }
        render text: "${map as JSON}", encoding: "UTF-8"
    }

    /**
     * 删除用户
     * @return
     */
    def userDelete() {
        def map = [:]
        def dataDes = AESCoder.decryptAES(params?.data, CAS_KEY);
        def md5str = AESCoder.getMD5(dataDes)
        if(md5str != params?.sign){
            map.result = false
            map.message = "数据摘要验证失败"
        }else{
            println "---------------------------deleteUser删除用户-------------------------:" + dataDes
            def jsonObj=JSON.parse(dataDes),baseUser=BaseUser.findByCasid(jsonObj.id?:'-1');
            if(baseUser){
                try{
                    BaseUserBaseRole.removeAll(baseUser);
                    baseUser.delete(flush: true);
                    log.error("用户${jsonObj.id}删除成功！")
                    map.result = true;
                    map.message = "操作成功"
                }catch(e){
                    log.error("用户${jsonObj.id}删除异常：${e.message}");
                    map.result = false;
                    map.message = "操作失败：${e.message}"
                }
            }else{
                log.error("用户${jsonObj.id}不存在！");
                map.result = false
                map.message = "操作失败：用户不存在或 no id "
            }
        }
        render text: "${map as JSON}",encoding: "UTF-8"
    }

    /**
     * 禁用、启用用户
     */
    def userDisableOrNot(){
        def map = [:];
        def params = request.JSON ?: params;
        def dataDes = AESCoder.decryptAES(params?.data, CAS_KEY);
        def md5str = AESCoder.getMD5(dataDes)
        if(md5str != params?.sign){
            map.result = false
            map.message = "数据摘要验证失败"
        }else{
            println "---------------------------userDisableOrNot禁用启用-------------------------:" + dataDes
            def jsonObj=JSON.parse(dataDes);
            if(jsonObj.id&&jsonObj.enabled){
                try{
                    def baseUser=BaseUser.findByCasid(jsonObj.id);
                    baseUser.enabled=(jsonObj.enabled=='1'?true:false);
                    if(baseUser.save(flush: true)){
                        log.error("用户${jsonObj.id}启/禁用成功！");
                        map.result = true
                        map.message = "操作成功"
                    }else{
                        log.error("用户${jsonObj.id}启/禁用失败：${baseUser.errors}");
                        map.result = false
                        map.message = "操作失败：${baseUser.errors}"
                    }
                }catch(e){
                    log.error("用户${jsonObj.id}启/禁用异常：${e.message}");
                    map.result = true
                    map.message = "操作失败：${e.message}"
                }
            }else{
                map.result = false
                map.message = "用户${jsonObj.id}启/禁用缺少参数"
            }
        }
        render text: "${map as JSON}",encoding: "UTF-8"
    }


    /**
     * ----------------------------------同步机构----------------------------------
     * @return
     */
    def orgSaveOrUpdate() {
        def map = [:];
        def params = request.JSON ?: params;
        def dataDes = AESCoder.decryptAES(params?.data, CAS_KEY);
        def md5str = AESCoder.getMD5(dataDes);
        if (md5str != params?.sign) {
            map.result = false
            map.message = "数据摘要验证失败"
        } else {
            println "--------------------orgSaveOrUpdate保存机构-----------------------:" + dataDes
            def organization, jsonObj = JSON.parse(dataDes);
            try {
                if (jsonObj.id) {
                    organization = Organization.findByCasid(jsonObj.id);
                }
                if (!organization) {
                    organization = new Organization();
                    organization.casid = jsonObj.id;
                }
                organization.name = jsonObj.name;
                organization.code = jsonObj.code;
                organization.shortName = jsonObj.shortName;
                organization.parentName = jsonObj.parentName;
                organization.parentId = Organization.get(jsonObj.parentId);
                organization.path = jsonObj.path;
                organization.nationName = jsonObj.nationName;
                organization.nationId = jsonObj.nationId;
                organization.provinceName = jsonObj.provinceName;
                organization.provinceId = jsonObj.provinceId;
                organization.cityName = jsonObj.cityName;
                organization.cityId = jsonObj.cityId;
                organization.countyName = jsonObj.countyName;
                organization.countyId = jsonObj.countyId;
                if (organization.save(flush: true)) {
                    log.error("机构${jsonObj.name}更新成功");
                    map.result = true
                    map.message = "操作成功"
                } else {
                    log.error("机构${jsonObj.name}更新失败：${organization.errors}");
                    map.result = false
                    map.message = "操作失败：${organization.errors}"
                }
            } catch (e) {
                log.error("机构${jsonObj.name}更新异常：${e.message}");
                map.result = false
                map.message = "操作失败：${e.message}"
            }
            render text: "${map as JSON}", encoding: "UTF-8"
        }
    }

    /**
     * 删除机构
     * @return
     */
    def orgDelete() {
        def map = [:];
        def params = request.JSON ?: params;
        def dataDes = AESCoder.decryptAES(params?.data, CAS_KEY);
        def md5str = AESCoder.getMD5(dataDes)
        if(md5str != params?.sign){
            map.result = false
            map.message = "数据摘要验证失败"
        }else{
            println "---------------------------orgDelete删除机构-------------------------:" + dataDes
            def jsonObj=JSON.parse(dataDes);
            def organization=Organization.findByCasid(jsonObj.id?:'-1');
            if(organization){
                try{
                    organization.delete(flush: true);
                    log.error("机构${jsonObj.id}删除成功！");
                    map.result = true;
                    map.message = "操作成功"
                }catch(e){
                    log.error("机构${jsonObj.id}删除异常：${e.message}");
                    map.result = false;
                    map.message = "操作失败：${e.message}"
                }
            }else{
                log.error("机构${jsonObj.id}不存在！");
                map.result = false
                map.message = "操作失败：机构不存在或 no id "
            }
        }
        render text: "${map as JSON}",encoding: "UTF-8"
    }


}
