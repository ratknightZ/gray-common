package com.arms.common.email;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class EmailOperateUtil {

    public static String sendEmail(String sendEmail, String recivEmail, String title, String msg)
                                                                                                 throws EmailException {
        String result = null;
        HtmlEmail htmlEmail = new HtmlEmail();
        htmlEmail.setTLS(true);
        htmlEmail.setHostName("183.131.13.58");
        //      htmlEmail.setAuthentication("mingqian@yunjee.com", "4578323wsad"); // 用户名和密码   
        //        htmlEmail.setAuthentication(sendEmail, password);

        htmlEmail.addTo(recivEmail); // 接收方   
        htmlEmail.setFrom(sendEmail); // 发送方   
        htmlEmail.setSubject(title); // 标题   
        htmlEmail.setCharset("GBK");
        htmlEmail.setMsg(msg); // 内容   

        result = htmlEmail.send();

        return result;
    }

    public static void main(String[] args) throws EmailException {
        sendEmail("no-reply@taotie.com", "sa9530@163.com", "taotie", "23442");
    }

}
