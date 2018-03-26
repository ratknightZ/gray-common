package com.arms.common.http;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

/**
 * HTTPClient工具类
 * zhangchaojie
 */
public class HttpBuilder {
    private String              url;

    private List<NameValuePair> nameValuePairs;

    private static final Logger logger = Logger.getLogger(HttpBuilder.class);

    public HttpBuilder(String url) {
        this.url = url;
        nameValuePairs = new ArrayList<NameValuePair>();
    }

    public void addParam(String name, String value) {
        nameValuePairs.add(new BasicNameValuePair(name, value));
    }

    public String get() {
        long startTime = System.currentTimeMillis();
        return HttpClientUtil.asynGet(this.getUrl(), this.getNameValuePairs());
    }

    public String post() {
        long startTime = System.currentTimeMillis();
        return HttpClientUtil.asynPost(this.getUrl(), this.getNameValuePairs());
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<NameValuePair> getNameValuePairs() {
        return nameValuePairs;
    }

    public static void main(String[] args) {

        HttpBuilder httpBuilder = new HttpBuilder(
            "http://api.taotie.net/productservice/brands/get");
        httpBuilder.addParam("page", "1");
        httpBuilder.addParam("pageSize", "10");

        String result = httpBuilder.get();
        System.out.println(result);

    }
}
