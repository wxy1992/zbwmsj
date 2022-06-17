package com.wmsj.ssl;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-12
 * Time: 下午9:09
 * To change this template use File | Settings | File Templates.
 */
public class CustomizeHostnameVerifier implements HostnameVerifier {

    public boolean verify(String hostname, SSLSession session) {
        //System.out.println("Warning: URL Host: " + hostname + " vs. " + session.getPeerHost());
        return true;
    }
}

