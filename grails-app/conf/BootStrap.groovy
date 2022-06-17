import com.wmsj.cms.Catalog
import com.wmsj.cms.Site
import com.wmsj.core.BaseRole
import com.wmsj.core.BaseUser
import com.wmsj.core.BaseUserBaseRole
import com.wmsj.core.Organization
import com.wmsj.core.Requestmap
import grails.converters.JSON

class BootStrap {

    def init = { servletContext ->
        JSON.registerObjectMarshaller(Date) {
            return it?.format("yyyy-MM-dd HH:mm");
        }
        createDefaultRoles();
        createDefaultUsers();
        createRequestMap();
        createDefaultSite();
        createDefaultCatalogs();
    }
    def destroy = {
    }

    private void createDefaultRoles() {
        if (BaseRole.count() == 0) {
            def roles = ['ROLE_USER': '群众用户', 'ROLE_SUBADMIN': '二级管理员', 'ROLE_ADMIN': '超级管理员'];
            roles.keySet().each {
                if (!BaseRole.findByAuthority(it)) {
                    def role = new BaseRole(authority: it, description: roles[it], name: roles[it]);
                    if (!role.save(flush: true)) {
                        println role.errors
                    }
                }
            }
        }
    }

    private void createDefaultUsers() {
        if (BaseUser.count() == 0) {
            def admins = ['manager', 'admin']
            admins.each {
                def admin = BaseUser.findByUsername(it)
                if (!admin) {
                    admin = new BaseUser(username: it, password: '123456', enabled: true, accountExpired: false, accountLocked: false, passwordExpired: false, type: null)
                    if (admin.save(flush: true)) {
                        new BaseUserBaseRole(baseUser: admin, baseRole: BaseRole.findByAuthority('ROLE_MANAGER')).save(flush: true)
                        new BaseUserBaseRole(baseUser: admin, baseRole: BaseRole.findByAuthority('ROLE_ADMIN')).save(flush: true)
                    } else {
                        println admin.errors
                    }
                }
            }

            //二级单位用户
            def subadmins = ["政府办"     : "张北县政府办志愿服务队",
                             "政法委"     : "",
                             "教体科局"    : "张北县教育局",
                             "公安局"     : "",
                             "住建局"     : "张北县住房和城乡建设局志愿服务队",
                             "交通局"     : "张北交通运输局志愿服务队",
                             "文广旅局"    : "张北县文化广电和旅游局志愿服务队",
                             "卫健局"     : "",
                             "市场监督管理局" : "张北县市场监督管理局志愿服务队",
                             "城管局"     : "张北县市容市貌管理中心志愿服务队",
                             "审批局"     : "张北县行政审批局志愿服务队",
                             "工信局"     : "张北县工信局服务队",
                             "园林局"     : "张北园林局志愿服务队",
                             "邮 政"     : "",
                             "应急管理局"   : "张北县应急管理局志愿服务队",
                             "退役军人事务局" : "张北县退役军人事务局志愿服务队",
                             "中都博物馆"   : "元中都博物馆志愿者协会",
                             "自然资源和规划局": "张北县自然资源和规划局志愿服务队",
                             "南山路办事处"  : "南山路办事处志愿者服务队",
                             "张库南办事处"  : "张北县张库南街办事处",
                             "花园街办事处"  : "张北花园街志愿服务队",
                             "北辰路办事处"  : "张北县北辰路办事处志愿服务队",
                             "税务局"     : "张北县税务局志愿服务队",
                             "房产"      : "张北县房地产管理处志愿服务队",
                             "县委办"     : "张北县委办志愿服务队",
                             "纪委监委"    : "张北县纪检委志愿服务队",
                             "组织部"     : "中共张北县委组织部志愿服务队",
                             "统战部"     : "张北县统战部志愿服务队",
                             "网信办"     : "张北网信办志愿服务队",
                             "电视台"     : "张北广播电视台志愿服务队",
                             "妇 联"     : "张北县妇联志愿服务队",
                             "司法局"     : "张北县司法局志愿服务队",
                             "团县委"     : "张北团县委志愿服务队",
                             "党 校"     : "张北县委党校志愿服务队",
                             "机关工委"    : "机关工委志愿服务队",
                             "总工会"     : "张北县总工会职工志愿服务队",
                             "农业农村局"   : "张北农牧局",
                             "乡村振兴局"   : "张北县乡村振兴局",
                             "科 协"     : "张北县科学技术协会志愿服务队",
                             "民政局"     : "张北县民政和民族宗教事务局",
                             "法 院"     : "张北县人民法院志愿队",
                             "财政局"     : "张北县财政局志愿服务组织",
                             "环保局"     : "张北县环保局志愿服务队",
                             "发改局"     : "张北发改局志愿服务队",
                             "人社局"     : "张北县人力资源和社保局志愿服务队",
                             "水务局"     : "张北水务局志愿服务队",
                             "经济开发区"   : "张北县经开区志愿服务队",
                             "残 联"     : "张北县残联志愿服务队",
                             "就业局"     : "就业局志愿服务队",
                             "消防队"     : "",
                             "社保局"     : "张北社保局志愿服务队",
                             "义和美新城"   : "张北县义合美新城党工委",
                             "张北镇"     : "张北县张北镇志愿服务队",
                             "台路沟乡"    : "台路沟乡志愿服务大队",
                             "油篓沟镇"    : "油篓沟镇志愿服务队",
                             "两面井乡"    : "两面井乡志愿服务队",
                             "白庙滩乡"    : "白庙滩乡志愿服务队",
                             "二泉井乡"    : "张北县二泉井乡志愿服务队",
                             "三号乡"     : "张北县三号乡志愿服务大队",
                             "大河镇"     : "大河镇5325003",
                             "大西湾乡"    : "张北县大西湾乡志愿服务队",
                             "战海乡"     : "战海乡人民政府志愿服务队",
                             "海流图乡"    : "海流图乡志愿服务队",
                             "馒头营乡"    : "馒头营乡志愿者服务队",
                             "单晶河乡"    : "单晶河乡志愿服务队",
                             "小二台镇"    : "小二台镇志愿服务队",
                             "大囫囵镇"    : "大囫囵镇志愿服务队",
                             "公会镇"     : "张北公会镇志愿服务队",
                             "二台镇"     : "二台镇志愿服务大队",
                             "郝家营乡"    : "郝家营乡人民政府志愿队"
            ];
            def parentOrg = Organization.get(1L);
            if (!parentOrg) {
                parentOrg = new Organization(name: '张北县新时代文明实践中心', shortName: '张北县新时代文明实践中心', parent: null);
                parentOrg.save(flush: true);
            }
            subadmins.each {
                def shortName = it.key;
                def name = it.value;
                def organization = new Organization(name: name, shortName: shortName, parent: parentOrg);
                if (organization.save(flush: true)) {
                    def subadmin = new BaseUser(username: shortName, realName: name, organization:organization,
                            department: name, password: '123456', enabled: true, accountExpired: false, accountLocked: false, passwordExpired: false)
                    if (subadmin.save(flush: true)) {
                        new BaseUserBaseRole(baseUser: subadmin, baseRole: BaseRole.findByAuthority('ROLE_SUBADMIN')).save(flush: true)
                    } else {
                        println subadmin.errors
                    }
                }
            }
        }
    }

    private void createRequestMap() {
        if (Requestmap.count() == 0) {
            for (String url in [
                    '/error', '/index', '/index.gsp', '/**/favicon.ico', '/shutdown',
                    '/assets/**', '/**/js/**', '/**/css/**', '/**/images/**',
                    '/login/**', '/logout/**', 'uploads/*', '/web/**','/api/**']) {
                new Requestmap(url: url, configAttribute: 'permitAll').save()
            }
            new Requestmap(url: '/site/**', configAttribute: 'ROLE_ADMIN').save(flush: true)
            new Requestmap(url: '/catalog/**', configAttribute: 'ROLE_ADMIN').save(flush: true)
            new Requestmap(url: '/baseUser/**', configAttribute: 'ROLE_ADMIN').save(flush: true)
            new Requestmap(url: '/baseRole/**', configAttribute: 'ROLE_ADMIN').save(flush: true)
            new Requestmap(url: '/newsAdmin/**', configAttribute: 'ROLE_SUBADMIN,ROLE_ADMIN').save(flush: true)
            new Requestmap(url: '/trade/**', configAttribute: 'ROLE_SUBADMIN,ROLE_ADMIN').save(flush: true)
            new Requestmap(url: '/', configAttribute: 'isFullyAuthenticated()').save(flush: true)
            new Requestmap(url: '/**', configAttribute: 'isFullyAuthenticated()').save(flush: true)
        }

    }

    private void createDefaultSite(){
        if(Site.count()==0){
            def code="zbwmsjzx";
            def site=new Site(homesite: true,sequencer:1);
            site.name=code;
            site.domainName=code;
            site.template=code;
            site.shortName="新时代文明实践中心";
            site.companyName="张北县新时代文明实践中心";
            if(!site.save(flush: true)){
                log.error(site.errors);
            }
        }
    }

    private void createDefaultCatalogs(){
        if(Catalog.count()==0){
            def site=Site.findByName("zbwmsjzx");
            def catalogs=['文明实践':[],'实践阵地':[],'志愿团体':[],
                          '学习课堂':['理论','文化','教育','科普','体育'],
                          '生活服务':['生活缴费','便民生活','生活出行']];
            catalogs.eachWithIndex {catalog,index->
                def indexCatalog=new Catalog(name:catalog.key,site: site,positions: 1,sequencer: index+1);
                if(indexCatalog.save(flush: true)){
                    def children=catalog.value;
                    if(children.size()>0){
                        children.eachWithIndex {child,subindex->
                            def childCatalog=new Catalog(name: child,parent:indexCatalog,site: site,positions: 1,sequencer:index+1+subindex+1);
                            if(!childCatalog.save(flush: true)){
                                log.error("子栏目创建异常："+childCatalog.errors);
                            }
                        }
                    }
                }else{
                    log.error("首页栏目创建异常："+indexCatalog.errors);
                }
            }
        }

    }
}
