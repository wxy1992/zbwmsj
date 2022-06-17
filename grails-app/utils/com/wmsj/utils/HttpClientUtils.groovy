package com.wmsj.utils

import com.bjrxkj.ssl.CustomizeHostnameVerifier
import com.bjrxkj.ssl.CustomizeX509TrustManager
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.apache.http.entity.ContentType

import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager
import javax.servlet.http.HttpServletRequest
import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.HttpClient
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.message.BasicNameValuePair
import org.apache.http.params.CoreConnectionPNames
import org.apache.http.util.EntityUtils

/**
 * Created by Administrator on 2018/1/10.
 */
class HttpClientUtils {
    private static Log logger = LogFactory.getLog(HttpClientUtils.class);
    private static HttpClientUtils instance;
    public static HttpClientUtils newInstance(){
        synchronized(HttpClientUtils.class){
            if(!instance){
                instance = new HttpClientUtils();
            }
        }
        return instance;
    }
    public String httpGetData(String reqURL){
        String result; //响应内容
        HttpClient httpClient = new DefaultHttpClient(); //创建默认的httpClient实例
        HttpGet httpGet = new HttpGet(reqURL);  //创建org.apache.http.client.methods.HttpGet
        //设置超时时间1分钟
        try{
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
            HttpResponse response = httpClient.execute(httpGet); //执行GET请求
            HttpEntity entity = response.getEntity();            //获取响应实体
            if(null != entity){
                result = EntityUtils.toString(entity, "UTF-8");
                EntityUtils.consume(entity); //Consume response content
            }
        }catch(ClientProtocolException e){
            logger.error("与[" + reqURL + "]通信过程中发生异常,堆栈信息如下", e);
        }finally{
            if (httpGet != null) {
                httpGet.abort();
            }
            if(httpClient){
                httpClient.getConnectionManager().shutdown();//关闭连接,释放资源
            }
        }
        //返回结果含有特殊字符，replac替换特殊字符；
        return result;
    }

    public String httpPostDataJson(String reqURL,String json) {
        String result;
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(reqURL);
        try {
            httpPost.addHeader("Content-Type", "application/json");
            StringEntity se = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(se);
            HttpResponse re = httpClient.execute(httpPost);
            result = EntityUtils.toString(re.getEntity(),"UTF-8");
//            println "httpPostDataJson：-----${result}";
        } catch (Exception e) {
            log.error("与[" + reqURL + "]通信过程中发生异常,堆栈信息如下", e);
        } finally {
            if (httpPost != null) {
                httpPost.abort();
            }
            if (httpClient) {
                httpClient.getConnectionManager().shutdown();
            }
        }
        return result;
    }

    public String httpPostDataMap(String reqURL,Map map){
        String responseContent = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(reqURL);
        //设置超时时间1分钟
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
        List formParams = new ArrayList(); //创建参数队列
        for(Object obj : map.entrySet()){
            Map.Entry entry = (Map.Entry)obj;
            formParams.add(new BasicNameValuePair(entry.getKey().toString(), entry.getValue().toString()));
        }
        try{
            httpPost.setEntity(new UrlEncodedFormEntity(formParams, "UTF-8"));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                responseContent = EntityUtils.toString(entity, "UTF-8" );
                EntityUtils.consume(entity);
            }
        }catch(Exception e){
            logger.error("与[" + reqURL + "]通信过程中发生异常,堆栈信息如下", e);
        }finally{
            if (httpPost != null) {
                httpPost.abort();
            }
            if(httpClient){
                httpClient.getConnectionManager().shutdown();
            }
        }
        //返回结果含有特殊字符，replac替换特殊字符；
        return responseContent;
    }
    private static initSSLSetting=false;
    /**
     *
     * @param address url
     * @param data {k:v}
     * @param method POST/GET
     * @param cookie
     * @return
     */
    public String httpsConnectionJson(String reqURL,String data,String method, cookie){
        URL url = new URL(reqURL);
        HttpsURLConnection conn = url.openConnection();
        try{
            if(!initSSLSetting){
                SSLContext sslContext = SSLContext.getInstance("TLS"); //或SSL
                X509TrustManager[] xtmArray =[new CustomizeX509TrustManager()];
                sslContext.init(null, xtmArray, new java.security.SecureRandom());
                if (sslContext != null) {
                    HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
                }
                HttpsURLConnection.setDefaultHostnameVerifier(new CustomizeHostnameVerifier());
                initSSLSetting=true;
            }

            conn.setRequestMethod(method);
            conn.setDoOutput(true);
            if(cookie!=null){
                conn.setRequestProperty("Cookie", cookie);
            }
            conn.outputStream.withWriter('utf-8'){writer->
                writer.write(data);
            }
            def result =  conn.inputStream.getText('utf-8');
            return result;
        } catch (Exception e) {
            log.error("短信请求异常：${e}");
        }finally{
            conn.disconnect();
        }
    }


    //获取ip地址
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
