package com.example.xhbblog.Interceptor;

import com.example.xhbblog.annotation.AccessLimit;
import com.example.xhbblog.utils.IpUtil;
import org.omg.PortableInterceptor.Interceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Component
public class AccessLimitInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod){
            HandlerMethod hm = (HandlerMethod) handler;
            //获取方法中的注解,看是否有该注解
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);

            if(accessLimit == null){
                return true;
            }
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            String key = IpUtil.getIpAddr(request); //获得Ip地址
            Integer count= (Integer) redisTemplate.opsForValue().get(key);
            //从redis中获取用户访问的次数
            if(count == null){
                //第一次访问
                redisTemplate.opsForValue().set(key,1);
                redisTemplate.expire(key,seconds, TimeUnit.SECONDS);
            }else if(count < maxCount){
                //加1
                redisTemplate.opsForValue().increment(key);
            }else{
                //超出访问次数
                return false;
            }
        }
        return true;
    }
}
