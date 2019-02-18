package com.pagoda.nerp.uniseq.cgdd.common;

import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {

    private static final Logger logger = LoggerFactory.getLogger(EmailUtil.class);

    //编码方式
    private static final String ENCODING = "UTF-8";
    //正文类型
    private static final String CONTENT_TYPE = "text/html;charset=UTF-8";

    @Autowired
    private EmailConfig emailConfig;

    /**
     * @param receiver             收件人邮箱账号
     * @param subject              邮件主题
     * @param content              邮件正文
     * @param sourceFile[]         源文件(路径+文件名)
     * @param attachmentFileName[] 附件文件名, 与参数sourceFile[]一一对应
     * @return
     * @author heshixi
     * @since 2018年1月23日
     */
    public BaseResult emailSend(String receiver, String subject, String content, String[] sourceFile, String[] attachmentFileName) {

        // 设置附件中文名过长不截断
        System.setProperty("mail.mime.splitlongparameters", "false");

        // 用于连接邮件服务器的参数配置
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.host", emailConfig.getSmtpServer());
        props.setProperty("mail.smtp.auth", "true");

        //邮箱服务器要求SMTP连接需要使用SSL安全认证
        if ("true".equals(emailConfig.getRequiredSSLConnection())) {
            props.setProperty("mail.smtp.port", emailConfig.getPort());
            props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.smtp.socketFactory.fallback", "false");
            props.setProperty("mail.smtp.socketFactory.port", emailConfig.getPort());
        }

        // 根据参数配置，创建会话对象
        Session session = Session.getInstance(props);

        // 创建邮件对象
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(emailConfig.getSenderAccount(), null, EmailUtil.ENCODING));
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiver, null, EmailUtil.ENCODING));
            message.setSubject(subject, EmailUtil.ENCODING);
            MimeBodyPart text = new MimeBodyPart();
            text.setContent(content, EmailUtil.CONTENT_TYPE);

            //添加附件, 合并邮件正文和附件
            MimeMultipart mm = new MimeMultipart();
            mm.addBodyPart(text);
            if ((sourceFile != null && sourceFile.length > 0) && (attachmentFileName != null && attachmentFileName.length > 0)
                    && sourceFile.length == attachmentFileName.length) {
                for (int i = 0; i < sourceFile.length; i++) {
                    DataHandler attachmentDh = new DataHandler(new FileDataSource(sourceFile[i]));
                    MimeBodyPart attachment = new MimeBodyPart();
                    attachment.setDataHandler(attachmentDh);
                    attachment.setFileName(MimeUtility.encodeWord(attachmentFileName[i]));
                    mm.addBodyPart(attachment);
                }
            }
            mm.setSubType("mixed");

            message.setContent(mm);
            message.setSentDate(new Date());
            message.saveChanges();

            //发送邮件
            Transport transport = session.getTransport();
            transport.connect(emailConfig.getSenderAccount(), emailConfig.getSenderPassword());
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }  catch (Exception e) {
            logger.error("发送邮件失败", e);
            return new BaseResult(ErrorCodeMappingEnums.ERROR, "发送邮件失败");
        }
        logger.info("----------------- 发送邮件成功!");
        return new BaseResult(ErrorCodeMappingEnums.SUCCESS, "邮件发送成功");
    }

}
