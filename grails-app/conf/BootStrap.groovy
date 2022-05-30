import com.bjrxkj.core.BaseRole
import com.bjrxkj.core.BaseUser
import com.bjrxkj.core.BaseUserBaseRole
import com.bjrxkj.core.Requestmap
import grails.converters.JSON

class BootStrap {

    def init = { servletContext ->
        JSON.registerObjectMarshaller(Date) {
            return it?.format("yyyy-MM-dd HH:mm");
        }
//        createDefaultRoles();
//        createDefaultUsers();
        createRequestMap();
    }
    def destroy = {
    }
    private void createDefaultRoles(){
        if(BaseRole.count()==0){
            def roles=['ROLE_USER':'访问用户','ROLE_EDITOR':'编辑人员','ROLE_AUDITOR':'初步审核员',
                       'ROLE_PUBAUDITOR':'拟发审核员','ROLE_MANAGER':'管理员','ROLE_ADMIN':'超级管理员'];
            roles.keySet().each{
                if (!BaseRole.findByAuthority(it)) {
                    def role=new BaseRole(authority:it,description:roles[it],name:roles[it]);
                    if(!role.save(flush: true)){
                        println role.errors
                    }
                }
            }
        }
    }
    private void createDefaultUsers() {
        if(BaseUser.count()==0){
            def admins=['manager','admin']
            admins.each {
                def admin = BaseUser.findByUsername(it)
                if (!admin) {
                    admin = new BaseUser(username:it,password:'123456',enabled:true,accountExpired:false,accountLocked:false,passwordExpired:false,type: null)
                    if(admin.save(flush: true)){
                        new BaseUserBaseRole(baseUser:admin,baseRole: BaseRole.findByAuthority('ROLE_MANAGER')).save(flush: true)
                        new BaseUserBaseRole(baseUser:admin,baseRole: BaseRole.findByAuthority('ROLE_ADMIN')).save(flush: true)
                    }
                    else{
                        println admin.errors
                    }
                }
            }
        }
    }
    private void createRequestMap() {
        if(Requestmap.count()==0){
            for (String url in [
                    '/error', '/index', '/index.gsp', '/**/favicon.ico', '/shutdown',
                    '/assets/**', '/**/js/**', '/**/css/**', '/**/images/**',
                    '/login/**', '/logout/**','uploads/*','/web/**','/news/**']) {
                new Requestmap(url: url, configAttribute: 'permitAll').save()
            }
            new Requestmap(url: '/site/**', configAttribute: 'ROLE_MANAGER,ROLE_ADMIN').save(flush: true)
            new Requestmap(url: '/catalog/**', configAttribute: 'ROLE_MANAGER,ROLE_ADMIN').save(flush: true)
            new Requestmap(url: '/baseUser/**', configAttribute: 'ROLE_MANAGER,ROLE_ADMIN').save(flush: true)
            new Requestmap(url: '/baseRole/**', configAttribute: 'ROLE_MANAGER,ROLE_ADMIN').save(flush: true)
            new Requestmap(url: '/newsAdmin/**', configAttribute: 'ROLE_EDITOR,ROLE_AUDITOR,ROLE_PUBAUDITOR,ROLE_MANAGER,ROLE_ADMIN').save(flush: true)
            new Requestmap(url: '/', configAttribute: 'isFullyAuthenticated()').save(flush: true)
            new Requestmap(url: '/**', configAttribute: 'isFullyAuthenticated()').save(flush: true)
        }

    }

}
