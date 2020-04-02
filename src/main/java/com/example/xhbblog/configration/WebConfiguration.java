package com.example.xhbblog.configration;

import com.example.xhbblog.Interceptor.LoginInterceptor;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.File;
import java.io.FileInputStream;

@Configuration  //注明这是一个配置类
public class WebConfiguration extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(new LoginInterceptor())
        .addPathPatterns("/admin/**").excludePathPatterns("/admin/adminLogin/**")
        .excludePathPatterns("/admin/checkUser");
    }

    public static void main(String[] args) {
    }
}
