package com.example.xhbblog.service.impl;

import com.example.xhbblog.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    private Logger LOG= LoggerFactory.getLogger(this.getClass());

    @Value("${myEmail}")
    private String from;

    /**
     * 异步的发送邮件任务
     * @param subject
     * @param to
     * @param text
     */
    @Async("taskExecutor")
    @Override
    public void sendEmail(String subject,String to, String text) {
        if(to!=null){
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(subject);
            message.setFrom(from);
            message.setTo(to);
            message.setSentDate(new Date());
            message.setText(text);
            LOG.info("邮件服务:从{}给{}发送消息{},标题为",from,to,message,subject);
            javaMailSender.send(message);
        }
    }


}
