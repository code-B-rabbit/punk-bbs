package com.example.xhbblog.Interceptor;

import com.example.xhbblog.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;

@Component
public class UserLoginInterceptor implements HandlerInterceptor {

    Logger logger =LoggerFactory.getLogger(UserLoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session=request.getSession();
        User user= (User) session.getAttribute("user");
        if(user!=null&&user.getRole().equals("user"))
        {
            logger.info("用户页面请求通过");
            return true;
        } else{
            logger.info("拦截请求{}",request.getRequestURI());
            response.sendRedirect("/userAdmin/adminLogin");
            return false;
        }
    }

}
