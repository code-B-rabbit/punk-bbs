package com.example.xhbblog.Interceptor;

import com.example.xhbblog.annotation.AccessLimit;
import com.example.xhbblog.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    Logger logger =LoggerFactory.getLogger(LoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session=request.getSession();
        User user= (User) session.getAttribute("user");
        if(user!=null&&user.getRole().equals("admin"))           //只有权限为admin才能放行
        {
            return true;
        } else{
            logger.info("拦截请求{}",request.getRequestURI());
            response.sendRedirect("/admin/adminLogin?message="+ URLEncoder.encode("您需要登录管理员才可进入","utf8"));
            return false;
        }
    }

}
