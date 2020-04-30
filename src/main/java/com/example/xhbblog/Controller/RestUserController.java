package com.example.xhbblog.Controller;

import com.example.xhbblog.Service.UserService;
import com.example.xhbblog.pojo.User;
import com.example.xhbblog.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class RestUserController {

    @Autowired
    private UserService userService;

    @PostMapping("/check")        //验证用户
    public Map<String,Boolean> checkUser(User user, HttpSession session, HttpServletRequest request,HttpServletResponse response)
    {
        String password=user.getPassword();        //暂时存储一下旧的密码
        Map<String,Boolean> anw=new HashMap<>();
        User u=userService.check(user);
        if(u==null)
        {
            anw.put("message",false);
        }else
        {
            session.setAttribute("user",u);
            anw.put("message",true);
            if(user.getChecked()!=null&&user.getChecked()==true)
            {
                CookieUtil.set(response,"name",user.getName(),true);
                CookieUtil.set(response,"password",password,true);
            }else{
                CookieUtil.remove(request,response,"name");
                CookieUtil.remove(request,response,"password");
            }
        }
        return anw;
    }

    @PostMapping("/check/login")    //是否登录
    public Map<String,Boolean> check(HttpSession session)
    {
        Map<String,Boolean> anw=new HashMap<>();
        anw.put("has",session.getAttribute("user")!=null);
        return anw;
    }

    @GetMapping("/login")   //登录路由
    public ModelAndView login()
    {
        return new ModelAndView("login");
    }

    @GetMapping("/register")
    public ModelAndView register()
    {
        return new ModelAndView("register");
    }

    @GetMapping("/check")        //查看是否有重名
    public Map check(String name)
    {
        Map<String,Boolean> anw=new HashMap<>();
        anw.put("exists",userService.checkName(name));
        return anw;
    }

    @GetMapping("/checkEmail")        //查看是否有重邮箱
    public Map checkEmail(String email)
    {
        Map<String,Boolean> anw=new HashMap<>();
        anw.put("exists",userService.checkEmail(email));
        return anw;
    }


    @PostMapping("/addUser")        //添加用户
    public void addUser(@Valid User u, BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpSession session, HttpServletResponse response) throws IOException {
        if(bindingResult.hasErrors())
        {
            redirectAttributes.addFlashAttribute("message",bindingResult.getFieldError().getDefaultMessage());
        }else{
            userService.add(u);         //我忘了这里是否直接给user直接set上id...
            session.setAttribute("user",u);
            redirectAttributes.addFlashAttribute("message","账户注册成功!!!");
        }
        response.sendRedirect("/user/register");       //restController下正常重定向无效
    }


    @RequestMapping("/deleteUser")      //注销
    public Map delete(HttpSession session)
    {
        Map<String,String> anw=new HashMap<>();
        if(session.getAttribute("user")!=null)
        {
            anw.put("message","注销成功!");
        }else{
            anw.put("message","您尚未登录");
        }
        return anw;
    }


}
