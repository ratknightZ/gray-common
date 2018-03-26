package com.arms.common.http;

import org.apache.http.NameValuePair;

public class NameValuePairImp implements NameValuePair {

    private String name;

    private String value;

    public NameValuePairImp(String name, String value) {
        super();
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }
}