package com.example.xhbblog.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 创建用于捕捉 @Controller 异常的全局文件MyExceptionController
 */
@ControllerAdvice(annotations = {Controller.class, RestController.class})
public class MyExceptionController {
    private static final Logger logger= LoggerFactory.getLogger(MyExceptionController.class);
    public static final String DEFAULT_ERROR_VIEW = "error";


    /**
     * 处理所有的Controller层面的异常
     * 这里从request中取得一场状态码,若为404则选择忽略邮件报警(客户端导致的错误)
     * 如果这里添加 @ResponseBody 注解 表示抛出的异常以 Rest 的方式返回，这时就系统就不会指向到错误页面 /error
     * */
    @ExceptionHandler(Exception.class)
    public final ModelAndView handleAllExceptions(Exception ex, HttpServletRequest request, HttpServletResponse response){
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if(statusCode==null||statusCode!=404){
            ex.printStackTrace();
            Map map=new HashMap<>();
            for (String s : request.getParameterMap().keySet()) {
                map.put(s,request.getParameterMap().get(s));
            }
            logger.error("请求为{},参数为{},发现异常{}",request.getRequestURL(),map,ex.getMessage());
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("msg", ex);
        modelAndView.addObject("url", request.getRequestURL());
        modelAndView.setViewName(DEFAULT_ERROR_VIEW);

        //返回ModelAndView
        return modelAndView;
    }

}