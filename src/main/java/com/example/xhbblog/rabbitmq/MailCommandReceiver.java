package com.example.xhbblog.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * 真正发送邮件异步消息的类
 */
@Component
@RabbitListener(queues = "mail")
@Slf4j
public class MailCommandReceiver {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${myEmail}")
    private String from;

    @RabbitHandler
    public void process(SimpleMailMessage simpleMailMessage) {
        log.info("队列发送消息{}",simpleMailMessage);
        javaMailSender.send(simpleMailMessage);
    }

}
