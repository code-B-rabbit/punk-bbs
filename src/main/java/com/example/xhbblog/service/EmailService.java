package com.example.xhbblog.service;

/**
 * 异步发送邮件业务接口
 */
public interface EmailService {

    /**
     * 发送邮件业务类
     * @param subject
     * @param to
     * @param text
     */
    public void sendEmail(String  subject,String to,String text);
}
