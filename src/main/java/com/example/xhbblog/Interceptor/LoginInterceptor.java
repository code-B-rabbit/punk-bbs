package com.example.xhbblog.Interceptor;

import com.example.xhbblog.pojo.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session=request.getSession();
        User user= (User) session.getAttribute("user");
        if(user!=null&&user.getRole().equals("admin"))           //只有权限为admin才能放行
        {
            return true;
        } else{
            response.sendRedirect("/admin/adminLogin?message="+ URLEncoder.encode("您需要登录管理员才可进入","utf8"));
            return false;
        }
    }

}
