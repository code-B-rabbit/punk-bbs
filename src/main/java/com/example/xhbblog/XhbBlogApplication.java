package com.example.xhbblog;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableCaching
@EnableScheduling           //允许定时任务
public class XhbBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(XhbBlogApplication.class, args);
        //System.setProperty("es.set.netty.runtime.available.processors", "false");
    }

}
