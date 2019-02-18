package com.pagoda.nerp.uniseq.cgdd.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 发送邮件配置信息类
 * @author heshixi
 * @date 2018-01-23
 */
@Component
public class EmailConfig {

    @Value("${email.sender.account}")
    private String senderAccount;
    
    @Value("${email.sender.password}")
    private String senderPassword;
    
    @Value("${email.smtp.server}")
    private String smtpServer;
    
    @Value("${email.ssl.connection}")
    private String requiredSSLConnection;
    
    @Value("${email.ssl.port}")
    private String port;

    public String getSenderAccount() {
        return senderAccount;
    }

    public void setSenderAccount(String senderAccount) {
        this.senderAccount = senderAccount;
    }

    public String getSenderPassword() {
        return senderPassword;
    }

    public void setSenderPassword(String senderPassword) {
        this.senderPassword = senderPassword;
    }

    public String getSmtpServer() {
        return smtpServer;
    }

    public void setSmtpServer(String smtpServer) {
        this.smtpServer = smtpServer;
    }

    public String getRequiredSSLConnection() {
        return requiredSSLConnection;
    }

    public void setRequiredSSLConnection(String requiredSSLConnection) {
        this.requiredSSLConnection = requiredSSLConnection;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
    
}
