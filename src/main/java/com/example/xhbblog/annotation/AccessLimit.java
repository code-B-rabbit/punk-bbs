package com.example.xhbblog.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 接口防刷所用的注解
 */
@Retention(RUNTIME)
@Target(METHOD)   //注解在方法上
public @interface AccessLimit {
    int maxCount();
    int seconds();
}
