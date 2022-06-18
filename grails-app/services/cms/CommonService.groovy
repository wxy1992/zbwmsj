package cms

import com.wmsj.business.Trade
import com.wmsj.cms.Attachment
import com.wmsj.cms.News
import grails.transaction.Transactional

@Transactional
class CommonService {

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
     * 封面图片
     * @return
     */
    def picture(def params,def request,def response){
        def picturePath;
        def objInstance;
        if(params.type=="trade"){
            objInstance = Trade.get(params.id.toLong());
        }else{
            objInstance = News.get(params.id.toLong());
        }
        if(objInstance && objInstance.picture){
            picturePath=objInstance.picture;
        }else{
            picturePath=request.servletContext.getRealPath("/template/default.png");
        }
        try{
            def outdata=new File(picturePath).bytes;
            response.setContentType("image/jpeg");
            response.setContentLength(outdata.size());
            OutputStream out = response.outputStream;
            out.write(outdata);
            out.close();
        }catch(Exception e){
            return
        }
    }


    def getFile(def params,def response){
        def file= Attachment.get(params.id.toLong());
        if(file){
            try{
                def outdata=file.data;
                if(!outdata){
                    outdata=new File(file.filepath).bytes
                }
                if(file.type==3){
                    response.setContentType("image/jpeg");
                }else{
                    response.setContentType("application/x-msdownload;charset=GBK")
                    response.addHeader("Content-Disposition", 'attachment; filename="' + new String("${file.name}".getBytes("GBK"),"ISO8859-1")+'"')
                }
                response.setContentLength(outdata.size());
                OutputStream out = response.outputStream;
                out.write(outdata);
                out.close()
            }
            catch(Exception e){
                return
            }

        }else{
            //throw new Exception('无法下载')
            return
        }

    }
}
