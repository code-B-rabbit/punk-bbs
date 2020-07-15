package com.example.xhbblog.Controller;
import com.example.xhbblog.Service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 创建用于捕捉 @Controller 异常的全局文件MyExceptionController
 */
@ControllerAdvice(annotations = {Controller.class, RestController.class})
public class MyExceptionController {
    private static final Logger logger= LoggerFactory.getLogger(MyExceptionController.class);
    public static final String DEFAULT_ERROR_VIEW = "error";

    @Autowired
    private EmailService emailService;


    /**
     * 处理所有的Controller层面的异常
     * 如果这里添加 @ResponseBody 注解 表示抛出的异常以 Rest 的方式返回，这时就系统就不会指向到错误页面 /error
     * */
    @ExceptionHandler(Exception.class)
    public final ModelAndView handleAllExceptions(Exception ex, HttpServletRequest request, HttpServletResponse response){

        ex.printStackTrace();
        logger.error("请求为{}发现异常{}",request.getRequestURL(),ex.getMessage());

        emailService.sendEmail("服务器发现\"朋克\"系统错误","511928849@qq.com",
                String.format("请求为%s发现异常%s",request.getRequestURL(),ex.getMessage()));//发送错误信息邮件

        ModelAndView modelAndView = new ModelAndView();
        //将异常信息设置如modelAndView
        modelAndView.addObject("msg", ex);
        modelAndView.addObject("url", request.getRequestURL());
        modelAndView.setViewName(DEFAULT_ERROR_VIEW);

        //返回ModelAndView
        return modelAndView;
    }

}