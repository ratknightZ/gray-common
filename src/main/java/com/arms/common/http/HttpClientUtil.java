package com.arms.common.http;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.arms.common.vo.JsonVO;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.arms.core.configure.ConfigResolver;
import com.arms.core.configure.DefaultConfigResolver;

/**
 * Httpclient 工具类
 * @author mingqian
 * @version $Id: HttpClientUtil.java, v 0.1 Nero Exp $
 */
public class HttpClientUtil {

    private static final Logger logger             = LoggerFactory.getLogger(HttpClientUtil.class);

    public static String        CHARSET_ENCODING   = "UTF-8";
    // private static String  
    // USER_AGENT="Mozilla/4.0 (compatible; MSIE 6.0; Win32)";//ie6  
    public static String        USER_AGENT         = "Mozilla/4.0 (compatible; MSIE 7.0; Win32)";  // ie7  

    // private static String  
    // USER_AGENT="Mozilla/4.0 (compatible; MSIE 8.0; Win32)";//ie8  

    public static String        APPLICATION_SOURCE = "";

    static {
        ConfigResolver configResolver = new DefaultConfigResolver();
        try {
            Configuration config = configResolver.getAppConfig();
            APPLICATION_SOURCE = config.getString("application_source");
        } catch (ConfigurationException e) {
            logger.error("", e);
        } catch (MalformedURLException e) {
            logger.error("", e);
        }
    }

    /** 
     * 获取DefaultHttpClient对象 
     *  
     * @param charset 
     *            字符编码 
     * @return DefaultHttpClient对象 
     */
    private static DefaultHttpClient getDefaultHttpClient(final String charset) {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        // 模拟浏览器，解决一些服务器程序只允许浏览器访问的问题  
        httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, USER_AGENT);
        httpclient.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);
        httpclient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET,
            charset == null ? CHARSET_ENCODING : charset);

        // 浏览器兼容性  
        httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY,
            CookiePolicy.BROWSER_COMPATIBILITY);
        // 定义重试策略  
        httpclient.setHttpRequestRetryHandler(requestRetryHandler);

        return httpclient;
    }

    /** 
     * 访问https的网站 
     * @param httpclient 
     */
    private static void enableSSL(DefaultHttpClient httpclient) {
        //调用ssl  
        try {
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, new TrustManager[] { trustAllManager }, null);
            SSLSocketFactory sf = new SSLSocketFactory(sslcontext,
                SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            Scheme https = new Scheme("https", 443, sf);
            httpclient.getConnectionManager().getSchemeRegistry().register(https);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 
     * 重写验证方法，取消检测ssl 
     */
    private static TrustManager            trustAllManager     = new X509TrustManager() {

                                                                   @Override
                                                                   public void checkClientTrusted(java.security.cert.X509Certificate[] arg0,
                                                                                                  String arg1)
                                                                                                              throws CertificateException {
                                                                       // TODO Auto-generated method stub  

                                                                   }

                                                                   @Override
                                                                   public void checkServerTrusted(java.security.cert.X509Certificate[] arg0,
                                                                                                  String arg1)
                                                                                                              throws CertificateException {
                                                                       // TODO Auto-generated method stub  

                                                                   }

                                                                   @Override
                                                                   public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                                                       // TODO Auto-generated method stub  
                                                                       return null;
                                                                   }

                                                               };

    /** 
     * 异常自动恢复处理, 使用HttpRequestRetryHandler接口实现请求的异常恢复 
     */
    private static HttpRequestRetryHandler requestRetryHandler = new HttpRequestRetryHandler() {
                                                                   // 自定义的恢复策略  
                                                                   @Override
                                                                   public boolean retryRequest(IOException exception,
                                                                                               int executionCount,
                                                                                               HttpContext context) {
                                                                       // 设置恢复策略，在发生异常时候将自动重试3次  
                                                                       if (executionCount >= 3) {
                                                                           // 如果连接次数超过了最大值则停止重试  
                                                                           return false;
                                                                       }
                                                                       if (exception instanceof NoHttpResponseException) {
                                                                           // 如果服务器连接失败重试  
                                                                           return true;
                                                                       }
                                                                       if (exception instanceof SSLHandshakeException) {
                                                                           // 不要重试ssl连接异常  
                                                                           return false;
                                                                       }
                                                                       HttpRequest request = (HttpRequest) context
                                                                           .getAttribute(ExecutionContext.HTTP_REQUEST);
                                                                       boolean idempotent = (request instanceof HttpEntityEnclosingRequest);
                                                                       if (!idempotent) {
                                                                           // 重试，如果请求是考虑幂等  
                                                                           return true;
                                                                       }
                                                                       return false;
                                                                   }
                                                               };

    /** 
     * 使用ResponseHandler接口处理响应，HttpClient使用ResponseHandler会自动管理连接的释放，解决了对连接的释放管理 
     */
    private static ResponseHandler<String> responseHandler     = new ResponseHandler<String>() {
                                                                   // 自定义响应处理  
                                                                   @Override
                                                                   public String handleResponse(HttpResponse response)
                                                                                                                      throws ClientProtocolException,
                                                                                                                      IOException {
                                                                       HttpEntity entity = response
                                                                           .getEntity();
                                                                       if (entity != null) {
                                                                           String charset = EntityUtils
                                                                               .getContentCharSet(entity) == null ? CHARSET_ENCODING
                                                                               : EntityUtils
                                                                                   .getContentCharSet(entity);
                                                                           return new String(
                                                                               EntityUtils
                                                                                   .toByteArray(entity),
                                                                               charset);
                                                                       } else {
                                                                           return null;
                                                                       }
                                                                   }
                                                               };

    /** 
     * 使用post方法获取相关的数据 
     *  
     * @param url 
     * @param paramsList 
     * @return 
     */
    public static String post(String url, List<NameValuePair> paramsList) {
        BasicHeader header = new BasicHeader("accept", "application/json");
        return httpRequest(url, paramsList, "POST", null, null, header);
    }

    /** 
     * 使用post方法并且通过代理获取相关的数据 
     *  
     * @param url 
     * @param paramsList 
     * @param proxy 
     * @return 
     */
    public static String post(String url, List<NameValuePair> paramsList, HttpHost proxy) {
        return httpRequest(url, paramsList, "POST", proxy, null, null);
    }

    public static String asynPost(String url, List<NameValuePair> paramsList) {
        BasicHeader header = new BasicHeader("accept", "application/json");
        return httpRequest(url, paramsList, "POST", null, null, header);
    }

    public static String asynGet(String url, List<NameValuePair> paramsList) {
        BasicHeader header = new BasicHeader("accept", "application/json");
        return httpRequest(url, paramsList, "GET", null, null, header);
    }

    /** 
     * 使用get方法获取相关的数据 
     *  
     * @param url 
     * @param paramsList 
     * @return 
     */
    public static String get(String url, List<NameValuePair> paramsList) {
        BasicHeader header = new BasicHeader("accept", "application/json");
        return httpRequest(url, paramsList, "GET", null, null, header);
    }

    public static String get(String url, List<NameValuePair> paramsList, String charsetEncoding) {
        BasicHeader header = new BasicHeader("accept", "application/json");
        return httpRequest(url, paramsList, "GET", null, charsetEncoding, header);
    }

    /** 
     * 使用get方法并且通过代理获取相关的数据 
     *  
     * @param url 
     * @param paramsList 
     * @param proxy 
     * @return 
     */
    public static String get(String url, List<NameValuePair> paramsList, HttpHost proxy) {
        return httpRequest(url, paramsList, "GET", proxy, null, null);
    }

    public static String get(String url, List<NameValuePair> paramsList, HttpHost proxy,
                             String charsetEncoding) {
        return httpRequest(url, paramsList, "GET", proxy, charsetEncoding, null);
    }

    public static String get(String url, List<NameValuePair> paramsList, BasicHeader header) {
        return httpRequest(url, paramsList, "GET", null, null, header);
    }

    /**
     *
     * @param url
     * @param paramsList
     * @param method
     * @param proxy
     * @param charsetEncoding
     * @param headers
     * @return
     */
    public static String httpRequest(String url, List<NameValuePair> paramsList, String method,
                                     HttpHost proxy, String charsetEncoding, BasicHeader header) {
        String responseStr = null;
        // 判断输入的值是是否为空  
        if (null == url || "".equals(url)) {
            return null;
        }

        // 创建HttpClient实例
        if (charsetEncoding == null || charsetEncoding.equals("null"))
            charsetEncoding = CHARSET_ENCODING;
        DefaultHttpClient httpclient = getDefaultHttpClient(charsetEncoding);

        //判断是否是https请求  
        if (url.startsWith("https")) {
            enableSSL(httpclient);
        }
        String formatParams = null;
        // 将参数进行utf-8编码  
        if (null != paramsList && paramsList.size() > 0) {
            formatParams = URLEncodedUtils.format(paramsList, charsetEncoding);
        }
        // 如果代理对象不为空则设置代理  
        if (null != proxy) {
            httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        }

        try {
            // 如果方法为Get  
            if ("GET".equalsIgnoreCase(method)) {
                if (formatParams != null) {
                    url = (url.indexOf("?")) < 0 ? (url + "?" + formatParams) : (url.substring(0,
                        url.indexOf("?") + 1) + formatParams);
                }
                HttpGet hg = new HttpGet(url);
                if (header != null) {
                    hg.setHeader(header);
                    hg.setHeader("Cookie", APPLICATION_SOURCE);
                }
                responseStr = httpclient.execute(hg, responseHandler);

                // 如果方法为Post  
            } else if ("POST".equalsIgnoreCase(method)) {
                HttpPost hp = new HttpPost(url);
                if (formatParams != null) {
                    StringEntity entity = new StringEntity(formatParams);
                    entity.setContentType("application/x-www-form-urlencoded");
                    hp.setEntity(entity);
                }
                if (header != null) {
                    hp.setHeader(header);
                    hp.setHeader("Cookie", APPLICATION_SOURCE);
                }
                responseStr = httpclient.execute(hp, responseHandler);

            }

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block  
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block  
            e.printStackTrace();
        }
        return responseStr;
    }

    /** 
     * 提交数据到服务器 
     *  
     * @param url 
     * @param params 
     * @param authenticated 
     * @throws IOException 
     * @throws ClientProtocolException 
     */
    public static String httpFileRequest(String url, Map<String, String> fileMap,
                                         Map<String, String> stringMap, int type, HttpHost proxy) {
        String responseStr = null;
        // 判断输入的值是是否为空  
        if (null == url || "".equals(url)) {
            return null;
        }
        // 创建HttpClient实例  
        DefaultHttpClient httpclient = getDefaultHttpClient(CHARSET_ENCODING);

        //判断是否是https请求  
        if (url.startsWith("https")) {
            enableSSL(httpclient);
        }

        // 如果代理对象不为空则设置代理  
        if (null != proxy) {
            httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        }
        //发送文件  
        HttpPost hp = new HttpPost(url);
        MultipartEntity multiEntity = new MultipartEntity();
        try {
            //type=0是本地路径，否则是网络路径  
            if (type == 0) {
                for (String key : fileMap.keySet()) {
                    multiEntity.addPart(key, new FileBody(new File(fileMap.get(key))));
                }
            } else {
                for (String key : fileMap.keySet()) {
                    multiEntity.addPart(key, new ByteArrayBody(getUrlFileBytes(fileMap.get(key)),
                        key));
                }
            }
            // 加入相关参数 默认编码为utf-8  
            for (String key : stringMap.keySet()) {
                multiEntity.addPart(key,
                    new StringBody(stringMap.get(key), Charset.forName(CHARSET_ENCODING)));
            }
            hp.setEntity(multiEntity);
            responseStr = httpclient.execute(hp, responseHandler);
        } catch (Exception e) {
            // TODO Auto-generated catch block  
            e.printStackTrace();
        }
        return responseStr;
    }

    /**
     *将相关文件提交给服务器
     * @param url
     * @param multipartEntity
     * @param proxy
     * @return
     */
    public static String httpFileRequest(String url, MultipartEntity multipartEntity, HttpHost proxy) {
        String responseStr = null;
        // 判断输入的值是是否为空  
        if (null == url || "".equals(url)) {
            return null;
        }
        // 创建HttpClient实例  
        DefaultHttpClient httpclient = getDefaultHttpClient(CHARSET_ENCODING);

        //判断是否是https请求  
        if (url.startsWith("https")) {
            enableSSL(httpclient);
        }

        // 如果代理对象不为空则设置代理  
        if (null != proxy) {
            httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        }
        //发送文件  
        HttpPost hp = new HttpPost(url);
        try {
            hp.setEntity(multipartEntity);
            responseStr = httpclient.execute(hp, responseHandler);
        } catch (Exception e) {
            // TODO Auto-generated catch block  
            e.printStackTrace();
        }
        return responseStr;
    }

    /** 
     * 将相关文件和参数提交到相关服务器 
     * @param url 
     * @param fileMap 
     * @param StringMap 
     * @return 
     */
    public static String postFile(String url, Map<String, String> fileMap,
                                  Map<String, String> stringMap) {
        return httpFileRequest(url, fileMap, stringMap, 0, null);
    }

    /** 
     * 将相关文件和参数提交到相关服务器 
     * @param url 
     * @param fileMap 
     * @param StringMap 
     * @return 
     */
    public static String postUrlFile(String url, Map<String, String> urlMap,
                                     Map<String, String> stringMap) {
        return httpFileRequest(url, urlMap, stringMap, 1, null);
    }

    /** 
     * 获取网络文件的字节数组 
     *  
     * @param url 
     * @return 
     * @throws IOException 
     * @throws ClientProtocolException 
     * @throws ClientProtocolException 
     * @throws IOException 
     */
    public static byte[] getUrlFileBytes(String url) throws ClientProtocolException, IOException {

        byte[] bytes = null;
        // 创建HttpClient实例  
        DefaultHttpClient httpclient = getDefaultHttpClient(CHARSET_ENCODING);
        // 获取url里面的信息  
        HttpGet hg = new HttpGet(url);
        HttpResponse hr = httpclient.execute(hg);
        bytes = EntityUtils.toByteArray(hr.getEntity());
        // 转换内容为字节  
        return bytes;
    }

    /** 
     * 获取图片的字节数组 
     *  
     * @createTime 2011-11-24 
     * @param url 
     * @return 
     * @throws IOException 
     * @throws ClientProtocolException 
     * @throws ClientProtocolException 
     * @throws IOException 
     */
    public static byte[] getImg(String url) throws ClientProtocolException, IOException {
        byte[] bytes = null;
        // 创建HttpClient实例  
        DefaultHttpClient httpclient = getDefaultHttpClient(CHARSET_ENCODING);
        // 获取url里面的信息  
        HttpGet hg = new HttpGet(url);
        HttpResponse hr = httpclient.execute(hg);
        bytes = EntityUtils.toByteArray(hr.getEntity());
        // 转换内容为字节  
        return bytes;
    }

    public static void main(String[] args) throws URISyntaxException, ClientProtocolException,
                                          IOException {

        String url = "http://localhost/countJoinedTimes?userId=1&activityId=2";

        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();

        paramsList.add(new BasicNameValuePair("userId", "1"));
        paramsList.add(new BasicNameValuePair("activityId", "2"));
        String str = HttpClientUtil.asynGet(url, paramsList);

        JsonVO jsonVO = new JsonVO(str);

        jsonVO.getException();
        System.out.println(str);

    }
}