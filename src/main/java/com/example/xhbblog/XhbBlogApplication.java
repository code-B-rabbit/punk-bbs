package com.example.xhbblog;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan({"com.example"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableCaching
@EnableScheduling           //允许定时任务
@EnableAsync               //允许异步任务
public class XhbBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(XhbBlogApplication.class, args);
    }

}
