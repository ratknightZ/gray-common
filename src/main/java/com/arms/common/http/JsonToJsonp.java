package com.arms.common.http;

import org.apache.commons.lang.StringUtils;

public class JsonToJsonp {

    public static String toJsonp(String json, String callback) {
        String jsonP = null;
        if (StringUtils.isNotBlank(json) && StringUtils.isNotBlank(callback)) {
            jsonP = callback + "(" + json + ");";
        }
        return jsonP;
    }
}
