package com.example.xhbblog.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static String MAIL_CHANNEL="mail";

    /**
     * 邮件消息队列
     * @return
     */
    @Bean
    public Queue Queue() {
        return new Queue(MAIL_CHANNEL);
    }

}