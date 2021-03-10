package com.rigai.rigeye.sso.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author chenxing
 *         Created by ChenXing on 2017/11/13.
 */

public class CasUrlConstructor {
    private CasUrlConstructor(){}

    public static String constructRedirectUrl(String casServerLoginUrl, String serviceParameterName, String serviceUrl, boolean renew, boolean gateway) {
        return casServerLoginUrl + (casServerLoginUrl.contains("?")?"&":"?") + serviceParameterName + "=" + urlEncode(serviceUrl) ;
    }

    private static String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException var2) {
            throw new RuntimeException(var2);
        }
    }
}
