package com.arms.common.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudTopic;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.common.ServiceException;
import com.aliyun.mns.model.BatchSmsAttributes;
import com.aliyun.mns.model.MessageAttributes;
import com.aliyun.mns.model.RawTopicMessage;
import com.aliyun.mns.model.TopicMessage;
import com.arms.common.enums.MnsSignName;

public class MnsUtil {

    private static final String ACCESS_ID                = "LTAI8L35O55n4Jxu";

    private static final String ACCESS_KEY               = "48Q1fpyz180Pkd0dEINTFUqTwa3lbC";

    private static final String MNS_END_POINT            = "http://1770358567860830.mns.cn-hangzhou.aliyuncs.com/";

    private static final String TOPIC                    = "sms.topic-cn-hangzhou";

    //    private static final String SIGN_NAME                = "Pillow社区";

    //    private static final String SMS_CODE_TEMPLATE_CODE   = "SMS_71315467";
    private static final String SMS_CODE_TEMPLATE_CODE   = "SMS_78630206";

    private static final String SMS_TEMPLATE_PARAM_KEY_1 = "code";

    private static MnsUtil      mnsUtil                  = null;

    private static Logger       logger                   = LoggerFactory.getLogger(MnsUtil.class);

    private MnsUtil() {

    }

    public static MnsUtil getInstence() {
        if (mnsUtil == null) {
            mnsUtil = new MnsUtil();
        }
        return mnsUtil;
    }

    public TopicMessage postSMS(MnsSignName mnsSignName, long phoneNumber, String message) {
        /**
         * Step 1. 获取主题引用
         */
        CloudAccount account = new CloudAccount(ACCESS_ID, ACCESS_KEY, MNS_END_POINT);
        MNSClient client = account.getMNSClient();
        CloudTopic topic = client.getTopicRef(TOPIC);
        /**
         * Step 2. 设置SMS消息体（必须）
         *
         * 注：目前暂时不支持消息内容为空，需要指定消息内容，不为空即可。
         */
        RawTopicMessage msg = new RawTopicMessage();
        msg.setMessageBody("sms-message");
        //        msg.setMessageBody(message);
        /**
         * Step 3. 生成SMS消息属性
         */
        MessageAttributes messageAttributes = new MessageAttributes();
        BatchSmsAttributes batchSmsAttributes = new BatchSmsAttributes();
        // 3.1 设置发送短信的签名（SMSSignName）
        batchSmsAttributes.setFreeSignName(mnsSignName.getValue());
        // 3.2 设置发送短信使用的模板（SMSTempateCode）
        batchSmsAttributes.setTemplateCode(SMS_CODE_TEMPLATE_CODE);
        // 3.3 设置发送短信所使用的模板中参数对应的值（在短信模板中定义的，没有可以不用设置）
        BatchSmsAttributes.SmsReceiverParams smsReceiverParams = new BatchSmsAttributes.SmsReceiverParams();
        smsReceiverParams.setParam(SMS_TEMPLATE_PARAM_KEY_1, message);
        //        smsReceiverParams.setParam(SMS_TEMPLATE_PARAM_KEY_2, appName);
        // 3.4 增加接收短信的号码
        batchSmsAttributes.addSmsReceiver(phoneNumber + "", smsReceiverParams);
        messageAttributes.setBatchSmsAttributes(batchSmsAttributes);
        TopicMessage ret = null;
        logger.info("Start sending message: [cellphone: " + phoneNumber + ", message: " + message
                    + "]");
        try {
            /**
             * Step 4. 发布SMS消息
             */
            ret = topic.publishMessage(msg, messageAttributes);
            if (ret != null) {
                logger.info("MessageId: " + ret.getMessageId());
                logger.info("MessageBody: " + ret.getMessageBody());
                logger.info("MessageMD5: " + ret.getMessageBodyMD5());
                logger.info("MessageTag: " + ret.getMessageTag());
            }
            //            System.out.println("MessageId: " + ret.getMessageId());
            //            System.out.println("MessageBody：" + ret.getMessageBody());
            //            System.out.println("MessageMD5: " + ret.getMessageBodyMD5());
            //            System.out.println("MessageTag：" + ret.getMessageTag());
        } catch (ServiceException se) {
            logger.info("ServiceException ErrorCode and RequestId: " + se.getErrorCode()
                        + se.getRequestId());
            logger.info("ServiceException Message: " + se.getMessage());
            //            System.out.println(se.getErrorCode() + se.getRequestId());
            //            System.out.println(se.getMessage());
            se.printStackTrace();
        } catch (Exception e) {
            logger.info("Exception: " + e.getMessage());
            e.printStackTrace();
        }
        client.close();
        return ret;
    }

    public static void main(String[] args) {
        long now = System.currentTimeMillis();
        TopicMessage topicMessage = MnsUtil.getInstence().postSMS(MnsSignName.Taotie, 18258477020L,
            "222333");
        //        TopicMessage topicMessage = MnsUtil.getInstence().postSMS(18258477020L, "你好[Soldier]");
        System.out.println(topicMessage.toString());
    }

}
