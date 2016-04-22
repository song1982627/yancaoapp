package com.venusource.yancao.api;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;




import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.venusource.yancao.YcApplication;
import com.venusource.yancao.log.LogUtil;

import android.util.Base64;

public class HttpsClient   {
	private static final int SET_CONNECTION_TIMEOUT = 5 * 1000;  
    private static final int SET_SOCKET_TIMEOUT = 20 * 1000;
	private static String url = "https://api.devincloud.club/";
	
	private static class MySSLSocketFactory extends SSLSocketFactory {  
        SSLContext sslContext = SSLContext.getInstance("TLS");  
  
        public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException,  
                KeyManagementException, KeyStoreException, UnrecoverableKeyException {  
            super(truststore);  
  
            TrustManager tm = new X509TrustManager() {  
                public void checkClientTrusted(X509Certificate[] chain, String authType)  
                        throws CertificateException {  
                }  
  
                public void checkServerTrusted(X509Certificate[] chain, String authType)  
                        throws CertificateException {  
                }  
  
                public X509Certificate[] getAcceptedIssuers() {  
                    return null;  
                }  
            };  
  
            sslContext.init(null, new TrustManager[] { tm }, null);  
        }  
  
        @Override  
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose)  
                throws IOException, UnknownHostException {  
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);  
        }  
  
        @Override  
        public Socket createSocket() throws IOException {  
            return sslContext.getSocketFactory().createSocket();  
        }  
    }  
	
	public static HttpClient getNewHttpClient(YcApplication yc) {  
        try {  
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());  
            trustStore.load(null, null);  
  
            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);  
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);  
  
            HttpParams params = new BasicHttpParams();  
  
            HttpConnectionParams.setConnectionTimeout(params, 10000);  
            HttpConnectionParams.setSoTimeout(params, 10000);  
  
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);  
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);  
                      
            HttpProtocolParams.setUseExpectContinue(params, true);           
            
            HttpProtocolParams.setUserAgent(
            params,yc.getUserAgent());

            HttpConnectionParams.setConnectionTimeout(params, SET_CONNECTION_TIMEOUT);  
            HttpConnectionParams.setSoTimeout(params, SET_SOCKET_TIMEOUT);  
  
            SchemeRegistry registry = new SchemeRegistry();  
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));  
            registry.register(new Scheme("https", sf, 443)); 
            
            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);  
            HttpClient client = new DefaultHttpClient(ccm, params);             
            return client;  
        } catch (Exception e) {         	
            return new DefaultHttpClient();  
        }  
    }  

	
//	public static Object post(Map<String,String> params,Map<String,String> authParams,String apiUrl) {  
//        // 创建默认的httpClient实例.     
//        //CloseableHttpClient httpclient = createSSLClientDefault();  
//		HttpClient httpClient = new DefaultHttpClient();   
//        String posturl = url  +  apiUrl;
//        Map<String, String> urlParam = new HashMap<String, String>();
//        try {
//			urlParam = SignedRequestsHelper.requestSign("POST");
//			int i = 0;
//			for (String obj : urlParam.keySet()) {
//				if (i == 0) {
//					posturl += "?" + obj + "=" + urlParam.get(obj);
//				} else {
//					posturl += "&" + obj + "=" + urlParam.get(obj);
//				}
//				i++;
//			}
//		} catch (Exception e2) {
//			// TODO Auto-generated catch block
//			e2.printStackTrace();
//		}
//        
//        // 创建httppost     
//        HttpPost httppost = new HttpPost(posturl);  
//        // 创建参数队列     
//        
//        httppost.setHeader("Content-Type", "application/json;charset=utf-8");
//        if (authParams != null && authParams.size() > 0) {
//        	String auth = new String(authParams.get("username") + ":" +  authParams.get("password"));
//        	try {
//				String encodedAuthorization = new String(Base64.encodeBase64(auth.getBytes("utf-8")), "utf-8");
//				httppost.setHeader("Authorization", "Basic " + encodedAuthorization);
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} 
//        	
//        }
//        												
//        List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();  
//        if (params != null && params.size() > 0) {
//        	for (String obj:params.keySet()) {
//        		 formparams.add(new BasicNameValuePair(obj, params.get(obj)));  
//        	}
//        }    
//        UrlEncodedFormEntity uefEntity;  
//        try {  
//            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");  
//            httppost.setEntity(uefEntity);  
//            CloseableHttpResponse response = httpclient.execute(httppost);  
//            try {  
//                HttpEntity entity = response.getEntity();  
//                if (entity != null) {                 
//                   String result = EntityUtils.toString(entity, "UTF-8");  
//                   return toJsonObject(result);
//                }  
//            } finally {  
//                response.close();  
//            }  
//        } catch (ClientProtocolException e) {  
//            e.printStackTrace();  
//        } catch (UnsupportedEncodingException e1) {  
//            e1.printStackTrace();  
//        } catch (IOException e) {  
//            e.printStackTrace();  
//        } finally {  
//            // 关闭连接,释放资源     
//            try {  
//                httpclient.close();  
//            } catch (IOException e) {  
//                e.printStackTrace();  
//            }  
//        }  
//        return null;
//    }  
	
	public static Object get(Map<String,String> params,Map<String,String> authParams,String apiUrl,YcApplication yc) {  
        HttpClient httpClient = getNewHttpClient(yc);   
        String getturl = url  +  apiUrl;
        Map<String, String> urlParam = new HashMap<String, String>();
        try {
			urlParam = SignedRequestsHelper.requestSign("GET");
			int i = 0;
			for (String obj : urlParam.keySet()) {
				if (i == 0) {
					getturl += "?" + obj + "=" + urlParam.get(obj);
				} else {
					getturl += "&" + obj + "=" + urlParam.get(obj);
				}
				i++;
			}
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
        LogUtil.PrintTextLog("getturl=" + getturl);
        // 创建httpGET    
        HttpGet httpget = new HttpGet(getturl);  
        // 创建参数队列     
        
        httpget.setHeader("Content-Type", "application/json;charset=utf-8");
        httpget.addHeader("User-Agent","your agent");

        if (authParams != null && authParams.size() > 0) {
        	String auth = new String(authParams.get("username") + ":" +  authParams.get("password"));
        	try {
				String encodedAuthorization = Base64.encodeToString(auth.getBytes("utf-8"),Base64.NO_WRAP);
				httpget.setHeader("Authorization", "Basic " + encodedAuthorization);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
        	
        }
        try { 											
	        List<BasicNameValuePair> formparams = new ArrayList<BasicNameValuePair>();  
	        if (params != null && params.size() > 0) {
	        	for (String obj:params.keySet()) {
	        		 formparams.add(new BasicNameValuePair(obj, params.get(obj)));  
	        	}
	        	UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");  
	            String str = EntityUtils.toString(uefEntity);
	            httpget.setURI(new URI(httpget.getURI().toString() + "&" + str));
	        }    
	        HttpResponse  response = httpClient.execute(httpget);  
            try {  
                HttpEntity entity = response.getEntity();  
                LogUtil.PrintTextLog("entity=" + entity);
                if (entity != null) {                 
                   String result = EntityUtils.toString(entity, "UTF-8"); 
                   LogUtil.PrintTextLog("result=" + result);
                   return toJsonObject(result);
                }  
            } finally {  
                response = null;
            }  
        } catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (UnsupportedEncodingException e1) {  
            e1.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            // 关闭连接,释放资源     
        	httpClient.getConnectionManager().shutdown();
        }  
        return null;
    }  
	
	
	private static Object toJsonObject(String result) {
		// 将json字符串转换成jsonObject
		if (result != null && !"".equals(result)) {
			final char[] strChar = result.substring(0, 1).toCharArray();
			final char firstChar = strChar[0];
			if (firstChar == '{') {
				JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(result);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return jsonObject;
				
			} else if (firstChar == '[') {
				JSONArray jsonArray = null;
				try {
					jsonArray = new JSONArray(result);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
				return jsonArray;				
			} 

		}
		return null;

	}


	


}
