#高检工作网门户网网站
##网站嵌套方式为原始方式，即<rici:news catalogID="13" max="6" view="/sites/gjgzw/templates/onlyli" titleSize="18" />
###网站群页面在views/sites/中，公共部分在layouts/sites中，其中gjgzw为工作网主站，business为统一业务2.0运维子站，museum为人民博物馆
###网站群静态资源在web-app/sites中，子目录名称与上一致
###
* 集成ukey登录
    * base_user中ukey字段为用户key的唯一标识JCY_CERT_GN，login/auth.gsp中有ukey登录链接，点击跳转到key验证地址，目前只支持检察院专用浏览器，
    * 验证通过后跳转到本站并将key信息写入cookie，SiteController中获取cookie中JCY_CERT_GN值，查找用户表中该用户，使其登录
* 稿件信息导入、导出
    * 导入拓尔思和本站zip包，zip包中包括xml文件及图片、附件类文件
    newsAdmin/list.gsp中uploadModal模态框中包括导入栏目id,存储方式及更新方式，选择zip包后自动上传到/newsAdmin/importZipFile，
    调用commonService.importZipFile解析zip包，importXml解析xml文件，
    具体代码解释在方法代码注释中
    * 导出
    该方法用于高检互联网接口数据过渡系统导出到本网站
    newsAdmin/newsList.gsp中exportSelectNews function,后台地址/newsAdmin/exportSelectNews，勾选稿件并导出成压缩包
    具体代码解释在方法代码注释中
* 高检互联网门户网站数据导入（开普抓取信息的接口）
    * 该系统git地址为http://git.declare.org.cn/wxy/new_sppcms.git中的sppwebExport系统
    目前只能部署在ip为218.240.156.210的服务器上，若修改需通知开普运维更换白名单ip地址
    具体代码解释在该项目README文件及项目源码注释中
    

   