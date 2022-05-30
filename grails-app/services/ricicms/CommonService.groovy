package ricicms

import com.bjrxkj.cms.Attachment
import com.bjrxkj.cms.Catalog
import com.bjrxkj.cms.News
import grails.converters.JSON
import grails.transaction.Transactional
import groovy.xml.MarkupBuilder
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.security.MessageDigest
import java.util.regex.Matcher
import java.util.regex.Pattern
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

@Transactional
class CommonService {

    def springSecurityService;
    byte[] imageBytes(def request) {
        def temPath = request.servletContext.getRealPath("/template/default.png");
        FileInputStream fin;
        byte[] bytes = null;
        try {
            fin = new FileInputStream(new File(temPath));
            bytes  = new byte[fin.available()];
            //将文件内容写入字节数组
            fin.read(bytes);
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bytes;
    }

    //获取访问者IP
    public String getIpAddr(request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 解析zip
     * @param zipFileData
     * @param path
     * @return
     */
    Boolean importZipFile(def zipFileData,def path,def docfilePath,Map impParams){
        Boolean result=false;
        try{
            def tempfile=new File(path);
            zipFileData.transferTo(tempfile);
            ZipFile zipFile= new ZipFile(tempfile, Charset.forName('GBK'));
            if(zipFile&&zipFile.size()>0){
                def zipEnumerations=zipFile.entries()
                while (zipEnumerations.hasMoreElements()){//循环压缩包内节点
                    def zipEntry=zipEnumerations.nextElement()
                    def zipIns = zipFile.getInputStream(zipEntry)
                    if(!zipEntry.isDirectory()){
                        if (zipEntry.getName().endsWith('.xml')){//判断xml文件则解析xml
                            importXml(zipIns,zipFile,impParams);
                            result=true;
                            zipIns.close();
                            tempfile.delete();
                        }else if(impParams['attachmentType']!='toByte'){//静态资源且存储方式为直接存储则直接复制到服务器
                            def filepath=docfilePath+zipEntry.getName();
                            Files.copy(zipIns,new File(filepath).toPath(), StandardCopyOption.REPLACE_EXISTING);
                        }
                    }
                }
            }
        }catch(e){
            println e.message;
        }
        return result;
    }

    Map changeSrcurl(def today){
        def map=[:];
        def time=today.format('yyyy-MM-dd');
        def updatesrc_sql="update News set content=replace(content,'src=\"W','src=\"/uploads/docfile/W') where last_updated>=:time";
        def updatehref_sql="update News set content=replace(content,'href=\"W','href=\"/uploads/docfile/W') where last_updated>=:time";
        map.rowssrc=News.executeUpdate(updatesrc_sql,[time:time]);
        map.rowshref=News.executeUpdate(updatehref_sql,[time:time]);
        return map;
    }

    /**
     * 解析xml文件
     * @param zipFileData
     * @param path
     * @return
     */
    void importXml(def xmlfile,def zipFile,Map impParams){
        //xml文件中稿件信息标签与我们所需字段对应关系
        def nodeMap=['title':'DOCTITLE','contentStr':'DOCCONTENT','content':'DOCHTMLCON','publishDatestr':'DOCRELTIME','status':'DOCSTATUS',
                     'author':'DOCAUTHOR','source':'DOCSOURCENAME','oldchannelId':'DOCCHANNEL','importid':'DOCID','oldSrc':'DOCFILENAME'];
        //xml文件中附件信息标签与我们所需字段对应关系
        def attachmentMap=['dateCreated':'CRTIME','name':'APPFILE','title':'APPDESC'];
        def impType=impParams['impType'];
        def attachmentType=impParams['attachmentType'];
        if(xmlfile) {
            def root = new XmlParser().parse(xmlfile);
            if (root.name() == 'WCMDOCUMENTS') {
                root.children().each { Node newsnode ->
//                    try{
                    if (newsnode.name() == 'WCMDOCUMENT') {
                        def fields = newsnode.find { return it.name() == 'PROPERTIES' }.value()
                        def newsmap = [:];
                        nodeMap.keySet().each { f ->
                            newsmap[f] = fields.find { it.name() == nodeMap[f] }.value()[0];
                        }
                        def news_obj,catalog = Catalog.get(impParams['impCatalogId'].toLong());
                        if (newsmap['title'] && impParams['impCatalogId']) {
                            if(impType=='update'){//更新则查询并更新
                                news_obj = News.findByImportidAndCatalog(newsmap['importid'],catalog);
                                if(!news_obj){
                                    news_obj = new News();
                                }
                            }else{//直接insert
                                news_obj = new News();
                            }
                            news_obj.properties = newsmap;
                            if(newsmap['publishDatestr']){
                                news_obj.publishDate = Date.parse('yyyy-MM-dd HH:mm:ss', newsmap['publishDatestr'].toString());
                            }
                            if(newsmap['status']=='10'){
                                news_obj.state = '发布';//状态代码
                            }else if(newsmap['status']=='15'){
                                news_obj.state = '回收站';//状态代码
                            }
                            news_obj.publisher = springSecurityService.currentUser;
                            news_obj.catalog = catalog;
                            if (news_obj.save(flush: true)) {
                                println news_obj.id;
                                if(Attachment.countByNews(news_obj)>0){
                                    Attachment.where { news==news_obj }.deleteAll();
                                }
                                if(news_obj.oldSrc){
                                    news_obj.redirectURL='/uploads/docfile/'+news_obj.oldSrc;
                                }
                                try{//获取附件存储
                                    newsnode.children().each { Node ats ->
                                        if (ats.name() == 'WCMAPPENDIXS') {
                                            ats.children().each { Node at ->
                                                if (at.name() == 'WCMAPPENDIX') {
                                                    try{
                                                        def attchment = new Attachment();
                                                        def field = at.find { return it.name() == 'PROPERTIES' }.value()
                                                        def atsmap = [:]
                                                        attachmentMap.keySet().each { f ->
                                                            atsmap[f] = field.find { it.name() == attachmentMap[f] }.value()[0];
                                                        }
                                                        if (atsmap['name']) {
                                                            if(atsmap['dateCreated']){
                                                                attchment.oldCreated = Date.parse('yyyy-MM-dd HH:mm:ss', atsmap['dateCreated'].toString());
                                                            }
                                                            attchment.properties = atsmap;
                                                            attchment.news = news_obj;
                                                            attchment.filepath="/uploads/docfile/${attchment.name}";
                                                            if(attachmentType=='toByte'){//若二进制存储则存储数据库中
                                                                def zipEntry=zipFile.getEntry("${atsmap['name']}");
                                                                def zipIns=zipFile.getInputStream(zipEntry);
                                                                ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
                                                                byte[] buff = new byte[1000];
                                                                int rc = 0;
                                                                while ((rc = zipIns.read(buff, 0, 100)) > 0) {
                                                                    swapStream.write(buff, 0, rc);
                                                                }
                                                                byte[] in2b = swapStream.toByteArray();
                                                                attchment.data=in2b;
                                                                zipIns.close();
                                                            }
                                                            if(!attchment.save(flush:true)){
                                                                println "稿件保存失败：${attchment.errors}"
                                                            }
                                                        }
                                                    }catch(e){
                                                        println "附件保存异常：${e.message}"
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }catch(e){
                                    println "附件保存异常：${e.message}"
                                }

                            } else {
                                println "稿件保存失败：${news_obj.errors}"
                            }
                        }
                    }
//                    }catch(e){
//                        println "新闻导入异常：${e.message}"
//                    }
                }
            }
            this.changeSrcurl(new Date());//更新附件及图片地址
        }
    }

//
//    public String sendGetUrl(String url, String strategyCode, String key) {
//        String respmsg = "";
//        String data = "";
//        if (null != url && !"".equals(url)) {
//            if (null != strategyCode && !"".equals(strategyCode)) {
//                if (null != key && !"".equals(key)) {
//                    if (key.length() != 8) {
//                        return "密钥长度为8！";
//                    } else {
//                        if (url.split("\\?").length > 1) {
//                            data = url.split("\\?")[1];
//                        }
//
//                        String finalData = initData(key, data);
//                        url = url.split("\\?")[0] + "?strategyCode=" + strategyCode + "&data=" + finalData + "&encryptType=1";
//
//                        HttpClient httpClient = new DefaultHttpClient(); //创建默认的httpClient实例
//                        HttpGet httpGet = new HttpGet(url);  //创建org.apache.http.client.methods.HttpGet
//                        //设置超时时间1分钟
//                        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
//
//                        try{
//                            HttpResponse response = httpClient.execute(httpGet); //执行GET请求
//                            HttpEntity entity = response.getEntity();            //获取响应实体
//                            if(null != entity){
//                                respmsg = EntityUtils.toString(entity, "UTF-8");
//                                EntityUtils.consume(entity); //Consume response content
//                            }
//                        }catch(ClientProtocolException e){
//                            log.error("与[" + url + "]通信过程中发生异常,堆栈信息如下", e);
//                        }finally{
//                            if (httpGet != null) {
//                                httpGet.abort();
//                            }
//                            if(httpClient){
//                                httpClient.getConnectionManager().shutdown();//关闭连接,释放资源
//                            }
//                        }
//                        //返回结果含有特殊字符，replac替换特殊字符；
//                        if ("strategyCodeError".equals(respmsg.trim())) {
//                            respmsg = "{\"status\":false,\"msg\":\"请检查策略配置！\",\"data\":\"\"}";
//                        } else {
//                            respmsg = initReturnData(key, respmsg);
//                        }
//                        return respmsg;
//                    }
//                } else {
//                    return "密钥不能为空！";
//                }
//            } else {
//                return "策略编码不能为空！";
//            }
//        } else {
//            return "接口地址不能为空！";
//        }
//    }

    private static void makeZip(String input, String output, String name) throws Exception {
        //要生成的压缩文件
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(output));
        String[] paths = input.split("\\|");
        File[] files = new File[paths.length];
        byte[] buffer = new byte[1024];
        for (int i = 0; i < paths.length; i++) {
            files[i] = new File(paths[i]);
        }
        for (int i = 0; i < files.length; i++) {
            FileInputStream fis = new FileInputStream(files[i]);
            if (files.length == 1 && name != null) {
                out.putNextEntry(new ZipEntry(name));
            } else {
                out.putNextEntry(new ZipEntry(files[i].getName()));
            }
            int len;
            // 读入需要下载的文件的内容，打包到zip文件
            while ((len = fis.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            out.closeEntry();
            fis.close();
        }
        out.close();
    }

    private String initData(String key, String data) {
        String params = "";

        try {
            if (!data.equals("")) {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(data.getBytes("UTF-8"));
                byte[] digestByte = md.digest();
                params = DesUtils.byteArr2HexStr(digestByte) + "@@@" + DesUtils.encrypt(data, key);
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return params;
    }

    private String initReturnData(String key, String data) {
        String str = "";

        try {
            if (!data.equals("")) {
                str = DesUtils.decrypt(replaceBlank(data), key);
            }
        } catch (Exception var4) {
            var4.printStackTrace();
            str = "{\"status\":false,\"msg\":\"请检查密钥或加密方式是否正确！\",\"data\":\"\"}";
        }

        return str;
    }

    private String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }


    def cleanAllNews(def params){
        if(!params.max) params.max='5000'
        def newslist=News.createCriteria().list {
            ge('dateCreated',Date.parse('yyyy-MM-dd',new Date().format('yyyy-MM-dd')))
//            ge('id',params.minId.toLong())
//            le('id',params.maxId.toLong())
            isNotNull('content')
//            order("id","asc")
//            maxResults(params.max?.toInteger())
        }
        newslist.each {
            this.cleanHTMLStyle(it);
        }
        return 'ok'
    }

    def cleanHTMLStyle(News news){
        if(news){
            try{
                def content=news.content;
                String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>";
                Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
                Matcher m_style = p_style.matcher(content);
                content = m_style.replaceAll(""); // 过滤style标签

                String regEx1 = " style=\"(.*?)\"";
                String regEx2 = " face=\"(.*?)\"";
                String regEx3 = " class=\"(.*?)\"";
                Pattern p1 = Pattern.compile(regEx1);
                Pattern p2 = Pattern.compile(regEx2);
                Pattern p3 = Pattern.compile(regEx3);
                Matcher m1 = p1.matcher(content);
                if (m1.find()) {
                    content = m1.replaceAll("");
                }
                Matcher m2 = p2.matcher(content);
                if (m2.find()) {
                    content = m2.replaceAll("");
                }
                Matcher m3 = p3.matcher(content);
                if (m3.find()) {
                    content = m3.replaceAll("");
                }
                news.content=content;
                news.save(flush:true);
                println "${news.id} ok";
            }catch(e){
                log.error(e.message);
            }
        }
    }

    /**
     * 外网接口-获取外网栏目
     * @return
     */
    def catalogsFromSppweb(){
        String strategyCode="demoCode";
        String key="abcd1234";
        String url="http://36.112.137.58:8888/website-webapp/rest/channel/getChannelsByWebsiteCode?websiteCode=spp";
        String msg= this.sendGetUrl(url,strategyCode,key);
        return msg;
    }

    /**
     * 外网接口-根据栏目从外网导出新闻
     * @param params
     * @return
     */
    List importNewsFromSppweb(def params){
        List newsList=[];
        String strategyCode="demoCode";
        String key="abcd1234";
        def channelId=Catalog.catalogMap[params.catalogId][1];
        if(params.catalogId&&channelId){
            String url="http://36.112.137.58:8888/website-webapp/rest/manuscript/getManuscriptByChannelId?channelId=" +
                    "${channelId}&pageNum=${params.pageNum}&pageSize=${params.pageSize}&startTime=" +
                    "${params.startTime}&endTime=${params.endTime}";
            println url
            String msg= this.sendGetUrl(url,strategyCode,key);
            def msgJson = JSON.parse(msg);
            println "msgJson.data：${msgJson.data}"
            msgJson.data.each {newsjson->
                if(newsjson.status.toString()=='4'&&Catalog.catalogMap[params.catalogId]){
                    def n=new News();
                    n.oldchannelId=channelId;
                    n.importid=newsjson.manuscriptId;
                    n.title=newsjson.title;
                    n.subtitle=newsjson.subTitle;
                    n.outline=newsjson.memo;
                    n.state="发布";
                    n.catalog=Catalog.get(params.catalogId.toLong());
                    n.content=newsjson.content;
                    n.source=newsjson.source;
                    n.keywords=newsjson.keyword;
                    n.publisher=springSecurityService.currentUser;
                    n.publishDate=new Date(newsjson.publishedTime.toLong());
                    n.dateCreated=new Date(newsjson.createdTime.toLong());
                    newsList<<n;
                }
            }
        }
        return newsList;
    }

    /**
     * 外网接口-根据稿件ID查看稿件
     * @return
     */
    def showNewsContent(def params){
        String strategyCode="demoCode";
        String key="abcd1234";
        String url="http://36.112.137.58:8888/rest/manuscript/getManuscriptById?manuscriptId=${params.newsId}";
        String msg= this.sendGetUrl(url,strategyCode,key);
        def msgJson = JSON.parse(msg);
        println "msgJson.data：${msgJson.data}"
        msgJson.data.each {newsjson->
            if(newsjson.status.toString()=='4'){
                def n=new News();
                n.importid=newsjson.manuscriptId;
                n.title=newsjson.title;
                n.subtitle=newsjson.subTitle;
                n.outline=newsjson.memo;
                n.state="发布";
                n.catalog=Catalog.get(params.catalogId.toLong());
                n.content=newsjson.content;
                n.keywords=newsjson.keyword;
                n.publisher=springSecurityService.currentUser;
                n.publishDate=new Date(newsjson.publishedTime.toLong());
                n.dateCreated=new Date(newsjson.createdTime.toLong());
                if(!n.save(flush:true)){
                    println n.errors
                }
            }
        }
        return msg;
    }


    /**
     * map转XML
     * @param newsList
     * @param filepath
     * @return
     */
    def mapToXML(List newsList,def zippath){
        def xmlpath="${zippath}.xml";
        def xmlFile=new File(xmlpath);
        def mb = new MarkupBuilder(xmlFile.newPrintWriter());
        mb.mkp.xmlDeclaration(version:"1.0", encoding:"UTF-8")
        mb.WCMDOCUMENTS{
            newsList.each{News n->
                WCMDOCUMENT(Version: "1.0") {
                    PROPERTIES {
                        DOCID(n.id);
                        DOCVERSION(n.version);
                        DOCTYPE('20');
                        DOCPUBTIME(n.lastUpdated?.format('yyyy-MM-dd HH:mm:ss'));
                        CRUSER(n.publisher.username);
                        CRTIME(n.dateCreated?.format('yyyy-MM-dd HH:mm:ss'));
                        DOCRELTIME(n.publishDate?.format('yyyy-MM-dd HH:mm:ss'));
                        DOCFLAG('0');
                        DOCCHANNEL(n.catalogId);
                        CHNLNAME{mkp.yieldUnescaped("<![CDATA[${n.catalog.name}]]>")};
                        DOCTITLE{mkp.yieldUnescaped("<![CDATA[${n.title}]]>")};
                        SUBDOCTITLE{mkp.yieldUnescaped("<![CDATA[${n.subtitle}]]>")};
                        DOCCONTENT{mkp.yieldUnescaped("<![CDATA[${n.contentStr}]]>")};
                        DOCHTMLCON{mkp.yieldUnescaped("<![CDATA[${n.content}]]>")};
                        DOCFILENAME(null);
                        DOCSTATUS((n.state=='发布'?'10':'15'));
                        DOCAUTHOR(n.author);
                        DOCSOURCENAME(n.source);
                        DOCKEYWORDS(n.keywords);
                        DOCSYSSRC('工作网');
                    }
                }
            }
        }

        try {
            makeZip(xmlpath,"${zippath}.zip",xmlpath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传附件
     */
    def fileUpload(def dirPath,def aFile,Map params){
        def map=[:];
        map.status=0;
        map.message="网络错误，请重试";
        map.file_id='';
        try{
            def timecode=new Date().format('yyyyMM');
            if( aFile && !aFile.isEmpty()){
                def attachmentInstance=new Attachment();
                if(params.newsId){
                    attachmentInstance.news=News.get(params.newsId.toLong());
                }
                dirPath+="/${timecode}";
                attachmentInstance.type=params.type?.toInteger();
                attachmentInstance.name=aFile.getOriginalFilename();
                if(params.sq){
                    attachmentInstance.sq=params.sq?.toInteger();
                }
                def arr=attachmentInstance.name.tokenize('.');
                String uuid = UUID.randomUUID().toString().replaceAll("-","");
                File dir=new File(dirPath);
                if(!dir.exists()){
                    dir.mkdirs();
                }
                def docfilePath="${dirPath}/${uuid}.${arr.last()}";
                attachmentInstance.filepath=docfilePath;
                if(attachmentInstance.save(flush:true)){
                    def file=new File(docfilePath);
                    aFile.transferTo(file);
                    if(params.type=='2'){
//                        def mp4File=new File("${dirPath}/${uuid}.mp4");
//                        this.videoConvert(file,mp4File);
                    }

                    map.status=2;
                    map.message="上传成功";
                    map.file_id=attachmentInstance.id;
                }
            }else{
                map.message="文件为0字节文件！";
            }
        }catch(e){
            log.error(e.message);
        }
        return map;
    }


}
