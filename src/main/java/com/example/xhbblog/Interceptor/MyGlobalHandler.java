package com.example.xhbblog.Interceptor;

import com.example.xhbblog.Service.FriendLyLinkService;
import com.example.xhbblog.pojo.FriendlyLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ControllerAdvice        //全局controller增强
@ComponentScan
public class MyGlobalHandler {

    @Autowired
    private FriendLyLinkService friendLyLinkService;

    private static final Logger LOG = LoggerFactory.getLogger(MyGlobalHandler.class);


    @ModelAttribute("fls")
    public List<FriendlyLink> friendlyLinks()
    {
        return friendLyLinkService.ListOf(true);
    }

    @ExceptionHandler(Exception.class)        //全局错误配置
    public ModelAndView customException(HttpServletRequest request,Exception e) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("error");
        LOG.error(e.getMessage());     //日志输出该错误信息
        return mv;
    }


}
