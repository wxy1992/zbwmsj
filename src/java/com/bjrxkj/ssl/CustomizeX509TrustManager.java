package com.bjrxkj.ssl;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

/**
 * Created with IntelliJ IDEA.
 * User: xiaopeng
 * Date: 13-9-12
 * Time: 下午9:06
 * make http ssl connection
 */
public class CustomizeX509TrustManager implements X509TrustManager {

    public void checkClientTrusted(X509Certificate[] chain, String authType) {
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType) {
        //System.out.println("cert: " + chain[0].toString() + ", authType: " + authType);
    }

    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}

