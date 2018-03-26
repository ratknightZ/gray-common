package com.arms.common.http;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.log4j.Logger;

/**
 * 帮助解析http返回
 * @author kenan
 * @version $Id: HttpResultHelper.java, v 0.1 kenan Exp $
 */
public class HttpResultHelper {

    private static final int    _SPLITED_HTML_ARR_LENGTH = 2;

    private static final String _AND                     = "&";

    private static final String _EQUALS                  = "=";

    private static final String _HTML_TAG                = "<html>";

    private static final String _FIRST_SPLIT_CHAR        = "<body><h1>";

    private static final String _SECOND_SPLIT_CHAR       = "</h1><HR";

    private static final Logger logger                   = Logger.getLogger(HttpResultHelper.class);

    public static void loggerInfo(long timeConsuming, String result, HttpBuilder httpBuilder) {
        if (logger.isDebugEnabled()) {
            logger.debug("HttpClient request url    ------> " + httpBuilder.getUrl()
                         + " ---> And params are " + paramasToString(httpBuilder) + " ---> Takes "
                         + timeConsuming + " millis");
            if (StringUtils.isNotBlank(result) && !result.contains(_HTML_TAG)) {
                logger.debug("HttpClient request result ------> " + result);
            }
        }
    }

    public static String filterErrorHead(String result) {
        if (isHtml(result)) {
            try {
                String[] resultArr = result.split(_FIRST_SPLIT_CHAR);
                if (resultArr.length == _SPLITED_HTML_ARR_LENGTH) {
                    resultArr = resultArr[1].split(_SECOND_SPLIT_CHAR);
                    if (resultArr.length == _SPLITED_HTML_ARR_LENGTH) {
                        return resultArr[0];
                    }
                }
            } catch (Exception e) {
                logger.error("", e);
                logger.error(result);
            }
        }
        return result;
    }

    public static boolean isHtml(String result) {
        if (StringUtils.isNotBlank(result) && result.contains(_HTML_TAG)) {
            return true;
        }
        return false;
    }

    private static String paramasToString(HttpBuilder httpBuilder) {
        if (httpBuilder.getNameValuePairs() == null || httpBuilder.getNameValuePairs().isEmpty()) {
            return "empty";
        }

        StringBuffer stringBuffer = new StringBuffer();

        for (NameValuePair nameValuePair : httpBuilder.getNameValuePairs()) {
            stringBuffer.append(_AND);
            stringBuffer.append(nameValuePair.getName());
            stringBuffer.append(_EQUALS);
            stringBuffer.append(nameValuePair.getValue());
        }
        return stringBuffer.toString().substring(1);
    }

}
