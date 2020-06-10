package com.example.xhbblog.configration;

import com.example.xhbblog.Interceptor.AccessLimitInterceptor;
import com.example.xhbblog.Interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration  //注明这是一个配置类
public class WebConfiguration extends WebMvcConfigurerAdapter {


    @Autowired
    private AccessLimitInterceptor accessLimitInterceptor;

    @Autowired
    private LoginInterceptor loginInterceptor;

    /**
     * 将权限校验拦截器以及限刷拦截器注入进去
     * @param registry
     */

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(loginInterceptor)
        .addPathPatterns("/admin/**").excludePathPatterns("/admin/adminLogin/**")
        .excludePathPatterns("/admin/checkUser");

        registry.addInterceptor(accessLimitInterceptor);
    }
}
