package com.example.xhbblog.aop;

import javax.servlet.http.HttpServletRequest;

import com.example.xhbblog.annotation.NoRepeatSubmit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @功能描述 aop解析注解
 * @author www.gaozz.club
 * @date 2018-11-02
 */
@Aspect
@Component
@Slf4j
public class NoRepeatSubmitAop {

    private Log logger = LogFactory.getLog(getClass());

    @Autowired
    private RedisTemplate redisTemplate;

    @Pointcut("execution(public  * com.example.xhbblog.controller.*.*(..))")
    public void loginPointcut() {

    }



    /**
     * 这里一直没有解决前端jquery重复提交请求的问题,所以只能在这里防止重复访问了
     * @param pjp
     * @param nrs
     * @return
     */
    @Around("execution(* com.example..*Controller.*(..)) && @annotation(nrs)")
    public Object arround(ProceedingJoinPoint pjp, NoRepeatSubmit nrs) {
        synchronized (this){             //接口防止重复访问
            ValueOperations<String, Integer> opsForValue = redisTemplate.opsForValue();
            try {
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                String sessionId = RequestContextHolder.getRequestAttributes().getSessionId();
                HttpServletRequest request = attributes.getRequest();
                String key = sessionId + "-" + request.getServletPath();
                if (opsForValue.get(key) == null) {// 如果缓存中有这个url视为重复提交
                    Object o = pjp.proceed();
                    opsForValue.set(key, 0, 2, TimeUnit.SECONDS);
                    return o;
                } else {
                    logger.error("重复提交");
                    return null;
                }
            } catch (Throwable e) {
                e.printStackTrace();
                logger.error("验证重复提交时出现未知异常!");
                return "{\"code\":-889,\"message\":\"验证重复提交时出现未知异常!\"}";
            }
        }
    }

    @Before("loginPointcut()")
    public void loginLogBefore(JoinPoint joinPoint) {
        String url = null;
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            url = request.getRequestURL().toString();
        }
        Object[] args = joinPoint.getArgs();
        log.info("用户访问登陆请求 url={} 参数列表 args={}", url, args);
    }

}
