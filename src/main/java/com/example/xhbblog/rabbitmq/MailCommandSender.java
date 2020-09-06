package com.example.xhbblog.rabbitmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

/**
 * 用于向邮件队列发送信息的控制器
 */
@Component
public class MailCommandSender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendMessage(SimpleMailMessage msg){
        amqpTemplate.convertAndSend(RabbitConfig.MAIL_CHANNEL,msg);
    }

}
