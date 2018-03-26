package com.arms.common.http;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class HttpUtils {

    public static void main(String[] args) {
        List<NameValuePair> nameValuePairs = getNameValuePairs();
        HttpUtils.add("123", "321", nameValuePairs);
        System.out.println(nameValuePairs.toString());
    }

    public static List<NameValuePair> getNameValuePairs() {
        return new ArrayList<NameValuePair>();
    }

    public static void add(String name, Object value, List<NameValuePair> nameValuePairs) {
        nameValuePairs.add(new BasicNameValuePair(name, value + ""));
    }
}
