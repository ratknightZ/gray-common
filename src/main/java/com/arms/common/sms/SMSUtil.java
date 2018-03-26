package com.arms.common.sms;

import com.arms.common.http.HttpBuilder;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.arms.core.configure.ConfigResolver;
import com.arms.core.configure.DefaultConfigResolver;

/**
 * 短信工具类
 * @author mingqian
 * @version $Id: SMSUtil.java, v 0.1 Nero Exp $
 */
public class SMSUtil {
    private static final String _ONLINE_SEND_SMS_URL  = "http://api.taotie.com/promotionservice/sendSMS";

    private static final String _OFFLINE_SEND_SMS_URL = "http://api.taotie.net/promotionservice/sendSMS";

    private static final String _APP_ENV_KEY          = "app.env";

    private static final String _RELEASE              = "release";

    private static String       _SEND_SMS_URL         = _OFFLINE_SEND_SMS_URL;

    private static String       _APP_ENV              = "";

    private static SMSUtil      smsUtil               = null;

    private static final Logger logger                = LoggerFactory.getLogger(SMSUtil.class);

    static {
        ConfigResolver config = new DefaultConfigResolver();

        try {
            Configuration configuration = config.getAppConfig();
            _APP_ENV = configuration.getString(_APP_ENV_KEY);

            if (_RELEASE.equals(_APP_ENV)) {
                _SEND_SMS_URL = _ONLINE_SEND_SMS_URL;
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    private SMSUtil() {

    }

    public static SMSUtil getInstence() {
        if (smsUtil == null) {
            smsUtil = new SMSUtil();
        }
        return smsUtil;
    }

    /**
     * 短信发送接口更改为内部请求
     * @author fangyi
     * */
    public String postSMS(long phoneNumber, String message) {
        HttpBuilder httpBuilder = new HttpBuilder(_SEND_SMS_URL);
        httpBuilder.addParam("phoneNumber", phoneNumber + "");
        httpBuilder.addParam("message", message);
        return httpBuilder.get();
    }

    public static void main(String[] args) {
        long now = System.currentTimeMillis();
        String content = "亲爱哒，美妆大牌折上折3月24日劲爆来袭！1件9折 2件7折 3件6折！全场包邮噢！你懂的→http://t.cn/RAvzI8Y 退订回T）";
        String result = SMSUtil.getInstence().postSMS(18625974641L, content);
        System.out.println(result);
        System.out.println(System.currentTimeMillis() - now);

    }
}
