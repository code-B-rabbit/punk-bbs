package com.example.xhbblog.configration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration           //自己自定义的一个博客配置类
public class BlogConfig {


    //异步任务配置
    @Bean("taskExecutor")
    public Executor taskExecutor () {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数10：线程池创建时候初始化的线程数
        executor.setCorePoolSize(10);
        // 最大线程数20：
        executor.setMaxPoolSize(15);
        // 缓冲队列200：
        executor.setQueueCapacity(200);
        // 允许线程的空闲时间60秒：
        executor.setKeepAliveSeconds(60);
        // 线程池名的前缀：
        executor.setThreadNamePrefix("taskExecutor-");
        /*
        线程池对拒绝任务的处理策略：这里采用了CallerRunsPolicy策略，
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，
        executor.setAwaitTerminationSeconds(600);
        return executor;
    }

    /**
     * 服务器支持跨域
     * @return
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/*")
                        .allowedOrigins("*")
                        .allowCredentials(true)
                        .allowedMethods("GET", "POST", "DELETE", "PUT","PATCH")
                        .maxAge(3600);
            }
        };
    }


}
