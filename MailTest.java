package com.bgy.email.test;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class MailTest {

    private static final String myEmailSMTPHost = "smtp.qq.com";
    
    public static void main(String[] args) throws MessagingException, IOException {
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.host", MailTest.myEmailSMTPHost);
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.socketFactory.port", "465");        
        Session session = Session.getInstance(props);
        session.setDebug(true);
        
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("1985508467@qq.com", "test", "UTF-8"));
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress("1985508467@qq.com", "duanli", "UTF-8"));
        message.setSubject("系统公告(含文本+图片+附件)", "UTF-8");
        
        //组装 文本,图片和附件
/*        MimeBodyPart image = new MimeBodyPart();
        DataHandler imageDh = new DataHandler(new FileDataSource("F:\\test.jpg"));
        image.setDataHandler(imageDh);
        image.setContentID("image_test_jpg");
*/        
        MimeBodyPart text = new MimeBodyPart();
        text.setContent("this is a image:dfgsgsfgsdfgzsergzstehjnzsbshbserhs再不疯狂就老了", "text/html;charset=UTF-8");
        
/*        MimeMultipart mmTextImage = new MimeMultipart();
        mmTextImage.addBodyPart(text);
        mmTextImage.setSubType("related");
        
        MimeBodyPart text_iamge = new MimeBodyPart();
        text_iamge.setContent(mmTextImage);*/
        
        MimeBodyPart attachment = new MimeBodyPart();
        DataHandler attachmentDh = new DataHandler(new FileDataSource("F:\\入职引导.pdf"));
        attachment.setDataHandler(attachmentDh);
        attachment.setFileName((MimeUtility.encodeText("发给天童鞋的.pdf")));
        
        MimeMultipart mm = new MimeMultipart();
        mm.addBodyPart(text);
        mm.addBodyPart(attachment);
        mm.setSubType("mixed");
        
        
        message.setContent(mm);
        message.setSentDate(new Date());
        message.saveChanges();
        
        Transport transport = session.getTransport();
        transport.connect("1985508467@qq.com", "jtasjwjjluelfdih");
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
        
    }

}
