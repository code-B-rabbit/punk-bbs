package com.example.xhbblog.Interceptor;

import com.example.xhbblog.Service.FriendLyLinkService;
import com.example.xhbblog.pojo.FriendlyLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@ControllerAdvice        //全局controller增强
public class MyGlobalHandler {

    @Autowired
    private FriendLyLinkService friendLyLinkService;

    @ModelAttribute("fls")
    public List<FriendlyLink> friendlyLinks()
    {
        return friendLyLinkService.ListOf(true);
    }

    @ExceptionHandler(Exception.class)        //全局错误配置
    public ModelAndView customException(Exception e) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("message", e.getMessage());
        mv.setViewName("error");
        return mv;
    }


}
