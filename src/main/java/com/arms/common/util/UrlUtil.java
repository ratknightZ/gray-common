package com.arms.common.util;/** * URL相关工具类 *  * @author ben * @version $Id: UrlUtil.java, v 0.1 2013-4-3 下午3:53:46 ben Exp $ */public class UrlUtil {    public static final int    ID_INC_NUM = 10000;    public static final String URL_SUFFIX = ".html";    /**     * 链接加参数     *     * @param url     * @param paramName     * @param paramValue     * @return     */    public static String addParam(String url, String paramName, String paramValue) {        String separator = (url.indexOf("?") == -1) ? "?" : "&";        return url + separator + paramName + "=" + paramValue;    }    /**     * 链接去参数     *     * @param url     * @param paramName     * @param paramValue     * @return     */    public static String removeParam(String url, String paramName, String paramValue) {        url = url.replace(paramName + "=" + paramValue, "");        if (url.contains("&&")) {            url = url.replace("&&", "&");        }        if (url.contains("?&")) {            url = url.replace("?&", "?");        }        if (url.endsWith("?")) {            url = url.replace("?", "");        }        if (url.endsWith("&")) {            url = url.substring(0, url.lastIndexOf("&"));        }        return url;    }    /**     * 链接去参数     *     * @param url     * @param paramName     * @return     */    public static String removeParam(String url, String paramName) {        if (url.contains(paramName)) {            int fromIndex = url.indexOf(paramName);            int index = url.indexOf("&", fromIndex);            if (index != -1) {                url = url.substring(fromIndex, index);            }            return url;        }        return url;    }    public static String addPageParam(String url, int page) {        return addParam(url, "page", String.valueOf(page));    }    public static String splitHttp(String url) {        if (url != null) {            if (url.contains("https://"))                url = url.replace("https://", "");            else if (url.contains("http://"))                url = url.replace("http://", "");        }        return url;    }    /**     * 处理url链接地址有误问题     *     * @param url     * @return     */    public static String initUrl(String url) {        if (url == null && "".equals(url)) {            return null;        }        if (url.endsWith("?")) {            return url.replace("?", "");        }        if (url.contains("?&")) {            url = url.replace("?&", "?");            return url;        }        return url;    }}