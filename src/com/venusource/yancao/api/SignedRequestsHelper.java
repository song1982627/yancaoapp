package com.venusource.yancao.api;



import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.venusource.yancao.log.LogUtil;

import android.util.Base64;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SimpleTimeZone;

public class SignedRequestsHelper {
   
    private static final String ALGORITHM = "HmacSHA1";
    private static final String UTF8_CHARSET = "UTF-8";
    private static final String SEPARATOR = "&";
    static final String SIGN_FLAG = "Signature";
    static final String[] signedParams = {"Version", "AccessKeyId", SIGN_FLAG,
            "SignatureMethod", "Timestamp", "SignatureVersion",
            "SignatureNonce"};

   

    public static Map<String, String>  requestSign(String httpMethod) throws IOException {
    	Map<String, String> params = new HashMap<String, String>();		
		params.put("AccessKeyId", "6tnym1brnz0r7");
		params.put("SignatureMethod", "HMAC-SHA1");		
		params.put("SignatureVersion", "1.0");
		params.put("SignatureNonce", "hahaha");
		params.put("Version", formatIso8601Date(new Date()));
		params.put("Timestamp", formatIso8601Date(new Date()));   	  
    	
        // 第一步：检查参数是否已经排序
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        // 第二步：把所有参数名和参数值串在一起
        StringBuilder stringToSign = new StringBuilder();
        stringToSign.append(httpMethod).append(SEPARATOR);
        stringToSign.append(percentEncode("/")).append(SEPARATOR);

        StringBuilder query = new StringBuilder();
        for (String key : keys) {
            String value = params.get(key);
            if (value != null && !"".equals(value)) {
                query.append("&").append(percentEncode(key)).append("=")
                        .append(percentEncode(value));
            }
        }

        // 第三步：使用HMAC加密
        String stringSign = stringToSign.append(
                percentEncode(query.toString().substring(1))).toString();
        byte[] bytes = encryptHMAC(stringSign, "0Sa35kE87rNID0");

        // 第四步：把二进制转化为大写的十六进制或者Base64编码
        // return byte2hex(bytes);
        // return byte2base64(bytes);
        LogUtil.PrintTextLog("byte2base64(bytes)=" + byte2base64(bytes));
        
        params.put("Signature", percentEncode(byte2base64(bytes)));
        LogUtil.PrintTextLog("Signature=" + percentEncode(byte2base64(bytes)));
        return params;
    }

    public static byte[] encryptHMAC(String data, String secret) throws IOException {
        byte[] bytes = null;
        try {
            SecretKey secretKey = new SecretKeySpec(
                    secret.getBytes(UTF8_CHARSET), ALGORITHM);
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            bytes = mac.doFinal(data.getBytes(UTF8_CHARSET));
        } catch (GeneralSecurityException gse) {
            throw new IOException(gse.toString());
        }
        return bytes;
    }

    public static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }

    public static String byte2base64(byte[] bytes) {
        return new String(Base64.encode(bytes,Base64.NO_WRAP));
    }

    private static String percentEncode(String s) {
        String out;
        try {
            out = URLEncoder.encode(s, UTF8_CHARSET).replace("+", "%20")
                    .replace("*", "%2A").replace("%7E", "~");
        } catch (UnsupportedEncodingException e) {
            out = s;
        }
        return out;
    }

   private static final String ISO8601_DATE_FORMAT = "yyyy-MM-dd";

    public  static String formatIso8601Date(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(ISO8601_DATE_FORMAT);
        df.setTimeZone(new SimpleTimeZone(0, "GMT"));
        return df.format(date);
    }
}

